package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.util.Pair;
import net.programmer.igoodie.tsl.util.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// EITHER <action> [OR <action>]+ [ALL DISPLAYING msg+]?
// EITHER CHANCE <n> PERCENT <action> [OR CHANCE <n> PERCENT <action>]+ [ALL DISPLAYING msg+]?
// EITHER WEIGHT <n> <action> [OR WEIGHT <n> <action>]+ [ALL DISPLAYING msg+]?
public class EitherAction extends TSLAction {

    public static final Pattern PERCENTAGE_PATTERN = Pattern.compile("^(\\d+)(\\.(\\d+))?$");

    protected SamplerMode samplerMode;
    protected WeightedSampler<TSLAction> actionSampler = new WeightedSampler<>();

    public EitherAction(TSLPlatform platform, List<String> args) throws TSLSyntaxException {
        super(platform, args);
        args = consumeAllMessagePart(args);

        List<List<String>> actionChunks = Utils.splitIntoChunks(args, arg -> arg.equalsIgnoreCase("OR"));

        if (actionChunks.size() <= 1) {
            throw new TSLSyntaxException("Expected at least 2 actions, instead found -> {}", actionChunks.size());
        }

        for (List<String> actionChunk : actionChunks) {
            parseWeight(actionChunk).using((weight, actionTokens) -> {
                String actionName = actionTokens.get(0);
                TSLAction.Supplier<?> actionDefinition = platform.getActionDefinition(actionName)
                        .orElseThrow(() -> new TSLSyntaxException("Unknown action -> {}", actionName));
                List<String> actionArgs = actionTokens.subList(1, actionTokens.size());
                TSLAction action = actionDefinition.generate(platform, actionArgs);

                this.actionSampler.addElement(action, weight);
            });
        }

        if (samplerMode == SamplerMode.PERCENTAGE) {
            long percentageSum = actionSampler.nodes.stream()
                    .collect(Collectors.summarizingInt(node -> node.weight))
                    .getSum();

            if (percentageSum > 100_0000000) {
                throw new TSLSyntaxException("Expected a total of 100% probability, instead found -> {}%",
                        actionSampler.nodes.stream().map(node -> node.weight)
                                .map(weight -> PercentageWeight.toString(weight) + "%")
                                .collect(Collectors.joining(" + ", "", " = "
                                        + PercentageWeight.toString(percentageSum)))
                );
            }
        }
    }

    protected Pair<Integer, List<String>> parseWeight(List<String> tokens) throws TSLSyntaxException {
        ListAccessor<String> tokenAccessor = ListAccessor.of(tokens);

        if (tokenAccessor.get(0).filter(t -> t.equalsIgnoreCase("CHANCE")).isPresent()) {
            if (samplerMode == null) {
                samplerMode = SamplerMode.PERCENTAGE;
            } else if (samplerMode != SamplerMode.PERCENTAGE) {
                throw new TSLSyntaxException("Mixed EITHER branch types aren't allowed. Expected CHANCE <n> PERCENT.");
            }

            String percentageString = tokenAccessor.get(1)
                    .orElseThrow(() -> new TSLSyntaxException("Expected a percentage number after CHANCE."));

            tokenAccessor.get(2).filter(t -> t.equalsIgnoreCase("PERCENT"))
                    .orElseThrow(() -> new TSLSyntaxException("Expected PERCENT after the percentage."));

            int weight = PercentageWeight.fromString(percentageString);

            return new Pair<>(weight, tokens.subList(3, tokens.size()));
        }

        if (samplerMode == null) {
            samplerMode = SamplerMode.DEFAULT;
        } else if (samplerMode != SamplerMode.DEFAULT) {
            throw new TSLSyntaxException("Mixed EITHER branch types aren't allowed.");
        }

        return new Pair<>(1, tokens);
    }

