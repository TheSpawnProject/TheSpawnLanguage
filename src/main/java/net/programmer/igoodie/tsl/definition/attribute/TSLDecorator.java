package net.programmer.igoodie.tsl.definition.attribute;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.exception.TSLInternalError;
import net.programmer.igoodie.tsl.exception.TSLRuntimeError;
import net.programmer.igoodie.tsl.parser.token.TSLDecoratorCall;
import net.programmer.igoodie.tsl.parser.token.TSLToken;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.registry.TSLRegistrable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// @decorator(arg1,arg2)
public abstract class TSLDecorator extends TSLAttributeGenerator implements TSLRegistrable {

    public TSLDecorator(TSLPlugin plugin, String name) {
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

        TSLToken token = tokens.get(0);

        if (!(token instanceof TSLDecoratorCall)) {
            throw new TSLInternalError("Passed a token that is not a Decorator to the attribute evaluator.");
        }

        try {
            List<String> arguments = ((TSLDecoratorCall) token).getArgs();
            return generateDecoratorAttributes(arguments);

        } catch (TSLRuntimeError error) {
            throw new TSLRuntimeError(error.getMessage(), token);
        }
    }

    @NotNull
    public abstract GoodieObject generateDecoratorAttributes(List<String> argument) throws TSLRuntimeError;

}
