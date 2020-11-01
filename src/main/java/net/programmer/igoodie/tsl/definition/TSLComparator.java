package net.programmer.igoodie.tsl.definition;

public abstract class TSLComparator extends TSLDefinition {

    public TSLComparator(String name) {
        super(name);
    }

    public abstract boolean compare(Object lefthand, Object righthand);

}
