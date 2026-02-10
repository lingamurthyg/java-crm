package DAO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CustomerDaoImpl
 */
public class CustomerDaoImplTest {

    @Test
    public void testCustomerDaoImplClassExists() {
        assertNotNull(CustomerDaoImpl.class);
    }

    @Test
    public void testCustomerDaoImplExtendsGeneralDaoImpl() {
        assertTrue(GeneralDaoImpl.class.isAssignableFrom(CustomerDaoImpl.class));
    }

    @Test
    public void testHasGetCustomerByIdMethod() {
        try {
            CustomerDaoImpl.class.getMethod("getCustomer", int.class);
        } catch (NoSuchMethodException e) {
            fail("getCustomer(int) method should exist");
        }
    }

    @Test
    public void testHasGetCustomerByNameAndAddressMethod() {
        try {
            CustomerDaoImpl.class.getMethod("getCustomer", String.class, int.class);
        } catch (NoSuchMethodException e) {
            fail("getCustomer(String, int) method should exist");
        }
    }

    @Test
    public void testHasGetAllActiveCustomersMethod() {
        try {
            CustomerDaoImpl.class.getMethod("getAllActiveCustomers");
        } catch (NoSuchMethodException e) {
            fail("getAllActiveCustomers method should exist");
        }
    }

    @Test
    public void testHasInsertCustomerMethod() {
        try {
            CustomerDaoImpl.class.getMethod("insertCustomer", String.class, int.class);
        } catch (NoSuchMethodException e) {
            fail("insertCustomer method should exist");
        }
    }

    @Test
    public void testHasDeleteCustomerMethod() {
        try {
            CustomerDaoImpl.class.getMethod("deleteCustomer", int.class);
        } catch (NoSuchMethodException e) {
            fail("deleteCustomer method should exist");
        }
    }

    @Test
    public void testHasUpdateCustomerMethod() {
        try {
            CustomerDaoImpl.class.getMethod("updateCustomer", String.class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            fail("updateCustomer method should exist");
        }
    }

    @Test
    public void testCustomerDaoImplCanBeInstantiated() {
        CustomerDaoImpl dao = new CustomerDaoImpl();
        assertNotNull(dao);
    }
}
