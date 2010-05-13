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
package com.sun.darkstar.example.snowman.game.gui.widget;

import java.io.IOException;

import org.fenggui.DecoratorAppearance;
import org.fenggui.Display;
import org.fenggui.IContainer;
import org.fenggui.IWidget;
import org.fenggui.ObservableWidget;
import org.fenggui.ScrollBar;
import org.fenggui.StandardWidget;
import org.fenggui.event.ISliderMovedListener;
import org.fenggui.event.SliderMovedEvent;
import org.fenggui.event.mouse.IMouseWheelListener;
import org.fenggui.event.mouse.MouseWheelEvent;
import org.fenggui.render.Graphics;
import org.fenggui.render.IOpenGL;
import org.fenggui.theme.XMLTheme;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.fenggui.util.Dimension;

/**
 * Container that allows an inner widget to be bigger in size than the container
 * itself. The overlapping parts of the bigger child widget are 
 * clipped. This results in a rectangular window (the view port) in which a part
 * of the child widget is visible. This window can be moved
 * on the child component by calling <code>scrollHorizontal()</code> and
 * <code>scrollVertical()</code> or moving the adjacent scroll bars.<br/>
 * <br/>
 * In case the inner widget is too big to be displayed at once,
 * scroll bars are automatically added to the ScrollContainer and
 * can not be suppressed at the moment. If the internal widget fits
 * into the available space, the scroll bars are not visible. The 
 * scroll bar addition and removal is done during the layouting 
 * process.<br /><br />
 * Therefore, if the inner widget is modified (e.g. it is a table to
 * which rows are added), the scroll container has to be notified by
 * calling <code>layout()</code>.
 * 
 * @author Johannes, last edited by $Author: marcmenghin $, $Date: 2007-08-24 13:59:30 +0200 (Fr, 24 Aug 2007) $
 * @version $Revision: 344 $
 * Monkey Island 3 Theme - Using the Row Boat (http://www.scummbar.com/mi2/MI3-CD1/35%20-%20Using%20the%20Row%20Boat.mp3)
 */

/*
 * This is a duplicate of FengGUI ScrollContainer with one additional method
 * that allows manually set vertical scroll bar display.
 */
public class ScrollContainer extends StandardWidget implements IContainer
{
	/**
	 * The boolean flag indicates if the vertical bar should be placed on the left side
	 * of the inner widget.
	 */
	private boolean verticalBarOnLeft;
	
	/**
	 * The inner Widget. It holds the actual content that has to be
	 * scrolled.
	 */
	private IWidget innerWidget = null;
	
	/**
	 * The appearance definiton of ScrollContainer.
	 */
	private ScrollContainerAppearance appearance = null;
	
	/**
	 * The vertical scroll bar.
	 */
	private ScrollBar verticalScrollBar = null;

	/**
	 * The horizontal scroll bar.
	 */
	private ScrollBar horizontalScrollBar = null;
	
	/**
	 * flag indicating whether the vertical scroll bar is currently displayed.
	 */
	private boolean displayVerticalScrollBar = false;
	
	/**
	 * flag indicating whether the horizontal scroll bar is currently displayed.
	 */
	private boolean displayHorizontalScrollBar = false;
	
	/**
	 * The flag indicates if the vertical scroll bar is enabled.
	 */
	private boolean verticalScrollBarEnabled;
	
	private IMouseWheelListener mouseWheelListener = new IMouseWheelListener()
	{
		public void mouseWheel(MouseWheelEvent mouseWheelEvent)
		{
			if(!displayVerticalScrollBar) return;
			
			for(int i=0; i < mouseWheelEvent.getRotations();i++)
				stepScrollVertical(mouseWheelEvent.wheeledUp());
		}
	};
	
