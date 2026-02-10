package Controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GeneralController
 */
public class GeneralControllerTest {

    private GeneralController controller;

    @BeforeEach
    public void setUp() {
        controller = new GeneralController();
    }

    @Test
    public void testCheckFieldsWithEmptyField() {
        TextField emptyField = new TextField();
        emptyField.setText("");
        emptyField.setPromptText("TestField");

        Boolean result = controller.checkFields(emptyField);

        assertTrue(result);
        assertNotNull(controller.errorStr);
    }

    @Test
    public void testCheckFieldsWithFilledField() {
        TextField filledField = new TextField();
        filledField.setText("TestValue");

        Boolean result = controller.checkFields(filledField);

        assertFalse(result);
    }

    @Test
    public void testCheckFieldsWithMultipleFields() {
        TextField field1 = new TextField();
        field1.setText("Value1");
        TextField field2 = new TextField();
        field2.setText("");
        field2.setPromptText("Field2");

        Boolean result = controller.checkFields(field1, field2);

        assertTrue(result);
    }

    @Test
    public void testCheckFieldsWithNoFields() {
        Boolean result = controller.checkFields();

        assertFalse(result);
    }

    @Test
    public void testCheckFieldsWithEmptyPromptText() {
        TextField emptyField = new TextField();
        emptyField.setText("");
        emptyField.setPromptText("");

        Boolean result = controller.checkFields(emptyField);

        assertTrue(result);
    }

    @Test
    public void testCheckFieldsWithNullTextField() {
        TextField nullField = null;

        assertThrows(NullPointerException.class, () -> {
            controller.checkFields(nullField);
        });
    }

    @Test
    public void testCheckFieldsMultipleEmptyFields() {
        TextField field1 = new TextField();
        field1.setText("");
        field1.setPromptText("Field1");
        TextField field2 = new TextField();
        field2.setText("");
        field2.setPromptText("Field2");

        Boolean result = controller.checkFields(field1, field2);

        assertTrue(result);
    }

    @Test
    public void testCheckFieldsAllFilled() {
        TextField field1 = new TextField();
        field1.setText("Value1");
        TextField field2 = new TextField();
        field2.setText("Value2");
        TextField field3 = new TextField();
        field3.setText("Value3");

        Boolean result = controller.checkFields(field1, field2, field3);

        assertFalse(result);
    }
}
