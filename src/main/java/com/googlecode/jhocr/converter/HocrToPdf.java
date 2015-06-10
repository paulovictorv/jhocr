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
import com.googlecode.jhocr.element.HocrPage;
import com.googlecode.jhocr.parser.HocrParser;
import com.googlecode.jhocr.util.LoggUtilException;
import com.googlecode.jhocr.util.enums.PDFF;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TODO add documentation
 * TODO improve the way the information is being passed to the document e.g.: com-googlecode-jhocr-info
 * TODO add documentation, for example why exactly 72.0f
 * 
 */
public class HocrToPdf {
	public static float						POINTS_PER_INCH			= 72.0f;
	private final OutputStream				outputStream;
	private final List<HocrDocumentItem>	items					= new ArrayList<HocrDocumentItem>();
	private List<HashMap<String, Object>>	outlines				= new ArrayList<HashMap<String, Object>>();
	private boolean							useImageDpi				= true;
	private PDFF							pdfFormat				= null;

	private final static Logger				logger					= LoggerFactory.getLogger(new LoggUtilException().toString());

	private static final String				KEY_JHOCR_INFO			= "com-googlecode-jhocr-info";
    private static final String KEY_JHOCR_INFO_VALUE = "This document was generated with jhocr, for more information visit: https://code.google.com/p/jhocr";

