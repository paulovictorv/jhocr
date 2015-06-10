package com.googlecode.jhocr;

import com.googlecode.jhocr.util.ResourceLoader;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by paulo on 10/06/15.
 */
public class TIFFLoadingTest {

    @Test
    public void shouldLoadTiffImage() throws IOException {
        InputStream input = ResourceLoader.asStream("test-data/multipage.tif");
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(input);

        Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);

        System.out.println(imageReaders.hasNext());
        System.out.println(imageReaders.next().getClass().getSimpleName());

    }

}
