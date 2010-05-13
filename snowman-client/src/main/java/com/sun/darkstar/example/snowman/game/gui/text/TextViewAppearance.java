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

import java.io.IOException;

import org.fenggui.DecoratorAppearance;
import org.fenggui.render.DirectTextRenderer;
import org.fenggui.render.Font;
import org.fenggui.render.Graphics;
import org.fenggui.render.IOpenGL;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;
import org.fenggui.util.Rectangle;

/**
 * <code>TextViewAppearance</code> defines the appearance of <code>TextView</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 11-07-2008 17:20 EST
 * @version Modified date: 11-10-2008 18:15 EST
 */
public class TextViewAppearance extends DecoratorAppearance {
	/**
	 * The text view object.
	 */
	private TextView view;
	/**
	 * The default text color.
	 */
	private Color textColor;
	/**
	 * The text renderer object.
	 */
	private DirectTextRenderer textRenderer;

	/**
	 * Constructor of TextViewAppearance.
	 * @param view The text view object.
	 */
	public TextViewAppearance(TextView view) {
		super(view);
		this.view = view;
		this.textColor = Color.BLACK;
		this.textRenderer = new DirectTextRenderer();
	}

	/**
	 * Either serializes or deserializes the data contained by the object
	 * by calling the processing methods of the passed InputOutputStream.
	 * 
	 * @param stream the stream used by the serialization or deserialization
	 * @throws IOException thrown if an I/O exception occurs during the operation
	 * @throws IXMLStreamableException if the input/output operations fail
	 */
	@Override
	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException{
		super.process(stream);
		if(stream.isInputStream()) {
			this.setFont(stream.processChild("Font", this.getFont(), Font.getDefaultFont(), Font.class));
		}
		this.textColor = stream.processChild("Color", this.textColor, Color.BLACK, Color.class);		
	}

	/**
	 * Set the font of the text renderer.
	 * @param font The font to be set.
	 */
	public void setFont(Font font) {
		this.textRenderer.setFont(font);
		this.view.updateMinSize();
	}

	/**
	 * Set the default text color.
	 * @param textColor The color to be set.
	 */
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	/**
	 * Retrieve the font of the text renderer.
	 * @return The font of the text renderer.
	 */
	public Font getFont() {
		return this.textRenderer.getFont();
	}

	/**
	 * Retrieve the default text color.
	 * @return The defualt text color.
	 */
	public Color getTextColor() {
		return this.textColor;
	}

	@Override
	public Dimension getContentMinSizeHint() {
		return new Dimension(this.view.getMinWidth(), this.view.fullHeight);
	}

	@Override
	public void paintContent(Graphics g, IOpenGL gl) {
		int y = this.getContentHeight();
		int x = 0;
		x += g.getTranslation().getX();
		y += g.getTranslation().getY();
		Rectangle clipRect = new Rectangle(g.getClipSpace());
		clipRect.setX(0);
		clipRect.setY(clipRect.getY() - this.view.getDisplayY() - this.getContentHeight());
		for (TextRun run : this.view.getRuns()) {
			if (run.getBoundingRect().intersect(clipRect)) {
				run.paint(g, x, y);
			}
		}
	}
}
