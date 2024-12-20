package net.programmer.igoodie.tsl.runtime.predicate;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.tsl.runtime.event.TSLEvent;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;

import java.util.function.BiPredicate;

public class TSLPredicate implements BiPredicate<TSLRule, TSLEventContext> {

    protected final String fieldName;
    protected final TSLComparator comparator;

    public TSLPredicate(String fieldName, TSLComparator comparator) {
        this.fieldName = fieldName;
        this.comparator = comparator;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public boolean test(TSLRule rule, TSLEventContext ctx) {
        GoodieObject eventArgs = ctx.getEventArgs();
        TSLEvent.Property<?> property = rule.getEvent().getPropertyType(fieldName);
        Object left = property.read(eventArgs).orElse(null);
        return comparator.compare(left);
    }

}
