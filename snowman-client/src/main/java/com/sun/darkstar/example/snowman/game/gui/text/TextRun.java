/*
 *
 * Copyright (c) 2007-2010, Oracle and/or its affiliates.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in
 *       the documentation and/or other materials provided with the
 *       distribution.
 *     * Neither the name of Sun Microsystems, Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.sun.darkstar.example.snowman.game.gui.text;

import java.util.ArrayList;

import org.fenggui.render.Graphics;
import org.fenggui.render.IOpenGL;
import org.fenggui.render.Font;
import org.fenggui.text.TextStyle;
import org.fenggui.util.CharacterPixmap;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;
import org.fenggui.util.Rectangle;

/**
 * <code>TextRun</code> defines the text displayed in <code>TextView</code>
 * with its own specified text style.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 11-06-2008 19:18 EST
 * @version Modified date: 11-07-2008 17:39 EST
 */
public class TextRun{
	/**
	 * The characters array of the text.
	 */
	private char[] chars;
	/**
	 * The text style of this text run.
	 */
	private TextStyle style;
	/**
	 * The bounding rectangle of this text run.
	 */
	private final Rectangle boundingRect;
	/**
	 * The array list containing the sub strings.
	 */
	private final ArrayList<Substring> substrings;
	/**
	 * Flag indicates if the new line should be fixed.
	 */
	private boolean newLineFixed;

	/**
	 * Constructor of TextRun.
	 * @param chars The character array representation of the text.
	 * @param style The associated <code>TextStyle</code>.
	 */
	public TextRun(char[] chars, TextStyle style) {
		super();
		this.chars = chars;
		this.style = style;
		this.boundingRect = new Rectangle(0, 0, 0, 0);
		this.substrings = new ArrayList<Substring>();
	}

	/**
	 * Constructor of TextRun.
	 * @param text The string representation of the text.
	 * @param style The associated <code>TextStyle</code>.
	 */
	public TextRun(String text, TextStyle style) {
		this(text.toCharArray(), style);
	}

	/**
	 * Set the text color of this text run.
	 * @param color The new color to be set.
	 */
	public void setTextColor(Color color) {
		this.style.setColor(color);
	}                     

	/**
	 * Retrieve the bounding rectangle of this text run.
	 * @return The bounding rectangle of the text run.
	 */
	public Rectangle getBoundingRect() {
		return this.boundingRect;
	}

	/**
	 * Retrieve the text of this text run.
	 * @return The character array of the text.
	 */
	public char[] getChars() {
		return this.chars;
	}

	/**
	 * Retrieve the font of the text run.
	 * @return The font of the text run.
	 */
	public Font getFont() {
		return this.style.getFont();
	}

	/**
	 * Retrieve the color of this text run.
	 * @return The text color.
	 */
	public Color getColor() {
		return this.style.getColor();
	}

