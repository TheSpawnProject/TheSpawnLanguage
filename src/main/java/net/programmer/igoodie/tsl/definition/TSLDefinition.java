package net.programmer.igoodie.tsl.definition;

public abstract class TSLDefinition {

    protected String name;

    public TSLDefinition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
