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
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.fenggui.FengGUI;
import org.fenggui.ITextWidget;
import org.fenggui.ObservableWidget;
import org.fenggui.ScrollContainer;
import org.fenggui.event.ITextChangedListener;
import org.fenggui.event.TextChangedEvent;
import org.fenggui.event.mouse.IMousePressedListener;
import org.fenggui.event.mouse.MousePressedEvent;
import org.fenggui.render.Font;
import org.fenggui.text.TextStyle;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

/**
 * <code>TextView</code> defines a text display area which displays the text
 * added to it with specified <code>TextStyle</code>. The texts which are
 * displayed will fade out if there is no new texts are added in 10 seconds.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 11-06-2008 18:54 EST
 * @version Modified date: 11-11-2008 17:39 EST
 */
public class TextView extends ObservableWidget implements ITextWidget{
	/**
	 * The minimum width of text view.
	 */
	private int minWidth;
	/**
	 * The point defines the next draw position.
	 */
	private Point nextDrawPoint;
	/**
	 * The dimension object.
	 */
	private Dimension scratchDimension;
	/**
	 * The full height of the text view.
	 */
	protected int fullHeight;
	/**
	 * The array list of text changed listener.
	 */
	private ArrayList<ITextChangedListener> textChangedHook;
	/**
	 * The text view appearance object.
	 */
	private TextViewAppearance appearance;
	/**
	 * The default text style.
	 */
	private TextStyle defaulStyle;
	/**
	 * The <code>ConcurrentLinkedQueue</code> of <code>TextRun</code>.
	 */
	private ConcurrentLinkedQueue<TextRun> runs;
	/**
	 * The text fader object.
	 */
	private TextViewFader fader;
	
	/**
	 * Constructor of TextView.
	 */
	public TextView() {
		// Set up default values.
		this.minWidth = 10;
		this.nextDrawPoint =  new Point(0, 0);
		this.scratchDimension = new Dimension(0, 0);
		this.textChangedHook = new ArrayList<ITextChangedListener>();
		this.appearance = new TextViewAppearance(this);
		this.defaulStyle = new TextStyle(this.appearance.getFont(), this.appearance.getTextColor());
		this.runs = new ConcurrentLinkedQueue<TextRun>();
		this.fader = new TextViewFader(this);
		FengGUI.getTheme().setUp(this);
	}

	/**
	 * Prepares all of the TextRuns for rendering.
	 */
	private void prepareAll() {
		this.scratchDimension.setSize(0, 0);
		this.nextDrawPoint.setX(0);
		this.nextDrawPoint.setY(0);
		for(TextRun run : this.runs) {
			this.prepare(run);
		}
	}

	/**
	 * Prepare a TextRun to be displayed. If word-wrapping is necessary, the TextRun
	 * will break itself down into individual lines.
	 */
	private void prepare(TextRun run) {
		int width = Math.max(this.minWidth,	getAppearance().getContentWidth());
		run.prepare(width, this.scratchDimension, this.nextDrawPoint);
		this.fullHeight = this.scratchDimension.getHeight();
	}

	/**
	 * Fire text changed event.
	 */
	private void processTextChanged(String text) {
		this.updateMinSize();
		this.fireTextChangedEvent(text);
	}

	/**
	 * Fire a <code>TextChangedEvent</code>.
	 */
	private void fireTextChangedEvent(String text) {
		TextChangedEvent e = new TextChangedEvent(this, text);
		for(ITextChangedListener listener : this.textChangedHook) {
			listener.textChanged(e);
		}
		if(this.getDisplay() != null) {
			this.getDisplay().fireGlobalEventListener(e);
		}
	}

	/**
	 * Build logic.
	 */
	protected void buildLogic() {
		this.addMousePressedListener(new IMousePressedListener() {
			public void mousePressed(MousePressedEvent mp) {
				int x = mp.getDisplayX() - getDisplayX() - getAppearance().getPadding().getLeft();
				int y = mp.getDisplayY() - getDisplayY() - getAppearance().getPadding().getBottom()	- getAppearance().getContentHeight();
				TextRun run = getRun(x, y);
				if (run != null)
				{
					System.out.println("Click on : " + new String(run.getChars()));
				}
			}
		});
	}

	/**
	 * Layout the text view.
	 */
	@Override
	public void layout() {
		int oldHeight = this.fullHeight;
		this.prepareAll();
		if(oldHeight != this.fullHeight) {
			this.getParent().layout();
		} else {
			super.layout();
		}
	}

	/**
	 * Update the minimum size of text view.
	 */
	@Override
	public void updateMinSize() {
		this.setMinSize(getAppearance().getMinSizeHint());
		if(this.getParent() != null && this.getParent() instanceof ScrollContainer)
		{
			((ScrollContainer)this.getParent()).layout();
		}
		else if(this.getParent() != null)
		{
			this.getParent().updateMinSize();
		}
	}

