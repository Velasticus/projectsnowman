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
package com.sun.darkstar.example.snowman.game.entity.view;

import com.sun.darkstar.example.snowman.common.entity.view.View;
import com.sun.darkstar.example.snowman.common.interfaces.IDynamicEntity;
import com.sun.darkstar.example.snowman.interfaces.IDynamicView;

/**
 * <code>DynamicView</code> extends <code>View</code> and implements
 * <code>IDynamicView</code> to represent a dynamic view for its base
 * <code>IDynamicEntity</code>.
 * <p>
 * <code>DynamicView</code> only defines an abstraction for its subclasses
 * that represent specific types of dynamic views. Subclasses need to
 * implement specific update logic.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-16-2008 16:27 EST
 * @version Modified date: 07-01-2008 17:12 EST
 */
public abstract class DynamicView extends View implements IDynamicView {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 5540885068355371725L;

	/**
	 * Constructor of <code>DynamicView</code>.
	 * @param entity The <code>IDynamicEntity</code> this view represents.
	 */
	public DynamicView(IDynamicEntity entity) {
		super(entity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return DynamicView.class;
	}
}
