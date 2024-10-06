package net.programmer.igoodie.node.predicate.comparator;

import net.programmer.igoodie.exception.TSLSyntaxException;

import java.util.Set;

public class ContainsComparator extends TSLComparator {

    public ContainsComparator(Object right) throws TSLSyntaxException {
        super(right);
        this.right = this.tryParseNumber(right);
    }

    @Override
    public boolean compare(Object left) {
        if (left instanceof Set)
            return ((Set<?>) left).contains(right.toString().toLowerCase());
        if (left instanceof String)
            return ((String) left).contains(right.toString());
        return left.toString().contains(right.toString());
    }

}
