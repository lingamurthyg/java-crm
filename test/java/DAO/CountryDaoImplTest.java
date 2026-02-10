package DAO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CountryDaoImpl
 */
public class CountryDaoImplTest {

    @Test
    public void testCountryDaoImplClassExists() {
        assertNotNull(CountryDaoImpl.class);
    }

    @Test
    public void testCountryDaoImplExtendsGeneralDaoImpl() {
        assertTrue(GeneralDaoImpl.class.isAssignableFrom(CountryDaoImpl.class));
    }

    @Test
    public void testHasGetCountryByIdMethod() {
        try {
            CountryDaoImpl.class.getMethod("getCountry", int.class);
        } catch (NoSuchMethodException e) {
            fail("getCountry(int) method should exist");
        }
    }

    @Test
    public void testHasGetCountryByNameMethod() {
        try {
            CountryDaoImpl.class.getMethod("getCountry", String.class);
        } catch (NoSuchMethodException e) {
            fail("getCountry(String) method should exist");
        }
    }

    @Test
    public void testHasInsertCountryMethod() {
        try {
            CountryDaoImpl.class.getMethod("insertCountry", String.class);
        } catch (NoSuchMethodException e) {
            fail("insertCountry method should exist");
        }
    }

    @Test
    public void testCountryDaoImplCanBeInstantiated() {
        CountryDaoImpl dao = new CountryDaoImpl();
        assertNotNull(dao);
    }
}
