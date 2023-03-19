package net.programmer.igoodie.plugins.grammar;

import net.programmer.igoodie.tsl.runtime.attribute.TSLAttribute;

public class TSLGrammarCoreAttributes {

    public static final TSLAttribute<Boolean> DEBUG_MODE_ATTR = new TSLAttribute<>(TSLGrammarCore.PLUGIN_INSTANCE, "debug_mode",
            goodieElement -> goodieElement.asPrimitive().getBoolean());

}
