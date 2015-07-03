package com.googlecode.jhocr.converter;

import com.googlecode.jhocr.converter.exceptions.PageReadException;
import com.googlecode.jhocr.util.LoggUtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Provides an abstraction around the image provider.
 * <p/>
 * This allows the client code to choose how to decode the image, allowing for more flexible use cases.
 * <p/>
 * <p/>
 * Created by paulo on 03/07/15.
 */
class ImagePageProvider {

    private final static Logger logger = LoggerFactory.getLogger(new LoggUtilException().toString());

    private ImageReader imageReader;
    private List<BufferedImage> pageImages;

    public ImagePageProvider(List<BufferedImage> pageImages) {
        this.pageImages = pageImages;
    }

    public ImagePageProvider(InputStream imgInputStream) {
        try {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(imgInputStream);

            if (imageInputStream == null) {
                throw new IOException();
            }

            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);

            if (imageReaders != null && !imageReaders.hasNext()) {
                //TODO see if we can extract the intended format
                throw new IOException("Format not supported.");
            }

            imageReader = imageReaders.next();
            imageReader.setInput(imageInputStream);

            logger.info("Using image reader {}", imageReader.getClass().getSimpleName());
        } catch (IOException e) {
            logger.error("Error while creating image input stream", e);
        }
    }

    /**
     * Extracts page with the specified index on the image page source.
     *
     * @param i the page index
     * @return the page image
     * @throws PageReadException if non-existing index is provided
     */
    public BufferedImage getPage(int i) {
        try {
            if (imageReader == null) {
                return pageImages.get(i);
            } else {
                return imageReader.read(i);
            }
        } catch (IOException | IndexOutOfBoundsException e) {
            logger.error("Error while reading the image page: ", e);
            throw new PageReadException(e);
        }
    }
}
