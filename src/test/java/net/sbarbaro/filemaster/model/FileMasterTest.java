package net.sbarbaro.filemaster.model;

import java.util.StringTokenizer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * SerializedFile
 * <p>
 * Configures the file that will be used to persist the state of this
 * application
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public class FileMasterTest {

    /**
     * Test of getDefaultSerializedFileName method, of class SerializedFile.
     */
    @Test
    public void testGetName() throws Exception {
        System.out.println("getName");
        String result = FileMaster.getDefaultSerializedFileName();
        System.out.println(result);

        StringTokenizer st = new StringTokenizer(result, "-");

        // Verify the filename prefix
        assertTrue(st.hasMoreElements());
        String s = (String) st.nextElement();
        assertEquals("FileMaster", s);

        // Verify the next part of the filename is the username
        assertTrue(st.hasMoreElements());
        s = (String) st.nextElement();
        assertEquals(System.getProperty("user.name"), s);

        // Verify the first five parts of the six part, hexadecimal, MAC address
        for (int i = 0; i < 5; i++) {
            assertTrue(st.hasMoreElements());
            s = (String) st.nextElement();
            int macValue = Integer.parseInt(s, 16);
            assertTrue("macValue>" + macValue, macValue >= 0);
            assertTrue("macValue<" + macValue, macValue < 256);
        }

        // Separate the last hex part of the MAC address from the file extension
        assertTrue(st.hasMoreElements());
        st = new StringTokenizer((String) st.nextElement(), ".");

        // Verify the last element of the MAC address
        assertTrue(st.hasMoreElements());
        s = (String) st.nextElement();
        int macValue = Integer.parseInt(s, 16);
        assertTrue("macValue>" + macValue, macValue >= 0);
        assertTrue("macValue<" + macValue, macValue < 256);

        // Verify the file extension
        assertTrue(st.hasMoreElements());
        s = (String) st.nextElement();
        assertEquals("out", s);

    }

}
