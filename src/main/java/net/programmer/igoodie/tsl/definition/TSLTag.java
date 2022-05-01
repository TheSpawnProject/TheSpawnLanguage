package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.base.TSLDefinition;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.snippet.TSLTagSnippet;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// #! TAG_NAME ARG1 ARG2
public abstract class TSLTag extends TSLDefinition {

    public TSLTag(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    public void onLoaded(@NotNull TSLRuleset ruleset, TSLTagSnippet snippet) {}

    @NotNull
    public abstract GoodieObject generateAttributes(TSLContext context, TSLPlainWord tagName, List<TSLToken> arguments) throws TSLRuntimeError;

}
