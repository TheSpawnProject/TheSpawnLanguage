package net.programmer.igoodie.plugins.grammar.predicates;

import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLComparator;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.definition.TSLPredicate;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.util.Couple;
import net.programmer.igoodie.util.StringUtilities;

import java.util.List;
import java.util.stream.Collectors;

public class BinaryOperationPredicate extends TSLPredicate {

    public static final BinaryOperationPredicate INSTANCE = new BinaryOperationPredicate();

    private BinaryOperationPredicate() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "Binary Operation");
    }

    @Override
    public boolean formatMatches(TheSpawnLanguage language, TSLEvent event, List<TSLToken> tokens) throws TSLSyntaxError {
        if (tokens.size() < 3) {
            return false;
        }

        TSLToken eventFieldToken = tokens.get(0);

        if (!(eventFieldToken instanceof TSLString)) {
            return false;
        }

        String eventField = ((TSLString) eventFieldToken).getWord();

        if (eventField == null || !event.getAcceptedFields().contains(eventField)) {
            throw new TSLSyntaxError("Unknown field for the event -> " + eventFieldToken, eventFieldToken);
        }

        Couple<TSLComparator, Integer> couple = longestMatchingComparator(language, tokens);
        TSLComparator comparator = couple.getFirst();
        Integer argsTokenIndex = couple.getSecond();

        return comparator != null && argsTokenIndex <= tokens.size();
    }

    @Override
    public boolean satisfies(TSLContext context, List<TSLToken> tokens) {
        TSLToken eventFieldToken = tokens.get(0);
        String eventField = ((TSLString) eventFieldToken).getWord();

        Couple<TSLComparator, Integer> couple = longestMatchingComparator(context.getLanguage(), tokens);
        TSLComparator comparator = couple.getFirst();
        Integer argsTokenIndex = couple.getSecond();

        return comparator.satisfies(
                TSLEvent.extractField(context.getEventArguments(), eventField),
                tokens.subList(argsTokenIndex, tokens.size()).stream().map(t -> t.evaluate(context)).collect(Collectors.toList())
        );
    }

    private Couple<TSLComparator, Integer> longestMatchingComparator(TheSpawnLanguage language, List<TSLToken> tokens) {
        StringBuilder comparatorId = new StringBuilder();
        TSLComparator longestMatch = null;
        int argsTokenIndex = -1;

        for (int i = 1; i < tokens.size(); i++) {
            TSLToken token = tokens.get(i);

            if (comparatorId.length() != 0) comparatorId.append(" ");
            comparatorId.append(StringUtilities.allUpper(token.getRaw()));

            TSLComparator comparator = language.COMPARATOR_REGISTRY.get(comparatorId.toString());
            if (comparator != null) {
                longestMatch = comparator;
                argsTokenIndex = i + 1;
            }
        }

        return new Couple<>(longestMatch, argsTokenIndex);
    }

}
