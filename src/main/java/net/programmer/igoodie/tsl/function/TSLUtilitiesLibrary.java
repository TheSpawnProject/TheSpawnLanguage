package net.programmer.igoodie.tsl.function;

import java.util.*;

public class TSLUtilitiesLibrary {

    public Date now() {
        return new Date();
    }

    public Object randomOne(Object... objects) {
        int randomIndex = new Random().nextInt(objects.length);
        return objects[randomIndex];
    }

    public Object iff(boolean condition, Object truthy, Object falsy) {
        return condition ? truthy : falsy;
    }

    public List<Object> union(List<Object> list1, List<Object> list2) {
        LinkedList<Object> union = new LinkedList<>();
        union.addAll(list1);
        union.addAll(list2);
        return union;
    }

    public Number clamp(Number value, Number min, Number max) {
        double valueDouble = value.doubleValue();
        double minDouble = min.doubleValue();
        double maxDouble = max.doubleValue();
        return Math.max(minDouble, Math.min(maxDouble, valueDouble));
    }

}
