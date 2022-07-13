package net.programmer.igoodie.tsl.runtime;

import net.programmer.igoodie.goodies.util.StringUtilities;

import java.util.*;

public class TSLReservedNames {

    public static final List<String> PREDEFINED_NAMES = Collections.unmodifiableList(Arrays.asList(
            "TSL", // Reserved by The Spawn Language
            "ON", "FROM", "WITH", // Reserved by the ruleset parts
            "EVENT", // Reserved by event arguments on JS ${event.actor}
            "__context", // Reserved by inner JS workings to pass TSLContext everywhere
            "__importedLibs" // Reserved by inner JS workings to store loaded function libraries

    ));

    protected Set<String> reservedNames; // UPPER_SNAKE_CASE

    public TSLReservedNames() {
        this.reservedNames = new HashSet<>(PREDEFINED_NAMES);
    }

    public void reserve(String name) {
        this.reservedNames.add(StringUtilities.upperSnake(name));
    }

    public boolean isReserved(String name) {
        return this.reservedNames.contains(StringUtilities.upperSnake(name));
    }

}
