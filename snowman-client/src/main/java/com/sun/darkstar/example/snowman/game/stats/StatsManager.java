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

package com.sun.darkstar.example.snowman.game.stats;

import java.util.ArrayList;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jme.util.Debug;
import com.jme.util.stat.StatCollector;
import com.jme.util.stat.StatType;
import com.jme.util.stat.graph.GraphFactory;
import com.jme.util.stat.graph.LineGrapher;
import com.jme.util.stat.graph.TabledLabelGrapher;

/**
 * <p>
 * Manager class used to create, track and update stats graphs for Project
 * Snowman. We view stats in groups called pages, 1 page at a time.
 * </p>
 * <p>
 * Note that this class will not do anything useful if the system property
 * "jme.stats" has not been set.
 * </p>
 * 
 * @author Joshua Slack
 */
public class StatsManager {

	/**
	 * A single Node used to keep graph updates and render calls tied to a
	 * single root.
	 */
	private final Node rootNode;

	/**
	 * An array of StatPage objects, each managing stat spatial and grapher
	 * objects for a particular display of stats.
	 */
	private ArrayList<StatPage> pages = new ArrayList<StatPage>();

	/**
	 * Our single instance of the StatsManager
	 */
	private final static StatsManager instance = new StatsManager();

	/**
	 * The current stat page we are tracking.
	 */
	private PageType currentPageType = PageType.None;

	/**
	 * The possible state page types
	 * 
	 */
	public enum PageType {
		None, RendererStats, ClientStats, ServerStats;
	}

	/**
	 * Our constructor. Sets up the rootNode to never be culled and be in the
	 * ortho queue.
	 */
	private StatsManager() {
		rootNode = new Node("stats root node");
		rootNode.setCullHint(Spatial.CullHint.Never);
		rootNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
	}

	/**
	 * 
	 * @return Our singleton instance of the StatsManager
	 */
	public static StatsManager getInstance() {
		return instance;
	}

	/**
	 * (re)create our stats window. Generally this would be done at the
	 * beginning of the app, but could also be needed if you change the
	 * resolution, etc. of the window during program execution.
	 */
	public void recreateStatsDisplay() {
		if (Debug.stats) {
			// Set our rate and max samples
			StatCollector.setSampleRate(1000);
			StatCollector.setMaxSamples(40);

			// remove any children from root node
			rootNode.detachAllChildren();

			// build each graph system and attach
			attachNoneStatsPage();
			attachRendererStatsPage();
			attachClientStatsPage();
			attachServerStatsPage();

			// reset using our current page type
			setCurrentPageType(currentPageType);

			// update RS and GS
			rootNode.updateRenderState();
			rootNode.updateGeometricState(0, true);
		}
	}

	/**
	 * Setup a blank page that does not show any stats.
	 */
	private void attachNoneStatsPage() {
		StatPage page = new StatPage(PageType.None);
		addPage(page);
	}

