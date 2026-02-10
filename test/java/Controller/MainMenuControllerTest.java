package Controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MainMenuController
 */
public class MainMenuControllerTest {

    private MainMenuController controller;

    @BeforeEach
    public void setUp() {
        controller = new MainMenuController();
    }

    @Test
    public void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    public void testMainMenuControllerIsGeneralController() {
        assertTrue(controller instanceof GeneralController);
    }

    @Test
    public void testMainMenuControllerClassExists() {
        assertNotNull(MainMenuController.class);
    }

    @Test
    public void testHasOnActionDisplayCustomersMethod() {
        try {
            MainMenuController.class.getDeclaredMethod("onActionDisplayCustomers", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionDisplayCustomers method should exist");
        }
    }

    @Test
    public void testHasOnActionDisplayAppointmentsMethod() {
        try {
            MainMenuController.class.getDeclaredMethod("onActionDisplayAppointments", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionDisplayAppointments method should exist");
        }
    }

    @Test
    public void testHasOnActionDisplayReportsMethod() {
        try {
            MainMenuController.class.getDeclaredMethod("onActionDisplayReports", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionDisplayReports method should exist");
        }
    }

    @Test
    public void testHasOnActionLogoutMethod() {
        try {
            MainMenuController.class.getDeclaredMethod("onActionLogout", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionLogout method should exist");
        }
    }
}
