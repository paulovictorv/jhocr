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
 * Class used to store information "ocr_line" element and their children.
 * 
 * Element example: {@code <span class='ocr_line' id='line_4' title="bbox 36 194 585 225">}
 */
public class HocrLine extends HocrElement {

	public static final String	TAGNAME		= "span";
	public static final String	CLASSNAME	= "ocr_line";

	private HocrParagraph		paragraph;
    private List<HocrWord> words = new ArrayList<>();

    private StringBuilder lineTextBuilder;

	/**
	 * Constructs an <code>HocrLine</code> with a unique id and a coordinates <code>BBox</code>.
	 * 
	 * @param id
	 *            Set the id of element.
	 * @param bbox
	 *            Sets the coordinates of element.
	 */
	public HocrLine(String id, BBox bbox) {
        super(id, bbox);
        this.lineTextBuilder = new StringBuilder();
    }

	@Override
	public String getClassName() {
		return CLASSNAME;
	}

	@Override
	public String getTagName() {
		return TAGNAME;
	}

	/**
	 * @return The children <code>HocrWord</code> of this.
	 */
	public List<HocrWord> getWords() {
		return words;
	}

	/**
	 * Set the children <code>HocrWord</code> of this.
	 * 
	 * @param words
	 *            The children <code>HocrWord</code> of this.
	 */
	public void setWords(List<HocrWord> words) {
		this.words = words;
	}

	/**
	 * Add new word.
	 * 
	 * @param word
	 *            The new word.
	 */
	public void addWord(HocrWord word) {
		word.setLine(this);
        lineTextBuilder.append(word).append(" ");
        getWords().add(word);
	}

	/**
	 * @return the {@link #paragraph} object.
	 */
	public HocrParagraph getParagraph() {
		return paragraph;
	}

	/**
	 * 
	 * @param paragraph
	 *            will be set.
	 */
	public void setParagraph(HocrParagraph paragraph) {
		this.paragraph = paragraph;
	}

	/**
	 * @return all words, concatenating children words with space.
	 */
	public String getText() {
        return lineTextBuilder.toString();
    }

	/**
	 * 
	 * @return the informations of this element as a <code>String</code>.
	 * 
	 *         <pre>
	 * {
	 * 	return &quot;HocrLine{&quot; + super.toString() + &quot;, words=&quot; + words.size() + &quot;}&quot;;
	 * }
	 * </pre>
	 */
	@Override
	public String toString() {
		return "HocrLine{" + super.toString() + ", words=" + words.size() + "}";
	}
}
