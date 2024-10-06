package net.programmer.igoodie.runtime.predicate.comparator;

import net.programmer.igoodie.exception.TSLSyntaxException;

public class GteComparator extends TSLComparator {

    public GteComparator(Object right) throws TSLSyntaxException {
        super(right);
    }

    @Override
    public boolean compare(Object left) {
        if (left instanceof Number && right instanceof Number)
            return ((Number) left).doubleValue() >= ((Number) right).doubleValue();

        return false;
    }

}
