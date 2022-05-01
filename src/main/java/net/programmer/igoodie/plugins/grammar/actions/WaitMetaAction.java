package net.programmer.igoodie.plugins.grammar.actions;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WaitMetaAction extends TSLAction {

    public static final WaitMetaAction INSTANCE = new WaitMetaAction();

    public static final Map<String, Long> UNIT_COEF = Stream.of(
            new AbstractMap.SimpleEntry<>("milliseconds", 1L),
            new AbstractMap.SimpleEntry<>("seconds", 1_000L),
            new AbstractMap.SimpleEntry<>("minutes", 60 * 1_000L)
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    private WaitMetaAction() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "WAIT");
    }

    @Override
    public String getUsage() {
        return getName() + " <time> (minutes|seconds|milliseconds)";
    }

    @Override
    public @Nullable List<Couple<String, String>> getCompletionSnippets() {
        return Arrays.asList(
                new Couple<>(getName() + " minutes",
                        getName() + " ${1:time} minutes"),
                new Couple<>(getName() + " seconds",
                        getName() + " ${1:time} seconds"),
                new Couple<>(getName() + " milliseconds",
                        getName() + " ${1:time} milliseconds")
        );
    }

    @Override
    public void validateTokens(TSLToken nameToken, List<TSLToken> arguments, TSLRule rule, TSLParser parser) throws TSLSyntaxError {
        if (arguments.size() < 2) {
            throw new TSLSyntaxError("Expected amount and a time unit", nameToken);
        }
        if (arguments.size() > 2) {
            throw new TSLSyntaxError("Expected 2 tokens, found " + arguments.size() + " instead", nameToken);
        }

        TSLToken unitToken = arguments.get(1);
        if (unitToken instanceof TSLPlainWord) {
            Long timeCoef = UNIT_COEF.get(unitToken.getRaw().toLowerCase());
            if (timeCoef == null) {
                throw new TSLSyntaxError("Unknown time unit", unitToken);
            }
        }
    }

    @Override
    public void perform(List<TSLToken> arguments, TSLContext context) {
        TSLToken timeToken = arguments.get(0);
        TSLToken unitToken = arguments.get(1);

        double time = parseDouble(timeToken, context);
        String unit = unitToken.evaluate(context);

        Long timeCoef = UNIT_COEF.get(unit.toLowerCase());

        if (timeCoef == null) {
            throw new TSLRuntimeError("Unknown time unit", unitToken);
        }

        sleepThread((long) (time * timeCoef));
    }

    private void sleepThread(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
