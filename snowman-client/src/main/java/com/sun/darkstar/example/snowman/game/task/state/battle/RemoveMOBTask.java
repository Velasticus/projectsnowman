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
package com.sun.darkstar.example.snowman.game.task.state.battle;

import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity.EEntityType;
import com.sun.darkstar.example.snowman.common.interfaces.IDynamicEntity;
import com.sun.darkstar.example.snowman.common.interfaces.IEntity;
import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.game.entity.view.scene.CharacterView;
import com.sun.darkstar.example.snowman.game.entity.scene.CharacterEntity;
import com.sun.darkstar.example.snowman.game.entity.util.EntityManager;
import com.sun.darkstar.example.snowman.game.entity.view.util.ViewManager;
import com.sun.darkstar.example.snowman.game.input.util.InputManager;
import com.sun.darkstar.example.snowman.game.task.RealTimeTask;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;
import com.sun.darkstar.example.snowman.game.task.util.TaskManager;
import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;
import com.sun.darkstar.example.snowman.game.state.scene.BattleState;

/**
 * <code>RemoveMOBTask</code> extends <code>RealTimeTask</code> to define the
 * task that removes an entity.
 * <p>
 * <code>RemoveMOBTask</code> execution logic:
 * 1. Retrieve the entity with given ID number.
 * 2. If entity is a character entity and is carrying a flag, drop the flag.
 * 3. Remove the view of the entity.
 * 4. Remove the controller of the entity.
 * 5. Remove the entity itself.
 * <p>
 * <code>RemoveMOBTask</code> are considered 'equal' if and only if the ID
 * number is the same.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 08-14-2008 16:24 EST
 * @version Modified date: 08-14-2008 16:40 EST
 */
public class RemoveMOBTask extends RealTimeTask {
	/**
	 * The ID number of the entity to be removed.
	 */
	private final int id;

	/**
	 * Constructor of <code>RemoveMOBTask</code>.
	 * @param game The <code>Game</code> instance.
	 * @param id The ID number of the entity to be removed.
	 */
	public RemoveMOBTask(Game game, int id) {
		super(ETask.Remove, game);
		this.id = id;
	}

	@Override
	public void execute() {
		// Step 1.
		IEntity entity = EntityManager.getInstance().getEntity(this.id);
		// Step 2.
		if(entity instanceof CharacterEntity) {
			CharacterEntity character = (CharacterEntity)entity;
			if(character.isCarrying()) {
				TaskManager.getInstance().createTask(ETask.Attach, character.getFlag().getID(), 0, false);
			}
                        CharacterView v = (CharacterView)ViewManager.getInstance().getView(entity);
                        ((BattleState) this.game.getGameState(EGameState.BattleState)).getWorld().getDynamicRoot().detachChild(v.getLabelNode());
		}
		// Step 3.
		ViewManager.getInstance().removeView(entity);
		// Step 4.
		if(entity.getType() == EEntityType.Dynamic) {
			InputManager.getInstance().removeController((IDynamicEntity)entity);
		}
		// Step 5.
		EntityManager.getInstance().removeEntity(this.id);
	}

	@Override
	public boolean equals(Object object) {
		if(super.equals(object)) {
			if(object instanceof RemoveMOBTask) {
				RemoveMOBTask given = (RemoveMOBTask)object;
				return (given.id == this.id);
			}
		}
		return false;
	}
}
