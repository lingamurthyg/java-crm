package Utilities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LogFiles
 */
public class LogFilesTest {

    @Test
    public void testSetupLogger() {
        assertDoesNotThrow(() -> {
            LogFiles.setupLogger();
        });
    }

    @Test
    public void testLogFilesClassExists() {
        assertNotNull(LogFiles.class);
    }

    @Test
    public void testHasSetupLoggerMethod() {
        try {
            LogFiles.class.getMethod("setupLogger");
        } catch (NoSuchMethodException e) {
            fail("setupLogger method should exist");
        }
    }

    @Test
    public void testHasLogUserActivityMethod() {
        try {
            LogFiles.class.getMethod("logUserActivity");
        } catch (NoSuchMethodException e) {
            fail("logUserActivity method should exist");
        }
    }

    @Test
    public void testSetupLoggerDoesNotThrow() {
        assertDoesNotThrow(() -> {
            LogFiles.setupLogger();
        });
    }
}
