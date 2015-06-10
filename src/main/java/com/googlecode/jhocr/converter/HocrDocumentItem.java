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

/**
 * Contains the<br>
 * hocr file {@link com.googlecode.jhocr.converter.HocrDocumentItem#hocrInputStream} and <br>
 * the image {@link com.googlecode.jhocr.converter.HocrDocumentItem#imageInputStream}.
 * 
 */
public class HocrDocumentItem {

    private final static Logger logger = LoggerFactory.getLogger(new LoggUtilException().toString());
    private ImageReader imageReader;

    private InputStream hocrInputStream;
    private ImageInputStream imageInputStream;


	/**
	 * Returns an {@link com.googlecode.jhocr.converter.HocrDocumentItem} object that can be used for the hocr to pdf conversion from example {@link com.googlecode.jhocr.converter.HocrToPdf}.
	 * 
	 * @param hocrInputStream
	 *            is the input stream of the hocr file.
	 * @param imageInputStream
	 *            is the input stream of the image.
	 */
	public HocrDocumentItem(InputStream hocrInputStream, InputStream imageInputStream) {
        try {
            this.hocrInputStream = hocrInputStream;
            this.imageInputStream = ImageIO.createImageInputStream(imageInputStream);

            if (this.imageInputStream == null) {
                throw new IOException();
            }

            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(this.imageInputStream);

            if (imageReaders != null && !imageReaders.hasNext()) {
                //TODO see if we can extract the intended format
                throw new IOException("Format not supported.");
            }

            imageReader = imageReaders.next();
            imageReader.setInput(this.imageInputStream);
            logger.info("Using image reader {}", imageReader.getClass().getSimpleName());


        } catch (IOException e) {
            logger.error("Error while creating image input stream", e);
        }
    }

	/**
	 * @return the {@link #hocrInputStream} of the hocr file.
	 */
	public InputStream getHocrInputStream() {
		return hocrInputStream;
	}

    public BufferedImage getImageForPage(Integer pageNumber) {
        try {
            return imageReader.read(pageNumber - 1);
        } catch (IOException | IndexOutOfBoundsException e) {
            logger.error("Error while reading the image page: ", e);
            throw new PageReadException(e);
        }
    }
}
