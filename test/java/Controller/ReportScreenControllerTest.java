package Controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ReportScreenController
 */
public class ReportScreenControllerTest {

    private ReportScreenController controller;

    @BeforeEach
    public void setUp() {
        controller = new ReportScreenController();
    }

    @Test
    public void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    public void testReportScreenControllerIsGeneralController() {
        assertTrue(controller instanceof GeneralController);
    }

    @Test
    public void testReportScreenControllerClassExists() {
        assertNotNull(ReportScreenController.class);
    }

    @Test
    public void testHasOnActionGenerateReportMethod() {
        try {
            ReportScreenController.class.getDeclaredMethod("onActionGenerateReport", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionGenerateReport method should exist");
        }
    }

    @Test
    public void testHasOnActionReturnMethod() {
        try {
            ReportScreenController.class.getDeclaredMethod("onActionReturn", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionReturn method should exist");
        }
    }

    @Test
    public void testImplementsInitializable() {
        assertTrue(javafx.fxml.Initializable.class.isAssignableFrom(ReportScreenController.class));
    }
}
