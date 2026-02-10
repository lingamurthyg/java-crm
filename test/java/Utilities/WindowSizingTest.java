package Utilities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for WindowSizing
 */
public class WindowSizingTest {

    @Test
    public void testWindowSizingClassExists() {
        assertNotNull(WindowSizing.class);
    }

    @Test
    public void testHasGetWidthMethod() {
        try {
            WindowSizing.class.getMethod("getWidth", javafx.stage.Stage.class);
        } catch (NoSuchMethodException e) {
            fail("getWidth method should exist");
        }
    }

    @Test
    public void testHasGetHeightMethod() {
        try {
            WindowSizing.class.getMethod("getHeight", javafx.stage.Stage.class);
        } catch (NoSuchMethodException e) {
            fail("getHeight method should exist");
        }
    }

    @Test
    public void testHasSetMinimumsMethod() {
        try {
            WindowSizing.class.getMethod("setMinimums", javafx.stage.Stage.class);
        } catch (NoSuchMethodException e) {
            fail("setMinimums method should exist");
        }
    }

    @Test
    public void testHasPrepStageMethod() {
        try {
            WindowSizing.class.getMethod("prepStage", javafx.stage.Stage.class);
        } catch (NoSuchMethodException e) {
            fail("prepStage method should exist");
        }
    }

    @Test
    public void testHasSetAlertCoordinatesMethod() {
        try {
            WindowSizing.class.getMethod("setAlertCoordinates", javafx.stage.Stage.class, javafx.scene.control.Alert.class);
        } catch (NoSuchMethodException e) {
            fail("setAlertCoordinates method should exist");
        }
    }
}