	/**
	 * @param outputStream
	 *            of the PDF.
	 */
	public HocrToPdf(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * 
	 * @return the {@link #outputStream} object.
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * @return the {@link #items} collection.
	 */
	public List<HocrDocumentItem> getItems() {
		return items;
	}

	/**
	 * The {@link #pdfFormat} will determine which method will be called e.g. <br>
	 * {@link #convertToPDFA(PdfAConformanceLevel)} or <br>
	 * {@link #convertToPDFX(int)} etc.
	 * 
	 * @return true if the conversion was successful.
	 */
	public boolean convert() {
		boolean result = false;

		if (!getItems().isEmpty() && getItems() != null) {

			PDFF pdff = getPdfFormat();

			if (pdff != null) {

				if (pdff.getValue() instanceof Integer) {

					result = convertToPDFX((Integer) pdff.getValue());

				} else if (pdff instanceof PDFF) {

					PdfAConformanceLevel pdfCL = (PdfAConformanceLevel) pdff.getValue();
					result = convertToPDFA(pdfCL);

				}

			} else {
				result = convertSimple();
			}

		}

		return result;

	}

	/**
	 * This is the old <code>convert()</code> method, almost untouched.<br>
	 * This method will be used if {@link #pdfFormat} is not set.
	 * 
	 * @return true if the conversion was successful.
	 */
	private boolean convertSimple() {
		boolean result = false;

		Document document = new Document();

		try {
			PdfWriter writer = PdfWriter.getInstance(document, getOutputStream());

			document.open();
			document.addHeader(KEY_JHOCR_INFO, KEY_JHOCR_INFO_VALUE);
			document.setMargins(0, 0, 0, 0);
            processDocument(document, writer);


            /**
             * Closing the document body stream.
             */
            document.close();
            getOutputStream().close();
			result = true;

		} catch (UnsupportedOperationException e) {
			document.close();
			logger.error("This operation is not yet implemented.", e);
			result = false;
		} catch (DocumentException e) {
			document.close();
			logger.error("exception while genrating the PDF.", e);
			result = false;
		} catch (IOException e) {
			document.close();
			logger.error("FileSystem I/O Exception, please check the log and file system persmissions.", e);
			result = false;
		}

		return result;
	}

    private void processDocument(Document document, PdfWriter writer) {
        /**
         * TODO add documentation
         */
        for (HocrDocumentItem item : getItems()) {

            HocrParser parser = new HocrParser(item.getHocrInputStream());

            HocrDocument hocrDocument = parser.parse();

            /**
             * TODO add documentation
             */
            for (HocrPage hocrPage : hocrDocument.getPages()) {
                HocrPageProcessor pageProcessor = new HocrPageProcessor(hocrPage,
                        item.getImageForPage(hocrPage.getPageNumber()),
                        isUseImageDpi());

                if (pageProcessor.isInitialized()) {
                    pageProcessor.process(document, writer);
                }
            }
        }

        if (!outlines.isEmpty()) {
            writer.setOutlines(outlines);
        }
    }

    /**
     *
     * @param pdfXConformance
	 *            determines into which format the PDF-X will be converted.
	 * @return true if the conversion was successful.
	 */
	private boolean convertToPDFX(int pdfXConformance) {
		boolean result = false;
		Document document = new Document();

		try {
			PdfWriter writer = PdfWriter.getInstance(document, getOutputStream());
			writer.setPDFXConformance(pdfXConformance);

			document.open();
			document.addHeader(KEY_JHOCR_INFO, KEY_JHOCR_INFO_VALUE);
			document.setMargins(0, 0, 0, 0);

			/**
			 * TODO add documentation
			 */
            processDocument(document, writer);

			/**
			 * Closing the document body stream.
			 */
			document.close();
			getOutputStream().close();
			result = true;

		} catch (UnsupportedOperationException e) {
			document.close();
			logger.error("This operation is not yet implemented.", e);
			result = false;
		} catch (DocumentException e) {
			document.close();
			logger.error("exception while genrating the PDF.", e);
			result = false;
		} catch (IOException e) {
			document.close();
			logger.error("FileSystem I/O Exception, please check the log and file system persmissions.", e);
			result = false;
		}

		return result;

	}

	/**
	 * @param pdfConformanceLevel
	 *            determines into which format the PDF-A&/B will be converted.
	 * @return true if the conversion was successful.
	 */
	private boolean convertToPDFA(PdfAConformanceLevel pdfConformanceLevel) {
		boolean result = false;
		Document document = new Document();

		try {
			PdfAWriter writer = PdfAWriter.getInstance(document, getOutputStream(), pdfConformanceLevel);
			writer.createXmpMetadata();

			document.open();
			document.addHeader(KEY_JHOCR_INFO, KEY_JHOCR_INFO_VALUE);
			document.setMargins(0, 0, 0, 0);

			/**
			 * TODO add documentation
			 */
            processDocument(document, writer);

			InputStream is = this.getClass().getResourceAsStream("/sRGB.profile");

			ICC_Profile icc = ICC_Profile.getInstance(is);
			writer.setOutputIntents(KEY_JHOCR_INFO, KEY_JHOCR_INFO_VALUE, "http://www.color.org", "sRGB IEC61966-2.1", icc);

			/**
			 * Closing the document body stream.
			 */
			document.close();
			getOutputStream().close();
			result = true;

		} catch (UnsupportedOperationException e) {
			document.close();
			logger.error("This operation is not yet implemented.", e);
			result = false;
		} catch (DocumentException e) {
			document.close();
			logger.error("exception while genrating the PDF.", e);
			result = false;
		} catch (IOException e) {
			document.close();
			logger.error("FileSystem I/O Exception, please check the log and file system persmissions.", e);
			result = false;
		}

		return result;

	}

	/**
	 * Adds a new {@link com.googlecode.jhocr.converter.HocrDocumentItem} to the {@link #items} collection.
	 * 
	 * @param hocrInputStream
	 *            Html-OCR (HOCR) InputStream
	 * @param imgInputStream
	 *            Image InputStream, this image will be used to create the pdf searchable
	 */
	public void addHocrDocument(InputStream hocrInputStream, InputStream imgInputStream) {
		this.items.add(new HocrDocumentItem(hocrInputStream, imgInputStream));
	}

	/**
	 * @return the {@link #outlines} collection.
	 */
	public List<HashMap<String, Object>> getOutlines() {
		return outlines;
	}

	/**
	 * 
	 * @param outlines
	 *            will be set.
	 */
	public void setOutlines(List<HashMap<String, Object>> outlines) {
		this.outlines = outlines;
	}

	/**
	 * @param outline
	 *            will be set.
	 */
	public void addOutline(HashMap<String, Object> outline) {
		getOutlines().add(outline);
	}

	/**
	 * @return the {@link #useImageDpi} value.
	 */
	public boolean isUseImageDpi() {
		return useImageDpi;
	}

	/**
	 * @param useImageDpi
	 *            will be set.
	 */
	public void setUseImageDpi(boolean useImageDpi) {
		this.useImageDpi = useImageDpi;
	}

	/**
	 * 
	 * @return the {@link #pdfFormat} that was set for the current document.
	 */
	public PDFF getPdfFormat() {
		return pdfFormat;
	}

	/**
	 * The PDF can be converted to a certain format, you can find all current supported and tested formats in the <code>PDFF.java</code> class.
	 * 
	 * @param pdfFormat
	 *            sets the PDF format for the current document to be converted.
	 */
	public void setPdfFormat(PDFF pdfFormat) {
		this.pdfFormat = pdfFormat;
	}

}
