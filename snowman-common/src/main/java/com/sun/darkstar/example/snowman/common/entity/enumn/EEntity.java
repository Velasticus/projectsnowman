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
package com.sun.darkstar.example.snowman.common.entity.enumn;

import com.sun.darkstar.example.snowman.common.util.SingletonRegistry;

/**
 * <code>EEntity</code> defines the enumerations of all types of entities in
 * the game world. It also maintains the default mass value of each type of
 * entity.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-04-2008 14:52 EST
 * @version Modified date: 08-07-2008 13:57 EST
 */
public enum EEntity {
	/**
	 * The terrain entity.
	 */
	Terrain(EEntityType.Editable, Float.POSITIVE_INFINITY),
	/**
	 * The red locally controlled snowman entity.
	 */
	SnowmanLocalRed(EEntityType.Dynamic, SingletonRegistry.getHPConverter().getDefaultMass()),
	/**
	 * The blue locally controlled snowman entity.
	 */
	SnowmanLocalBlue(EEntityType.Dynamic, SingletonRegistry.getHPConverter().getDefaultMass()),
	/**
	 * The red distributed snowman entity.
	 */
	SnowmanDistributedRed(EEntityType.Dynamic, SingletonRegistry.getHPConverter().getDefaultMass()),
	/**
	 * The blue distributed snowman entity.
	 */
	SnowmanDistributedBlue(EEntityType.Dynamic, SingletonRegistry.getHPConverter().getDefaultMass()),
	/**
	 * The red flag entity.
	 */
	FlagRed(EEntityType.Dynamic, Float.POSITIVE_INFINITY),
	/**
	 * The blue flag entity.
	 */
	FlagBlue(EEntityType.Dynamic, Float.POSITIVE_INFINITY),
	/**
	 * The red goal area
	 */
	FlagRedGoal(EEntityType.Dynamic, Float.POSITIVE_INFINITY),
	/**
	 * The blue goal area
	 */
	FlagBlueGoal(EEntityType.Dynamic, Float.POSITIVE_INFINITY),
	/**
	 * The snow ball entity.
	 */
	Snowball(EEntityType.Dynamic, 0.005f),
        /**
         * The snow ball trail
         */
        SnowballTrail(EEntityType.Dynamic, Float.POSITIVE_INFINITY),
	/**
	 * The tree entity.
	 */
	Tree(EEntityType.Editable, Float.POSITIVE_INFINITY),
	/**
	 * The house entity.
	 */
	House(EEntityType.Editable, Float.POSITIVE_INFINITY),
	/**
	 * The camp fire entity.
	 */
	CampFire(EEntityType.Editable, Float.POSITIVE_INFINITY),
	/**
	 * The snow globe entity.
	 */
	SnowGlobe(EEntityType.Editable, Float.POSITIVE_INFINITY);
	
	/**	
	 * The <code>EEntityType</code> value.
	 */
	private final EEntityType type;
	/**
	 * The mass value.
	 */
	private final float mass;
	
	/**
	 * Constructor of <code>EEntity</code>
	 * @param type The <code>EEntityType</code> enumeration.
	 */
	private EEntity(EEntityType type, float mass) {
		this.type = type;
		this.mass = mass;
	}
	
	/**
	 * Retrieve the type enumeration of this entity type.
	 * @return The <code>EEntityType</code> enumeration.
	 */
	public EEntityType getType() {
		return this.type;
	}
	
	/**
	 * Retrieve the mass value of this entity type.
	 * @return The float mass value.
	 */
	public float getMass() {
		return this.mass;
	}
	
	/**
	 * <code>EEntityType</code> defines all the types of <code>IEntity</code>
	 * in the system.
	 * 
	 * @author Yi Wang (Neakor)
	 * @author Tim Poliquin (Weenahmen)
	 * @version Creation date: 05-27-2008 15:10 EST
	 * @version Modified date: 05-27-2008 16:09 EST
	 */
	public enum EEntityType {
		/**
		 * The static entity type.
		 */
		Static,
		/**
		 * The dynamic entity type.
		 */
		Dynamic,
		/**
		 * The editable entity type.
		 */
		Editable
	}
}
