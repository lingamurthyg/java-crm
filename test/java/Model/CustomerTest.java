package Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Customer
 */
public class CustomerTest {

    private Customer customer;
    private LocalDateTime testDate;

    @BeforeEach
    public void setUp() {
        testDate = LocalDateTime.of(2024, 1, 15, 10, 30);
        customer = new Customer(1, "John Smith", 100, true, testDate, "admin", testDate, "admin");
    }

    @AfterEach
    public void tearDown() {
        Customer.setCurrentCustomer(null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(customer);
        assertEquals(1, customer.getCustomerId());
        assertEquals("John Smith", customer.getCustomerName());
        assertEquals(100, customer.getAddressId());
        assertTrue(customer.isActive());
    }

    @Test
    public void testGetCustomerId() {
        assertEquals(1, customer.getCustomerId());
    }

    @Test
    public void testSetCustomerId() {
        customer.setCustomerId(999);
        assertEquals(999, customer.getCustomerId());
    }

    @Test
    public void testGetCustomerName() {
        assertEquals("John Smith", customer.getCustomerName());
    }

    @Test
    public void testSetCustomerName() {
        customer.setCustomerName("Jane Doe");
        assertEquals("Jane Doe", customer.getCustomerName());
    }

    @Test
    public void testGetAddressId() {
        assertEquals(100, customer.getAddressId());
    }

    @Test
    public void testSetAddressId() {
        customer.setAddressId(200);
        assertEquals(200, customer.getAddressId());
    }

    @Test
    public void testIsActive() {
        assertTrue(customer.isActive());
    }

    @Test
    public void testSetActive() {
        customer.setActive(false);
        assertFalse(customer.isActive());
    }

    @Test
    public void testGetCreateDate() {
        assertEquals(testDate, customer.getCreateDate());
    }

    @Test
    public void testSetCreateDate() {
        LocalDateTime newDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        customer.setCreateDate(newDate);
        assertEquals(newDate, customer.getCreateDate());
    }

    @Test
    public void testGetCreatedBy() {
        assertEquals("admin", customer.getCreatedBy());
    }

    @Test
    public void testSetCreatedBy() {
        customer.setCreatedBy("system");
        assertEquals("system", customer.getCreatedBy());
    }

    @Test
    public void testGetLastUpdate() {
        assertEquals(testDate, customer.getLastUpdate());
    }

    @Test
    public void testSetLastUpdate() {
        LocalDateTime newDate = LocalDateTime.of(2024, 6, 15, 12, 0);
        customer.setLastUpdate(newDate);
        assertEquals(newDate, customer.getLastUpdate());
    }

    @Test
    public void testGetLastUpdateBy() {
        assertEquals("admin", customer.getLastUpdateBy());
    }

    @Test
    public void testSetLastUpdateBy() {
        customer.setLastUpdateBy("user2");
        assertEquals("user2", customer.getLastUpdateBy());
    }

    @Test
    public void testGetCurrentCustomer() {
        Customer.setCurrentCustomer(customer);
        assertEquals(customer, Customer.getCurrentCustomer());
    }

    @Test
    public void testSetCurrentCustomer() {
        Customer.setCurrentCustomer(customer);
        assertNotNull(Customer.getCurrentCustomer());
    }

    @Test
    public void testSetCurrentCustomerToNull() {
        Customer.setCurrentCustomer(null);
        assertNull(Customer.getCurrentCustomer());
    }

    @Test
    public void testToString() {
        String result = customer.toString();
        assertNotNull(result);
        assertTrue(result.contains("John Smith"));
        assertTrue(result.contains("1"));
    }

    @Test
    public void testInactiveCustomer() {
        Customer inactive = new Customer(2, "Inactive User", 101, false, testDate, "admin", testDate, "admin");
        assertFalse(inactive.isActive());
    }

    @Test
    public void testSetCustomerNameToEmpty() {
        customer.setCustomerName("");
        assertEquals("", customer.getCustomerName());
    }

    @Test
    public void testSetAddressIdToZero() {
        customer.setAddressId(0);
        assertEquals(0, customer.getAddressId());
    }
}
