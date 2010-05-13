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
package com.sun.darkstar.example.snowman.game.input;

import com.sun.darkstar.example.snowman.common.interfaces.IDynamicEntity;
import com.sun.darkstar.example.snowman.common.interfaces.IEntity;
import com.sun.darkstar.example.snowman.game.input.enumn.EInputType;
import com.sun.darkstar.example.snowman.interfaces.IController;

/**
 * <code>Controller</code> implements <code>IController</code> to define the
 * basic abstraction of an entity controller. It is responsible for monitoring
 * and processing user inputs. It generates corresponding <code>ITask</code>
 * in response to input events.
 * <p>
 * <code>Controller</code> is also responsible for monitoring the state of the
 * <code>IEntity</code> it controls to generate corresponding <code>ITask</code>
 * during the update cycle. This allows the <code>Controller</code> to modify
 * the <code>IEntity</code> continuously through several update iterations.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-11-2008 15:35 EST
 * @version Modified date: 07-25-2008 17:15 EST
 */
public abstract class Controller implements IController {
	/**
	 * The <code>IDynamicEntity</code> this controller controls.
	 */
	protected final IDynamicEntity entity;
	/**
	 * The <code>EInputType</code> enumeration.
	 */
	protected final EInputType type;
	/**
	 * The flag indicates if this controller is active.
	 */
	private boolean active;
	
	/**
	 * Constructor of <code>Controller</code>.
	 * @param entity The <code>IDynamicEntity</code> this controller controls.
	 * @param type The <code>EInputType</code> enumeration.
	 */
	public Controller(IDynamicEntity entity, EInputType type) {
		if(entity == null) throw new IllegalArgumentException("Null entity.");
		this.entity = entity;
		this.type = type;
	}
	
	@Override
	public void update(float interpolation) {
		if(this.active) this.updateLogic(interpolation);
	}
	
	/**
	 * Update the detailed logic of this controller.
	 * @param interpolation The frame rate interpolation value in seconds.
	 */
	protected abstract void updateLogic(float interpolation);

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public EInputType getInputType() {
		return this.type;
	}

	@Override
	public IEntity getEntity() {
		return this.entity;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}
}
