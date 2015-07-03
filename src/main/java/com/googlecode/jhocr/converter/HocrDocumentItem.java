/**
 * Copyright (©) 2013 Pablo Filetti Moreira & O.J. Sousa Rodrigues
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.googlecode.jhocr.converter;

import com.googlecode.jhocr.element.HocrDocument;
import com.googlecode.jhocr.parser.HocrParser;
import com.googlecode.jhocr.util.LoggUtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

/**
 * Contains the<br>
 * hocr file {@link com.googlecode.jhocr.converter.HocrDocumentItem#hocrInputStream} and <br>
 * the image {@link com.googlecode.jhocr.converter.HocrDocumentItem#imageProvider}.
 * 
 */
public class HocrDocumentItem {

    private final static Logger logger = LoggerFactory.getLogger(new LoggUtilException().toString());

    private ImagePageProvider imageProvider;
    private HocrDocument hocrDocument;


    /**
     * Returns an {@link com.googlecode.jhocr.converter.HocrDocumentItem} object that can be used for the hocr to pdf conversion from example {@link com.googlecode.jhocr.converter.HocrToPdf}.
	 * 
	 * @param hocrInputStream
	 *            is the input stream of the hocr file.
	 * @param imageInputStream
	 *            is the input stream of the image.
	 */
	public HocrDocumentItem(InputStream hocrInputStream, InputStream imageInputStream) {
        HocrParser hocrParser = new HocrParser(hocrInputStream);
        HocrDocument parse = hocrParser.parse();
        ImagePageProvider imagePageProvider = new ImagePageProvider(imageInputStream);
        init(parse, imagePageProvider);
    }

    public HocrDocumentItem(String hocrSource, InputStream imageInputStream) {
        HocrParser hocrParser = new HocrParser(hocrSource);
        HocrDocument parse = hocrParser.parse();
        ImagePageProvider imagePageProvider = new ImagePageProvider(imageInputStream);
        init(parse, imagePageProvider);
    }

    public HocrDocumentItem(HocrDocument hocrDocument, InputStream imgInputStream) {
        ImagePageProvider imagePageProvider = new ImagePageProvider(imgInputStream);
        init(hocrDocument, imagePageProvider);
    }

    public HocrDocumentItem(HocrDocument hocrDocument, List<BufferedImage> pageImages) {
        ImagePageProvider imagePageProvider = new ImagePageProvider(pageImages);
        init(hocrDocument, imagePageProvider);
    }

    public HocrDocumentItem(InputStream hocrSource, List<BufferedImage> bufferedImages) {
        HocrParser hocrParser = new HocrParser(hocrSource);
        HocrDocument parse = hocrParser.parse();
        imageProvider = new ImagePageProvider(bufferedImages);
        hocrDocument = parse;
    }

    private void init(HocrDocument document, ImagePageProvider provider) {
        this.hocrDocument = document;
        this.imageProvider = provider;
    }

    public BufferedImage getImageForPage(Integer pageNumber) {
        return imageProvider.getPage(pageNumber - 1);
    }

    public HocrDocument getHocrDocument() {
        return hocrDocument;
    }
}