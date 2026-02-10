package javacrm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for JavaCRM
 */
public class JavaCRMTest {

    @Test
    public void testJavaCRMClassExists() {
        assertNotNull(JavaCRM.class);
    }

    @Test
    public void testJavaCRMExtendsApplication() {
        assertTrue(javafx.application.Application.class.isAssignableFrom(JavaCRM.class));
    }

    @Test
    public void testHasStartMethod() {
        try {
            JavaCRM.class.getMethod("start", javafx.stage.Stage.class);
        } catch (NoSuchMethodException e) {
            fail("start method should exist");
        }
    }

    @Test
    public void testHasMainMethod() {
        try {
            JavaCRM.class.getMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            fail("main method should exist");
        }
    }

    @Test
    public void testMainMethodIsStatic() {
        try {
            assertTrue(java.lang.reflect.Modifier.isStatic(
                JavaCRM.class.getMethod("main", String[].class).getModifiers()
            ));
        } catch (NoSuchMethodException e) {
            fail("main method should exist");
        }
    }

    @Test
    public void testMainMethodIsPublic() {
        try {
            assertTrue(java.lang.reflect.Modifier.isPublic(
                JavaCRM.class.getMethod("main", String[].class).getModifiers()
            ));
        } catch (NoSuchMethodException e) {
            fail("main method should exist");
        }
    }
}
