package net.programmer.igoodie.tsl.function;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TSLDebugLibrary {

    private JSEngine jsEngine;

    public TSLDebugLibrary(JSEngine jsEngine) {
        this.jsEngine = jsEngine;
    }

    public String typeOf(Object object) {
        return String.format("DebugType: [%s] - [%s]",
                object.getClass().getSimpleName(),
                object.toString());
    }

    public List<String> fields(Object object) {
        return Arrays.stream(object.getClass().getFields())
                .map(Field::toString)
                .collect(Collectors.toList());
    }

    public List<String> methods(Object object) {
        return Arrays.stream(object.getClass().getMethods())
                .map(Method::toString)
                .collect(Collectors.toList());
    }

}
