package net.programmer.igoodie.node.predicate.comparator;

import net.programmer.igoodie.exception.TSLSyntaxException;

import java.util.Objects;

public class IsComparator extends TSLComparator {

    public IsComparator(Object right) throws TSLSyntaxException {
        super(right);
    }

    @Override
    public boolean compare(Object left) {
        if (left instanceof Boolean && right instanceof Boolean)
            return left.toString().equalsIgnoreCase(right.toString());

        if (left instanceof String && right instanceof String)
            return ((String) right).equalsIgnoreCase((String) left);

        return Objects.equals(left, right);
    }

}
