package net.sbarbaro.filemaster.model;

/**
 * Enumeration of common image aspect ratios
 *
 * @author steven
 */
public enum ImageAspectRatio {

    _2X1(2, 1),
    _3X2(3, 2),
    _4X3(4, 3),
    _5X2(5, 2),
    _5X3(5, 3),
    _5X4(5, 4),
    _7X5(7, 5),
    _8X5(8, 5, "16:10"),
    _8X7(8, 7, "16:14"),
    _14X11(14, 11),
    _16X9(16, 9),
    IMAX(1.43f, 1f),
    _185X100(1.85f, 1f),
    _239X100(2.39f, 1f),
    _240X100(2.40f, 1f),
    GOLDEN_RATIO(16.18f, 10f),
    PANORAMIC("Panoramic"),
    SQUARE("Square"),
    OTHER("Other");


    /*    
     Private constructor for exact image aspect ratio
     */
    private ImageAspectRatio(int a, int b) {
        this(a, b, a + ":" + b);
    }

    /*    
     Private constructor for exact image aspect ratio
     */
    private ImageAspectRatio(int a, int b, String text) {
        this.text = text;
        this.a = a;
        this.b = b;
    }
    /*    
     Private constructor for exact image aspect ratio specfied by floats
     */

    private ImageAspectRatio(float a, float b) {
        this.text = a + ":" + (int) b;
        this.a = (int) a * 100;
        this.b = (int) b * 100;
    }

    @Override
    public String toString() {
        return text;
    }

    /**
     * Derive an ImageAspectRatio from the given dimension.
     *
     * @param width The pixel width of an image
     * @param height The pixel height of an image
     * @return An ImageAspectRatio value, or ImageAspectRatio.OTHER if a
     * standard aspect ratio cannot be determined for the given inputs.
     */
    public static ImageAspectRatio fromValues(int width, int height) {

        ImageAspectRatio result = null;

        // The first term in an aspact ration should be the larger
        // of the two terms
        int w = Math.max(width, height);
        int h = Math.min(width, height);

        // Scale the image down to an aspect ratio based on the greatest
        // common denonimator
        int gcd = (int) gcd(w, h);

        // Calculate scaled dimensions
        int a = w / gcd;
        int b = h / gcd;

        if (a == b) {

            result = SQUARE;

        } else if (a > 3 * b) {

            result = PANORAMIC;

        } else {

            for (ImageAspectRatio value : values()) {
                if (value.a == a && value.b == b) {
                    result = value;
                    break;
                }
            }
        }

        if (null == result) {

            result = OTHER;

        }

        return result;

    }

    /**
     * Calculate the greatest common denominator for the given a and b values
     *
     * @param a
     * @param b
     * @return The gcd of b and a
     */
    public static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    /*
     Private constructor for image aspect ratios defined by name only
     */
    private ImageAspectRatio(String text) {
        this.a = 0;
        this.b = 0;
        this.text = text;
    }

    // The ratio terms that relate to the width and height of an image
    private final int a, b;

    // The name of an ImageAspectRatio
    private final String text;
}
