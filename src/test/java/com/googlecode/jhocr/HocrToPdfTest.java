package com.googlecode.jhocr;

import com.googlecode.jhocr.converter.HocrToPdf;
import com.googlecode.jhocr.util.JHOCRUtil;
import com.googlecode.jhocr.util.ResourceLoader;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by paulo on 10/06/15.
 */
public class HocrToPdfTest {

    @Test
    public void shouldConvertMultipageTif() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream hocrStream = ResourceLoader.asStream("test-data/multipage.html");
        InputStream imageStream = ResourceLoader.asStream("test-data/multipage.tif");

        HocrToPdf hocrToPdf = new HocrToPdf(byteArrayOutputStream);
        hocrToPdf.addHocrDocument(hocrStream, imageStream);

        hocrToPdf.convert();

        JHOCRUtil jhocrUtil = new JHOCRUtil();

        jhocrUtil.testGeneratedPDF("Multipage", byteArrayOutputStream.toByteArray());
    }

}
