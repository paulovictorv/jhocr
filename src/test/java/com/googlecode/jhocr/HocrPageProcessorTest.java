package com.googlecode.jhocr;

import com.googlecode.jhocr.converter.HocrPageProcessor;
import com.googlecode.jhocr.element.HocrDocument;
import com.googlecode.jhocr.element.HocrPage;
import com.googlecode.jhocr.parser.HocrParser;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertTrue;

/**
 * Created by paulo on 10/06/15.
 */
public class HocrPageProcessorTest {

    private static HocrPage hocrPage;
    private static ClassLoader classLoader = HocrPageProcessor.class.getClassLoader();

    @BeforeClass
    public static void init() {
        HocrParser hocrParser = new HocrParser(classLoader.getResourceAsStream("test-data/jhocr_test_case_0001.html"));
        HocrDocument parse = hocrParser.parse();
        hocrPage = parse.getPages().get(0);
    }

    @Test
    public void shouldInitializeWithInputStream() {
        InputStream resourceAsStream = classLoader.getResourceAsStream("test-data/jhocr_test_case_0001.tif");
        HocrPageProcessor hocrPageProcessor = new HocrPageProcessor(hocrPage, resourceAsStream, true);

        assertTrue(hocrPageProcessor.isInitialized());
    }

    @Test
    public void shouldInitializeWithBufferedImage() throws IOException {
        InputStream resourceAsStream = classLoader.getResourceAsStream("test-data/multipage.tif");
        BufferedImage read = ImageIO.read(resourceAsStream);
        HocrPageProcessor hocrPageProcessor = new HocrPageProcessor(hocrPage, read, true);

        assertTrue(hocrPageProcessor.isInitialized());
    }


}
