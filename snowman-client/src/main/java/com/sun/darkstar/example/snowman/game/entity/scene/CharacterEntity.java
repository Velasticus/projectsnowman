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
import com.sun.darkstar.example.snowman.common.entity.enumn.EState;
import com.sun.darkstar.example.snowman.common.entity.enumn.ECursorState;
import com.sun.darkstar.example.snowman.common.interfaces.IDynamicEntity;
import com.sun.darkstar.example.snowman.common.util.SingletonRegistry;
import com.sun.darkstar.example.snowman.game.entity.DynamicEntity;

/**
 * <code>CharacterEntity</code> extends <code>DynamicEntity</code> to define
 * the base snowman character in the game.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-24-2008 11:34 EST
 * @version Modified date: 08-06-2008 11:18 EST
 */
public class CharacterEntity extends DynamicEntity {
    /**
     * The name of the snowman
     */
    protected String name;
	/**
	 * The current HP of the snowman.
	 */
	protected int hp;
	/**
	 * The current <code>EState</code>.
	 */
	protected EState state;
        /**
	 * The current <code>ECursorState</code>.
	 */
	protected ECursorState cursorState;
	/**
	 * The current <code>Vector3f</code> destination.
	 */
	protected Vector3f destination;
	/**
	 * The target <code>IDynamicEntity</code> instance.
	 */
	protected IDynamicEntity target;
	/**
	 * The flag entity instance.
	 */
	protected DynamicEntity flag;

	/**
	 * Constructor of <code>CharacterEntity</code>.
	 * @param enumn The <code>EEntity</code> enumeration.
	 * @param id The ID number of this snowman.
	 */
	public CharacterEntity(EEntity enumn, int id, String name) {
		super(enumn, id);
                this.name = name;
		this.hp = SingletonRegistry.getHPConverter().getMaxHP();
		this.state = EState.Idle;
                this.cursorState = ECursorState.Invalid;
	}
	
	/**
	 * Set the HP of this snowman.
	 * @param hp The new HP value to set.
	 */
	public void setHP(int hp) {
		this.hp = hp;
	}
	
	/**
	 * Add the given delta value to the character HP.
	 * @param delta The change in HP value.
	 */
	public void addHP(int delta) {
		this.hp -= delta;
	}
	
	/**
	 * Set the current state of the snowman.
	 * @param state The <code>EState</code> enumeration.
	 */
	public void setState(EState state) {
		this.state = state;
	}
        
        /**
	 * Set the current cursor state of the snowman.
	 * @param cursorState The <code>ECursorState</code> enumeration.
	 */
	public void setCursorState(ECursorState cursorState) {
		this.cursorState = cursorState;
	}
	
	/**
	 * Set the destination of this snowman.
	 * @param destination The <code>Vector3f</code> destination to be set.
	 */
	public void setDestination(Vector3f destination) {
		this.destination = destination;
	}
	
	/**
	 * Set the current target of this character.
	 * @param target The target <code>IDynamicEntity</code> instance.
	 */
	public void setTarget(IDynamicEntity target) {
		this.target = target;
	}
	
	/**
	 * Set the flag entity the snowman is carrying.
	 * @param flag The flag <code>DynamicEntity</code> instance.
	 */
	public void setFlag(DynamicEntity flag) {
		this.flag = flag;
	}

	/**
	 * Retrieve the current HP value.
	 * @return The integer HP value.
	 */
	public int getHP() {
		return this.hp;
	}
	
	/**
	 * Retrieve the current state of the snowman.
	 * @return The <code>EState</code> enumeration.
	 */
	public EState getState() {
		return this.state;
	}
        
        /**
	 * Retrieve the current cursor state of the snowman.
	 * @return The <code>ECursorState</code> enumeration.
	 */
	public ECursorState getCursorState() {
		return this.cursorState;
	}
	
	/**
	 * Retrieve the destination of this snowman.
	 * @return The <code>Vector3f</code> destination.
	 */
	public Vector3f getDestination() {
		return this.destination;
	}
	
	/**
	 * Retrieve the target <code>IDynamicEntity</code> instance.
	 * @return The target <code>IDynamicEntity</code> instance.
	 */
	public IDynamicEntity getTarget() {
		return this.target;
	}
	
	/**
	 * Retrieve the flag entity that is carried by the character.
	 * @return The <code>IDynamicEntity</code> instance.
	 */
	public IDynamicEntity getFlag() {
		return this.flag;
	}
	
	/**
	 * Check if this character is still alive.
	 * @return True if this character is still alive. False otherwise.
	 */
	public boolean isAlive() {
		return (this.hp > 0);
	}
	
	/**
	 * Check if the character is carrying a flag.
	 * @return True if the character is carrying a flag. False otherwise.
	 */
	public boolean isCarrying() {
		return (this.flag != null);
	}
        
        public String getName() {
            return name;
        }
}
