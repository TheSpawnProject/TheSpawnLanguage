package net.programmer.igoodie.plugins.library;

import net.programmer.igoodie.tsl.function.binding.JSLibraryBinding;
import org.mozilla.javascript.annotations.JSFunction;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TSLUtilitiesLibrary extends JSLibraryBinding {

    @Override
    public String getName() {
        return "_";
    }

    @JSFunction
    public Date now() {
        return new Date();
    }

    @JSFunction
    public Object randomOne(Object... objects) {
        int randomIndex = new Random().nextInt(objects.length);
        return objects[randomIndex];
    }

    @JSFunction
    public Object iff(boolean condition, Object truthy, Object falsy) {
        return condition ? truthy : falsy;
    }

    @JSFunction
    public List<Object> union(List<?>... lists) {
        LinkedList<Object> union = new LinkedList<>();
        for (List<?> list : lists) union.addAll(list);
        return union;
    }

    @JSFunction
    public Number clamp(Number value, Number min, Number max) {
        double valueDouble = value.doubleValue();
        double minDouble = min.doubleValue();
        double maxDouble = max.doubleValue();
        return Math.max(minDouble, Math.min(maxDouble, valueDouble));
    }

}
