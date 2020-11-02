package net.programmer.igoodie.tsl.runtime.node;

import com.google.gson.JsonObject;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.TSLComparator;
import net.programmer.igoodie.tsl.definition.TSLEventField;
import net.programmer.igoodie.tsl.parser.token.TSLToken;

public class PredicateNode implements RuleNode {

    protected TSLEventField<?> field;
    protected TSLComparator comparator;
    protected TSLToken righthand;

    protected RuleNode nextNode;

    public PredicateNode(TSLEventField<?> field, TSLComparator comparator, TSLToken righthand) {
        this.field = field;
        this.comparator = comparator;
        this.righthand = righthand;
    }

    public TSLEventField<?> getField() {
        return field;
    }

    public TSLComparator getComparator() {
        return comparator;
    }

    public void setNextNode(RuleNode nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public RuleNode getNext() {
        return nextNode;
    }

    @Override
    public boolean proceed(TSLContext context) {
        JsonObject eventArguments = context.getEventArguments();

        Object lefthand = field.extractValue(eventArguments);
        String righthand = this.righthand.evaluate(context);

        if (!comparator.compare(lefthand, righthand))
            return false;

        return nextNode.proceed(context);
    }

}
