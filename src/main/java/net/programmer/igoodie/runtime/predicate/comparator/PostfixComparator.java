package net.programmer.igoodie.runtime.predicate.comparator;

import net.programmer.igoodie.exception.TSLSyntaxException;

public class PostfixComparator extends TSLComparator {

    public PostfixComparator(Object right) throws TSLSyntaxException {
        super(right);
    }

    @Override
    public boolean compare(Object left) {
        return left.toString().toLowerCase()
                .endsWith(right.toString().toLowerCase());
    }

}
