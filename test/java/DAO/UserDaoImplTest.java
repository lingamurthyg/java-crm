package DAO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserDaoImpl
 */
public class UserDaoImplTest {

    @Test
    public void testUserDaoImplClassExists() {
        assertNotNull(UserDaoImpl.class);
    }

    @Test
    public void testUserDaoImplExtendsGeneralDaoImpl() {
        assertTrue(GeneralDaoImpl.class.isAssignableFrom(UserDaoImpl.class));
    }

    @Test
    public void testHasLogInMethod() {
        try {
            UserDaoImpl.class.getMethod("logIn", String.class, String.class);
        } catch (NoSuchMethodException e) {
            fail("logIn method should exist");
        }
    }

    @Test
    public void testHasGetAllActiveUsersMethod() {
        try {
            UserDaoImpl.class.getMethod("getAllActiveUsers");
        } catch (NoSuchMethodException e) {
            fail("getAllActiveUsers method should exist");
        }
    }

    @Test
    public void testUserDaoImplCanBeInstantiated() {
        UserDaoImpl dao = new UserDaoImpl();
        assertNotNull(dao);
    }
}
