package DAO;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AppointmentDaoImpl
 */
public class AppointmentDaoImplTest {

    @Test
    public void testAppointmentDaoImplClassExists() {
        assertNotNull(AppointmentDaoImpl.class);
    }

    @Test
    public void testAppointmentDaoImplExtendsGeneralDaoImpl() {
        assertTrue(GeneralDaoImpl.class.isAssignableFrom(AppointmentDaoImpl.class));
    }

    @Test
    public void testHasGetAppointmentMethod() {
        try {
            AppointmentDaoImpl.class.getMethod("getAppointment", LocalDateTime.class, LocalDateTime.class);
        } catch (NoSuchMethodException e) {
            fail("getAppointment method should exist");
        }
    }

    @Test
    public void testHasGetAppointmentListMethod() {
        try {
            AppointmentDaoImpl.class.getMethod("getAppointmentList", int.class, LocalDateTime.class, LocalDateTime.class);
        } catch (NoSuchMethodException e) {
            fail("getAppointmentList method should exist");
        }
    }

    @Test
    public void testHasGetTypeReportMethod() {
        try {
            AppointmentDaoImpl.class.getMethod("getTypeReport", javafx.collections.ObservableList.class, Integer.class);
        } catch (NoSuchMethodException e) {
            fail("getTypeReport method should exist");
        }
    }

    @Test
    public void testHasGetAvgApptReportMethod() {
        try {
            AppointmentDaoImpl.class.getMethod("getAvgApptReport", javafx.collections.ObservableList.class);
        } catch (NoSuchMethodException e) {
            fail("getAvgApptReport method should exist");
        }
    }

    @Test
    public void testHasInsertAppointmentMethod() {
        try {
            AppointmentDaoImpl.class.getMethod("insertAppointment", String.class, String.class, String.class, String.class, String.class, String.class, LocalDateTime.class, LocalDateTime.class);
        } catch (NoSuchMethodException e) {
            fail("insertAppointment method should exist");
        }
    }

    @Test
    public void testHasDeleteAppointmentMethod() {
        try {
            AppointmentDaoImpl.class.getMethod("deleteAppointment", int.class);
        } catch (NoSuchMethodException e) {
            fail("deleteAppointment method should exist");
        }
    }

    @Test
    public void testHasUpdateAppointmentMethod() {
        try {
            AppointmentDaoImpl.class.getMethod("updateAppointment", String.class, String.class, String.class, String.class, String.class, String.class, LocalDateTime.class, LocalDateTime.class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            fail("updateAppointment method should exist");
        }
    }

    @Test
    public void testHasCheckAppointmentOverlapMethod() {
        try {
            AppointmentDaoImpl.class.getMethod("checkAppointmentOverlap", int.class, int.class, LocalDateTime.class, LocalDateTime.class, Boolean.class);
        } catch (NoSuchMethodException e) {
            fail("checkAppointmentOverlap method should exist");
        }
    }

    @Test
    public void testHasCheckUpcomingApptMethod() {
        try {
            AppointmentDaoImpl.class.getMethod("checkUpcomingAppt");
        } catch (NoSuchMethodException e) {
            fail("checkUpcomingAppt method should exist");
        }
    }

    @Test
    public void testAppointmentDaoImplCanBeInstantiated() {
        AppointmentDaoImpl dao = new AppointmentDaoImpl();
        assertNotNull(dao);
    }
}
