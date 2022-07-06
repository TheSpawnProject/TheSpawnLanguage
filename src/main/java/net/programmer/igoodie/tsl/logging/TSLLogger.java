package net.programmer.igoodie.tsl.logging;

import net.programmer.igoodie.tsl.plugin.TSLPlugin;

import java.util.logging.Logger;

public class TSLLogger {

    public static Logger createLogger(TSLPlugin plugin, TSLLogHandler handler) {
        return createLogger(plugin.getManifest().getPluginId(), handler);
    }

    public static Logger createLogger(String namespace, TSLLogHandler handler) {
        Logger logger = Logger.getLogger(namespace);
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        return logger;
    }

}
