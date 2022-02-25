package net.programmer.igoodie.tsl.definition.attribute;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.exception.TSLInternalError;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.snippet.TSLTagSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.registry.TSLRegistrable;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// #! TAG_NAME ARG1 ARG2
public abstract class TSLTag extends TSLAttributeGenerator implements TSLRegistrable {

    public TSLTag(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    @Override
    public String getRegistryId() {
        return getName();
    }

    @NotNull
    @Override
    public final GoodieObject generateAttributes(TSLContext context, List<TSLToken> tokens) throws TSLRuntimeError {
        if (tokens.isEmpty()) {
            throw new TSLInternalError("Need at least one token");
        }

        TSLToken tagName = tokens.get(0);

        if (!(tagName instanceof TSLPlainWord)) {
            throw new TSLInternalError("Expected tag name to be a TSL String", tagName);
        }

        List<TSLToken> arguments = tokens.subList(1, tokens.size());

        return generateTagAttributes(context, ((TSLPlainWord) tagName), arguments);
    }

    @NotNull
    public abstract GoodieObject generateTagAttributes(TSLContext context, TSLPlainWord tagName, List<TSLToken> arguments) throws TSLRuntimeError;

    public void onLoaded(@NotNull TSLRuleset ruleset, TSLTagSnippet snippet) {}

}
