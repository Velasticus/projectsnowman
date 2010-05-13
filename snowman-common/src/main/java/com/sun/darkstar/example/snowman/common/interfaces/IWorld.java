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
 * <code>IWorld</code> extends <code>IAbstractWorld</code> and
 * <code>IFinal</code> to define the interface of run time game world data
 * structure.
 * <p>
 * <code>IWorld</code> maintains only a list of <code>IStaticView</code>.
 * During the registration process, <code>IEditableEntity</code> and
 * <code>IInfluence</code> are accessed through the views.
 * <p>
 * <code>IWorld</code> is constructed through world editor during the world
 * export process. It provides the functionality to process the information of
 * an <code>IEditableWorld</code> to create corresponding static information
 * before export.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-17-2008 13:10 EST
 * @version Modified date: 07-23-2008 11:04 EST
 */
public interface IWorld extends IAbstractWorld, IFinal {

	/**
	 * Retrieve all the views maintained by the world.
	 * @return The <code>ArrayList</code> of <code>IStaticView</code>.
	 */
	public ArrayList<IStaticView> getViews();
}
