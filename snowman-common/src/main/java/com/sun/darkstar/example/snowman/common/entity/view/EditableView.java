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

import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.SharedNode;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.sun.darkstar.example.snowman.common.interfaces.IEditableEntity;
import com.sun.darkstar.example.snowman.common.interfaces.IEditableView;
import com.sun.darkstar.example.snowman.common.interfaces.IFinal;
import com.sun.darkstar.example.snowman.common.interfaces.IStaticEntity;

/**
 * <code>EditableView</code> extends <code>View</code> and implements the
 * <code>IEditableView</code> interface to define the view for all types
 * of <code>IEditableEntity</code> utilized by the world editor during the
 * world editing stages.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-01-2008 15:40 EST
 * @version Modified date: 07-25-2008 16:55 EST
 */
public class EditableView extends View implements IEditableView {
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = -4899825988268393506L;

	/**
	 * Constructor of <code>EditableView</code>.
	 */
	public EditableView() {
		super();
	}
	
	/**
	 * Constructor of <code>EditableView</code>.
	 * @param entity The <code>IEditableEntity</code> instance.
	 */
	public EditableView(IEditableEntity entity) {
		super(entity);
		this.buildAxisView();
		this.buildWireView();
	}
	
	@Override
	public void attachSpatial(Spatial spatial) {
		if (spatial instanceof TriMesh) {
			SharedMesh shared = new SharedMesh((TriMesh)spatial);
			this.attachChild(shared);
		} else if (spatial instanceof Node) {
			SharedNode shared = new SharedNode((Node)spatial);
			this.attachChild(shared);
		} else {
			throw new IllegalArgumentException("Illegal Spatial type: "+spatial.getClass().getName());
		}
	}
	
	/**
	 * Build the axis view used during editing.
	 */
	private void buildAxisView() {
		// TODO
	}
	
	/**
	 * Build the wire frame view used during editing.
	 */
	private void buildWireView() {
		// TODO
	}
	
	@Override
	public IFinal constructFinal() {
		IStaticEntity entity = (IStaticEntity)((IEditableEntity)this.getEntity()).constructFinal();
		StaticView view = new StaticView(entity);
		view.process(this);
		view.lock();
		return view;
	}

	@Override
	public void setAxisEnabled(boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWireEnabled(boolean enabled) {
		// TODO Auto-generated method stub

	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return this.getClass();
	}
}
