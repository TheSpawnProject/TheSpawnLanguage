package net.programmer.igoodie.tsl.definition.attribute;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.TSLDefinition;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.util.GoodieUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class TSLAttributeGenerator extends TSLDefinition {

    // Default accessor; disallow external usage.
    TSLAttributeGenerator(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    public final GoodieObject generateAttributesWithNamespace(List<TSLToken> tokens) {
        TSLPlugin plugin = getPlugin();
        GoodieObject generateAttributes = generateAttributes(tokens);
        return GoodieUtils.mapKeys(generateAttributes, plugin::prependNamespace);
    }

    @NotNull
    public abstract GoodieObject generateAttributes(List<TSLToken> tokens) throws TSLRuntimeError;

}
