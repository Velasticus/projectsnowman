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
package com.sun.darkstar.example.snowman.game.entity.scene;

import com.jme.math.Vector3f;
import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity;
import com.sun.darkstar.example.snowman.game.entity.DynamicEntity;

/**
 * <code>SnowballEntity</code> extends <code>DynamicEntity</code> to define
 * a snow ball that is thrown by a character.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-25-2008 16:40 EST
 * @version Modified date: 07-28-2008 16:50 EST
 */
public class SnowballEntity extends DynamicEntity {
	/**
	 * The maximum height of the snow ball.
	 */
	private final float maxHeight;
	/**
	 * The <code>Vector3f</code> destination of this snow ball.
	 */
	private Vector3f destination;
        /**
	 * The target <code>CharacterEntity</code> instance.
	 */
	protected CharacterEntity target;
        /**
         * The trail of this snowball
         */
        protected SnowballTrailEntity trail;

	/**
	 * Constructor of <code>SnowballEntity</code>.
	 * @param id The ID number of this entity.
	 */
	public SnowballEntity(int id) {
		super(EEntity.Snowball, id);
		this.maxHeight = 1.2f;
	}
	
	/**
	 * Set the destination of this snow ball.
	 * @param destination The <code>Vecto3f</code> destination.
	 */
	public void setDestination(Vector3f destination) {
		this.destination = destination;
	}
        
        /**
	 * Set the current target of this character.
	 * @param target The target <code>CharacterEntity</code> instance.
	 */
	public void setTarget(CharacterEntity target) {
		this.target = target;
	}
        
        /**
	 * Set the current trail of this snowball.
	 * @param trail The trail <code>SnowballTrailEntity</code> instance
	 */
	public void setTrail(SnowballTrailEntity trail) {
		this.trail = trail;
	}
	
	/**
	 * Retrieve the maximum height the snow ball will reach.
	 * @return The float maximum height value.
	 */
	public float getMaxHeight() {
		return this.maxHeight;
	}
	
	/**
	 * Retrieve the destination of this snow ball.
	 * @return The <code>Vector3f</code> destination.
	 */
	public Vector3f getDestination() {
		return this.destination;
	}
        
        /**
	 * Retrieve the target <code>CharacterEntity</code> instance.
	 * @return The target <code>CharacterEntity</code> instance.
	 */
	public CharacterEntity getTarget() {
		return this.target;
	}
        
        /**
	 * Retrieve the trail <code>SnowballTrailEntity</code> instance.
	 * @return The trail <code>SnowballTrailEntity</code> instance.
	 */
	public SnowballTrailEntity getTrail() {
		return this.trail;
	}
}
