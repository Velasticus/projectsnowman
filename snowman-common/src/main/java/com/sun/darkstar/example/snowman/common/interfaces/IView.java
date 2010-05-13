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

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.export.Savable;

/**
 * <code>IView</code> defines the interface for all types of view that
 * represents entities in the system which follows the MVC (Model View Controller)
 * design pattern.
 * <p>
 * <code>IView</code> defines the graphic representation of an entity. It
 * maintains graphic information includes meshes, animations and etc.
 * associated with the entity. It is able to be directly attached to the
 * scene graph for rendering.
 * <p>
 * <code>IView</code> maintains a reference to the <code>IEntity</code> it
 * represents since every <code>IView</code> has to have a data store for the
 * actual object in game world. Two <code>View</code> are considered 'equal'
 * if and only if they represent the same <code>IEntity</code>.
 * <p>
 * <code>IView</code> extends <code>Savable</code> interface so it can be
 * directly saved into a jME binary format which can then be imported
 * later on at game run time.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 05-27-2008 14:53 EST
 * @version Modified date: 07-01-2008 18:21 EST
 */
public interface IView extends Savable {

	/**
	 * Attach the given mesh to this view.
	 * @param child The <code>Spatial</code> to be attached.
	 */
	public void attachSpatial(Spatial child);
	
	/**
	 * Attach this view to the given <code>Node</code>.
	 * @param parent The <code>Node</code> scene graph parent to attach to.
	 */
	public void attachTo(Node parent);
	
	/**
	 * Detach this view from the parent scene graph.
	 * @return True if view is detached successfully. False if this view is not in scene graph.
	 */
	public boolean detachFromParent();

	/**
	 * Retrieve the entity this view represents.
	 * @return The <code>IEntity</code> instance.
	 */
	public IEntity getEntity();
}
