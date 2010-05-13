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
package com.sun.darkstar.example.snowman.common.interfaces;

import java.util.ArrayList;

/**
 * <code>IEditableWorld</code> extends <code>IAbstractWorld</code> and
 * <code>IEditable</code> to define the interface for an editable world
 * during world editing stages through world editor.
 * <p>
 * <code>IEditableWorld</code> maintains only <code>IEditableView</code>.
 * During the registration process, <code>IEditableEntity</code> and
 * <code>IInfluence</code> are accessed through the views.
 * <p>
 * During the final world export process, all <code>IEditableView</code> are
 * invoked by the <code>World</code> to first construct <code>IStaticEntity</code>
 * based on the <code>IEditableEntity</code> of the views, then construct
 * <code>IStaticView</code> based on both the <code>IStaticEntity</code> and
 * the <code>IEditableView</code> itself.
 * <p>
 * <code>IEditableWorld</code> is created at the world editing stage where all
 * the editable entities in the game world are constructed and placed.
 * <p>
 * <code>IEditableWorld</code> is finalized into an <code>IWorld</code> instance
 * that is utilized by the <code>Game</code> at game run time.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-01-2008 24:39 EST
 * @version Modified date: 07-08-2008 16:27 EST
 */
public interface IEditableWorld extends IAbstractWorld, IEditable {

	/**
	 * Attach the given editable view to this editable world.
	 * @param view The <code>IEditableView</code> to be attached.
	 */
	public void attachView(IEditableView view);
	
	/**
	 * Detach the given editable view from this editable world.
	 * @param view The <code>IEditableView</code> to be detached.
	 */
	public void detachView(IEditableView view);
	
	/**
	 * Retrieve a shallow copy of the list of editable views.
	 * @return The <code>ArrayList</code> of <code>IEditableView</code> instances.
	 */
	public ArrayList<IEditableView> getViews();
}
