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

import com.sun.darkstar.example.snowman.common.interfaces.IEditable;
import com.sun.darkstar.example.snowman.common.interfaces.IStaticEntity;
import com.sun.darkstar.example.snowman.common.interfaces.IStaticView;

/**
 * <code>StaticView</code> extends <code>View</code> and implements
 * <code>IStaticView</code> to represent a static view for its base
 * <code>IStaticEntity</code>.
 * <p>
 * <code>StaticView</code> is constructed by <code>ViewManager</code>
 * when a <code>IStaticEntity</code> is created.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-16-2008 16:23 EST
 * @version Modified date: 07-07-2008 18:01 EST
 */
public class StaticView extends View implements IStaticView {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = -6155991300919576009L;

	/**
	 * Constructor of <code>StaticView</code>.
	 */
	public StaticView() {
		super();
	}
	
	/**
	 * Constructor of <code>StaticView</code>.
	 * @param entity The <code>IStaticEntity</code> this view represents. 
	 */
	public StaticView(IStaticEntity entity) {
		super(entity);
	}

	@Override
	public void process(IEditable editable) {
		if(editable instanceof EditableView) {
			EditableView given = (EditableView)editable;
			if(given.getChildren() == null) return;
			for(int i = 0; i < given.getQuantity(); i++) {
				this.attachChild(given.getChild(i));
			}
			this.setLocalTranslation(given.getLocalTranslation());
		}
	}

	@Override
	public void lock() {
		this.lockMeshes();
		this.lockBounds();
		this.lockTransforms();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return StaticView.class;
	}
}
