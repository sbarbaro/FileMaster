package net.sbarbaro.filemaster.model;

/**
 * Enumeration of common image aspect ratios
 *
 * @author steven
 */
public enum ImageAspectRatio {

    _4X3(4, 3),
    _5X3(5, 3),
    _5X4(5, 4),
    _16X9(16, 9),
    _3X2(3, 2),
    _2X1(2, 1),
    _16X10(16, 10),
    PANORAMIC("Panoramic"),
    SQUARE("Square"),
    OTHER("Other");

    /*    
    _4X3(4, 3),
    _5X3(5, 3),
    _5X4(5, 4),
    _16X9(16, 9),
    _3X2(3, 2),
    _2X1(2, 1),
    PANORAMIC("Panoramic"),
    SQUARE("Square"),
    OTHER("Other");
     Constructor for exact image aspect ratio
     */
    private ImageAspectRatio(int a, int b) {
        this.text = a + ":" + b;
        this.a = a;
        this.b = b;
    }

    private ImageAspectRatio(String text) {
        this.a = 0;
        this.b = 0;
        this.text = text;
    }

    // The ratio terms that relate to the width and height of an image
    private final int a, b;

    // The name of an ImageAspectRatio
    private final String text;

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
     * f Calculate the greatest common denominator for the given a and b values
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
}
