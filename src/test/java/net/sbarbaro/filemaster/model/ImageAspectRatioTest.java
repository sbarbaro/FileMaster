package net.sbarbaro.filemaster.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author steven
 */
public class ImageAspectRatioTest {

    public ImageAspectRatioTest() {
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
     * Test of values method, of class ImageAspectRatio.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        ImageAspectRatio[] expResult
                = { ImageAspectRatio._2X1,
                    ImageAspectRatio._3X2,
                    ImageAspectRatio._4X3,
                    ImageAspectRatio._5X3,
                    ImageAspectRatio._5X4,
                    ImageAspectRatio._8X5,
                    ImageAspectRatio._16X9,
                    ImageAspectRatio._16X10,
                    ImageAspectRatio.IMAX,
                    ImageAspectRatio._185X100,
                    ImageAspectRatio._239X100,
                    ImageAspectRatio._240X100,
                    ImageAspectRatio.GOLDEN_RATIO,
                    ImageAspectRatio.PANORAMIC,
                    ImageAspectRatio.SQUARE,
                    ImageAspectRatio.OTHER,

                };
        ImageAspectRatio[] result = ImageAspectRatio.values();
        assertEquals(expResult.length, result.length);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of valueOf method, of class ImageAspectRatio.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "_4X3";
        ImageAspectRatio expResult = ImageAspectRatio._4X3;
        ImageAspectRatio result = ImageAspectRatio.valueOf(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class ImageAspectRatio.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        // Test exact case
        ImageAspectRatio instance = ImageAspectRatio._16X9;
        String expResult = "16:9";
        String result = instance.toString();
        assertEquals(expResult, result);

        // Test qualitative case
        instance = ImageAspectRatio.SQUARE;
        expResult = "Square";
        result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of gcd method, of class ImageAspectRatio.
     */
    @Test
    public void testGcd() {
        System.out.println("gcd");
        long a = 4000L;
        long b = 5000L;
        long expResult = 1000L;
        long result = ImageAspectRatio.gcd(a, b);
        assertEquals(expResult, result);
    }

    /**
     * Test of fromValues method, of class ImageAspectRatio.
     */
    @Test
    public void testFromValues() {
        System.out.println("fromValues");
        int width = 4000;
        int height = 5000;
        ImageAspectRatio expResult = ImageAspectRatio._5X4;
        ImageAspectRatio result = ImageAspectRatio.fromValues(width, height);
        assertEquals(expResult, result);

        width = 5000;
        height = 4000;

        expResult = ImageAspectRatio._5X4;
        result = ImageAspectRatio.fromValues(width, height);
        assertEquals(expResult, result);
    }

}
