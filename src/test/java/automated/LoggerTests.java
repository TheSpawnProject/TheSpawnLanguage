package automated;

import net.programmer.igoodie.tsl.logging.TSLLogHandler;
import net.programmer.igoodie.tsl.logging.TSLLogger;
import org.junit.jupiter.api.Test;
import util.OsUtils;

import java.io.File;

public class LoggerTests {

    @Test
    public void shouldLogToFolder() {
        if (OsUtils.getOS() != OsUtils.OS.WINDOWS) return; // Only run for Windows

        String dataFolder = System.getenv("APPDATA");
        String logFolder = dataFolder + File.separator + ".igoodie/TSL/logs/iGoodie";

        TSLLogHandler logHandler = new TSLLogHandler(new File(logFolder), "iGoodie")
                .hookConsoleLog()
                .historyLimit(2);

        TSLLogger logger = TSLLogger.createLogger("iGoodie", logHandler);

        logger.info("Hello world!");
        logger.warn("Mayday Mayday!");

        logger.error("{} {} {}", 1, 2, 3);
        logger.error("{3} {2} {1} {0}", 1, 2, 3);
        logger.error("%s {}", "Testinggg", 999);
    }

}
