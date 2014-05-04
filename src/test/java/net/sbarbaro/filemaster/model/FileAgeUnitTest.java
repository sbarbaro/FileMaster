/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sbarbaro.filemaster.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ajb
 */
public class FileAgeUnitTest {

    public FileAgeUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getMillis method, of class FileAgeUnit.
     */
    @Test
    public void testGetMillis() {
        System.out.println("getMillis");

        for (int i = 1; i < 3; i++) {
            // SECONDS
            int expResult = i * 1000;
            int result = FileAgeUnit.SECONDS.getMillis(i);
            assertEquals(expResult, result);

            // MINUTES
            expResult = i * 60000;
            result = FileAgeUnit.MINUTES.getMillis(i);
            assertEquals(expResult, result);

            // HOURS
            expResult = i * 3600000;
            result = FileAgeUnit.HOURS.getMillis(i);
            assertEquals(expResult, result);

            // DAYS
            expResult = i * 24 * 3600000;
            result = FileAgeUnit.DAYS.getMillis(i);
            assertEquals(expResult, result);

            // WEEKS
            expResult = i * 7 * 24 * 3600000;
            result = FileAgeUnit.WEEKS.getMillis(i);
            assertEquals(expResult, result);

            // WEEKS
            expResult = i * 52 * 7 * 24 * 3600000;
            result = FileAgeUnit.YEARS.getMillis(i);
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of toString method, of class FileAgeUnit.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        FileAgeUnit instance = FileAgeUnit.SECONDS;
        String expResult = "Seconds";
        String result = instance.toString();
        assertEquals(expResult, result);

    }

}
