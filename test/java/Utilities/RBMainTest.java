package Utilities;

import org.junit.jupiter.api.Test;
import java.util.ResourceBundle;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for RBMain
 */
public class RBMainTest {

    @Test
    public void testSetRb() {
        assertDoesNotThrow(() -> {
            RBMain.setRb();
        });
    }

    @Test
    public void testGetRbAfterSet() {
        RBMain.setRb();
        ResourceBundle rb = RBMain.getRb();
        assertNotNull(rb);
    }

    @Test
    public void testRBMainClassExists() {
        assertNotNull(RBMain.class);
    }

    @Test
    public void testSetRbMethodExists() {
        try {
            RBMain.class.getMethod("setRb");
        } catch (NoSuchMethodException e) {
            fail("setRb method should exist");
        }
    }

    @Test
    public void testGetRbMethodExists() {
        try {
            RBMain.class.getMethod("getRb");
        } catch (NoSuchMethodException e) {
            fail("getRb method should exist");
        }
    }

    @Test
    public void testGetRbReturnsResourceBundle() {
        RBMain.setRb();
        ResourceBundle rb = RBMain.getRb();
        assertTrue(rb instanceof ResourceBundle);
    }
}
