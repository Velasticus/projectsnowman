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

import java.util.LinkedList;

import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jme.util.stat.graph.AbstractStatGrapher;
import com.jme.util.stat.graph.DefColorFadeController;
import com.sun.darkstar.example.snowman.game.stats.StatsManager.PageType;

/**
 * Object representing a single display of logically grouped graphs and
 * presentation surfaces.
 * 
 * @author Joshua Slack
 */
public class StatPage {

	private final LinkedList<StatQuad> graphSurfaces = new LinkedList<StatQuad>();
	private final LinkedList<AbstractStatGrapher> graphers = new LinkedList<AbstractStatGrapher>();
	private final Node pageRootNode;

	private final PageType page;

	/**
	 * Construct a new StatPage of the given page type.
	 * 
	 * @param pageType
	 *            the type of page this will represent.
	 */
	public StatPage(PageType pageType) {
		this.page = pageType;
		pageRootNode = new Node("root node for page: " + pageType);
	}

	/**
	 * Turn on or off the graphers in this StatPage and add a
	 * DefColorFadeController to our StatQuad surfaces to fade them in or out
	 * 
	 * @param enabled
	 *            <i>true</i> to fade in and enable the contents of this stat
	 *            page, <i>false</i> to fade them out and disable them.
	 */
	public void setEnabled(boolean enabled) {
		// set our graphers to on/off
		for (AbstractStatGrapher asg : graphers) {
			asg.setEnabled(enabled);
		}

		for (StatQuad surface : graphSurfaces) {
			// Set up for fade, first clear any existing controller to prevent
			// conflict
			surface.clearControllers();
			// Now add our new fade controller
			surface.addController(new DefColorFadeController(surface,
					enabled ? .6f : 0f, enabled ? .5f : -.5f));
		}
	}

	/**
	 * Setup and add a new graphing surface to this StatPage. Sets the CullHint
	 * to Always and the alpha to 0 in preparation for fadein.
	 * 
	 * @param surface
	 *            the quad surface to add
	 */
	public void addGraphSurfaces(StatQuad surface) {
		surface.setCullHint(CullHint.Always);
		surface.getDefaultColor().a = 0;
		graphSurfaces.add(surface);
		pageRootNode.attachChild(surface);
	}

	/**
	 * Setup and add a new grapher to this StatPage. Sets the grapher to
	 * disabled until we later enable this StatPage.
	 * 
	 * @param grapher
	 *            the grapher to add
	 */
	public void addGrapher(AbstractStatGrapher grapher) {
		grapher.setEnabled(false);
		graphers.add(grapher);
	}

	/**
	 * Ask the graphers in this StatPage to reset themselves.
	 * 
	 * @see AbstractStatGrapher#reset()
	 */
	public void resetGraphers() {
		for (AbstractStatGrapher grapher : graphers) {
			grapher.reset();
		}
	}

	/**
	 * @return the logical root for all surfaces in this StatPage
	 */
	public Node getPageRoot() {
		return pageRootNode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StatPage && ((StatPage) obj).page == page) {
			return true;
		}
		return false;
	}
}
