package net.programmer.igoodie.plugins.spawnjs.corelib.functions;

import net.programmer.igoodie.tsl.TheSpawnLanguage;
import net.programmer.igoodie.tsl.definition.TSLFunctionLibrary;
import net.programmer.igoodie.tsl.definition.base.TSLArguments;
import net.programmer.igoodie.tsl.exception.TSLExpressionError;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.function.TSLFunction;
import net.programmer.igoodie.tsl.function.scope.JSScope;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import net.programmer.igoodie.tsl.util.IOUtils;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Undefined;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class RequireFunction extends TSLFunction {

    public static final RequireFunction INSTANCE = new RequireFunction();

    @Override
    public String getName() {
        return "require";
    }

    @Override
    public Object call(TSLContext context, JSScope scope, Object... arguments) throws TSLExpressionError {
        String moduleArgument = requiredArgument(TSLArguments.STRING, arguments, 0);
//        String moduleArgument = stringArgument(arguments, 0);

        Object resolvedLibrary = tryResolvingLibrary(context, moduleArgument);

        if (resolvedLibrary != null) {
            return resolvedLibrary;
        }

        Object resolvedFile = tryResolvingFile(context, scope, moduleArgument);

        if (resolvedFile != null) {
            return resolvedFile;
        }

        return Undefined.instance;
    }

    private Object tryResolvingLibrary(TSLContext context, String moduleArgument) {
        TheSpawnLanguage tsl = context.getTsl();

        String[] modulePaths = moduleArgument.split("/");

        String namespace = requiredArgument(TSLArguments.STRING, modulePaths, 0);
        String libraryName = namespace.contains(":") ? namespace
                : namespace + ":" + TSLFunctionLibrary.ROOT_LIBRARY_NAME;

        TSLFunctionLibrary library = tsl.getFunctionLibrary(libraryName);

        if (library == null) {
            return null;
        }

        NativeObject importModule = new NativeObject();
        library.composeLibrary(importModule);

        for (int i = 1; i < modulePaths.length; i++) {
            String path = modulePaths[i];
            Object value = importModule.get(path);
            if (!(value instanceof NativeObject)) {
                return null;
            }
            importModule = ((NativeObject) value);
        }

        return importModule;
    }

    private Object tryResolvingFile(TSLContext context, JSScope scope, String moduleArgument) {
        JSEngine jsEngine = context.getTsl().getJsEngine();

        String scriptPath = isAbsolutePath(moduleArgument)
                ? moduleArgument
                : resolveRelativePath(context, scope, moduleArgument);

        if (scriptPath == null) {
            throw new TSLExpressionError("Cannot resolve module from -> " + moduleArgument);
        }

        File scriptFile = new File(getFileExtension(scriptPath).isPresent()
                ? scriptPath
                : scriptPath + ".js");

        if (!scriptFile.exists()) return null;

        String script = IOUtils.readString(scriptFile);
        JSScope moduleScope = jsEngine.getGlobalScope().fork();

        moduleScope.meta().scriptDirectory.define(scriptFile.getParent());
        moduleScope.meta().scriptFilename.define(scriptFile.getName());
        jsEngine.loadTSLContext(moduleScope, context);

        try {
            jsEngine.evaluate(script, moduleScope);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return moduleScope.get("exports");
    }

    private boolean isAbsolutePath(String path) {
        return new File(path).isAbsolute();
    }

    private String resolveRelativePath(TSLContext context, JSScope scope, String expr) {
        try {
            File parentFile = scope.meta().scriptDirectory.get()
                    .map(File::new)
                    .orElse(context.getBaseDir() != null
                            ? context.getBaseDir()
                            : new File(System.getProperty("user.dir")));

            return new File(parentFile, expr).getCanonicalPath();

        } catch (IOException e) {
            return null;
        }
    }

    public Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

}
