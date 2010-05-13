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
package com.sun.darkstar.example.snowman.game.entity.controller;

import com.jme.math.Vector3f;
import com.jme.system.DisplaySystem;
import com.sun.darkstar.example.snowman.common.util.SingletonRegistry;
import com.sun.darkstar.example.snowman.common.entity.enumn.EState;
import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity;
import com.sun.darkstar.example.snowman.common.entity.view.View;
import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;
import com.sun.darkstar.example.snowman.exception.ObjectNotFoundException;
import com.sun.darkstar.example.snowman.game.entity.scene.CharacterEntity;
import com.sun.darkstar.example.snowman.game.entity.view.scene.CharacterView;
import com.sun.darkstar.example.snowman.game.entity.view.util.ViewManager;
import com.sun.darkstar.example.snowman.game.input.Controller;
import com.sun.darkstar.example.snowman.game.input.enumn.EInputType;
import com.sun.darkstar.example.snowman.game.physics.util.PhysicsManager;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;
import com.sun.darkstar.example.snowman.game.task.util.TaskManager;

/**
 * <code>CharacterController</code> extends <code>Controller</code> to define
 * the base type controller that controls a <code>SnowmanEntity</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-24-2008 11:24 EST
 * @version Modified date: 08-12-2008 11:47 EST
 */
public class CharacterController extends Controller {
	/**
	 * The movement tolerance value.
	 */
	protected final float tolerance;
	/**
	 * The last known state.
	 */
	private EState lastState;
//	/**
//	 * The flag indicates if a snow ball has been thrown for the current attack.
//	 */
//	private boolean thrown;
        
        private int count = 0;

	/**
	 * Constructor of <code>CharacterController</code>.
	 * @param entity The <code>CharacterEntity</code> instance.
	 * @param type The <code>EInputType</code> enumeration.
	 */
	public CharacterController(CharacterEntity entity, EInputType type) {
		super(entity, type);
		this.tolerance = 0.1f;
	}

	@Override
	protected void updateLogic(float interpolation) {
            if (!this.getEntity().isAlive()) {
                return;
            }
            
            //update label position
            CharacterView view = (CharacterView) ViewManager.getInstance().getView(this.entity);
            Vector3f v1 = view.getLocalTranslation();
            Vector3f v2 = v1.add(0.0f, 
                                 SingletonRegistry.getHPConverter().convertScale(this.getEntity().getHP()), 
                                 0.0f);
            Vector3f v3 =
                    DisplaySystem.getDisplaySystem().getRenderer().getCamera().getScreenCoordinates(v2);
            v3.setX(v3.getX() - view.getLabel().getWidth()/2.0f);
            view.getLabelNode().setLocalTranslation(v3);
            
            switch (this.getEntity().getState()) {
                case Attacking:
                    if (((CharacterView) ViewManager.getInstance().getView(this.entity)).isCurrentComplete()) {
                        this.getEntity().setState(EState.Idle);
                        ViewManager.getInstance().markForUpdate(this.entity);
                        // Update target scale and speed.
                        ViewManager.getInstance().markForUpdate(this.getEntity().getTarget());
                    }
                    break;
                case Hit:
                    if (((CharacterView) ViewManager.getInstance().getView(this.entity)).isCurrentComplete()) {
                        this.getEntity().setState(EState.Idle);
                        ViewManager.getInstance().markForUpdate(this.entity);
                    }
                    break;
                case Grabbing:
                    this.getEntity().setDestination(null);
                    this.entity.resetVelocity();
                    this.getEntity().setState(EState.Idle);
                    ViewManager.getInstance().markForUpdate(this.entity);
                    break;
                default:
                    if (!this.getEntity().getVelocity().equals(Vector3f.ZERO)) {
                        if (this.validatePosition()) {
                            this.getEntity().setDestination(null);
                            this.entity.resetVelocity();
                            this.getEntity().setState(EState.Idle);
                            ViewManager.getInstance().markForUpdate(entity);
                        } else {
                            PhysicsManager.getInstance().markForUpdate(this.entity);
                        }

                        if (this.getEntity().isCarrying()) {
                            tryScore();
                        }
                    }
                    break;
            }
            this.lastState = this.getEntity().getState();
    }

    /**
     * Validate if the current position is within the tolerance range of the destination.
     * @return True if the character is considered reached the destination. False otherwise.
     */
    private boolean validatePosition() {
		CharacterEntity character = ((CharacterEntity)this.entity);
		if(character.getDestination() == null) return true;
		try {
			View view = (View)ViewManager.getInstance().getView(character);
			float dx = view.getLocalTranslation().x - character.getDestination().x;
			float dz = view.getLocalTranslation().z - character.getDestination().z;
			if((dx * dx) + (dz * dz) <= this.tolerance) return true;
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
        
        /**
         * Try to score with a score task if this player is carrying the flag
         */
        private void tryScore() {
            CharacterEntity character = ((CharacterEntity) this.entity);
            View view = (View) ViewManager.getInstance().getView(character);
            float xPos = view.getLocalTranslation().x;
            float zPos = view.getLocalTranslation().z;
            ETeamColor color = character.getEnumn() == EEntity.SnowmanLocalBlue ? ETeamColor.Blue : ETeamColor.Red;
            TaskManager.getInstance().createTask(ETask.Score, color, xPos, zPos);
        }
	
	@Override
	public CharacterEntity getEntity() {
		return (CharacterEntity)this.entity;
	}
}
