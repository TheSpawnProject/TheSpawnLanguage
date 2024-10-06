package net.programmer.igoodie.runtime.predicate;

import net.programmer.igoodie.TSL;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.runtime.event.TSLEventContext;
import net.programmer.igoodie.runtime.predicate.comparator.TSLComparator;

import java.util.function.Predicate;

public class TSLPredicate implements Predicate<TSLEventContext> {

    protected final String fieldName;
    protected final TSLComparator comparator;

    public TSLPredicate(String fieldName, TSLComparator comparator) {
        this.fieldName = fieldName;
        this.comparator = comparator;
    }

    @Override
    public boolean test(TSLEventContext ctx) {
        TSL tsl = ctx.getTsl();
        GoodieObject eventArgs = ctx.getEventArgs();
        Object left = tsl.getEventFieldExtractor(fieldName).apply(eventArgs).orElse(null);
        return comparator.compare(left);
    }

}
