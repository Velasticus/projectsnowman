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
package com.sun.darkstar.example.snowman.interfaces;

import com.sun.darkstar.example.snowman.common.interfaces.IEntity;
import com.sun.darkstar.example.snowman.game.input.enumn.EInputType;

/**
 * <code>IController</code> defines the interface for all types of entity
 * controller in the system which follows the MVC (Model View Controller)
 * design pattern.
 * <p>
 * <code>IController</code> defines the controlling unit of a dynamic
 * entity which can be controlled by user inputs. It is responsible for
 * mapping user inputs to the entity and view it controls.
 * <p>
 * <code>IController</code> generates corresponding <code>ITask</code> when
 * a user input is received to modify the entity and it controls.
 * <p>
 * <code>IController</code> is updated by the main game update/render loop
 * every frame to perform potential continuous actions. It maintains a
 * reference to the <code>IEntity</code> it controls.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 05-27-2008 14:53 EST
 * @version Modified date: 07-21-2008 12:01 EST
 */
public interface IController {

	/**
	 * Update this controller.
	 * @param interpolation The frame rate interpolation value in seconds.
	 */
	public void update(float interpolation);
	
	/**
	 * Set the activeness of this controller.
	 * @param active True if this controller should be activated. False otherwise.
	 */
	public void setActive(boolean active);

	/**
	 * Retrieve the input type of this controller.
	 * @return The <Code>EInputType</code> enumeration.
	 */
	public EInputType getInputType();
	
	/**
	 * Retrieve the entity this controller controls.
	 * @return The <code>IEntity</code> instance.
	 */
	public IEntity getEntity();
	
	/**
	 * Check if this controller is active.
	 * @return True if the controller is active. False otherwise.
	 */
	public boolean isActive();
}
