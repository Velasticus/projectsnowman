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

/**
 * <code>IEditableView</code> defines the interface for views that represent
 * <code>IEditableEntity</code> during the world editing stages.
 * <p>
 * <code>IEditableView</code> is the intermediate editing stage for all
 * <code>IStaticView</code>. An <code>IEditableView</code> is created through
 * the world editor and can only exist during the world editing stages. It is
 * eventually converted to an <code>IStaticView</code> during the world export
 * process.
 * <p>
 * <code>IEditableView</code> provides the functionalities that are used for
 * editing the view. These functionalities are utilized by the world editor.
 * <p>
 * <code>IEditableView</code> is finalized into an <code>IStaticView</code>
 * instance that is utilized by <code>IStaticEntity</code> at game run time.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-30-2008 21:53 EST
 * @version Modified date: 07-01-2008 11:47 EST
 */
public interface IEditableView extends IView, IEditable {

	/**
	 * Enable or disable the axis view on this editable view.
	 * @param enabled True if axis should be shown. False otherwise.
	 */
	public void setAxisEnabled(boolean enabled);
	
	/**
	 * Enable or disable the wire frame view on this editable view.
	 * @param enabled True if wire frame should be shown. False otherwise.
	 */
	public void setWireEnabled(boolean enabled);
}
