package net.programmer.igoodie.tsl.function.scope;

import net.programmer.igoodie.goodies.registry.Registrable;
import net.programmer.igoodie.goodies.registry.Registry;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.tsl.definition.TSLEvent;
import net.programmer.igoodie.tsl.function.binding.TSLContextGetter;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.mozilla.javascript.NativeObject;

import java.util.Optional;
import java.util.function.Supplier;

public final class TSLScopeMetaVariables {

    private final JSScope scope;
    private final Registry<String, Variable<?, ?>> variables = new Registry<>();

    public final Variable<String, String> scriptFilename = variables.register(new Variable<>(
            "__scriptfilename",
            (variableName, scope, filename) -> scope.putConst(variableName, scope, filename),
            (variableName, scope) -> Optional.ofNullable(scope.get(variableName)).map(Object::toString)));

    public final Variable<String, String> scriptDirectory = variables.register(new Variable<>(
            "__scriptdir",
            (variableName, scope, directory) -> scope.putConst(variableName, scope, directory),
            (variableName, scope) -> Optional.ofNullable(scope.get(variableName)).map(Object::toString)));

    public final Variable<TSLContext, TSLContextGetter> tslContextGetter = variables.register(new Variable<>(
            "__context",
            (variableName, scope, tslContext) -> scope.putConst(variableName, scope, new TSLContextGetter(tslContext)),
            (variableName, scope) -> Optional.ofNullable(scope.get(variableName))
                    .filter(result -> result instanceof TSLContextGetter)
                    .map(result -> ((TSLContextGetter) result))));

    public final Variable<NativeObject, NativeObject> importedLibraries = variables.register(new Variable<>(
            "__importedLibs",
            (variableName, scope, importedLibs) -> scope.putConst(variableName, scope, importedLibs),
            (variableName, scope) -> Optional.ofNullable(scope.get(variableName))
                    .filter(result -> result instanceof NativeObject)
                    .map(result -> ((NativeObject) result))));

    public final Variable<TSLEvent, String> eventName = variables.register(new Variable<>(
            "eventName",
            (variableName, scope, tslEvent) -> scope.putConst(variableName, scope, tslEvent.getName()),
            (variableName, scope) -> Optional.ofNullable(scope.get(variableName)).map(Object::toString)));

    public final Variable<GoodieObject, NativeObject> eventArguments = variables.register(new Variable<>(
            "event",
            (variableName, scope, eventArgs) -> scope.putConst(variableName, scope, TSLEvent.generateEventJsObject(eventArgs)),
            (variableName, scope) -> Optional.ofNullable(scope.get(variableName))
                    .filter(result -> result instanceof NativeObject)
                    .map(result -> ((NativeObject) result))));

    public TSLScopeMetaVariables(JSScope scope) {
        this.scope = scope;
    }

    /* --------------------- */

    @FunctionalInterface
    public interface Setter<I> {
        void set(String variableName, JSScope scope, I input);
    }

    @FunctionalInterface
    public interface Getter<O> {
        O get(String variableName, JSScope scope);
    }

    public class Variable<S, G> implements Registrable<String> {

        protected String name;
        protected Setter<S> setter;
        protected Getter<Optional<G>> getter;

        public Variable(String name,
                        Setter<S> setter,
                        Getter<Optional<G>> getter) {
            this.name = name;
            this.setter = setter;
            this.getter = getter;
        }

        @Override
        public String getId() {
            return null;
        }

        public void define(S input) {
            if (input != null) {
                this.setter.set(name, scope, input);
            }
        }

        public Optional<G> get() {
            return this.getter.get(name, scope);
        }

        public G defineIfAbsent(Supplier<S> definer) {
            Optional<G> currentValue = get();
            if (currentValue.isPresent()) {
                return currentValue.get();
            }

            define(definer.get());
            Optional<G> definedValue = get();
            assert definedValue.isPresent();
            return definedValue.get();
        }

    }

}
