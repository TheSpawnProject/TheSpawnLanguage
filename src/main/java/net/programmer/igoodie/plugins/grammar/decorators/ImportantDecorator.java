package net.programmer.igoodie.plugins.grammar.decorators;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.TSLDecorator;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.legacy.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ImportantDecorator extends TSLDecorator {

    public ImportantDecorator(TSLPlugin plugin, String name) {
        // TODO: implement this bad boi
        super(plugin, name);
    }

    @Override
    public @NotNull GoodieObject generateAttributes(TSLContext context, List<TSLToken> arguments) throws TSLRuntimeError {
        return new GoodieObject();
    }

}
