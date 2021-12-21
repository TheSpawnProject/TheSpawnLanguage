package net.programmer.igoodie.plugins.grammar.functions;

import net.programmer.igoodie.goodies.util.Couple;
import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.util.IOUtils;
import org.jetbrains.annotations.Nullable;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RunScriptFunction extends TSLFunction {

    public static final RunScriptFunction INSTANCE = new RunScriptFunction(System.getProperty("user.dir"));

    private String rootFolder;
    private boolean rootFolderSet;

    private RunScriptFunction(String relativeFolder) {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "runScript");
        this.rootFolder = relativeFolder;
    }

    /**
     * <b>DO NOT USE!</b>
     * For internal use only.
     */
    public void setRootFolder(String rootFolder) {
        if (this.rootFolderSet)
            throw new IllegalStateException("Root folder is already set.");
        this.rootFolder = rootFolder;
        this.rootFolderSet = true;
    }

    @Override
    public @Nullable List<Couple<String, String>> getCompletionSnippets() {
        return Collections.singletonList(
                new Couple<>(getName() + "(path)"
                        , getName() + "(\"${1:path/to/your/script.js}\")$0")
        );
    }

    @Override
    public Object calculate(Scriptable scope, Object... arguments) throws TSLExpressionException {
        String scriptPath = stringArgument(arguments, 0);

        String readScript = IOUtils.readString(isAbsolutePath(scriptPath)
                ? scriptPath : resolvePath(rootFolder, scriptPath));

        if (readScript == null) {
            return Undefined.instance;
        }

        JSEngine jsEngine = getPlugin().getLanguage().getJsEngine();

        String wrappedScript = String.format("(function(){%s})()",
                readScript.replace("tslReturn", "return"));

        return jsEngine.evaluate(wrappedScript, scope);
    }

    private boolean isAbsolutePath(String path) {
        return new File(path).isAbsolute();
    }

    private String resolvePath(String parentDir, String expr) {
        try {
            File parentFile = new File(parentDir);
            return new File(parentFile, expr).getCanonicalPath();

        } catch (IOException e) {
            return null;
        }
    }

}
