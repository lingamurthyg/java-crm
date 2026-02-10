package Controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LoginScreenController
 */
public class LoginScreenControllerTest {

    private LoginScreenController controller;

    @BeforeEach
    public void setUp() {
        controller = new LoginScreenController();
    }

    @Test
    public void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    public void testLoginScreenControllerIsGeneralController() {
        assertTrue(controller instanceof GeneralController);
    }

    @Test
    public void testLoginScreenControllerClassExists() {
        assertNotNull(LoginScreenController.class);
    }

    @Test
    public void testHasOnActionExitMethod() {
        try {
            LoginScreenController.class.getDeclaredMethod("onActionExit", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionExit method should exist");
        }
    }

    @Test
    public void testHasOnActionLoginMethod() {
        try {
            LoginScreenController.class.getDeclaredMethod("onActionLogin", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionLogin method should exist");
        }
    }

    @Test
    public void testHasOnKeyPressedLoginMethod() {
        try {
            LoginScreenController.class.getDeclaredMethod("onKeyPressedLogin", javafx.scene.input.KeyEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onKeyPressedLogin method should exist");
        }
    }

    @Test
    public void testImplementsInitializable() {
        assertTrue(javafx.fxml.Initializable.class.isAssignableFrom(LoginScreenController.class));
    }
}
