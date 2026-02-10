package Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for City
 */
public class CityTest {

    private City city;
    private LocalDateTime testDate;

    @BeforeEach
    public void setUp() {
        testDate = LocalDateTime.of(2024, 1, 15, 10, 30);
        city = new City(1, "New York", 100, testDate, "admin", testDate, "admin");
    }

    @Test
    public void testConstructor() {
        assertNotNull(city);
        assertEquals(1, city.getCityId());
        assertEquals("New York", city.getCityName());
        assertEquals(100, city.getCountryId());
    }

    @Test
    public void testGetCityId() {
        assertEquals(1, city.getCityId());
    }

    @Test
    public void testSetCityId() {
        city.setCityId(99);
        assertEquals(99, city.getCityId());
    }

    @Test
    public void testGetCityName() {
        assertEquals("New York", city.getCityName());
    }

    @Test
    public void testSetCityName() {
        city.setCityName("Los Angeles");
        assertEquals("Los Angeles", city.getCityName());
    }

    @Test
    public void testGetCountryId() {
        assertEquals(100, city.getCountryId());
    }

    @Test
    public void testSetCountryId() {
        city.setCountryId(200);
        assertEquals(200, city.getCountryId());
    }

    @Test
    public void testGetCreateDate() {
        assertEquals(testDate, city.getCreateDate());
    }

    @Test
    public void testSetCreateDate() {
        LocalDateTime newDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        city.setCreateDate(newDate);
        assertEquals(newDate, city.getCreateDate());
    }

    @Test
    public void testGetCreatedBy() {
        assertEquals("admin", city.getCreatedBy());
    }

    @Test
    public void testSetCreatedBy() {
        city.setCreatedBy("system");
        assertEquals("system", city.getCreatedBy());
    }

    @Test
    public void testGetLastUpdate() {
        assertEquals(testDate, city.getLastUpdate());
    }

    @Test
    public void testSetLastUpdate() {
        LocalDateTime newDate = LocalDateTime.of(2024, 6, 15, 12, 0);
        city.setLastUpdate(newDate);
        assertEquals(newDate, city.getLastUpdate());
    }

    @Test
    public void testGetLastUpdateBy() {
        assertEquals("admin", city.getLastUpdateBy());
    }

    @Test
    public void testSetLastUpdateBy() {
        city.setLastUpdateBy("user2");
        assertEquals("user2", city.getLastUpdateBy());
    }

    @Test
    public void testGetCountryObj() {
        assertNull(city.getCountryObj());
    }

    @Test
    public void testSetCityNameToEmpty() {
        city.setCityName("");
        assertEquals("", city.getCityName());
    }

    @Test
    public void testSetCityIdToZero() {
        city.setCityId(0);
        assertEquals(0, city.getCityId());
    }

    @Test
    public void testConstructorWithDifferentCity() {
        City chicago = new City(2, "Chicago", 100, testDate, "user1", testDate, "user1");
        assertEquals("Chicago", chicago.getCityName());
        assertEquals(2, chicago.getCityId());
    }
}
