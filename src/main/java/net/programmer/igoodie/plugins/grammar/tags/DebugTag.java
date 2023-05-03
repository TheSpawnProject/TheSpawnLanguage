package net.programmer.igoodie.plugins.grammar.tags;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCoreAttributes;
import net.programmer.igoodie.tsl.definition.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.legacy.parser.token.TSLPlainWord;
import net.programmer.igoodie.legacy.parser.token.TSLToken;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DebugTag extends TSLTag {

    public static final DebugTag INSTANCE = new DebugTag();

    private DebugTag() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "DEBUG");
    }

    @Override
    public @NotNull GoodieObject generateAttributes(TSLContext context, TSLPlainWord tagName, List<TSLToken> arguments) throws TSLRuntimeError {
        GoodieObject attributes = new GoodieObject();
        TSLGrammarCoreAttributes.DEBUG_MODE_ATTR.set(attributes, true);
        return attributes;
    }

}
