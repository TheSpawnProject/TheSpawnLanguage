package automated;

import net.programmer.igoodie.tsl.logging.TSLLogHandler;
import net.programmer.igoodie.tsl.logging.TSLLogger;
import org.junit.jupiter.api.Test;
import util.OsUtils;

import java.io.File;
import java.util.logging.Logger;

public class LoggerTests {

    @Test
    public void shouldLogToFolder() {
        if (OsUtils.getOS() != OsUtils.OS.WINDOWS) return; // Only run for Windows

        String dataFolder = System.getenv("APPDATA");
        String logFolder = dataFolder + File.separator + ".igoodie/TSL/iGoodie";

        TSLLogHandler logHandler = new TSLLogHandler(new File(logFolder), "iGoodie")
                .hookConsoleLog()
                .historyLimit(2);

        Logger logger = TSLLogger.createLogger("iGoodie", logHandler);

        logger.info("Hello world!");
        logger.warning("Mayday Mayday!");
    }

}
