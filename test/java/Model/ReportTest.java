package Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Report
 */
public class ReportTest {

    private Report report;

    @BeforeEach
    public void setUp() {
        report = new Report("Monthly Sales Report", 101);
    }

    @Test
    public void testConstructor() {
        assertNotNull(report);
        assertEquals("Monthly Sales Report", report.getReportName());
        assertEquals(101, report.getReportId());
    }

    @Test
    public void testGetReportName() {
        assertEquals("Monthly Sales Report", report.getReportName());
    }

    @Test
    public void testSetReportName() {
        report.setReportName("Quarterly Report");
        assertEquals("Quarterly Report", report.getReportName());
    }

    @Test
    public void testGetReportId() {
        assertEquals(101, report.getReportId());
    }

    @Test
    public void testSetReportId() {
        report.setReportId(999);
        assertEquals(999, report.getReportId());
    }

    @Test
    public void testToString() {
        String result = report.toString();
        assertNotNull(result);
        assertEquals("Monthly Sales Report", result);
    }

    @Test
    public void testConstructorWithEmptyName() {
        Report emptyReport = new Report("", 1);
        assertEquals("", emptyReport.getReportName());
    }

    @Test
    public void testConstructorWithZeroId() {
        Report zeroIdReport = new Report("Test Report", 0);
        assertEquals(0, zeroIdReport.getReportId());
    }

    @Test
    public void testConstructorWithNegativeId() {
        Report negativeIdReport = new Report("Negative Report", -1);
        assertEquals(-1, negativeIdReport.getReportId());
    }

    @Test
    public void testSetReportNameToNull() {
        report.setReportName(null);
        assertNull(report.getReportName());
    }

    @Test
    public void testSetReportNameToEmpty() {
        report.setReportName("");
        assertEquals("", report.getReportName());
    }

    @Test
    public void testSetReportIdToZero() {
        report.setReportId(0);
        assertEquals(0, report.getReportId());
    }

    @Test
    public void testLongReportName() {
        String longName = "This is a very long report name that contains multiple words and should be handled correctly";
        Report longReport = new Report(longName, 5);
        assertEquals(longName, longReport.getReportName());
    }
}
