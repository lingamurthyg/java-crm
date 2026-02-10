package DAO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DBConnection
 */
public class DBConnectionTest {

    @Test
    public void testGetConnectionInitially() {
        // Test that getConnection can be called without throwing exception
        assertDoesNotThrow(() -> {
            DBConnection.getConnection();
        });
    }

    @Test
    public void testDBConnectionClassExists() {
        assertNotNull(DBConnection.class);
    }

    @Test
    public void testHasStartConnectionMethod() {
        try {
            DBConnection.class.getMethod("startConnection");
        } catch (NoSuchMethodException e) {
            fail("startConnection method should exist");
        }
    }

    @Test
    public void testHasGetConnectionMethod() {
        try {
            DBConnection.class.getMethod("getConnection");
        } catch (NoSuchMethodException e) {
            fail("getConnection method should exist");
        }
    }

    @Test
    public void testHasCloseConnectionMethod() {
        try {
            DBConnection.class.getMethod("closeConnection");
        } catch (NoSuchMethodException e) {
            fail("closeConnection method should exist");
        }
    }

    @Test
    public void testCloseConnectionDoesNotThrow() {
        assertDoesNotThrow(() -> {
            DBConnection.closeConnection();
        });
    }
}
