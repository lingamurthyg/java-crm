package DAO;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GeneralDaoImpl
 */
public class GeneralDaoImplTest {

    @Test
    public void testGeneralDaoImplClassExists() {
        assertNotNull(GeneralDaoImpl.class);
    }

    @Test
    public void testGeneralDaoImplCanBeInstantiated() {
        GeneralDaoImpl dao = new GeneralDaoImpl();
        assertNotNull(dao);
    }

    @Test
    public void testGeneralDaoImplIsNotAbstract() {
        assertDoesNotThrow(() -> {
            new GeneralDaoImpl();
        });
    }

    @Test
    public void testGeneralDaoImplHasGetMetadataMethod() {
        try {
            GeneralDaoImpl.class.getDeclaredMethod("getMetadata", java.sql.ResultSet.class);
        } catch (NoSuchMethodException e) {
            fail("getMetadata method should exist");
        }
    }
}
