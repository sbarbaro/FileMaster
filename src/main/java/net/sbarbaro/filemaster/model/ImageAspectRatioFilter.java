package net.sbarbaro.filemaster.model;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Filters image files based on aspect ratio as determined by IIOImageMetadata
 * @author Steven A. Barbaro (steven@abarbaro.net)
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
        super(arf.getFileType());
        this.imageAspectRatioTarget = arf.getImageAspectRatioTarget();
    }

    /**
     * Accepts the image file specified by the input path based on calculated
     * aspect ration
     * @param pathIn The path of an image file to check
     * @return true if the calculated aspect ratio of the image matches this
     * imageAspectRatioTarget; otherwise, false.
     */
    @Override
    public boolean accept(Path pathIn) {

        boolean result = super.accept(pathIn);

        // Short-circuit if file is not an image
        if (result) {
            
            result = false;
            
            try {

                BufferedImage img = ImageIO.read(pathIn.toFile());

                ImageAspectRatio imageAspectRatio
                        = ImageAspectRatio.fromValues(img.getWidth(), img.getHeight());

                result = imageAspectRatio == imageAspectRatioTarget;

            } catch (Throwable t) {
       
                Logger.getLogger(ImageAspectRatioFilter.class.getName()).log(Level.WARNING, pathIn.getFileName().toString(), t);
            }
            
        }

        return result;
    }

    /**
     * @return  this imageAspectRatioTarget

     */
    public ImageAspectRatio getImageAspectRatioTarget() {
        return imageAspectRatioTarget;
    }

}