	/**
	 * Instantiates the horizontal scroll bar and registers
	 * its slider listeneres.
	 */
	private void createHorizontalScrollBar()
	{
		if (horizontalScrollBar != null)
			return;

		horizontalScrollBar = new ScrollBar(true);
		horizontalScrollBar.setParent(this);
		if (isInWidgetTree())
			horizontalScrollBar.addedToWidgetTree();
		horizontalScrollBar.getSlider().setValue(0);
		horizontalScrollBar.getSlider().addSliderMovedListener(new ISliderMovedListener()
		{
			public void sliderMoved(SliderMovedEvent sliderMovedEvent)
			{
				placeInnerWidgetHorizontally(sliderMovedEvent.getPosition());
			}
		});
		horizontalScrollBar.getIncreaseButton().setTraversable(false);
		horizontalScrollBar.getDecreaseButton().setTraversable(false);
		horizontalScrollBar.getSlider().getSliderButton().setTraversable(false);
		horizontalScrollBar.setXY(0, 0);
		horizontalScrollBar.setHeight(horizontalScrollBar.getMinHeight());
	
	}

	@Override
	public void process(InputOutputStream stream) throws IOException, IXMLStreamableException
	{
		super.process(stream);
		
		if(getInnerWidget() instanceof StandardWidget || getInnerWidget() == null)
		{
			if(stream.startSubcontext("innerWidget"))
			{
				setInnerWidget((StandardWidget) stream.processChild((StandardWidget)innerWidget, XMLTheme.TYPE_REGISTRY));
				stream.endSubcontext();
			}
		}
	}

	/**
	 * Called by mouse event listeners in scroll bars
	 */
	private void placeInnerWidgetHorizontally(double sliderValue)
	{
		if(this.verticalBarOnLeft)
		{
			int k = 0;
			if(displayVerticalScrollBar) k = verticalScrollBar.getMinWidth();
			innerWidget.setX(-(int) (sliderValue * getHorizontalScrollSpace()) + k);
		}
		else
		{
			innerWidget.setX(-(int) (sliderValue * getHorizontalScrollSpace()));
		}
	}

	/**
	 * Called by mouse event listeners in scroll bars
	 */
	private void placeInnerWidgetVertically(double sliderValue)
	{
		innerWidget.setY(-(int) (sliderValue * getVerticalScrollSpace())
			+ (displayHorizontalScrollBar? horizontalScrollBar.getHeight() : 0));
	}
	
	/**
	 * Instantiates the vertical scroll bar and registers its slider
	 * listeneres.
	 */
	private void createVerticalScrollBar()
	{
		if (verticalScrollBar != null)
			return;

		verticalScrollBar = new ScrollBar(false);
		verticalScrollBar.setParent(this);
		if (isInWidgetTree())
			verticalScrollBar.addedToWidgetTree();
		verticalScrollBar.getSlider().setValue(0);
		verticalScrollBar.getSlider().addSliderMovedListener(new ISliderMovedListener()
		{
			public void sliderMoved(SliderMovedEvent sliderMovedEvent)
			{
				placeInnerWidgetVertically(sliderMovedEvent.getPosition());
			}
		});
		verticalScrollBar.updateMinSize();
		verticalScrollBar.getIncreaseButton().setTraversable(false);
		verticalScrollBar.getDecreaseButton().setTraversable(false);
		verticalScrollBar.getSlider().getSliderButton().setTraversable(false);
	}
	
	public ScrollContainer() {
		this(false);
	}

	/**
	 * Creates a new ScrollContainer object.
	 */
	public ScrollContainer(boolean verticalBarOnLeft)
	{
		this.verticalBarOnLeft = verticalBarOnLeft;
		createVerticalScrollBar();
		createHorizontalScrollBar();
		
		appearance = new ScrollContainerAppearance(this);
		setupTheme(ScrollContainer.class);
		updateMinSize();
	}

	@Override
	public void addedToWidgetTree()
	{
		if (innerWidget != null)
			innerWidget.addedToWidgetTree();
		if (horizontalScrollBar != null)
			horizontalScrollBar.addedToWidgetTree();
		if (verticalScrollBar != null)
			verticalScrollBar.addedToWidgetTree();
	}

