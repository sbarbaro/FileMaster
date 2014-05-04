package net.sbarbaro.filemaster.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Filters image files based on aspect ratio as determined by IIOImageMetadata
 *
 * @author steven
 */
public class ImageAspectRatioFilter extends FileTypeFilter {

    private static final long serialVersionUID = -2774182403554547708L;

    private final ImageAspectRatio imageAspectRatioTarget;

    /**
     * Default constructor
     */
    public ImageAspectRatioFilter() {
        this(ImageAspectRatio.OTHER);
    }

    /**
     * Constructor
     *
     * @param imageAspectRatio
     */
    public ImageAspectRatioFilter(ImageAspectRatio imageAspectRatio) {
        super(FileType.Image);
        this.imageAspectRatioTarget = imageAspectRatio;
    }

    /**
     * Copy constructor
     *
     * @param arf The ImageAspectRatioFilter instance to copy
     */
    public ImageAspectRatioFilter(ImageAspectRatioFilter arf) {
        super(arf.getType());
        this.imageAspectRatioTarget = arf.getImageAspectRatioTarget();
    }

    public boolean accept(File pathname) {

        boolean result = super.accept(pathname);

        // Short-circuit if file is not an image
        if (!result) {
            return result;
        }

        try {

            BufferedImage img = ImageIO.read(pathname);

            ImageAspectRatio imageAspectRatio
                    = ImageAspectRatio.fromValues(img.getWidth(), img.getHeight());

            result = imageAspectRatio == imageAspectRatioTarget;

        } catch (IOException e) {
            Logger.getLogger(ImageAspectRatioFilter.class.getName()).log(Level.WARNING, null, e);
        } catch (Throwable t) {
            Logger.getLogger(ImageAspectRatioFilter.class.getName()).log(Level.SEVERE, null, t);
        }

        return result;
    }

    public ImageAspectRatio getImageAspectRatioTarget() {
        return imageAspectRatioTarget;
    }

}
