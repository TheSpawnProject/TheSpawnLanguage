package net.programmer.igoodie.tsl.runtime.predicate;

import net.programmer.igoodie.tsl.exception.TSLSyntaxException;

public abstract class TSLComparator {

    protected Object right;

    public TSLComparator(Object right) throws TSLSyntaxException {
        this.right = right;
    }

    public abstract boolean compare(Object left);

    protected final Object tryParseNumber(Object right) {
        try {
            return right instanceof String ? Double.parseDouble((String) right) : right;
        } catch (Exception e) {
            return right;
        }
    }

    @FunctionalInterface
    public interface Supplier<T extends TSLComparator> {
        T generate(Object right) throws TSLSyntaxException;
    }

}