	@Override
	public void removedFromWidgetTree()
	{
		if (innerWidget != null)
			innerWidget.removedFromWidgetTree();
		if (horizontalScrollBar != null)
			horizontalScrollBar.removedFromWidgetTree();
		if (verticalScrollBar != null)
			verticalScrollBar.removedFromWidgetTree();
	}

	/**
	 * Returns the Widget below a specified point. This is either the
	 * inner Widget or one of the scroll bars. 
	 */
	@Override
	public IWidget getWidget(int x, int y)
	{
		if (!getAppearance().insideMargin(x, y))
			return null;
		
		x -= getAppearance().getLeftMargins();
		y -= getAppearance().getBottomMargins();

		if (displayHorizontalScrollBar && horizontalScrollBar.getSize().contains(x - horizontalScrollBar.getX(), y
						- horizontalScrollBar.getY()))
			return horizontalScrollBar.getWidget(x - horizontalScrollBar.getX(), y
					- horizontalScrollBar.getY());
		if (displayVerticalScrollBar && verticalScrollBar.getSize().contains(x - verticalScrollBar.getX(), y
						- verticalScrollBar.getY()))
			return verticalScrollBar.getWidget(x - verticalScrollBar.getX(), y
					- verticalScrollBar.getY());

		if (innerWidget != null) {	
			int modX = x;
			int modY = y;
			
			if(displayHorizontalScrollBar)
				modX = (int) ((innerWidget.getSize().getWidth() - getAppearance().getContentWidth()) * getHorizontalScrollQuotient() + x);

			if(displayVerticalScrollBar)
				modY = (int) ((innerWidget.getSize().getHeight() - getAppearance().getContentHeight()) * getVerticalScrollQuotient() + y);
			
			return innerWidget.getWidget(modX, modY);
		}

		return this;
	}

	/**
	 * Returns how far the inner widget is scrolled to the right in percent.
	 * <pre>
	 * |   #######     |
	 * <--><-----><---> 
	 * <- getWidth() ->
	 *   
	 * </pre>
	 * @return computes <code>innerWidget.getX() / getHorizontalScrollSpace()</code>
	 */
	public double getHorizontalScrollQuotient()
	{
		return (double) innerWidget.getX() / getHorizontalScrollSpace();
	}

	/**
	 * Returns how far the inner widget is away from the bottom.
	 * XXX: getHorizontalScrollQuotient() works considerably different! Make both methods do the same on their respective dimension.
	 */
	public double getVerticalScrollQuotient()
	{
		if(displayHorizontalScrollBar) {
			return (double) (horizontalScrollBar.getHeight() - innerWidget.getY())
					/ getVerticalScrollSpace();
		} else {
			return (double) (- innerWidget.getY()) 
					/ getVerticalScrollSpace();
		}
	}

	public void scrollHorizontal(double percent)
	{
		// do not scroll if scrollbar doesn't exist
		if (!displayHorizontalScrollBar)
			return;

		if (percent > 1)
			percent = 1;
		if (percent < 0)
			percent = 0;

		double d = percent * getHorizontalScrollSpace();
		innerWidget.setX(-(int) d);

		horizontalScrollBar.getSlider().setValue(d);
	}

	public void scrollVertical(double percent)
	{
		// do not scroll if scrollbar doesn't exist
		if (!displayVerticalScrollBar)
			return;

		if (percent > 1)
			percent = 1;
		if (percent < 0)
			percent = 0;

		double d = percent * getVerticalScrollSpace();
		innerWidget.setY(-(int) d + horizontalScrollBar.getHeight());

		verticalScrollBar.getSlider().setValue(d);
	}

	public void stepScrollHorizontal(boolean right)
	{
		if (!displayHorizontalScrollBar)
			return;

		double curValue = horizontalScrollBar.getSlider().getValue();
		double horScrollSpace = getHorizontalScrollSpace();

		double step = 1.0 / (horScrollSpace / getWidth());

		if (right)
			curValue += step;
		else
			curValue -= step;

		horizontalScrollBar.getSlider().setValue(curValue);
	}

