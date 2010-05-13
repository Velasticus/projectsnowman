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

import com.jme.math.Vector3f;

/**
 * <code>IDynamicEntity</code> defines the interface for all types of dynamic
 * entity in the game world.
 * <p>
 * <code>IDynamicEntity</code> maintains all the physics information of the
 * dynamic entity that is essential for any physics calculations.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-04-2008 14:54 EST
 * @version Modified date: 07-28-2008 16:59 EST
 */
public interface IDynamicEntity extends IEntity {
	
	/**
	 * Add the given force to this entity.
	 * @param force The force in <code>Vector3f</code> form.
	 */
	public void addForce(Vector3f force);
	
	/**
	 * Clear the force acting on this entity.
	 */
	public void resetForce();
	
	/**
	 * Set the mass of this dynamic entity.
	 * @param mass The new float mass value to be set.
	 */
	public void setMass(float mass);
	
	/**
	 * Add the velocity of this dynamic entity.
	 * @param velocity The <code>Vector3f</code> velocity to be added.
	 */
	public void addVelocity(Vector3f velocity);
	
	/**
	 * Reset the velocity of this dynamic entity to 0.
	 */
	public void resetVelocity();

	/**
	 * Retrieve the mass value of this dynamic entity.
	 * @return The mass value of this <code>IDynamicEntity</code>.
	 */
	public float getMass();
	
	/**
	 * Retrieve the current velocity of this dynamic entity.
	 * @return The <code>Vector3f</code> velocity.
	 */
	public Vector3f getVelocity();

	/**
	 * Retrieve the net force of this dynamic entity.
	 * @return The net force in <code>Vector3f</code> form.
	 */
	public Vector3f getNetForce();
}
