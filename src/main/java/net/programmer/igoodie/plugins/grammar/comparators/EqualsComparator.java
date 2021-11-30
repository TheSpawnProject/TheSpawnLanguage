package net.programmer.igoodie.plugins.grammar.comparators;

import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.definition.TSLComparator;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

import java.util.List;

public class EqualsComparator extends TSLComparator {

    public static final EqualsComparator INSTANCE = new EqualsComparator();

    private EqualsComparator() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "=");
    }

    @Override
    public boolean verifySyntax(List<TSLToken> rightHand) throws TSLSyntaxError {
        return rightHand.size() == 1;
    }

    @Override
    public boolean satisfies(Object leftHand, List<String> rightHand) throws TSLRuntimeError {
        String rightHandValue = rightHand.get(0);

        if (leftHand instanceof Number) {
            try {
                return ((Number) leftHand).doubleValue() == Double.parseDouble(rightHandValue);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return leftHand.toString().equals(rightHandValue);
    }

}