	public void stepScrollVertical(boolean up)
	{
		if (!displayVerticalScrollBar)
			return;

		double curValue = verticalScrollBar.getSlider().getValue();
		double verScrollSpace = getVerticalScrollSpace();

		double step = 1.0 / (verScrollSpace / getHeight());

		if (up)
			curValue += step;
		else
			curValue -= step;

		verticalScrollBar.getSlider().setValue(curValue);
	}

	/**
	 * Returns the overlap of the view port to the inner widget
	 */
	private double getVerticalScrollSpace()
	{
		int contentHeight = getAppearance().getContentHeight();
		
		if(displayHorizontalScrollBar)
			contentHeight -= horizontalScrollBar.getHeight();
		
		return innerWidget.getSize().getHeight() - contentHeight;
	}

	/**
	 * Returns the overlap of the view port to the inner widget
	 */
	private double getHorizontalScrollSpace()
	{
		int contentWidth = getAppearance().getContentWidth();

		if(displayVerticalScrollBar)
			contentWidth -= verticalScrollBar.getWidth();
		
		return innerWidget.getSize().getWidth() - contentWidth;
	}

	/**
	 * Returns the horizontal scroll bar. 
	 * @return the horizontal scroll bar.
	 */
	public ScrollBar getHorizontalScrollBar()
	{
		return horizontalScrollBar;
	}

	/**
	 * Returns the inner widget assigned to the ScrollContainer.
	 * @return inner Widget
	 */
	public IWidget getInnerWidget()
	{
		return innerWidget;
	}

	/**
	 * Returns the vertical scroll bar. 
	 * @return vertical scroll bar.
	 */
	public ScrollBar getVerticalScrollBar()
	{
		return verticalScrollBar;
	}

	/**
	 * Sets the inner widget (requires a <code>layout()</code> call). 
	 * @param innerWidget the inner Widget
	 */
	public void setInnerWidget(IWidget innerWidget)
	{
		if(this.innerWidget != null)
		{
			this.innerWidget.removedFromWidgetTree();
			this.innerWidget.setParent(null);
			if(getDisplay() != null) {
				Display display = getDisplay();
				if(display.getFocusedWidget() != null && display.getFocusedWidget().getDisplay() == null) {
					display.setFocusedWidget(null);
				}
			}
			if(this.innerWidget instanceof ObservableWidget)
				((ObservableWidget)this.innerWidget).removeMouseWheelListener(mouseWheelListener);
		}
		
		this.innerWidget = innerWidget;
		innerWidget.setParent(this);
		if (isInWidgetTree())
			this.innerWidget.addedToWidgetTree();
		if(innerWidget instanceof ObservableWidget)
			((ObservableWidget)innerWidget).addMouseWheelListener(mouseWheelListener);
		
	}

	@Override
	public void updateMinSize()
	{
		if (innerWidget == null)
			return;
		
		setMinSize(50, 50);
		
		if(getParent() != null) getParent().updateMinSize();
	}

	public ScrollContainerAppearance getAppearance()
	{
		return appearance;
	}

