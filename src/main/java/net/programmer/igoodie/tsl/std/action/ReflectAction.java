package net.programmer.igoodie.tsl.std.action;

import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.CharStream;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEvent;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// REFLECT %Target1, Target2, Target3% [ONLY]? <action>
// REFLECT <N> <action>
// REFLECT * <action>
public class ReflectAction extends TSLAction {

    protected static List<ReflectProvider> providers = new ArrayList<>();

    public static ReflectProvider registerProvider(ReflectProvider provider) {
        providers.add(provider);
        return provider;
    }

    protected boolean greedyTargets;
    protected int randomTargets;
    protected boolean targetsOnly;
    protected List<String> targets;
    protected TSLAction action;

    public ReflectAction(TSLPlatform platform, List<String> args) throws TSLSyntaxException {
        super(platform, args);

        if (args.isEmpty()) {
            throw new TSLSyntaxException("Expected a target selector and an action.");
        }

        args = consumeTargetSelector(args);

        this.action = new TSLParser(platform, args).parseAction();
    }

    protected List<String> consumeTargetSelector(List<String> tokens) throws TSLSyntaxException {
        ListAccessor<String> tokenAccessor = ListAccessor.of(tokens);

        String targetExpr = tokenAccessor.get(0)
                .orElseThrow(() -> new TSLSyntaxException("Expected a target selector"));

        if (targetExpr.equals("*")) {
            this.greedyTargets = true;
            return tokens.subList(1, tokens.size());
        }

        try {
            this.randomTargets = Integer.parseInt(targetExpr);
            return tokens.subList(1, tokens.size());
        } catch (NumberFormatException ignored) {}

        this.targets = Arrays.asList(targetExpr.split(",\\s*"));
        this.targetsOnly = tokenAccessor.get(1)
                .map(s -> s.equalsIgnoreCase("ONLY"))
                .orElse(false);
        return this.targetsOnly
                ? tokens.subList(2, tokens.size())
                : tokens.subList(1, tokens.size());
    }

    @Override
    public boolean perform(TSLEventContext ctx) throws TSLPerformingException {
        if(this.greedyTargets) {

        }

        return false;
    }

    public interface ReflectProvider {

        List<String> getEventTargets(List<String> targets);

        List<String> getAllEventTargets();

        List<String> getRandomEventTargets(int count);

        void onEventReflection(String originalTarget, TSLEventContext ctx);

        default void unsubscribe() {
            ReflectAction.providers.removeIf(provider -> provider == this);
        }

    }

    public static void main(String[] args) throws TSLSyntaxException, IOException, TSLPerformingException {
        TSLPlatform platform = new TSLPlatform("Test", 1.0f);

        platform.initializeStd();

        platform.registerEvent(new TSLEvent("Donation")
                .addPropertyType(TSLEvent.PropertyBuilder.DOUBLE.create("amount")));

        ReflectProvider provider = ReflectAction.registerProvider(new ReflectProvider() {
            @Override
            public List<String> getEventTargets(List<String> targets) {
                return targets.stream().map(t -> "Player:" + t)
                        .collect(Collectors.toList());
            }

            @Override
            public List<String> getAllEventTargets() {
                return Arrays.asList(
                        "Player:iGoodie",
                        "Player:CoconutOrange",
                        "Player:TheDarkPirate",
                        "Player:TheDiaval"
                );
            }

            @Override
            public List<String> getRandomEventTargets(int count) {
                List<String> allEventTargets = new ArrayList<>(getAllEventTargets());
                List<String> randomTargets = new ArrayList<>();

                int n = Math.max(allEventTargets.size(), count);
                Collections.shuffle(allEventTargets);

                for (int i = 0; i < n; i++) {
                    randomTargets.add(allEventTargets.get(i));
                }

                return randomTargets;
            }

            @Override
            public void onEventReflection(String originalTarget, TSLEventContext ctx) {
                System.out.println("Reflecting from " + originalTarget + " -> " + ctx.getEventArgs());
            }
        });

        String script = String.join(" ", "",
                "REFLECT *",
                " IF amount IS 3 THEN",
                "  NOTHING DISPLAYING %HELL YEA!%",
                " ELSE",
                "  NOTHING DISPLAYING %Oh noes...%",
                "ON Donation"
        );

        TSLLexer lexer = new TSLLexer(CharStream.fromString(script));
        TSLParser parser = new TSLParser(platform, "Player:iGoodie", lexer.tokenize());
        TSLRuleset ruleset = parser.parse();

        TSLEventContext ctx = new TSLEventContext(platform, "Donation");
        ctx.setTarget("Player:iGoodie");

        ruleset.perform(ctx);

        provider.unsubscribe();

        ruleset.perform(ctx);
    }

}
