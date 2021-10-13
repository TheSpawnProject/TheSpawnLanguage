package net.programmer.igoodie.tsl.definition.attribute;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.exception.TSLInternalError;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.token.TSLString;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.registry.TSLRegistrable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
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
    public final GoodieObject generateAttributes(List<TSLToken> tokens) throws TSLRuntimeError {
        if (tokens.isEmpty()) {
            throw new TSLInternalError("Need at least one token");
        }

        TSLToken tagName = tokens.get(0);

        if (!(tagName instanceof TSLString)) {
            throw new TSLInternalError("Expected tag name to be a TSL String", tagName);
        }

        List<TSLToken> arguments = tokens.subList(1, tokens.size());
        List<TSLString> stringArguments = new LinkedList<>();

        for (TSLToken argument : arguments) {
            if (!(argument instanceof TSLString)) {
                throw new TSLInternalError("Expected argument to be a TSL String", argument);
            }
            stringArguments.add(((TSLString) argument));
        }

        return generateTagAttributes(((TSLString) tagName), stringArguments);
    }

    @NotNull
    public abstract GoodieObject generateTagAttributes(TSLString tagName, List<TSLString> arguments) throws TSLRuntimeError;

}
