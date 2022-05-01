package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.base.TSLDefinition;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// @decorator(arg1,arg2)
public abstract class TSLDecorator extends TSLDefinition {

    public TSLDecorator(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    @NotNull
    // TODO: turn arguments into List<TSLToken>
    public abstract GoodieObject generateAttributes(TSLContext context, List<String> arguments) throws TSLRuntimeError;

}