    protected List<String> consumeAllMessagePart(List<String> args) throws TSLSyntaxException {
        int indexAll = IntStream.range(0, args.size())
                .filter(i -> args.get(args.size() - i - 1).equalsIgnoreCase("ALL"))
                .map(i -> args.size() - i - 1)
                .findFirst().orElse(-1);

        if (indexAll == -1) return args;

        ListAccessor.of(args).get(indexAll + 1)
                .filter(arg -> arg.equalsIgnoreCase("DISPLAYING"))
                .orElseThrow(() -> new TSLSyntaxException("Expected 'DISPLAYING' after 'ALL'"));

        this.message = args.subList(indexAll + 2, args.size());

        return args.subList(0, indexAll + 2);
    }

    @Override
    public boolean perform(TSLEventContext ctx) {
        return actionSampler.sample().perform(ctx);
    }

    enum SamplerMode {DEFAULT, WEIGHTED, PERCENTAGE}

    public static class PercentageWeight {

        // 001_0000000 = 1%
        // 100_0000000 = 100%
        // 001_5000000 = 1.5%
        // 001_2222222 = 1.2222222%

        public static int fromString(String percentageStr) throws TSLSyntaxException {
            Matcher matcher = PERCENTAGE_PATTERN.matcher(percentageStr);

            if (!matcher.matches()) {
                throw new TSLSyntaxException("Malformed percentage number. Expected something like: 1, 1.5, 100, 50, etc.");
            }

            int integerPart = Integer.parseInt(matcher.group(1)) * 1_0000000;
            int fractionalPart = Optional.ofNullable(matcher.groupCount() > 1 ? matcher.group(3) : null)
                    .map(s -> Utils.rightPad(s, 7, '0'))
                    .map(Integer::parseInt)
                    .orElse(0);

            return integerPart + fractionalPart;
        }

        public static String toString(int weight) {
            return toString(((long) weight));
        }

        public static String toString(long weight) {
            long fractionalPart = weight % 10000000L;
            long integerPart = weight / 10000000L;

            return fractionalPart == 0 ?
                    String.valueOf(integerPart) :
                    (integerPart + "." + fractionalPart).replaceAll("0+$", "");
        }
    }

    public static class WeightedSampler<T> {

        public class Node implements Comparable<Node> {

            protected int weight;
            protected T data;

            @Override
            public int compareTo(Node other) {
                return Integer.compare(this.weight, other.weight);
            }

        }

        protected Random random;
        protected List<Node> nodes;

        public WeightedSampler() {
            this.random = new Random();
            this.nodes = new ArrayList<>();
        }

        public void addElement(T data, int weight) {
            Node node = new Node();
            node.weight = weight;
            node.data = data;

            this.nodes.add(node);
            Collections.sort(this.nodes);
        }

        public T sample() {
            if (nodes.size() == 0) {
                throw new IllegalStateException("No elements to sample from.");
            }

            Node selectedNode = null;
            double highestThreshold = -1;

            for (Node node : nodes) {
                double threshold = random.nextDouble() * node.weight;

                if (threshold > highestThreshold) {
                    highestThreshold = threshold;
                    selectedNode = node;
                }
            }

            assert selectedNode != null;
            return selectedNode.data;
        }

    }

//    public static void main(String[] args) throws TSLSyntaxException {
//        TSLPlatform platform = new TSLPlatform("TestPlatform", 1.0f);
//
//        platform.initializeStd();
//
//        platform.registerAction("DEBUG", (platform1, args1) -> new TSLAction(platform, args1) {
//            @Override
//            public boolean perform(TSLEventContext ctx) {
//                System.out.println(args1);
//                return true;
//            }
//        });
//
//        EitherAction action = new EitherAction(platform, Arrays.asList(
//                "CHANCE", "50.555", "PERCENT", "DEBUG", "1",
//                "OR",
//                "CHANCE", "49.445", "PERCENT", "DEBUG", "2"
//        ));
//
//        for (int i = 0; i < 100; i++) {
//            TSLEventContext ctx = new TSLEventContext(platform, "Foo");
//            ctx.setTarget("iGoodie");
//            action.perform(ctx);
//        }
//    }

}