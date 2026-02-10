package Controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ViewCustomerScreenController
 */
public class ViewCustomerScreenControllerTest {

    private ViewCustomerScreenController controller;

    @BeforeEach
    public void setUp() {
        controller = new ViewCustomerScreenController();
    }

    @Test
    public void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    public void testViewCustomerScreenControllerIsGeneralController() {
        assertTrue(controller instanceof GeneralController);
    }

    @Test
    public void testViewCustomerScreenControllerClassExists() {
        assertNotNull(ViewCustomerScreenController.class);
    }

    @Test
    public void testHasOnActionScheduleApptMethod() {
        try {
            ViewCustomerScreenController.class.getDeclaredMethod("onActionScheduleAppt", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionScheduleAppt method should exist");
        }
    }

    @Test
    public void testHasOnActionAddCustomerMethod() {
        try {
            ViewCustomerScreenController.class.getDeclaredMethod("onActionAddCustomer", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionAddCustomer method should exist");
        }
    }

    @Test
    public void testHasOnActionUpdateCustomerMethod() {
        try {
            ViewCustomerScreenController.class.getDeclaredMethod("onActionUpdateCustomer", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionUpdateCustomer method should exist");
        }
    }

    @Test
    public void testHasOnActionDeleteCustomerMethod() {
        try {
            ViewCustomerScreenController.class.getDeclaredMethod("onActionDeleteCustomer", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionDeleteCustomer method should exist");
        }
    }

    @Test
    public void testHasOnActionReturnMethod() {
        try {
            ViewCustomerScreenController.class.getDeclaredMethod("onActionReturn", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionReturn method should exist");
        }
    }

    @Test
    public void testImplementsInitializable() {
        assertTrue(javafx.fxml.Initializable.class.isAssignableFrom(ViewCustomerScreenController.class));
    }
}
