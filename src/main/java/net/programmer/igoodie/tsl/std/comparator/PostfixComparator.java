package net.programmer.igoodie.tsl.std.comparator;

import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.predicate.TSLComparator;

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
