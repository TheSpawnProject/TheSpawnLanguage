package net.programmer.igoodie.tsl.std.comparator;

import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.predicate.TSLComparator;

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
