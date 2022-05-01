package net.programmer.igoodie.plugins.grammar.predicates;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLComparator;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLPredicate;
import net.programmer.igoodie.tsl.exception.TSLInternalError;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BinaryOperationPredicate extends TSLPredicate {

    public static final BinaryOperationPredicate INSTANCE = new BinaryOperationPredicate();

    private BinaryOperationPredicate() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "Binary Operation");
    }

    @Override
    public boolean formatMatches(TheSpawnLanguage tsl, TSLEvent event, List<TSLToken> tokens) throws TSLSyntaxError {
        if (tokens.size() < 3) {
            return false;
        }

        TSLToken eventFieldToken = tokens.get(0);

        if (!(eventFieldToken instanceof TSLPlainWord)) {
            return false;
        }

        String eventField = ((TSLPlainWord) eventFieldToken).getWord();

        if (eventField == null || !event.getAcceptedFields().containsKey(eventField)) {
            throw new TSLSyntaxError("Unknown field for the event -> " + eventFieldToken, eventFieldToken);
        }

        Couple<TSLComparator, Integer> couple = longestMatchingComparator(tsl, tokens);
        TSLComparator comparator = couple.getFirst();
        Integer argsTokenIndex = couple.getSecond();

        return comparator != null && argsTokenIndex <= tokens.size();
    }

    @Override
    public boolean satisfies(TSLContext context, List<TSLToken> tokens) {
        TSLToken fieldNameToken = tokens.get(0);
        String fieldName = ((TSLPlainWord) fieldNameToken).getWord();

        GoodieObject eventArguments = context.getEventArguments();
        Object eventFieldValue = TSLEvent.extractField(eventArguments, fieldName);

        checkFieldValueType(context, fieldNameToken);

        Couple<TSLComparator, Integer> couple = longestMatchingComparator(context.getTsl(), tokens);
        TSLComparator comparator = couple.getFirst();
        Integer argsTokenIndex = couple.getSecond();

        List<String> rightHand = tokens.subList(argsTokenIndex, tokens.size()).stream()
                .map(t -> t.evaluate(context))
                .collect(Collectors.toList());

        return comparator.satisfies(
                eventFieldValue,
                rightHand
        );
    }

    private void checkFieldValueType(TSLContext context, TSLToken fieldNameToken) {
        String fieldName = ((TSLPlainWord) fieldNameToken).getWord();

        TSLEvent event = context.getEvent();

        if (event == null) {
            throw new TSLInternalError("TSLContext lacks the event");
        }

        Map<String, Class<?>> acceptedFields = event.getAcceptedFields();

        GoodieObject eventArguments = context.getEventArguments();
        Object eventFieldValue = TSLEvent.extractField(eventArguments, fieldName);

        // Event argument is absent. No checking is needed
        if (eventFieldValue == null) return;

        Class<?> actualType = eventFieldValue.getClass();
        Class<?> expectedType = acceptedFields.get(fieldName);

        if (!expectedType.isAssignableFrom(actualType)) {
            throw new TSLRuntimeError(String.format("Event Field value mismatches expected type. (Expected: %s, Found: %s)",
                    expectedType, actualType), fieldNameToken);
        }
    }

    private Couple<TSLComparator, Integer> longestMatchingComparator(TheSpawnLanguage tsl, List<TSLToken> tokens) {
        StringBuilder comparatorId = new StringBuilder();
        TSLComparator longestMatch = null;
        int argsTokenIndex = -1;

        for (int i = 1; i < tokens.size(); i++) {
            TSLToken token = tokens.get(i);

            if (comparatorId.length() != 0) comparatorId.append(" ");
            comparatorId.append(StringUtilities.allUpper(token.getRaw()));

            TSLComparator comparator = tsl.getComparator(comparatorId.toString());
            if (comparator != null) {
                longestMatch = comparator;
                argsTokenIndex = i + 1;
            }
        }

        return new Couple<>(longestMatch, argsTokenIndex);
    }

}
