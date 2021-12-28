package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.goodies.util.TypeUtilities;
import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.tsl.context.TSLContext;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.binding.JSFunctionBinding;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.registry.TSLRegistrable;
import org.mozilla.javascript.Scriptable;

import java.util.Optional;

public abstract class TSLFunction extends TSLDefinition implements TSLRegistrable {

    public TSLFunction(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    @Override
    public final String getRegistryId() {
        return getName();
    }

    public abstract Object calculate(TSLContext tslContext, Scriptable scope, Object... arguments) throws TSLExpressionException;

    public final JSFunctionBinding getBinding() {
        return new JSFunctionBinding() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public Calculator getCalculator() {
                return TSLFunction.this::calculate;
            }
        };
    }

    /* ----------------------------------- */

    protected String stringArgument(Object[] args, int index) {
        return stringArgumentOpt(args, index).orElseThrow(
                () -> new TSLExpressionException("Expected string argument at index:" + index));
    }

    protected Optional<String> stringArgumentOpt(Object[] args, int index) {
        Object element = ArrayAccessor.of(args).get(index);
        return element instanceof String
                ? Optional.of(((String) element))
                : Optional.empty();
    }

    protected Number numberArgument(Object[] args, int index) {
        return numberArgumentOpt(args, index).orElseThrow(
                () -> new TSLExpressionException("Expected number argument at index:" + index));
    }

    protected Optional<Number> numberArgumentOpt(Object[] args, int index) {
        Object element = ArrayAccessor.of(args).get(index);
        return element != null && TypeUtilities.isNumeric(element.getClass())
                ? Optional.of(((Number) element))
                : Optional.empty();
    }

}
