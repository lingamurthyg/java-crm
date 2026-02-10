package Utilities;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.collections.ObservableList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TimeFiles
 */
public class TimeFilesTest {

    @Test
    public void testCreateLocalDateTime() {
        LocalDate date = LocalDate.of(2024, 1, 15);
        String time12 = "2:30 PM";

        LocalDateTime result = TimeFiles.createLocalDateTime(date, time12);

        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(1, result.getMonthValue());
        assertEquals(15, result.getDayOfMonth());
        assertEquals(14, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void testCreateLocalDateTimeWithMorningTime() {
        LocalDate date = LocalDate.of(2024, 6, 20);
        String time12 = "8:00 AM";

        LocalDateTime result = TimeFiles.createLocalDateTime(date, time12);

        assertEquals(8, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void testLocalDateTimeToDBStr() {
        LocalDateTime ldt = LocalDateTime.of(2024, 1, 15, 14, 30);

        String result = TimeFiles.localDateTimeToDBStr(ldt);

        assertNotNull(result);
        assertTrue(result.contains("2024"));
    }

    @Test
    public void testLocalDateTimeToUITime() {
        LocalDateTime ldt = LocalDateTime.of(2024, 1, 15, 14, 30);

        String result = TimeFiles.localDateTimeToUITime(ldt);

        assertNotNull(result);
        assertTrue(result.contains("2:30") || result.contains("PM"));
    }

    @Test
    public void testLocalDateTimeToUIDate() {
        LocalDateTime ldt = LocalDateTime.of(2024, 1, 15, 14, 30);

        String result = TimeFiles.localDateTimeToUIDate(ldt);

        assertNotNull(result);
        assertTrue(result.contains("2024"));
    }

    @Test
    public void testGetAvailableAppointmentTimes() {
        ObservableList<String> times = TimeFiles.getAvailableAppointmentTimes();

        assertNotNull(times);
        assertFalse(times.isEmpty());
        assertTrue(times.size() > 0);
    }

    @Test
    public void testGetAvailableAppointmentTimesSize() {
        ObservableList<String> times = TimeFiles.getAvailableAppointmentTimes();

        // From 8:00 AM to 5:00 PM in 30-minute increments
        assertEquals(19, times.size());
    }

    @Test
    public void testDbStrNow() {
        String result = TimeFiles.dbStrNow();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetSearchTimesRollingYear() {
        LocalDateTime start = LocalDateTime.of(2024, 6, 15, 12, 0);

        ObservableList<String> result = TimeFiles.getSearchTimes(start, true);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetSearchTimesCalendarYear() {
        LocalDateTime start = LocalDateTime.of(2024, 6, 15, 12, 0);

        ObservableList<String> result = TimeFiles.getSearchTimes(start, false);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testCreateLocalDateTimeWithNoonTime() {
        LocalDate date = LocalDate.of(2024, 3, 10);
        String time12 = "12:00 PM";

        LocalDateTime result = TimeFiles.createLocalDateTime(date, time12);

        assertEquals(12, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void testCreateLocalDateTimeWithMidnightTime() {
        LocalDate date = LocalDate.of(2024, 3, 10);
        String time12 = "12:00 AM";

        LocalDateTime result = TimeFiles.createLocalDateTime(date, time12);

        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
    }
}
