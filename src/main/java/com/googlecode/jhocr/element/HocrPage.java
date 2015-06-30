/**
 * Copyright (©) 2013 Pablo Filetti Moreira
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

package com.googlecode.jhocr.element;

import com.googlecode.jhocr.attribute.BBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to store information "ocr_page" element and their children.
 * 
 * Element example: {@code <div class='ocr_page' id='page_1' title='image "phototest.tif"; bbox 0 0 640 480'>}
 */
public class HocrPage extends HocrElement {

	public static final String	TAGNAME		= "div";
	public static final String	CLASSNAME	= "ocr_page";

    private Integer pageNumber;
    private HocrDocument		document;
	private String				image;
    private List<HocrCarea> careas = new ArrayList<>();
    private StringBuilder pageTextBuilder;

	/**
	 * Constructs an <code>HocrPage</code> with a unique id and a coordinates <code>BBox</code>.
	 * 
	 * @param id Set the id of element.
	 * @param bbox Sets the coordinates of element.
	 * @param image Set the name of image of element.
	 */
	public HocrPage(String id, BBox bbox, String image) {
		super(id, bbox);
        pageTextBuilder = new StringBuilder();
        this.image = image;
        this.pageNumber = Integer.parseInt(id.split("_")[1]);
    }

	/**
	 * @return The name of image.
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Set the name of image.
	 * 
	 * @param image The name of image.
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return The children <code>HocrCarea</code> of this.
	 */
	public List<HocrCarea> getCareas() {
		return careas;
	}

	/**
	 * Set the children <code>HocrCarea</code> of this.
	 * @param careas The children <code>HocrCarea</code> of this.
	 */
	public void setCareas(List<HocrCarea> careas) {
		this.careas = careas;
	}

	/**
	 * Add new carea. 
	 * 
	 * @param carea The new carea.
	 */
	public void addCarea(HocrCarea carea) {
		carea.setPage(this);
        pageTextBuilder.append(carea.getText()).append(" ");
        getCareas().add(carea);
    }

	@Override
	public String getClassName() {
		return CLASSNAME;
	}

	@Override
	public String getTagName() {
		return TAGNAME;
	}

    @Override
    public String getText() {
        return pageTextBuilder.toString();
    }

    /**
     * @return The children <code>HocrParagraph</code> of this.
     */
	public List<HocrParagraph> getParagraphs() {

		List<HocrParagraph> paragraphs = new ArrayList<HocrParagraph>();

		for (HocrCarea carea : getCareas()) {
			paragraphs.addAll(carea.getParagraphs());
		}

		return paragraphs;
	}

	/**
	 * @return The children <code>HocrLine</code> of this.
	 */
	public List<HocrLine> getLines() {

		List<HocrLine> lines = new ArrayList<HocrLine>();

		for (HocrParagraph paragraph : getParagraphs()) {
			lines.addAll(paragraph.getLines());
		}

		return lines;
	}

	/**
	 * @return The children <code>HocrWord</code> of this.
	 */
	public List<HocrWord> getWords() {

		List<HocrWord> words = new ArrayList<HocrWord>();

		for (HocrLine line : getLines()) {
			words.addAll(line.getWords());
		}

		return words;
	}

	/**
	 * Set the parent <code>HocrDocument</code> of this.
	 * @param page The parent <code>HocrDocument</code> of this.
	 */
	public void setDocument(HocrDocument document) {
		this.document = document;
	}

	/**
	 * @return The parent <code>HocrDocument</code> of this.
	 */
	public HocrDocument getDocument() {
		return document;
	}

	/**
	 * Returns the informations of this element as a <code>String</code>.
	 *
	 * @return the informations of this element as a <code>String</code>.
	 */
	@Override
	public String toString() {
		return "HocrPage{" + super.toString() + ", image=" + image + ", careas=" + careas.size() + "}";
	}

    public Integer getPageNumber() {
        return pageNumber;
    }
}