	/**
	 * Layouts the ScrollContainer. That is the inner widget and both scroll bars
	 * (if required).
	 */
	public void layout()
	{
		if(innerWidget == null) return;
		
		/* The basic strategy goes like this: first we check whether we need the scroll bars
		 * to make the view port movable on the inner widget (step 1). Then we know whether we
		 * the vertical and the horizontal scroll bar. Adding a scroll bar reduces the size of the
		 * view port. We thus need to check whether we need to enable a scroll bar that has been
		 * unnecessary before concidering the activation of the other scroll bar (step 2). It is
		 * now save to setup the inner widget (step 3). Finally, we setup the scroll bars in step 4.
		 */
		
		final int contentHeight = getAppearance().getContentHeight();
		final int contentWidth  = getAppearance().getContentWidth();
		
		/* the view port is the space between the scroll bars (if present) in which
		 * the inner widget is displayed. By adding scroll bars, the size of the view port
		 * is reduced (obviously).
		 */
		int viewPortWidth  = contentWidth;
		int viewPortHeight = contentHeight;
		
		/* convenience vars to lower the effort to access the size of an IWidget */
		final int innerWidgetMinWidth  = getInnerWidget().getMinSize().getWidth();
		final int innerWidgetMinHeight = getInnerWidget().getMinSize().getHeight();
		int innerWidgetWidth     = getInnerWidget().getSize().getWidth();
		int innerWidgetHeight    = getInnerWidget().getSize().getHeight();
		
		/* STEP 1:
		 * We need to check whether we need to add or remove scroll bars in order 
		 * to enable the user to move the inner widget in the ScrollContainer.
		 */
		
		/* if inner widget is slimmer or equal than ScrollContainer */
		if (innerWidgetMinWidth <= contentWidth)
			displayHorizontalScrollBar = false;
		else /* no, inner widget is wider! */
		{	
			displayHorizontalScrollBar = true;
			viewPortHeight -= horizontalScrollBar.getMinHeight();
		}

		if (innerWidgetMinHeight <= contentHeight)
			displayVerticalScrollBar = false;
		else
		{
			if(this.verticalScrollBarEnabled) displayVerticalScrollBar = true;
			else this.displayVerticalScrollBar = false;
			viewPortWidth -= verticalScrollBar.getMinWidth();
		}

		/* STEP 2:
		 * by adding the vertical scroll bar, the size of the view port is reduced,
		 * so that it may by that we also need to horiztonal scroll bar after all. */
		if(displayVerticalScrollBar && !displayHorizontalScrollBar && viewPortWidth < innerWidgetMinWidth)
		{
			displayHorizontalScrollBar = true;
			viewPortHeight -= horizontalScrollBar.getMinHeight();
		}
		/* same for the horizontal scroll bar */
		if(!displayVerticalScrollBar && displayHorizontalScrollBar && viewPortHeight < innerWidgetMinHeight)
		{
			displayVerticalScrollBar = true;
			viewPortWidth -= verticalScrollBar.getMinWidth();
		}
		
		/* STEP 3:
		 * ok, now we know whether we need scroll bars to completly display the inner widget.
		 * We set the size of the inner widget such that it receives the min with in the dimension
		 * where it is bigger than the view port. In case a dimension is smaller then the available
		 * space in the view port we assume the view port size.
		 */ 
		if(innerWidgetMinWidth > viewPortWidth) 
			innerWidgetWidth = innerWidgetMinWidth;
		else
			innerWidgetWidth = viewPortWidth;
			
		if(innerWidgetMinHeight > viewPortHeight)
			innerWidgetHeight = innerWidgetMinHeight;
		else
			innerWidgetHeight = viewPortHeight;
			
		innerWidget.setSize(new Dimension(innerWidgetWidth, innerWidgetHeight));
		innerWidget.layout();
		placeInnerWidgetHorizontally(horizontalScrollBar.getSlider().getValue());
		placeInnerWidgetVertically(verticalScrollBar.getSlider().getValue());
		
		/* STEP 4:
		 * adjust slider height according to the new size of the inner widget...
		 * if inner Widget wider than ScrollContainer */
		if (displayHorizontalScrollBar)
		{
			horizontalScrollBar.setSize(viewPortWidth, horizontalScrollBar.getMinHeight());
			horizontalScrollBar.setXY(0, 0);
			horizontalScrollBar.layout();
			
			/* adjust Slider width */
			double d = (double) viewPortWidth / (double) innerWidgetWidth;
			horizontalScrollBar.getSlider().setSize(d);
			d = 10.0 / (double) (innerWidgetMinWidth - viewPortWidth);
			horizontalScrollBar.setButtonJump(d);
		}

		// if inner Widget taller than ScollContainer
		if (displayVerticalScrollBar)
		{
			verticalScrollBar.setSize(verticalScrollBar.getMinWidth(), viewPortHeight);
			if(this.verticalBarOnLeft)
			{
				verticalScrollBar.setXY(0, contentHeight - viewPortHeight);
			}
			else
			{
				verticalScrollBar.setXY(viewPortWidth, contentHeight - viewPortHeight);
			}
			verticalScrollBar.layout();
			
			/* adjust slider button height */
			double d = (double) (viewPortHeight) / (double) innerWidgetHeight;
			verticalScrollBar.getSlider().setSize(d);

			// TODO why is here no button jump set?
		}
	}
	
