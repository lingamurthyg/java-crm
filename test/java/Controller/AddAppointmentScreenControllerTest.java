package Controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AddAppointmentScreenController
 */
public class AddAppointmentScreenControllerTest {

    private AddAppointmentScreenController controller;

    @BeforeEach
    public void setUp() {
        controller = new AddAppointmentScreenController();
    }

    @Test
    public void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    public void testAddAppointmentScreenControllerIsGeneralController() {
        assertTrue(controller instanceof GeneralController);
    }

    @Test
    public void testAddAppointmentScreenControllerClassExists() {
        assertNotNull(AddAppointmentScreenController.class);
    }

    @Test
    public void testHasOnActionSaveMethod() {
        try {
            AddAppointmentScreenController.class.getDeclaredMethod("onActionSave", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionSave method should exist");
        }
    }

    @Test
    public void testHasOnActionReturnMethod() {
        try {
            AddAppointmentScreenController.class.getDeclaredMethod("onActionReturn", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionReturn method should exist");
        }
    }

    @Test
    public void testHasOnKeyPressedSubmitMethod() {
        try {
            AddAppointmentScreenController.class.getDeclaredMethod("onKeyPressedSubmit", javafx.scene.input.KeyEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onKeyPressedSubmit method should exist");
        }
    }

    @Test
    public void testImplementsInitializable() {
        assertTrue(javafx.fxml.Initializable.class.isAssignableFrom(AddAppointmentScreenController.class));
    }
}
