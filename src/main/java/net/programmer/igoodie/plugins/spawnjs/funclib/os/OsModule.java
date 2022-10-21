package net.programmer.igoodie.plugins.spawnjs.funclib.os;

import net.programmer.igoodie.plugins.spawnjs.SpawnJS;
import net.programmer.igoodie.tsl.definition.TSLFunctionLibrary;
import net.programmer.igoodie.tsl.exception.TSLExpressionException;
import net.programmer.igoodie.tsl.function.TSLFunction;
import net.programmer.igoodie.tsl.function.scope.JSScope;
import net.programmer.igoodie.tsl.runtime.TSLContext;
import org.mozilla.javascript.ScriptableObject;

import java.nio.ByteOrder;

public class OsModule extends TSLFunctionLibrary {

    public OsModule(String name) {
        super(SpawnJS.PLUGIN_INSTANCE, name);
    }

    private static String getOsName() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return "windows";

        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return "linux";

        } else if (osName.contains("mac")) {
            return "mac";

        } else if (osName.contains("sunos")) {
            return "solaris";
        }

        return "";
    }

    private static String getEndianness() {
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN)) {
            return "BE";
        } else {
            return "LE";
        }
    }

    @Override
    public void composeLibrary(ScriptableObject object) {
        registerConst(object, "EOL", getOsName().equals("windows") ? "\r\n" : "\n");
        registerConst(object, "devNull", getOsName().equals("windows") ? "\\\\.\\nul" : "/dev/null");

        // platform()
        registerFunction(object, new TSLFunction() {
            @Override
            public String getName() {
                return "platform";
            }

            @Override
            public Object call(TSLContext context, JSScope scope, Object... arguments) throws TSLExpressionException {
                return getOsName();
            }
        });

        // endianness()
        registerFunction(object, new TSLFunction() {
            @Override
            public String getName() {
                return "endianness";
            }

            @Override
            public Object call(TSLContext context, JSScope scope, Object... arguments) throws TSLExpressionException {
                return getEndianness();
            }
        });

        // homedir
        registerFunction(object, new TSLFunction() {
            @Override
            public String getName() {
                return "homedir";
            }

            @Override
            public Object call(TSLContext context, JSScope scope, Object... arguments) throws TSLExpressionException {
                return System.getProperty("user.home");
            }
        });
    }

}
