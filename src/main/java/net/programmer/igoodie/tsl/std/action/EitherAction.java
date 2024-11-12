package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

// EITHER <action> [OR <action>]+ [ALL DISPLAYING msg+]?
// EITHER CHANCE <n> PERCENT <action> [OR CHANCE <n> PERCENT <action>]+ [ALL DISPLAYING msg+]?
// EITHER WEIGHT <n> <action> [OR WEIGHT <n> <action>]+ [ALL DISPLAYING msg+]?
public class EitherAction extends TSLAction {

    protected SamplerMode samplerMode = SamplerMode.WEIGHTED;
    protected WeightedSampler<TSLAction> actionSampler;

    public EitherAction(TSLPlatform platform, List<String> args) throws TSLSyntaxException {
        super(platform, args);
        args = consumeAllMessagePart(args);

        List<List<String>> actionChunks = Utils.splitIntoChunks(args, arg -> arg.equalsIgnoreCase("OR"));

        boolean chanceMode = false;

        for (List<String> actionChunk : actionChunks) {

        }
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
        return false;
    }

    enum SamplerMode {WEIGHTED, PERCENTAGE}

    public static class WeightedSampler<T> {

        public class Node implements Comparable<Node> {

            protected int weight;
            protected T data;

            @Override
            public int compareTo(Node other) {
                return Integer.compare(this.weight, other.weight);
            }

            public String asPercentageString() {
                // 001_0000000 = 1%
                // 100_0000000 = 100%
                // 001_5000000 = 1.5%
                // 001_2222222 = 1.2222222%

                int fractionalPart = weight % 10000000;
                int integerPart = weight / 10000000;

                String percentage = fractionalPart == 0 ?
                        String.valueOf(integerPart) :
                        integerPart + "." + fractionalPart;

                return String.format("%s%% - %s", percentage, data);
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

}
