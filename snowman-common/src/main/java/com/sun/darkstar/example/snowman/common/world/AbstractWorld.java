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
package com.sun.darkstar.example.snowman.common.world;

import java.io.IOException;

import com.jme.scene.Node;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.sun.darkstar.example.snowman.common.interfaces.IAbstractWorld;
import com.sun.darkstar.example.snowman.common.util.enumn.EWorld;

/**
 * <code>AbstractWorld</code> implements <code>IAbstractWorld</code> to define
 * the most basic abstraction of a world node.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 08-11-2008 15:12 EST
 * @version Modified date: 08-11-2008 16:27 EST
 */
public abstract class AbstractWorld extends Node implements IAbstractWorld {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The <code>EWorld</code> enumeration.
	 */
	protected EWorld enumn;
	/**
	 * The root node of static entities.
	 */
	protected Node staticRoot;
	/**
	 * The root node of dynamic entities.
	 */
	protected Node dynamicRoot;
	
	/**
	 * Constructor of <code>EditableWorld</code>.
	 */
	protected AbstractWorld() {}
	
	/**
	 * Constructor of <code>AbstractWorld</code>.
	 * @param enumn The <code>EWorld</code> enumeration.
	 */
	protected AbstractWorld(EWorld enumn) {
		super(enumn.toString());
		this.enumn = enumn;
		this.staticRoot = new Node("StaticRoot");
		this.dynamicRoot = new Node("DynamicRoot");
		this.attachChild(this.staticRoot);
		this.attachChild(this.dynamicRoot);
	}

	@Override
	public Node getStaticRoot() {
		return this.staticRoot;
	}

	@Override
	public Node getDynamicRoot() {
		return this.dynamicRoot;
	}

	@Override
	public EWorld getWorldEnumn() {
		return this.enumn;
	}
	
	@Override
	public void write(JMEExporter ex) throws IOException {
		super.write(ex);
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(this.enumn.toString(), "Enumeration", null);
		oc.write(this.staticRoot, "StaticRoot", null);
		oc.write(this.dynamicRoot, "DynamicRoot", null);
	}

	@Override
	public void read(JMEImporter im) throws IOException {
		super.read(im);
		InputCapsule ic = im.getCapsule(this);
		this.enumn = EWorld.valueOf(ic.readString("Enumeration", null));
		this.setName("World"+this.enumn.toString());
		this.staticRoot = (Node) ic.readSavable("StaticRoot", null);
		this.dynamicRoot = (Node) ic.readSavable("DynamicRoot", null);
	}
}
