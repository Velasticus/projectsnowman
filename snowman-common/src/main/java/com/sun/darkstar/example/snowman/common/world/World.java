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

import com.jme.scene.Spatial;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.sun.darkstar.example.snowman.common.entity.view.StaticView;
import com.sun.darkstar.example.snowman.common.interfaces.IEditable;
import com.sun.darkstar.example.snowman.common.interfaces.IEditableView;
import com.sun.darkstar.example.snowman.common.interfaces.IStaticView;
import com.sun.darkstar.example.snowman.common.interfaces.IWorld;
import com.sun.darkstar.example.snowman.common.util.enumn.EWorld;

/**
 * <code>World</code> extends jME <code>Node</code> and implements
 * <code>IWorld</code> to define the actual data structure of a world in game.
 * <p>
 * <code>World</code> should only be created based on <code>EditableWorld</code>
 * during the world export stage by the world editor. It should be imported as a
 * <code>Savable</code> resource using <code>DataManager</code> at run time.
 * <p>
 * <code>World</code> can be directly attached to the scene graph after it is
 * initialized. World needs to be initialized to register all the entities,
 * views, and influences it maintains with the corresponding <code>Manager</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-17-2008 15:23 EST
 * @version Modified date: 08-11-2008 15:20 EST
 */
public class World extends AbstractWorld implements IWorld {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 4623946654242838364L;
	/**
	 * The <code>ArrayList</code> of <code>IStaticView</code>.
	 */
	private ArrayList<IStaticView> views;
	
	/**
	 * Constructor of <code>World</code>.
	 */
	public World() {}
	
	/**
	 * Constructor of <code>World</code>.
	 * @param enumn The <code>EWorld</code> enumeration.
	 */
	public World(EWorld enumn) {
		super(enumn);
		this.views = new ArrayList<IStaticView>();
	}

	@Override
	public void process(IEditable editable) {
		if(editable instanceof EditableWorld) {
			EditableWorld given = (EditableWorld)editable;
			for(IEditableView v : given.getViews()) {
				StaticView view = (StaticView)v.constructFinal();
				this.views.add(view);
				if (v instanceof Spatial) {
					Spatial spat = (Spatial)v;
					view.setLocalRotation(spat.getLocalRotation());
					view.setLocalScale(spat.getLocalScale());
					view.setLocalTranslation(spat.getLocalTranslation());
				}
			}

			for(IStaticView view : this.views) {
				view.attachTo(this.staticRoot);
			}
		}
	}

	@Override
	public ArrayList<IStaticView> getViews() {
		return this.views;
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
