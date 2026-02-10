package Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Address
 */
public class AddressTest {

    private Address address;
    private LocalDateTime testDate;

    @BeforeEach
    public void setUp() {
        testDate = LocalDateTime.of(2024, 1, 15, 10, 30);
        address = new Address(1, "123 Main St", "Apt 4B", 50, "12345", "555-1234", testDate, "admin", testDate, "admin");
    }

    @Test
    public void testConstructor() {
        assertNotNull(address);
        assertEquals(1, address.getAddressId());
        assertEquals("123 Main St", address.getAddressName());
        assertEquals("Apt 4B", address.getAddress2Name());
        assertEquals(50, address.getCityId());
        assertEquals("12345", address.getPostalCode());
        assertEquals("555-1234", address.getPhone());
    }

    @Test
    public void testGetAddressId() {
        assertEquals(1, address.getAddressId());
    }

    @Test
    public void testSetAddressId() {
        address.setAddressId(99);
        assertEquals(99, address.getAddressId());
    }

    @Test
    public void testGetAddressName() {
        assertEquals("123 Main St", address.getAddressName());
    }

    @Test
    public void testSetAddressName() {
        address.setAddressName("456 Oak Ave");
        assertEquals("456 Oak Ave", address.getAddressName());
    }

    @Test
    public void testGetAddress2Name() {
        assertEquals("Apt 4B", address.getAddress2Name());
    }

    @Test
    public void testSetAddress2Name() {
        address.setAddress2Name("Suite 100");
        assertEquals("Suite 100", address.getAddress2Name());
    }

    @Test
    public void testGetCityId() {
        assertEquals(50, address.getCityId());
    }

    @Test
    public void testSetCityId() {
        address.setCityId(75);
        assertEquals(75, address.getCityId());
    }

    @Test
    public void testGetPostalCode() {
        assertEquals("12345", address.getPostalCode());
    }

    @Test
    public void testSetPostalCode() {
        address.setPostalCode("67890");
        assertEquals("67890", address.getPostalCode());
    }

    @Test
    public void testGetPhone() {
        assertEquals("555-1234", address.getPhone());
    }

    @Test
    public void testSetPhone() {
        address.setPhone("555-9999");
        assertEquals("555-9999", address.getPhone());
    }

    @Test
    public void testGetCreateDate() {
        assertEquals(testDate, address.getCreateDate());
    }

    @Test
    public void testSetCreateDate() {
        LocalDateTime newDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        address.setCreateDate(newDate);
        assertEquals(newDate, address.getCreateDate());
    }

    @Test
    public void testGetCreatedBy() {
        assertEquals("admin", address.getCreatedBy());
    }

    @Test
    public void testSetCreatedBy() {
        address.setCreatedBy("system");
        assertEquals("system", address.getCreatedBy());
    }

    @Test
    public void testGetLastUpdate() {
        assertEquals(testDate, address.getLastUpdate());
    }

    @Test
    public void testSetLastUpdate() {
        LocalDateTime newDate = LocalDateTime.of(2024, 6, 15, 12, 0);
        address.setLastUpdate(newDate);
        assertEquals(newDate, address.getLastUpdate());
    }

    @Test
    public void testGetLastUpdateBy() {
        assertEquals("admin", address.getLastUpdateBy());
    }

    @Test
    public void testSetLastUpdateBy() {
        address.setLastUpdateBy("user2");
        assertEquals("user2", address.getLastUpdateBy());
    }

    @Test
    public void testGetCityObj() {
        assertNull(address.getCityObj());
    }

    @Test
    public void testSetAddressNameToEmpty() {
        address.setAddressName("");
        assertEquals("", address.getAddressName());
    }

    @Test
    public void testSetAddress2NameToEmpty() {
        address.setAddress2Name("");
        assertEquals("", address.getAddress2Name());
    }

    @Test
    public void testSetPostalCodeToEmpty() {
        address.setPostalCode("");
        assertEquals("", address.getPostalCode());
    }

    @Test
    public void testConstructorWithEmptyAddress2() {
        Address addr = new Address(2, "789 Elm St", "", 60, "54321", "555-5678", testDate, "user1", testDate, "user1");
        assertEquals("", addr.getAddress2Name());
    }
}
