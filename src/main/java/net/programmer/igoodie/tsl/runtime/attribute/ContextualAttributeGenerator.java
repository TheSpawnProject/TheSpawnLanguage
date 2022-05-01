package net.programmer.igoodie.tsl.runtime.attribute;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.jetbrains.annotations.NotNull;

public interface ContextualAttributeGenerator extends AttributeGenerator {

    @NotNull
    GoodieObject generateAttributes(TSLContext context);

}
