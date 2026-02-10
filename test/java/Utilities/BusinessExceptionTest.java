package Utilities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BusinessException
 */
public class BusinessExceptionTest {

    @Test
    public void testDefaultConstructor() {
        BusinessException exception = new BusinessException();

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    public void testConstructorWithMessage() {
        String message = "Test error message";
        BusinessException exception = new BusinessException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testConstructorWithException() {
        Exception cause = new Exception("Cause exception");
        BusinessException exception = new BusinessException(cause);

        assertNotNull(exception);
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructorWithNullMessage() {
        BusinessException exception = new BusinessException((String) null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    public void testConstructorWithNullException() {
        BusinessException exception = new BusinessException((Exception) null);

        assertNotNull(exception);
        assertNull(exception.getCause());
    }

    @Test
    public void testConstructorWithEmptyMessage() {
        String message = "";
        BusinessException exception = new BusinessException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testExceptionInheritance() {
        BusinessException exception = new BusinessException("Test");

        assertTrue(exception instanceof Exception);
    }

    @Test
    public void testConstructorWithLongMessage() {
        String longMessage = "This is a very long error message that contains multiple words and should still be handled correctly by the BusinessException class";
        BusinessException exception = new BusinessException(longMessage);

        assertNotNull(exception);
        assertEquals(longMessage, exception.getMessage());
    }
}
