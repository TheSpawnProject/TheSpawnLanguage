package net.programmer.igoodie.plugins.grammar.functions;

import net.programmer.igoodie.plugins.grammar.TSLGrammarCore;
import net.programmer.igoodie.tsl.definition.TSLFunction;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.JSEngine;
import net.programmer.igoodie.tsl.util.IOUtils;
import org.mozilla.javascript.Undefined;

import java.io.File;
import java.io.IOException;

public class RunFunction extends TSLFunction {

    public static final RunFunction INSTANCE = new RunFunction(System.getProperty("user.dir"));

    private String relativeFolder;

    private RunFunction(String relativeFolder) {
        super(TSLGrammarCore.PLUGIN_INSTANCE, "run");
        this.relativeFolder = relativeFolder;
    }

    @Override
    public Object calculate(Object... arguments) throws TSLExpressionException {
        String scriptPath = stringArgument(arguments, 0);

//        System.out.println("Reading for JS: " + (isAbsolutePath(scriptPath)
//                ? scriptPath : resolvePath(relativeFolder, scriptPath)));

        String readScript = IOUtils.readString(isAbsolutePath(scriptPath)
                ? scriptPath : resolvePath(relativeFolder, scriptPath));

        if (readScript == null) {
            return Undefined.instance;
        }

        JSEngine jsEngine = getPlugin().getLanguage().getJsEngine();

        return jsEngine.evaluate(readScript);
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
