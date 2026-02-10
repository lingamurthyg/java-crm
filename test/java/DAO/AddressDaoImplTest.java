package DAO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AddressDaoImpl
 */
public class AddressDaoImplTest {

    @Test
    public void testAddressDaoImplClassExists() {
        assertNotNull(AddressDaoImpl.class);
    }

    @Test
    public void testAddressDaoImplExtendsGeneralDaoImpl() {
        assertTrue(GeneralDaoImpl.class.isAssignableFrom(AddressDaoImpl.class));
    }

    @Test
    public void testHasGetAddressByIdMethod() {
        try {
            AddressDaoImpl.class.getMethod("getAddress", int.class);
        } catch (NoSuchMethodException e) {
            fail("getAddress(int) method should exist");
        }
    }

    @Test
    public void testHasGetAddressByFieldsMethod() {
        try {
            AddressDaoImpl.class.getMethod("getAddress", String.class, String.class, int.class, String.class, String.class);
        } catch (NoSuchMethodException e) {
            fail("getAddress(String, String, int, String, String) method should exist");
        }
    }

    @Test
    public void testHasInsertAddressMethod() {
        try {
            AddressDaoImpl.class.getMethod("insertAddress", String.class, String.class, int.class, String.class, String.class);
        } catch (NoSuchMethodException e) {
            fail("insertAddress method should exist");
        }
    }

    @Test
    public void testAddressDaoImplCanBeInstantiated() {
        AddressDaoImpl dao = new AddressDaoImpl();
        assertNotNull(dao);
    }
}
