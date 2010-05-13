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
package com.sun.darkstar.example.snowman.common.entity.view;

import java.io.IOException;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.sun.darkstar.example.snowman.common.interfaces.IEntity;
import com.sun.darkstar.example.snowman.common.interfaces.IView;

/**
 * <code>View</code> defines the representation of an <code>IEntity</code>.
 * It is a subclass of <code>Node</code> which makes it possible to directly
 * attach <code>View</code> to the scene graph for rendering.
 * <p>
 * <code>View</code> only defines an abstraction for its subclasses that
 * represent specific types of views.
 * <p>
 * <code>View</code> overrides the binary import and export methods of its
 * parent class to allow import and export of its own variable fields.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-05-2008 14:44 EST
 * @version Modified date: 07-07-2008 13:31 EST
 */
public abstract class View extends Node implements IView {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 3285825999307706598L;
	/**
	 * The <code>IEntity</code> this <code>View</code> represents.
	 */
	protected IEntity entity;
	
	/**
	 * Constructor of <code>View</code>.
	 */
	public View() {
		super();
	}
	
	/**
	 * Constructor of <code>View</code>.
	 * @param entity The <code>IEntity</code> this view represents.
	 */
	public View(IEntity entity) {
		super(entity.getEnumn().toString()+"_View");
		this.entity = entity;
		this.setCullHint(CullHint.Dynamic);
	}
	
	@Override
	public void attachSpatial(Spatial mesh) {
		this.attachChild(mesh);
	}

	@Override
	public void attachTo(Node parent) {
		parent.attachChild(this);
	}

	@Override
	public boolean detachFromParent() {
		return this.removeFromParent();
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof IView) {
			IView given = (IView)object;
			return given.getEntity().equals(this.entity);
		}
		return false;
	}

	@Override
	public IEntity getEntity() {
		return this.entity;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return View.class;
	}
	
	@Override
	public void write (JMEExporter ex) throws IOException {
		super.write(ex);
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(this.entity, "Entity", null);
	}
	
	@Override
	public void read(JMEImporter im) throws IOException {
		super.read(im);
		InputCapsule ic = im.getCapsule(this);
		this.entity = (IEntity)ic.readSavable("Entity", null);
	}
}
