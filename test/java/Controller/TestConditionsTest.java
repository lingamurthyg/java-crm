package Controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TestConditions
 */
public class TestConditionsTest {

    @Test
    public void testTestConditionsClassExists() {
        assertNotNull(TestConditions.class);
    }

    @Test
    public void testHasDeleteTestCustomersMethod() {
        try {
            TestConditions.class.getMethod("deleteTestCustomers");
        } catch (NoSuchMethodException e) {
            fail("deleteTestCustomers method should exist");
        }
    }

    @Test
    public void testHasDeleteAppointmentsMethod() {
        try {
            TestConditions.class.getMethod("deleteAppointments");
        } catch (NoSuchMethodException e) {
            fail("deleteAppointments method should exist");
        }
    }

    @Test
    public void testHasCleanUpMethod() {
        try {
            TestConditions.class.getMethod("cleanUp");
        } catch (NoSuchMethodException e) {
            fail("cleanUp method should exist");
        }
    }

    @Test
    public void testHasCreateTestCustomerMethod() {
        try {
            TestConditions.class.getMethod("createTestCustomer");
        } catch (NoSuchMethodException e) {
            fail("createTestCustomer method should exist");
        }
    }

    @Test
    public void testTestCustomersListExists() {
        assertNotNull(TestConditions.testCustomers);
    }

    @Test
    public void testTestAppointmentsListExists() {
        assertNotNull(TestConditions.testAppointments);
    }
}
