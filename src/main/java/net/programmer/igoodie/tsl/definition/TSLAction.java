package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.registry.TSLRegistrable;
import net.programmer.igoodie.tsl.runtime.TSLRule;
import net.programmer.igoodie.util.StringUtilities;

import java.util.List;

public abstract class TSLAction extends TSLDefinition implements TSLRegistrable {

    public TSLAction(TSLPlugin plugin, String name) {
        super(plugin, StringUtilities.upperSnake(name));
    }

    @Override
    public String getRegistryId() {
        return getName();
    }

    public abstract String getUsage();

    public abstract void validateTokens(List<TSLToken> arguments, TSLRule rule, TSLParser parser) throws TSLSyntaxError;

    public abstract void perform(List<TSLToken> arguments, TSLContext context);

}