	/**
	 * Set up a page that shows some general renderer stats such as triangle
	 * count, texture bind, etc.
	 */
	private void attachRendererStatsPage() {
		StatPage page = new StatPage(PageType.RendererStats);
		addPage(page);

		int displayWidth = DisplaySystem.getDisplaySystem().getWidth();
		int displayHeight = DisplaySystem.getDisplaySystem().getHeight();

		final StatQuad lineGraphQuad = new StatQuad("lineGraph",
				displayWidth * .5f, displayHeight * .4f);
		lineGraphQuad.setLocalTranslation(displayWidth * .5f,
				displayHeight * .8f, 0);
		page.addGraphSurfaces(lineGraphQuad);

		final LineGrapher lineGrapher = GraphFactory.makeLineGraph(Math
				.round(lineGraphQuad.getWidth()), Math.round(lineGraphQuad
				.getHeight()), lineGraphQuad);
		page.addGrapher(lineGrapher);

		// Setup the stats we will track with the line graph
		lineGrapher.addConfig(StatType.STAT_FRAMES,
				LineGrapher.ConfigKeys.Color.name(), ColorRGBA.green);
		lineGrapher.addConfig(StatType.STAT_FRAMES,
				LineGrapher.ConfigKeys.Stipple.name(), 0XFF0F);
		lineGrapher.addConfig(StatType.STAT_TRIANGLE_COUNT,
				LineGrapher.ConfigKeys.Color.name(), ColorRGBA.cyan);
		lineGrapher.addConfig(StatType.STAT_TRIANGLE_COUNT,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		lineGrapher.addConfig(StatType.STAT_QUAD_COUNT,
				LineGrapher.ConfigKeys.Color.name(), ColorRGBA.lightGray);
		lineGrapher.addConfig(StatType.STAT_QUAD_COUNT,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		lineGrapher.addConfig(StatType.STAT_LINE_COUNT,
				LineGrapher.ConfigKeys.Color.name(), ColorRGBA.red);
		lineGrapher.addConfig(StatType.STAT_LINE_COUNT,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		lineGrapher.addConfig(StatType.STAT_GEOM_COUNT,
				LineGrapher.ConfigKeys.Color.name(), ColorRGBA.gray);
		lineGrapher.addConfig(StatType.STAT_GEOM_COUNT,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		lineGrapher.addConfig(StatType.STAT_TEXTURE_BINDS,
				LineGrapher.ConfigKeys.Color.name(), ColorRGBA.orange);
		lineGrapher.addConfig(StatType.STAT_TEXTURE_BINDS,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);

		// Make a numeric version of the graph now for "last sample" display.
		StatQuad labGraphQuad = new StatQuad("labelGraph", lineGraphQuad
				.getWidth(), displayHeight * .4f);
		labGraphQuad.setLocalTranslation(displayWidth * .5f,
				displayHeight * .4f, 0);
		page.addGraphSurfaces(labGraphQuad);

		TabledLabelGrapher tableGrapher = GraphFactory.makeTabledLabelGraph(
				Math.round(labGraphQuad.getWidth()), Math.round(labGraphQuad
						.getHeight()), labGraphQuad);
		tableGrapher.setColumns(2);
		tableGrapher.setMinimalBackground(true);
		tableGrapher.linkTo(lineGrapher);
		page.addGrapher(tableGrapher);

		// Setup the stats we will track with the table
		tableGrapher.addConfig(StatType.STAT_FRAMES,
				TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
		tableGrapher.addConfig(StatType.STAT_FRAMES,
				TabledLabelGrapher.ConfigKeys.Name.name(), "Frames/s:");
		tableGrapher.addConfig(StatType.STAT_TRIANGLE_COUNT,
				TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
		tableGrapher.addConfig(StatType.STAT_TRIANGLE_COUNT,
				TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Tris:");
		tableGrapher.addConfig(StatType.STAT_TRIANGLE_COUNT,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		tableGrapher.addConfig(StatType.STAT_QUAD_COUNT,
				TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
		tableGrapher.addConfig(StatType.STAT_QUAD_COUNT,
				TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Quads:");
		tableGrapher.addConfig(StatType.STAT_QUAD_COUNT,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		tableGrapher.addConfig(StatType.STAT_LINE_COUNT,
				TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
		tableGrapher.addConfig(StatType.STAT_LINE_COUNT,
				TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Lines:");
		tableGrapher.addConfig(StatType.STAT_LINE_COUNT,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		tableGrapher.addConfig(StatType.STAT_GEOM_COUNT,
				TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
		tableGrapher.addConfig(StatType.STAT_GEOM_COUNT,
				TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Objs:");
		tableGrapher.addConfig(StatType.STAT_GEOM_COUNT,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
		tableGrapher.addConfig(StatType.STAT_TEXTURE_BINDS,
				TabledLabelGrapher.ConfigKeys.Decimals.name(), 0);
		tableGrapher.addConfig(StatType.STAT_TEXTURE_BINDS,
				TabledLabelGrapher.ConfigKeys.Name.name(), "Avg.Tex binds:");
		tableGrapher.addConfig(StatType.STAT_TEXTURE_BINDS,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), true);
	}

	/**
	 * Set up a page that shows some general client stats such as number of
	 * snowballs thrown per second, number of movement commands per second, etc.
	 */
	private void attachClientStatsPage() {
		StatPage page = new StatPage(PageType.ClientStats);
		addPage(page);

		int displayWidth = DisplaySystem.getDisplaySystem().getWidth();
		int displayHeight = DisplaySystem.getDisplaySystem().getHeight();

		final StatQuad lineGraphQuad = new StatQuad("lineGraph",
				displayWidth * .5f, displayHeight * .4f);
		lineGraphQuad.setLocalTranslation(displayWidth * .5f,
				displayHeight * .8f, 0);
		page.addGraphSurfaces(lineGraphQuad);

		final LineGrapher lineGrapher = GraphFactory.makeLineGraph(Math
				.round(lineGraphQuad.getWidth()), Math.round(lineGraphQuad
				.getHeight()), lineGraphQuad);
		page.addGrapher(lineGrapher);

		// Make a numeric version of the graph now for "last sample" display.
		StatQuad labGraphQuad = new StatQuad("labelGraph", lineGraphQuad
				.getWidth(), displayHeight * .4f);
		labGraphQuad.setLocalTranslation(displayWidth * .5f,
				displayHeight * .4f, 0);
		page.addGraphSurfaces(labGraphQuad);

		// Setup the stats we will track with the line graph
		lineGrapher.addConfig(SnowmanStatType.STAT_SNOWBALL_COUNT,
				LineGrapher.ConfigKeys.Color.name(), ColorRGBA.green);
		lineGrapher.addConfig(SnowmanStatType.STAT_SNOWBALL_COUNT,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), false);
		lineGrapher.addConfig(SnowmanStatType.STAT_ENTITYMOVE_COUNT,
				LineGrapher.ConfigKeys.Color.name(), ColorRGBA.red);
		lineGrapher.addConfig(SnowmanStatType.STAT_ENTITYMOVE_COUNT,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), false);
		lineGrapher.addConfig(SnowmanStatType.STAT_LOCALMOVE_COUNT,
				LineGrapher.ConfigKeys.Color.name(), ColorRGBA.cyan);
		lineGrapher.addConfig(SnowmanStatType.STAT_LOCALMOVE_COUNT,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), false);

		TabledLabelGrapher tableGrapher = GraphFactory.makeTabledLabelGraph(
				Math.round(labGraphQuad.getWidth()), Math.round(labGraphQuad
						.getHeight()), labGraphQuad);
		tableGrapher.setColumns(2);
		tableGrapher.setMinimalBackground(true);
		tableGrapher.linkTo(lineGrapher);
		page.addGrapher(tableGrapher);

		// Setup the stats we will track with the table
		tableGrapher.addConfig(SnowmanStatType.STAT_SNOWBALL_COUNT,
				TabledLabelGrapher.ConfigKeys.Decimals.name(), 1);
		tableGrapher.addConfig(SnowmanStatType.STAT_SNOWBALL_COUNT,
				TabledLabelGrapher.ConfigKeys.Name.name(), "SnowBalls/s:");
		tableGrapher.addConfig(SnowmanStatType.STAT_SNOWBALL_COUNT,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), false);
		tableGrapher.addConfig(SnowmanStatType.STAT_ENTITYMOVE_COUNT,
				TabledLabelGrapher.ConfigKeys.Decimals.name(), 1);
		tableGrapher.addConfig(SnowmanStatType.STAT_ENTITYMOVE_COUNT,
				TabledLabelGrapher.ConfigKeys.Name.name(), "EntMove/s:");
		tableGrapher.addConfig(SnowmanStatType.STAT_ENTITYMOVE_COUNT,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), false);
		tableGrapher.addConfig(SnowmanStatType.STAT_LOCALMOVE_COUNT,
				TabledLabelGrapher.ConfigKeys.Decimals.name(), 1);
		tableGrapher.addConfig(SnowmanStatType.STAT_LOCALMOVE_COUNT,
				TabledLabelGrapher.ConfigKeys.Name.name(), "LocalMove/s:");
		tableGrapher.addConfig(SnowmanStatType.STAT_LOCALMOVE_COUNT,
				TabledLabelGrapher.ConfigKeys.FrameAverage.name(), false);
	}

	/**
	 * Set up a page that shows server stats, as reported by the darkstar
	 * server.
	 * TODO: Once we get the source figured out, set this up to report the stats available.
	 */
	private void attachServerStatsPage() {
		StatPage page = new StatPage(PageType.ServerStats);
		addPage(page);

		int displayWidth = DisplaySystem.getDisplaySystem().getWidth();
		int displayHeight = DisplaySystem.getDisplaySystem().getHeight();

		final StatQuad lineGraphQuad = new StatQuad("lineGraph",
				displayWidth * .5f, displayHeight * .4f);
		lineGraphQuad.setLocalTranslation(displayWidth * .5f,
				displayHeight * .8f, 0);
		page.addGraphSurfaces(lineGraphQuad);

		final LineGrapher lineGrapher = GraphFactory.makeLineGraph(Math
				.round(lineGraphQuad.getWidth()), Math.round(lineGraphQuad
				.getHeight()), lineGraphQuad);
		page.addGrapher(lineGrapher);

		// Make a numeric version of the graph now for "last sample" display.
		StatQuad labGraphQuad = new StatQuad("labelGraph", lineGraphQuad
				.getWidth(), displayHeight * .4f);
		labGraphQuad.setLocalTranslation(displayWidth * .5f,
				displayHeight * .4f, 0);
		page.addGraphSurfaces(labGraphQuad);

		TabledLabelGrapher tableGrapher = GraphFactory.makeTabledLabelGraph(
				Math.round(labGraphQuad.getWidth()), Math.round(labGraphQuad
						.getHeight()), labGraphQuad);
		tableGrapher.setColumns(2);
		tableGrapher.setMinimalBackground(true);
		tableGrapher.linkTo(lineGrapher);
		page.addGrapher(tableGrapher);
	}

	/**
	 * Simple method for adding a page to the manager to keep all pages adding
	 * in the same way.
	 * 
	 * @param page
	 *            the page to add
	 */
	private void addPage(StatPage page) {
		pages.add(page);
		rootNode.attachChild(page.getPageRoot());
	}

	/**
	 * Switch our view to the page of the given type.
	 * 
	 * @param type
	 *            the type of page to switch to. If such a page does not exist
	 *            or if we are already on that page, do nothing.
	 */
	public void setCurrentPageType(PageType type) {
		if (Debug.stats) {
			// update graphs only if page type is not None.
			Debug.updateGraphs = type != PageType.None;

			// flip page
			int found = pages.indexOf(new StatPage(type));
			// only do anything if we have a page object of this type.
			if (found >= 0) {
				pages.get(found).setEnabled(true);

				// disable old (if different then new) since we found new one.
				if (type != this.currentPageType) {
					found = pages.indexOf(new StatPage(this.currentPageType));
					if (found >= 0) {
						pages.get(found).setEnabled(false);
					}
				}

				this.currentPageType = type;
			}
		}
	}

	/**
	 * 
	 * @return the current type of page we are making available to display via
	 *         renderStats()
	 */
	public PageType getCurrentPageType() {
		return currentPageType;
	}

	/**
	 * Clears historical data and resets the geometry information of all stat
	 * pages. This is useful when you are switching modes and want the highest
	 * and lowest values for a graph to be reset.
	 */
	public void resetStats() {
		synchronized (StatCollector.getHistorical()) {
			StatCollector.getHistorical().clear();

			// go through all pages and reset
			for (StatPage page : pages) {
				page.resetGraphers();
			}
		}
	}

	/**
	 * Draw the current stats page. Must be called in GL thread.
	 */
	public void renderStats() {
		if (Debug.stats) {
			DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);
		}
	}

	/**
	 * Updates the attached stats trackers and controllers.
	 * 
	 * @param tpf
	 *            time since last update (in seconds)
	 */
	public void updateStats(float tpf) {
		if (Debug.stats) {
			StatCollector.update();
			rootNode.updateGeometricState(tpf, true);
		}
	}

	/**
	 * Show the next page type.
	 */
	public void flipForward() {
		PageType nextType;
		switch (currentPageType) {
		default:
		case ServerStats:
			nextType = PageType.None;
			break;
		case None:
			nextType = PageType.RendererStats;
			break;
		case RendererStats:
			nextType = PageType.ClientStats;
			break;
		case ClientStats:
			nextType = PageType.ServerStats;
			break;
		}
		setCurrentPageType(nextType);
	}

	/**
	 * Show previous page type.
	 */
	public void flipBack() {
		PageType nextType;
		switch (currentPageType) {
		default:
		case RendererStats:
			nextType = PageType.None;
			break;
		case None:
			nextType = PageType.ServerStats;
			break;
		case ServerStats:
			nextType = PageType.ClientStats;
			break;
		case ClientStats:
			nextType = PageType.RendererStats;
			break;
		}
		setCurrentPageType(nextType);
	}
}
