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
import java.util.ArrayList;

import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.sun.darkstar.example.snowman.common.interfaces.IEditableView;
import com.sun.darkstar.example.snowman.common.interfaces.IEditableWorld;
import com.sun.darkstar.example.snowman.common.interfaces.IFinal;
import com.sun.darkstar.example.snowman.common.util.enumn.EWorld;

/**
 * <code>EditableWorld</code> extends jME <code>Node</code> and
 * implements <code>IEditableWorld</code> define the data structure of world
 * during the world editing stages.
 * <p>
 * <code>EditableWorld</code> should only be used during the world editing
 * stages by the world editor. It can be exported intermediately to a binary
 * file during the editing stages and imported back for later on editing.
 * <p>
 * <code>EditableWorld</code> maintains a list of <code>IEditableView</code>
 * objects that are created through the world editor. These objects are
 * eventually converted to corresponding <code>IFinal</code> instances during
 * the world export process.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-01-2008 14:50 EST
 * @version Modified date: 08-11-2008 16:30 EST
 */
public class EditableWorld extends AbstractWorld implements IEditableWorld {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 7100272026657161759L;
	/**
	 * The <code>ArrayList</code> of <code>IEditableView</code>.
	 */
	private ArrayList<IEditableView> views;
	
	/**
	 * Constructor of <code>EditableWorld</code>.
	 */
	public EditableWorld() {}
	
	/**
	 * Constructor of <code>EditableWorld</code>.
	 * @param enumn The <code>EWorld</code> enumeration.
	 */
	public EditableWorld(EWorld enumn) {
		super(enumn);
		this.views = new ArrayList<IEditableView>();
	}

	@Override
	public IFinal constructFinal() {
		World world = new World(this.enumn);
		world.process(this);
		return world;
	}

	@Override
	public void attachView(IEditableView view) {
		if(this.views.contains(view) || view == null) return;
		this.views.add(view);

		view.attachTo(this.staticRoot);
	}

	@Override
	public void detachView(IEditableView view) {
		if(view == null) return;
		this.views.remove(view);
		view.detachFromParent();
	}

	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<IEditableView> getViews() {
		return (ArrayList<IEditableView>)this.views.clone();
	}

	@Override
	public void write(JMEExporter ex) throws IOException {
		super.write(ex);
		OutputCapsule oc = ex.getCapsule(this);
		oc.writeSavableArrayList(this.views, "Views", null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void read(JMEImporter im) throws IOException {
		super.read(im);
		InputCapsule ic = im.getCapsule(this);
		this.views = ic.readSavableArrayList("Views", null);
	}
}