	/**
	 * Check if this TextRun contains the specified point.
	 * @param x The x coordinate to find in the <code>TextView</code> coordinates system
	 * @param y The y coordinate to find in the <code>TextView</code> coordinates system
	 * @return True if the given point if in this run. False otherwise.
	 */
	public boolean contains(int x, int y) {
		if(this.boundingRect.contains(x, y)) {
			Substring target = null;
			for(Substring substring : this.substrings) {
				if(x >= substring.xOff && y >= substring.yOff) {
					target = substring;
				}
			}
			if(target != null && x < (target.xOff + getFont().getWidth(chars, target.begin, target.end))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Draw the TextRun.
	 * @param g The graphics object.
	 * @param xOff The x offset.
	 * @param yOff The y offset.
	 */
	protected void paint(Graphics g, int xOff, int yOff) {
		IOpenGL gl = g.getOpenGL();
		Font font = getFont();
		if (this.chars.length == 0) return;
		font.getCharPixMap('a').getTexture().bind();
		gl.enableTexture2D(true);
		gl.setTexEnvModeModulate();
		g.setColor(this.style.getColor());
		gl.startQuads();
		for(Substring substring : this.substrings) {
			int begin = substring.begin;
			int end = substring.end;
			int x = xOff + substring.xOff;
			int y = yOff + substring.yOff;
			CharacterPixmap charMap;
			for(int i = begin; i < end; i++) {
				char character = this.chars[i];
				charMap = font.getCharPixMap(character);
				final int imgWidth = charMap.getWidth();
				final int imgHeight = charMap.getHeight();
				final float endY = charMap.getEndY();
				final float endX = charMap.getEndX();
				final float startX = charMap.getStartX();
				final float startY = charMap.getStartY();
				gl.texCoord(startX, endY);
				gl.vertex(x, y);
				gl.texCoord(startX, startY);
				gl.vertex(x, imgHeight + y);
				gl.texCoord(endX, startY);
				gl.vertex(imgWidth + x, imgHeight + y);
				gl.texCoord(endX, endY);
				gl.vertex(imgWidth + x, y);
				x += charMap.getCharWidth();
			}
		}
		gl.end();
		gl.enableTexture2D(false);
	}


	/**
	 * Prepares this text run of text for word-wrapped stylized display, breaking it
	 * down into discrete Substrings that can be drawn easily.
	 * @param xMax The wrapping point or the width of the view.
	 * @param scratchDimension The dimension which is filled in based xMax.
	 * @param point The Point contains the x, y offset that this TextRun starts at. The values in point get
	 * 				replaced with the Point that the next text run should begin at.
	 */
	protected void prepare(int xMax, Dimension scratchDimension, Point point) {
		Font font = getFont();
		this.substrings.clear();
		int x = point.getX();
		int y = point.getY() - font.getHeight();
		if(xMax <= 0) xMax = 600;
		int xOff = x;
		int yOff = y;
		int height = Math.max(scratchDimension.getHeight(), -yOff);
		int begin = 0;
		int len = this.chars.length;
		int end = 0;
		int width = 0;
		do {
			if(this.chars[begin] == '\n') {
				begin++;
				if(!this.newLineFixed) {
					height += font.getHeight();
					yOff = -height;
					xOff = 0;
				} else {
					this.newLineFixed = false;
				}
				if(begin == this.chars.length) {
					width = 0;
					break;
				}
			}
			end = findEnd(begin, xOff, xMax);
			width = xOff + font.getWidth(this.chars, begin, end);
			this.substrings.add(new Substring(begin, end, xOff, yOff));
			xOff = 0;
			if(end != len) {
				height += font.getHeight();
				yOff = -height;
				begin = end;
			}
		} while (end != len);
		scratchDimension.setWidth(xMax);
		scratchDimension.setHeight(Math.max(height, scratchDimension.getHeight()));
		this.boundingRect.setX(0);
		this.boundingRect.setY(-scratchDimension.getHeight());
		this.boundingRect.setWidth(scratchDimension.getWidth());
		this.boundingRect.setHeight(point.getY() + scratchDimension.getHeight());
		point.setX(width);
		point.setY(yOff + font.getHeight());
	}


	/**
	 * Find the index of the last character of the substring that can be drawn before having to wrap.
	 * @param begin The offset of the first character.
	 * @param xOff The X position that the substring starts at.
	 * @param xMax The wrapping point.
	 * @return The index of the last character that can be drawn before wrapping.
	 */
	private int findEnd(int begin, int xOff, int xMax) {
		Font font = getFont();
		int end = begin;
		int width = xOff;
		while(end != this.chars.length) {
			char endChar = this.chars[end];
			int endCharWidth = font.getWidth(endChar);
			if(width + endCharWidth <= xMax && (this.chars[end] != '\n')) {
				width += endCharWidth;
				end++;
			} else {
				break;
			}
		}
		if(end == chars.length) return end;
		if(this.chars[end] == '\n') {
			this.newLineFixed = true;
			return end;
		}
		int oldEnd = end;
		while(end > begin) {
			if(this.chars[end - 1] == ' ') {
				return end;
			} else {
				end--;
			}
		}
		int newEnd = xOff == 0 ? oldEnd : end;
		return newEnd != begin ? newEnd : newEnd + 1;
	}

	/**
	 * Substring represents a substring that is ready to be drawn.
	 * 
	 * @author Yi Wang (Neakor)
	 * @version Creation date: 11-06-2008 19:18 EST
	 * @version Modified date: 11-06-2008 20:00 EST
	 */
	private class Substring{
		public int begin;
		public int end;
		public int xOff;
		public int yOff;

		/**
		 * Constructor of Substring.
		 * @param begin The starting index into the chars array.
		 * @param end The endding index into the chars array.
		 * @param xOff The x offset to start drawing.
		 * @param yOff The y offset to start drawing.
		 */
		public Substring(int begin, int end, int xOff, int yOff) {
			this.begin = begin;
			this.end = end;
			this.xOff = xOff;
			this.yOff = yOff;
		}
	}
}
