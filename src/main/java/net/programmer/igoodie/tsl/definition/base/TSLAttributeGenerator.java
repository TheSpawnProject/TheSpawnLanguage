package net.programmer.igoodie.tsl.definition.base;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.util.GoodieUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class TSLAttributeGenerator extends TSLDefinition {

    public TSLAttributeGenerator(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    public final GoodieObject generateAttributesWithNamespace(TSLContext context, List<TSLToken> tokens) {
        TSLPlugin plugin = getPlugin();
        GoodieObject generatedAttributes = generateAttributes(context, tokens);

        if (plugin instanceof TSLGrammarCore) {
            return generatedAttributes;
        } else {
            return GoodieUtils.mapKeys(generatedAttributes, plugin::prependNamespace);
        }
    }

    @NotNull
    public abstract GoodieObject generateAttributes(TSLContext context, List<TSLToken> tokens) throws TSLRuntimeError;

}
