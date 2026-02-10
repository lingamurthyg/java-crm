package Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for User
 */
public class UserTest {

    private User user;
    private LocalDateTime testDate;

    @BeforeEach
    public void setUp() {
        testDate = LocalDateTime.of(2024, 1, 15, 10, 30);
        user = new User(1, "testuser", "password123", true, testDate, "admin", testDate, "admin");
    }

    @AfterEach
    public void tearDown() {
        User.setCurrentUser(null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(user);
        assertEquals(1, user.getUserId());
        assertEquals("testuser", user.getUserName());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isActive());
    }

    @Test
    public void testGetUserId() {
        assertEquals(1, user.getUserId());
    }

    @Test
    public void testSetUserId() {
        user.setUserId(99);
        assertEquals(99, user.getUserId());
    }

    @Test
    public void testGetUserName() {
        assertEquals("testuser", user.getUserName());
    }

    @Test
    public void testSetUserName() {
        user.setUserName("newuser");
        assertEquals("newuser", user.getUserName());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testSetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    public void testIsActive() {
        assertTrue(user.isActive());
    }

    @Test
    public void testSetActive() {
        user.setActive(false);
        assertFalse(user.isActive());
    }

    @Test
    public void testGetCreateDate() {
        assertEquals(testDate, user.getCreateDate());
    }

    @Test
    public void testSetCreateDate() {
        LocalDateTime newDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        user.setCreateDate(newDate);
        assertEquals(newDate, user.getCreateDate());
    }

    @Test
    public void testGetCreatedBy() {
        assertEquals("admin", user.getCreatedBy());
    }

    @Test
    public void testSetCreatedBy() {
        user.setCreatedBy("system");
        assertEquals("system", user.getCreatedBy());
    }

    @Test
    public void testGetLastUpdate() {
        assertEquals(testDate, user.getLastUpdate());
    }

    @Test
    public void testSetLastUpdate() {
        LocalDateTime newDate = LocalDateTime.of(2024, 6, 15, 12, 0);
        user.setLastUpdate(newDate);
        assertEquals(newDate, user.getLastUpdate());
    }

    @Test
    public void testGetLastUpdateBy() {
        assertEquals("admin", user.getLastUpdateBy());
    }

    @Test
    public void testSetLastUpdateBy() {
        user.setLastUpdateBy("user2");
        assertEquals("user2", user.getLastUpdateBy());
    }

    @Test
    public void testToString() {
        String result = user.toString();
        assertNotNull(result);
        assertTrue(result.contains("testuser"));
        assertTrue(result.contains("1"));
    }

    @Test
    public void testGetCurrentUser() {
        User.setCurrentUser(user);
        User current = User.getCurrentUser();
        assertNotNull(current);
        assertEquals(user, current);
    }

    @Test
    public void testSetCurrentUser() {
        User.setCurrentUser(user);
        assertEquals(user, User.getCurrentUser());
    }

    @Test
    public void testSetCurrentUserToNull() {
        User.setCurrentUser(null);
        assertNull(User.getCurrentUser());
    }

    @Test
    public void testInactiveUser() {
        User inactiveUser = new User(2, "inactive", "pass", false, testDate, "admin", testDate, "admin");
        assertFalse(inactiveUser.isActive());
    }

    @Test
    public void testEmptyPassword() {
        User userWithEmptyPass = new User(3, "nopass", "", true, testDate, "admin", testDate, "admin");
        assertEquals("", userWithEmptyPass.getPassword());
    }
}
