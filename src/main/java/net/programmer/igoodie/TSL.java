package net.programmer.igoodie;

import net.programmer.igoodie.node.action.TSLAction;
import net.programmer.igoodie.node.predicate.comparator.TSLComparator;

import java.util.HashMap;
import java.util.Map;

public final class TSL {

    private final String platformName;
    private final Map<String, TSLAction.Supplier<?>> actionDefinitions;
    private final Map<String, TSLComparator.Supplier<?>> comparatorDefinitions;

    public TSL(String platformName) {
        this.platformName = platformName;
        this.actionDefinitions = new HashMap<>();
        this.comparatorDefinitions = new HashMap<>();
    }

    public <T extends TSLAction.Supplier<?>> T registerAction(String name, T action) {
        this.actionDefinitions.put(name.toUpperCase(), action);
        return action;
    }

    public <T extends TSLComparator.Supplier<?>> T registerComparator(String symbol, T comparator) {
        this.comparatorDefinitions.put(symbol.toUpperCase(), comparator);
        return comparator;
    }

}
