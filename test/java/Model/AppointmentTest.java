package Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Appointment
 */
public class AppointmentTest {

    private Appointment appointment;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime testDate;

    @BeforeEach
    public void setUp() {
        startTime = LocalDateTime.of(2024, 6, 15, 10, 0);
        endTime = LocalDateTime.of(2024, 6, 15, 11, 0);
        testDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        appointment = new Appointment(1, 100, 200, "Meeting", "Discuss project", "Office", "John Doe", "Business", "http://example.com", startTime, endTime, testDate, "admin", testDate, "admin");
    }

    @AfterEach
    public void tearDown() {
        Appointment.setCurrentAppointment(null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(appointment);
        assertEquals(1, appointment.getAppointmentId());
        assertEquals(100, appointment.getCustomerId());
        assertEquals(200, appointment.getUserId());
        assertEquals("Meeting", appointment.getTitle());
    }

    @Test
    public void testGetAppointmentId() {
        assertEquals(1, appointment.getAppointmentId());
    }

    @Test
    public void testSetAppointmentId() {
        appointment.setAppointmentId(999);
        assertEquals(999, appointment.getAppointmentId());
    }

    @Test
    public void testGetCustomerId() {
        assertEquals(100, appointment.getCustomerId());
    }

    @Test
    public void testSetCustomerId() {
        appointment.setCustomerId(500);
        assertEquals(500, appointment.getCustomerId());
    }

    @Test
    public void testGetUserId() {
        assertEquals(200, appointment.getUserId());
    }

    @Test
    public void testSetUserId() {
        appointment.setUserId(300);
        assertEquals(300, appointment.getUserId());
    }

    @Test
    public void testGetTitle() {
        assertEquals("Meeting", appointment.getTitle());
    }

    @Test
    public void testSetTitle() {
        appointment.setTitle("Conference");
        assertEquals("Conference", appointment.getTitle());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Discuss project", appointment.getDescription());
    }

    @Test
    public void testSetDescription() {
        appointment.setDescription("Review quarterly results");
        assertEquals("Review quarterly results", appointment.getDescription());
    }

    @Test
    public void testGetLocation() {
        assertEquals("Office", appointment.getLocation());
    }

    @Test
    public void testSetLocation() {
        appointment.setLocation("Conference Room A");
        assertEquals("Conference Room A", appointment.getLocation());
    }

    @Test
    public void testGetContact() {
        assertEquals("John Doe", appointment.getContact());
    }

    @Test
    public void testSetContact() {
        appointment.setContact("Jane Smith");
        assertEquals("Jane Smith", appointment.getContact());
    }

    @Test
    public void testGetType() {
        assertEquals("Business", appointment.getType());
    }

    @Test
    public void testSetType() {
        appointment.setType("Personal");
        assertEquals("Personal", appointment.getType());
    }

    @Test
    public void testGetUrl() {
        assertEquals("http://example.com", appointment.getUrl());
    }

    @Test
    public void testSetUrl() {
        appointment.setUrl("http://newurl.com");
        assertEquals("http://newurl.com", appointment.getUrl());
    }

    @Test
    public void testGetStart() {
        assertEquals(startTime, appointment.getStart());
    }

    @Test
    public void testSetStart() {
        LocalDateTime newStart = LocalDateTime.of(2024, 6, 16, 9, 0);
        appointment.setStart(newStart);
        assertEquals(newStart, appointment.getStart());
    }

    @Test
    public void testGetEnd() {
        assertEquals(endTime, appointment.getEnd());
    }

    @Test
    public void testSetEnd() {
        LocalDateTime newEnd = LocalDateTime.of(2024, 6, 16, 10, 0);
        appointment.setEnd(newEnd);
        assertEquals(newEnd, appointment.getEnd());
    }

    @Test
    public void testGetCreateDate() {
        assertEquals(testDate, appointment.getCreateDate());
    }

    @Test
    public void testSetCreateDate() {
        LocalDateTime newDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        appointment.setCreateDate(newDate);
        assertEquals(newDate, appointment.getCreateDate());
    }

    @Test
    public void testGetCreatedBy() {
        assertEquals("admin", appointment.getCreatedBy());
    }

    @Test
    public void testSetCreatedBy() {
        appointment.setCreatedBy("system");
        assertEquals("system", appointment.getCreatedBy());
    }

    @Test
    public void testGetLastUpdate() {
        assertEquals(testDate, appointment.getLastUpdate());
    }

    @Test
    public void testSetLastUpdate() {
        LocalDateTime newDate = LocalDateTime.of(2024, 6, 15, 12, 0);
        appointment.setLastUpdate(newDate);
        assertEquals(newDate, appointment.getLastUpdate());
    }

    @Test
    public void testGetLastUpdateBy() {
        assertEquals("admin", appointment.getLastUpdateBy());
    }

    @Test
    public void testSetLastUpdateBy() {
        appointment.setLastUpdateBy("user2");
        assertEquals("user2", appointment.getLastUpdateBy());
    }

    @Test
    public void testGetCurrentAppointment() {
        Appointment.setCurrentAppointment(appointment);
        assertEquals(appointment, Appointment.getCurrentAppointment());
    }

    @Test
    public void testSetCurrentAppointment() {
        Appointment.setCurrentAppointment(appointment);
        assertNotNull(Appointment.getCurrentAppointment());
    }

    @Test
    public void testSetCurrentAppointmentToNull() {
        Appointment.setCurrentAppointment(null);
        assertNull(Appointment.getCurrentAppointment());
    }

    @Test
    public void testGetDate() {
        String date = appointment.getDate();
        assertNotNull(date);
    }

    @Test
    public void testGetStartTime() {
        String time = appointment.getStartTime();
        assertNotNull(time);
    }

    @Test
    public void testGetEndTime() {
        String time = appointment.getEndTime();
        assertNotNull(time);
    }
}
