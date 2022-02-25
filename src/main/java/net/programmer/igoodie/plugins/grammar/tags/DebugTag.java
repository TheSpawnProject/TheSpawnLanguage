package net.programmer.igoodie.plugins.grammar.tags;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.definition.attribute.TSLAttribute;
import net.programmer.igoodie.tsl.definition.attribute.TSLTag;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.token.TSLPlainWord;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DebugTag extends TSLTag {

    public static final DebugTag INSTANCE = new DebugTag();

    public static final TSLAttribute DEBUG_MODE_ATTR
            = new TSLAttribute(INSTANCE.getPlugin(), "debug_mode");

    private DebugTag() {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "DEBUG");
    }

    @Override
    public @NotNull GoodieObject generateTagAttributes(TSLContext context, TSLPlainWord tagName, List<TSLToken> arguments) throws TSLRuntimeError {
        GoodieObject attributes = new GoodieObject();
        DEBUG_MODE_ATTR.set(attributes, true);
        return attributes;
    }

}
