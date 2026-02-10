package Controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AddCustomerScreenController
 */
public class AddCustomerScreenControllerTest {

    private AddCustomerScreenController controller;

    @BeforeEach
    public void setUp() {
        controller = new AddCustomerScreenController();
    }

    @Test
    public void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    public void testAddCustomerScreenControllerIsGeneralController() {
        assertTrue(controller instanceof GeneralController);
    }

    @Test
    public void testAddCustomerScreenControllerClassExists() {
        assertNotNull(AddCustomerScreenController.class);
    }

    @Test
    public void testHasOnActionSaveMethod() {
        try {
            AddCustomerScreenController.class.getDeclaredMethod("onActionSave", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionSave method should exist");
        }
    }

    @Test
    public void testHasOnActionReturnMethod() {
        try {
            AddCustomerScreenController.class.getDeclaredMethod("onActionReturn", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionReturn method should exist");
        }
    }

    @Test
    public void testHasOnKeyPressedSubmitMethod() {
        try {
            AddCustomerScreenController.class.getDeclaredMethod("onKeyPressedSubmit", javafx.scene.input.KeyEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onKeyPressedSubmit method should exist");
        }
    }
}
