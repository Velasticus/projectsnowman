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

import com.jme.util.Timer;
import com.sun.darkstar.example.snowman.common.entity.view.View;
import com.sun.darkstar.example.snowman.common.interfaces.IDynamicEntity;
import com.sun.darkstar.example.snowman.game.entity.view.scene.SnowballView;
import com.sun.darkstar.example.snowman.game.entity.view.scene.SnowballTrailView;
import com.sun.darkstar.example.snowman.exception.ObjectNotFoundException;
import com.sun.darkstar.example.snowman.game.entity.scene.SnowballEntity;
import com.sun.darkstar.example.snowman.game.entity.scene.SnowballTrailEntity;
import com.sun.darkstar.example.snowman.game.entity.util.EntityManager;
import com.sun.darkstar.example.snowman.game.entity.view.util.ViewManager;
import com.sun.darkstar.example.snowman.game.input.Controller;
import com.sun.darkstar.example.snowman.game.input.enumn.EInputType;
import com.sun.darkstar.example.snowman.game.input.util.InputManager;
import com.sun.darkstar.example.snowman.game.physics.util.PhysicsManager;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;
import com.sun.darkstar.example.snowman.game.task.util.TaskManager;

/**
 * <code>SnowballController</code> extends <code>Controller</code> to define
 * the controlling unit that is responsible for controlling the movement
 * of a <code>SnowballEntity</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-25-2008 17:00 EST
 * @version Modified date: 07-29-2008 12:18 EST
 */
public class SnowballController extends Controller {
	/**
	 * The movement tolerance value.
	 */
	protected final float tolerance;
	/**
	 * The flag indicates if the snow ball has been thrown.
	 */
	private boolean thrown;
        /**
         * timer so we can delay the throw
         */
        private final Timer timer;
        private final float startTime;
	
	/**
	 * Constructor of <code>SnowballController</code>.
	 * @param entity The <code>SnowballEntity</code> instance.
	 */
	public SnowballController(SnowballEntity entity) {
		super(entity, EInputType.None);
		this.tolerance = 0.5f;
                this.timer = Timer.getTimer();
                this.startTime = timer.getTimeInSeconds();
	}

	@Override
	protected void updateLogic(float interpolation) {
		if(!this.thrown) {
                    if(timer.getTimeInSeconds() > startTime+0.6f) {
                        SnowballView view = (SnowballView)ViewManager.getInstance().getView(entity);
                        SnowballTrailView trailView = (SnowballTrailView) ViewManager.getInstance().getView(((SnowballEntity)entity).getTrail());
                        view.show();
                        trailView.show(view.getBall());
                        ViewManager.getInstance().markForUpdate(entity);
			TaskManager.getInstance().createTask(ETask.MoveSnowball, this.entity);
			this.thrown = true;
                    }
		} else if(!this.validatePosition()) {
			PhysicsManager.getInstance().markForUpdate(this.entity);
                        ViewManager.getInstance().markForUpdate(((SnowballEntity)entity).getTrail());
		} else {
			EntityManager.getInstance().removeEntity(this.entity.getID());
			ViewManager.getInstance().removeView(this.entity);
                        
                        EntityManager.getInstance().removeEntity(((SnowballEntity)entity).getTrail().getID());
			ViewManager.getInstance().removeView(((SnowballEntity)entity).getTrail());
                        
			InputManager.getInstance().removeController((IDynamicEntity)this.entity);
		} 
	}
	
	/**
	 * Validate if the current position is within the tolerance range of the destination.
	 * @return True if the snow ball is considered reached the destination. False otherwise.
	 */
	private boolean validatePosition() {
		SnowballEntity snowball = ((SnowballEntity)this.entity);
		if(snowball.getDestination() == null) return true;
		try {
			View view = (View)ViewManager.getInstance().getView(snowball);
			float dx = view.getLocalTranslation().x - snowball.getDestination().x;
			float dz = view.getLocalTranslation().z - snowball.getDestination().z;
			return ((dx * dx) + (dz * dz) <= this.tolerance);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}
