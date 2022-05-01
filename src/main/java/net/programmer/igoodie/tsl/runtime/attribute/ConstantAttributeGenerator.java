package net.programmer.igoodie.tsl.runtime.attribute;

import net.programmer.igoodie.goodies.runtime.GoodieObject;
import org.jetbrains.annotations.NotNull;

public interface ConstantAttributeGenerator extends AttributeGenerator {

    @NotNull
    GoodieObject generateAttributes();

}
