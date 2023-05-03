package net.programmer.igoodie.plugins.grammar.actions;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.goodies.util.builder.InlineMapBuilder;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.definition.TSLAction;
import net.programmer.igoodie.tsl.definition.base.TSLArguments;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.legacy.parser.TSLParsingContext;
import net.programmer.igoodie.legacy.parser.token.TSLPlainWord;
import net.programmer.igoodie.legacy.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaitMetaAction extends TSLAction {

    public static final WaitMetaAction INSTANCE = new WaitMetaAction();

    public static final Map<String, Long> UNIT_COEF = InlineMapBuilder.of(HashMap<String, Long>::new)
            .entry("milliseconds", 1L)
            .entry("seconds", 1_000L)
            .entry("minutes", 60_000L)
            .build();

    private WaitMetaAction() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "WAIT");
    }

    @Override
    public String getUsage() {
        return getName() + " <time> (" + String.join("|", UNIT_COEF.keySet()) + ")";
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
    public void validateTokens(TSLToken nameToken, ListAccessor<TSLToken> arguments, TSLParsingContext parsingContext) throws TSLSyntaxError {
        if (arguments.size() < 2) {
            throw new TSLSyntaxError("Expected amount and a time unit", nameToken);
        }
        if (arguments.size() > 2) {
            throw new TSLSyntaxError("Expected 2 tokens, found " + arguments.size() + " instead", nameToken);
        }

        TSLToken unitToken = arguments.get(1).orElseThrow(InternalError::new);
        if (unitToken instanceof TSLPlainWord) {
            Long timeCoef = UNIT_COEF.get(unitToken.getRaw().toLowerCase());
            if (timeCoef == null) {
                throw new TSLSyntaxError("Unknown time unit", unitToken);
            }
        }
    }

    @Override
    public void perform(ListAccessor<TSLToken> arguments, TSLContext context) {
        TSLToken timeToken = arguments.get(0).orElseThrow(InternalError::new);
        TSLToken unitToken = arguments.get(1).orElseThrow(InternalError::new);

        double time = arguments.get(0)
                .flatMap(token -> TSLArguments.DOUBLE.parse(token, context))
                .orElse(0.0);

        long timeCoefficient = arguments.get(1)
                .map(token -> token.evaluate(context))
                .map(value -> UNIT_COEF.get(value.toLowerCase()))
                .orElseThrow(() -> new TSLRuntimeError("Unknown time unit", unitToken));

        sleepThread((long) (time * timeCoefficient));
    }

    private void sleepThread(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