	/**
	 * Sets the inner widget.
	 */
	public void addWidget(IWidget w)
	{
		setInnerWidget(w);
	}
	
	public void addWidget(IWidget w, int position)
	{
		setInnerWidget(w);
	}

	public IWidget getNextTraversableWidget(IWidget start) 
	{
		return getParent().getNextTraversableWidget(this);
	}

	public IWidget getPreviousTraversableWidget(IWidget start) 
	{
		return getParent().getPreviousTraversableWidget(this);
	}	
	
	public IWidget getNextWidget(IWidget start)
	{
		// XXX implement me!
		return null;
	}

	public IWidget getPreviousWidget(IWidget start)
	{
		return getParent().getPreviousWidget(this);
	}
	
	/**
	 * @param appearance the appearance to set
	 */
	public void setAppearance(ScrollContainerAppearance appearance)
	{
		this.appearance = appearance;
	}
	
	/**
	 * @return True if the vertical bar of this scroll container is placed on the left side
	 * of the inner widget. False otherwise.
	 */
	public boolean isVerticalBarOnLeft() {
		return this.verticalBarOnLeft;
	}
	
	public void setVerticalScrollBarEnabled(boolean enbaled) {
		this.verticalScrollBarEnabled = enbaled;
	}
	
	public class ScrollContainerAppearance extends DecoratorAppearance
	{
		protected ScrollContainer scrollContainer;

		public ScrollContainerAppearance(ScrollContainer scrollContainer)
		{
			super(scrollContainer);
			this.scrollContainer = scrollContainer;
		}

		public Dimension getContentMinSizeHint()
		{
			return null;
		}

		public void paintContent(Graphics g, IOpenGL gl)
		{
			IWidget innerWidget = scrollContainer.getInnerWidget();
			if (innerWidget == null) return;

			ScrollBar horizontalScrollBar = scrollContainer.getHorizontalScrollBar();
			if (displayHorizontalScrollBar)
			{
				g.translate(horizontalScrollBar.getX(), horizontalScrollBar.getY());
				horizontalScrollBar.paint(g);
				g.translate(-horizontalScrollBar.getX(), -horizontalScrollBar.getY());
			}

			ScrollBar verticalScrollBar = scrollContainer.getVerticalScrollBar();
			if (displayVerticalScrollBar)
			{
				g.translate(verticalScrollBar.getX(), verticalScrollBar.getY());
				verticalScrollBar.paint(g);
				g.translate(-verticalScrollBar.getX(), -verticalScrollBar.getY());
			}

			int verticalSBMinWidth = !displayVerticalScrollBar ? 0 : verticalScrollBar.getMinWidth();
			int horizontalSBMinHeight = !displayHorizontalScrollBar ? 0 : horizontalScrollBar.getMinHeight();

			if(this.scrollContainer.isVerticalBarOnLeft())
			{
				g.setClipSpace(verticalSBMinWidth, horizontalSBMinHeight, getContentWidth() - verticalSBMinWidth, getContentHeight()
						- horizontalSBMinHeight);
			}
			else
			{
				g.setClipSpace(0, horizontalSBMinHeight, getContentWidth() - verticalSBMinWidth, getContentHeight()
						- horizontalSBMinHeight);
			}
			g.translate(innerWidget.getX(), innerWidget.getY());
			innerWidget.paint(g);
			g.translate(-innerWidget.getX(), -innerWidget.getY());

		}
	}

}	
