package DAO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CityDaoImpl
 */
public class CityDaoImplTest {

    @Test
    public void testCityDaoImplClassExists() {
        assertNotNull(CityDaoImpl.class);
    }

    @Test
    public void testCityDaoImplExtendsGeneralDaoImpl() {
        assertTrue(GeneralDaoImpl.class.isAssignableFrom(CityDaoImpl.class));
    }

    @Test
    public void testHasGetCityByIdMethod() {
        try {
            CityDaoImpl.class.getMethod("getCity", int.class);
        } catch (NoSuchMethodException e) {
            fail("getCity(int) method should exist");
        }
    }

    @Test
    public void testHasGetCityByNameAndCountryMethod() {
        try {
            CityDaoImpl.class.getMethod("getCity", String.class, int.class);
        } catch (NoSuchMethodException e) {
            fail("getCity(String, int) method should exist");
        }
    }

    @Test
    public void testHasInsertCityMethod() {
        try {
            CityDaoImpl.class.getMethod("insertCity", String.class, int.class);
        } catch (NoSuchMethodException e) {
            fail("insertCity method should exist");
        }
    }

    @Test
    public void testCityDaoImplCanBeInstantiated() {
        CityDaoImpl dao = new CityDaoImpl();
        assertNotNull(dao);
    }
}
