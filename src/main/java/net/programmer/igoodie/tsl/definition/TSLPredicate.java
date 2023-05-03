package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.base.TSLDefinition;
import net.programmer.igoodie.tsl.exception.TSLSyntaxError;
import net.programmer.igoodie.legacy.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.runtime.TSLContext;

import java.util.List;

public abstract class TSLPredicate extends TSLDefinition {

    public TSLPredicate(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    public abstract boolean formatMatches(TheSpawnLanguage tsl, TSLEvent event, List<TSLToken> tokens) throws TSLSyntaxError;

    public abstract boolean satisfies(TSLContext context, List<TSLToken> tokens);

}
