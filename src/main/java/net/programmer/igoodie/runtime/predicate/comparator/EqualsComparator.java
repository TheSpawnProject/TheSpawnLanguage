package net.programmer.igoodie.runtime.predicate.comparator;

import net.programmer.igoodie.exception.TSLSyntaxException;

import java.util.Objects;

public class EqualsComparator extends TSLComparator {

    public EqualsComparator(Object right) throws TSLSyntaxException {
        super(right);
        this.right = this.tryParseNumber(right);
    }

    @Override
    public boolean compare(Object left) {
        if (left instanceof Number && right instanceof Number)
            return ((Number) left).doubleValue() == ((Number) right).doubleValue();

        return Objects.equals(left, right);
    }

}
