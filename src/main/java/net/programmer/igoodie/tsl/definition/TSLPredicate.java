package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.registry.TSLRegistrable;

import java.util.List;

public abstract class TSLPredicate extends TSLDefinition implements TSLRegistrable {

    public TSLPredicate(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    @Override
    public String getRegistryId() {
        return getName();
    }

    public abstract boolean formatMatches(List<TSLToken> tokens);

    public abstract boolean satisfies(TSLContext context, List<TSLToken> tokens);

}
