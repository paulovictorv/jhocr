package com.googlecode.jhocr;

import com.googlecode.jhocr.element.HocrDocument;
import com.googlecode.jhocr.parser.HocrParser;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;


/**
 * Created by paulo on 02/06/15.
 */
public class HocrParserTest {

    @Test
    public void shouldExtractPageNumber() {
        InputStream hocr = this.getClass().getClassLoader()
                .getResourceAsStream("test-data/jhocr_test_case_0001.html");
        HocrDocument parse = new HocrParser(hocr).parse();

        parse.getPages()
                .stream()
                .filter(page -> page.getPageNumber() != 0)
                .findFirst()
                .ifPresent(page -> assertEquals((int) page.getPageNumber(), 1));
    }

    @Test
    public void shouldExtractPageNumber2() {
        InputStream hocr = this.getClass().getClassLoader()
                .getResourceAsStream("test-data/multipage.html");
        HocrDocument parse = new HocrParser(hocr).parse();

        assertEquals(2, parse.getPages().size());

        parse.getPages()
                .stream()
                .filter(page -> page.getPageNumber() == 2)
                .findFirst()
                .ifPresent(page -> assertEquals((int) page.getPageNumber(), 2));
    }

}
