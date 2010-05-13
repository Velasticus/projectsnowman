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
package com.sun.darkstar.example.snowman.game.entity;

import com.jme.math.Vector3f;
import com.sun.darkstar.example.snowman.common.entity.Entity;
import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity;
import com.sun.darkstar.example.snowman.common.interfaces.IDynamicEntity;

/**
 * <code>DynamicEntity</code> extends <code>Entity</code> and implements
 * <code>IDynamicEntity</code> to represent an actual dynamic entity in
 * the game world.
 * <p>
 * <code>DynamicEntity</code> does not provide a default constructor for
 * binary import and export. This prevents any attempts of exporting
 * <code>DynamicEntity</code> into a binary format.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-13-2008 14:41 EST
 * @version Modified date: 07-28-2008 17:00 EST
 */
public class DynamicEntity extends Entity implements IDynamicEntity {
	/**
	 * The mass value of this <code>DynamicEntity</code>.
	 */
	protected float mass;
	/**
	 * The current <code>Vector3f</code> velocity.
	 */
	protected final Vector3f velocity;
	/**
	 * The force <code>Vector3f</code> currently in effect.
	 */
	protected final Vector3f force;
	
	/**
	 * Constructor of <code>DynamicEntity</code>
	 * @param enumn The <code>EEntity</code> enumeration.
	 * @param id The integer ID number of this entity.
	 */
	public DynamicEntity(EEntity enumn, int id) {
		super(enumn, id);
		this.mass = enumn.getMass();
		this.velocity = new Vector3f();
		this.force = new Vector3f();
	}

	@Override
	public void addForce(Vector3f force) {
		this.force.addLocal(force);
	}

	@Override
	public void resetForce() {
		this.force.zero();
	}

	@Override
	public void setMass(float mass) {
		this.mass = mass;
	}

	@Override
	public void addVelocity(Vector3f velocity) {
		this.velocity.addLocal(velocity);
	}

	@Override
	public void resetVelocity() {
		this.velocity.zero();
	}

	@Override
	public float getMass() {
		return this.mass;
	}

	@Override
	public Vector3f getVelocity() {
		return this.velocity;
	}

	@Override
	public Vector3f getNetForce() {
		return this.force;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return DynamicEntity.class;
	}
}
