package com.googlecode.jhocr.converter;

import com.googlecode.jhocr.element.HocrDocument;
import com.googlecode.jhocr.parser.HocrParser;
import com.googlecode.jhocr.util.ResourceLoader;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulo on 03/07/15.
 */
public class HocrDocumentItemTest {

    @Test
    public void shouldInitializeUsingHocrDocumentAndStream() {
        InputStream inputStream = ResourceLoader.asStream("test-data/multipage.html");
        HocrParser hocrParser = new HocrParser(inputStream);
        HocrDocument parse = hocrParser.parse();

        InputStream multipage = ResourceLoader.asStream("test-data/multipage.tif");
        HocrDocumentItem hocrDocumentItem = new HocrDocumentItem(parse, multipage);
        hocrDocumentItem.getImageForPage(1);
    }

    @Test
    public void shouldInitializeUsingHocrDocumentAndListOfBufferedImages() throws IOException {
        InputStream inputStream = ResourceLoader.asStream("test-data/multipage.html");
        InputStream multipage = ResourceLoader.asStream("test-data/multipage.tif");

        HocrParser hocrParser = new HocrParser(inputStream);
        HocrDocument parse = hocrParser.parse();
        List<BufferedImage> bufferedImages = loadImages(multipage);
        HocrDocumentItem hocrDocumentItem = new HocrDocumentItem(parse, bufferedImages);

        hocrDocumentItem.getImageForPage(1);
    }

    @Test
    public void shouldInitializeUsingInputStreamAndStream() {
        InputStream inputStream = ResourceLoader.asStream("test-data/multipage.html");
        InputStream multipage = ResourceLoader.asStream("test-data/multipage.tif");
        HocrDocumentItem hocrDocumentItem = new HocrDocumentItem(inputStream, multipage);
        hocrDocumentItem.getImageForPage(1);
    }

    @Test
    public void shouldInitializeUsingInputStreamAndListOfBufferedImages() throws IOException {
        InputStream inputStream = ResourceLoader.asStream("test-data/multipage.html");
        InputStream multipage = ResourceLoader.asStream("test-data/multipage.tif");

        List<BufferedImage> bufferedImages = loadImages(multipage);
        HocrDocumentItem hocrDocumentItem = new HocrDocumentItem(inputStream, bufferedImages);

        hocrDocumentItem.getImageForPage(1);
    }

    private List<BufferedImage> loadImages(InputStream multipage) throws IOException {
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(multipage);
        ImageReader tiffReader = ImageIO.getImageReadersByFormatName("TIFF").next();
        tiffReader.setInput(imageInputStream);
        int numImages = tiffReader.getNumImages(true);

        List<BufferedImage> list = new ArrayList<>();
        for (int i = 0; i < numImages; i++) {
            list.add(tiffReader.read(i));
        }

        return list;
    }

}