	/**
	 * Add the text view to widget tree.
	 */
	@Override
	public void addedToWidgetTree() {
		super.addedToWidgetTree();
		this.prepareAll();
	}

	/**
	 * Add a <code>ITextChangedListener</code> to the text view. The listener can be added only once.
	 * @param listener The ITextChangedListener object to be added.
	 */
	public void addSelectionChangedListener(ITextChangedListener listener) {
		if(!this.textChangedHook.contains(listener))
		{
			this.textChangedHook.add(listener);
		}
	}

	/**
	 * Remove the <code>ITextChangedListener</code> from the text view.
	 * @param listener The ITextChangedListener object to be added.
	 */
	public void removeSelectionChangedListener(ITextChangedListener listener) {
		this.textChangedHook.remove(listener);
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
		this.setText(stream.processAttribute("text", this.getText(), this.getText()));
		super.process(stream);
	}

	/**
	 * Append text to the text view with default style.
	 * @param text The text string representation.
	 */
	public void appendText(String text) {
		this.appendText(text, this.defaulStyle);
	}

	/**
	 * Append text to the text view.
	 * @param run The text run object to be appended.
	 */
	public void appendText(TextRun run) {
		this.runs.add(run);
		if(this.getParent() != null) {
			this.prepare(run);
		}
		this.processTextChanged(new String(run.getChars()));
	}

	/**
	 * Append text to the text view.
	 * @param text The text string representation.
	 * @param style The text style of the text.
	 */
	public void appendText(String text, TextStyle style) {
		if(text.length() != 0) {
			this.appendText(new TextRun(text, style));
		}
	}

	/**
	 * Append text to the text view on a new line with default style.
	 * @param text The text string representation.
	 */
	public void addTextLine(String text) {
		this.addTextLine(text, this.defaulStyle);
	}

	/**
	 * Append text to the text view on a new line.
	 * @param text The text string representation.
	 * @param style The text style of the text.
	 */
	public void addTextLine(String text, TextStyle style) {
		if(this.runs.isEmpty()) {
			this.appendText(text, style);
		} else {
			this.appendText(new StringBuilder().append('\n').append(text).toString(), style);
		}
	}

	/**
	 * Set the default text style.
	 * @param style The text style to be set.
	 */
	public void setStyle(TextStyle style) {
		this.defaulStyle = style;
	}

	/**
	 * Set the default text style font.
	 * @param font The font to be set.
	 */
	public void setFont(Font font) {
		this.defaulStyle.setFont(font);
	}

	/**
	 * Set the default text style text color.
	 * @param color The color to be set.
	 */
	public void setTextColor(Color color) {
		this.defaulStyle.setColor(color);
	}

	/**
	 * Set the minimum width of the text view.
	 * @param minWidth The minimum width to be set.
	 */
	public void setMinWidth(int minWidth) {
		this.minWidth = minWidth;
	}

	/**
	 * Set the text of the text view.
	 * @param text The text to be set.
	 */
	public void setText(String text) {
		this.runs.clear();
		this.appendText(text);
	}

	/**
	 * Enable or disable the fading effect of text view.
	 * @param enabled True if the fading effect should be enabled. False otherwise.
	 */
	public void setFadingEnabled(boolean enabled) {
		if(enabled) {
			this.fader.startFading();
		} else {
			this.fader.stopFading();
		}
	}

	/**
	 * Retrieve the default style of this text view.
	 * @return The default text style object.
	 */
	public TextStyle getDefaulStyle() {
		return this.defaulStyle;
	}

	/**
	 * Retrieve the minimum width of the text view.
	 * @return The minimum width of the text view.
	 */
	public int getMinWidth() {
		return this.minWidth;
	}

	/**
	 * Retrieve the string representation of all the texts.
	 * @return The string representation of all the texts.
	 */
	public String getText() {
		StringBuilder strBuilder = new StringBuilder();
		for(TextRun run : this.runs) {
			strBuilder.append(run.getChars());
		}
		return strBuilder.toString();
	}

	/**
	 * Get the text at location with given x and y values.
	 * @param x The x coordinate value.
	 * @param y The y coordinate value.
	 * @return The <code>TextRun</code> representation of the text.
	 */
	public TextRun getRun(int x, int y) {
		for(TextRun run : this.runs) {
			if(run.contains(x, y)) return run;
		}
		return null;
	}
	
	/**
	 * Retrieve the text runs.
	 * @return The <code>ConcurrentLinkedQueue</code> of <code>TextRun</code>.
	 */
	public ConcurrentLinkedQueue<TextRun> getRuns() {
		return this.runs;
	}

	/**
	 * Retrieve the text view appearance.
	 */
	@Override
	public TextViewAppearance getAppearance() {
		return this.appearance;
	}
}
