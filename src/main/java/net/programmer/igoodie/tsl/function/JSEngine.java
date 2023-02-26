package net.programmer.igoodie.tsl.function;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLFunctionLibrary;
import net.programmer.igoodie.tsl.exception.TSLImportError;
import net.programmer.igoodie.tsl.function.scope.JSScope;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.util.AccessUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.NativeObject;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Rhino syntax compatibility: https://mozilla.github.io/rhino/compat/engines.html
public class JSEngine {

    protected TheSpawnLanguage tsl;
    protected JSScope globalScope;

    public JSEngine(TheSpawnLanguage tsl) {
        this.tsl = tsl;
        this.globalScope = JSContextManager.createGlobalScope(this);
    }

    public JSScope getGlobalScope() {
        return globalScope;
    }

    /* ------------------------ */

    public void defineConst(String name, Object value) {
        if (globalScope.has(name, globalScope)) return;
        globalScope.putConst(name, globalScope, value);
    }

    public void loadCoreLibrary(TSLFunctionLibrary coreLibrary) {
        Class<?> accessedFromClass = AccessUtils.accessedFromClass(JSEngine.class);

        if (accessedFromClass != TheSpawnLanguage.class) {
            throw new TSLImportError("Core JS libraries cannot be loaded externally");
        }

        NativeObject libraryObject = new NativeObject();
        coreLibrary.composeLibrary(libraryObject);

        for (Object key : libraryObject.keySet()) {
            Object value = libraryObject.get(key);
            globalScope.put(key.toString(), globalScope, value);
        }
    }

    /* ------------------------------------ */

    public void loadTSLContext(JSScope scope, TSLContext tslContext) {
        if (tslContext != null) {
            scope.meta().tslContextGetter.define(tslContext);
            scope.meta().eventName.define(tslContext.getEvent());
            scope.meta().eventArguments.define(tslContext.getEventArguments());
            loadPluginLibraries(scope, tslContext.getTsl(), tslContext.getImportedPlugins());
        }
    }

    public void loadPluginLibraries(JSScope scope, TheSpawnLanguage tsl, Map<String, String> importedPlugins) {
        importedPlugins.forEach((alias, pluginId) -> {
            List<TSLFunctionLibrary> associatedModules = tsl.FUNC_LIBRARY_REGISTRY.stream()
                    .map(Map.Entry::getValue)
                    .filter(lib -> lib.getPlugin().getDescriptor().getPluginId().equals(pluginId))
                    .collect(Collectors.toList());

            if (associatedModules.size() == 0) {
                return;
            }

            scope.touchGlobalObject(alias, funcLibObject -> {
                for (TSLFunctionLibrary module : associatedModules) {
                    if (module.getName().equals(TSLFunctionLibrary.ROOT_LIBRARY_NAME)) {
                        module.composeLibrary(funcLibObject);

                    } else {
                        NativeObject moduleObject = new NativeObject();
                        module.composeLibrary(moduleObject);
                        funcLibObject.put(module.getName(), funcLibObject, moduleObject);
                    }
                }

                NativeObject libsMetaObject = scope.meta().importedLibraries.defineIfAbsent(NativeObject::new);
                libsMetaObject.put(alias, libsMetaObject, funcLibObject);
            });
        });
    }

    /* ------------------------------------ */

    public String evaluate(String script) throws EcmaError {
        return evaluate(script, new TSLContext(tsl));
    }

    public String evaluate(String script, TSLContext tslContext) throws EcmaError {
        JSScope scope = Optional
                .ofNullable(tslContext.getJsScope())
                .orElse(globalScope.fork());

        if (!scope.meta().tslContextGetter.get().isPresent()) {
            loadTSLContext(scope, tslContext);
        }

        return evaluate(script, scope);
    }

    public String evaluate(String script, JSScope scope) throws EcmaError {
        String sourceName = "immediate_evaluator";
        Context context = JSContextManager.getThreadContext();
        Object evaluation = context.evaluateString(scope, script, sourceName, 0, null);
        return scope.stringify(evaluation);
    }

}
