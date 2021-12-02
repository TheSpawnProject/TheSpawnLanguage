package net.programmer.igoodie.tsl.definition;

import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.binding.JSFunctionBinding;
import net.programmer.igoodie.tsl.plugin.TSLPlugin;
import net.programmer.igoodie.tsl.registry.TSLRegistrable;
import net.programmer.igoodie.util.ArrayAccessor;
import net.programmer.igoodie.util.TypeUtilities;
import org.mozilla.javascript.Scriptable;

public abstract class TSLFunction extends TSLDefinition implements TSLRegistrable {

    public TSLFunction(TSLPlugin plugin, String name) {
        super(plugin, name);
    }

    @Override
    public String getRegistryId() {
        return getName();
    }

    public abstract Object calculate(Scriptable scope, Object... arguments) throws TSLExpressionException;

    public JSFunctionBinding getBinding() {
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
        Object element = ArrayAccessor.of(args).getOrDefault(index, null);
        if (element instanceof String) return ((String) element);
        throw new TSLExpressionException("Expected string argument at index:" + index);
    }

    protected Number numberArgument(Object[] args, int index) {
        Object element = ArrayAccessor.of(args).getOrDefault(index, null);
        if (element != null && TypeUtilities.isNumeric(element.getClass())) return ((Number) element);
        throw new TSLExpressionException("Expected number argument at index:" + index);
    }

}
