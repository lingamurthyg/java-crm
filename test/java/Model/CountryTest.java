package Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Country
 */
public class CountryTest {

    private Country country;
    private LocalDateTime testDate;

    @BeforeEach
    public void setUp() {
        testDate = LocalDateTime.of(2024, 1, 15, 10, 30);
        country = new Country(1, "United States", testDate, "admin", testDate, "admin");
    }

    @Test
    public void testConstructor() {
        assertNotNull(country);
        assertEquals(1, country.getCountryId());
        assertEquals("United States", country.getCountryName());
        assertEquals(testDate, country.getCreateDate());
        assertEquals("admin", country.getCreatedBy());
    }

    @Test
    public void testGetCountryId() {
        assertEquals(1, country.getCountryId());
    }

    @Test
    public void testSetCountryId() {
        country.setCountryId(99);
        assertEquals(99, country.getCountryId());
    }

    @Test
    public void testGetCountryName() {
        assertEquals("United States", country.getCountryName());
    }

    @Test
    public void testSetCountryName() {
        country.setCountryName("Canada");
        assertEquals("Canada", country.getCountryName());
    }

    @Test
    public void testGetCreateDate() {
        assertEquals(testDate, country.getCreateDate());
    }

    @Test
    public void testSetCreateDate() {
        LocalDateTime newDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        country.setCreateDate(newDate);
        assertEquals(newDate, country.getCreateDate());
    }

    @Test
    public void testGetCreatedBy() {
        assertEquals("admin", country.getCreatedBy());
    }

    @Test
    public void testSetCreatedBy() {
        country.setCreatedBy("system");
        assertEquals("system", country.getCreatedBy());
    }

    @Test
    public void testGetLastUpdate() {
        assertEquals(testDate, country.getLastUpdate());
    }

    @Test
    public void testSetLastUpdate() {
        LocalDateTime newDate = LocalDateTime.of(2024, 6, 15, 12, 0);
        country.setLastUpdate(newDate);
        assertEquals(newDate, country.getLastUpdate());
    }

    @Test
    public void testGetLastUpdateBy() {
        assertEquals("admin", country.getLastUpdateBy());
    }

    @Test
    public void testSetLastUpdateBy() {
        country.setLastUpdateBy("user2");
        assertEquals("user2", country.getLastUpdateBy());
    }

    @Test
    public void testSetCountryIdToZero() {
        country.setCountryId(0);
        assertEquals(0, country.getCountryId());
    }

    @Test
    public void testSetCountryNameToEmpty() {
        country.setCountryName("");
        assertEquals("", country.getCountryName());
    }

    @Test
    public void testConstructorWithDifferentCountry() {
        Country uk = new Country(2, "United Kingdom", testDate, "user1", testDate, "user1");
        assertEquals("United Kingdom", uk.getCountryName());
        assertEquals(2, uk.getCountryId());
    }
}
