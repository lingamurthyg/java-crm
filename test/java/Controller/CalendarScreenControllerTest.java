package Controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CalendarScreenController
 */
public class CalendarScreenControllerTest {

    private CalendarScreenController controller;

    @BeforeEach
    public void setUp() {
        controller = new CalendarScreenController();
    }

    @Test
    public void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    public void testCalendarScreenControllerIsGeneralController() {
        assertTrue(controller instanceof GeneralController);
    }

    @Test
    public void testCalendarScreenControllerClassExists() {
        assertNotNull(CalendarScreenController.class);
    }

    @Test
    public void testHasOnActionAddAppointmentMethod() {
        try {
            CalendarScreenController.class.getDeclaredMethod("onActionAddAppointment", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionAddAppointment method should exist");
        }
    }

    @Test
    public void testHasOnActionUpdateAppointmentMethod() {
        try {
            CalendarScreenController.class.getDeclaredMethod("onActionUpdateAppointment", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionUpdateAppointment method should exist");
        }
    }

    @Test
    public void testHasOnActionDeleteAppointmentMethod() {
        try {
            CalendarScreenController.class.getDeclaredMethod("onActionDeleteAppointment", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionDeleteAppointment method should exist");
        }
    }

    @Test
    public void testHasOnActionReturnMethod() {
        try {
            CalendarScreenController.class.getDeclaredMethod("onActionReturn", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionReturn method should exist");
        }
    }

    @Test
    public void testHasOnActionFilterPeriodMethod() {
        try {
            CalendarScreenController.class.getDeclaredMethod("onActionFilterPeriod", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionFilterPeriod method should exist");
        }
    }

    @Test
    public void testHasOnActionFilterUserMethod() {
        try {
            CalendarScreenController.class.getDeclaredMethod("onActionFilterUser", javafx.event.ActionEvent.class);
        } catch (NoSuchMethodException e) {
            fail("onActionFilterUser method should exist");
        }
    }

    @Test
    public void testImplementsInitializable() {
        assertTrue(javafx.fxml.Initializable.class.isAssignableFrom(CalendarScreenController.class));
    }
}
