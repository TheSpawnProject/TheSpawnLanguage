package net.programmer.igoodie;

import net.programmer.igoodie.goodies.util.StringUtilities;
import net.programmer.igoodie.runtime.action.TSLAction;
import net.programmer.igoodie.runtime.predicate.comparator.TSLComparator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class TSL {

    private final String platformName;
    private final Map<String, TSLAction.Supplier<?>> actionDefinitions;
    private final Map<String, TSLComparator.Supplier<?>> comparatorDefinitions;
    private final Set<String> allowedEventNames;

    public TSL(String platformName) {
        this.platformName = platformName;
        this.actionDefinitions = new HashMap<>();
        this.comparatorDefinitions = new HashMap<>();
        this.allowedEventNames = new HashSet<>();
    }

    public <T extends TSLAction.Supplier<?>> T registerAction(String name, T action) {
        this.actionDefinitions.put(name.toUpperCase(), action);
        return action;
    }

    public <T extends TSLComparator.Supplier<?>> T registerComparator(String symbol, T comparator) {
        this.comparatorDefinitions.put(symbol.toUpperCase(), comparator);
        return comparator;
    }

    public void registerAllowedEvent(String name){
        this.allowedEventNames.add(StringUtilities.upperFirstLetters(name));
    }

}
