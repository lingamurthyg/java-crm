package Controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UpdateCustomerScreenController
 */
public class UpdateCustomerScreenControllerTest {

    private UpdateCustomerScreenController controller;

    @BeforeEach
    public void setUp() {
        controller = new UpdateCustomerScreenController();
    }

    @Test
    public void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    public void testUpdateCustomerScreenControllerIsGeneralController() {
        assertTrue(controller instanceof GeneralController);
    }

    @Test
    public void testUpdateCustomerScreenControllerClassExists() {
        assertNotNull(UpdateCustomerScreenController.class);
    }

    @Test
    public void testHasOnActionSaveMethod() {
        try {
            UpdateCustomerScreenController.class.getDeclaredMethod("onActionSave", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionSave method should exist");
        }
    }

    @Test
    public void testHasOnActionReturnMethod() {
        try {
            UpdateCustomerScreenController.class.getDeclaredMethod("onActionReturn", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionReturn method should exist");
        }
    }

    @Test
    public void testHasOnKeyPressedSubmitMethod() {
        try {
            UpdateCustomerScreenController.class.getDeclaredMethod("onKeyPressedSubmit", javafx.scene.input.KeyEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onKeyPressedSubmit method should exist");
        }
    }

    @Test
    public void testImplementsInitializable() {
        assertTrue(javafx.fxml.Initializable.class.isAssignableFrom(UpdateCustomerScreenController.class));
    }
}
