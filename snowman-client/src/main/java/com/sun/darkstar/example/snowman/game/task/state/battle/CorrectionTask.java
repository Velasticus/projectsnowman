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

import com.sun.darkstar.example.snowman.common.entity.enumn.EState;
import com.sun.darkstar.example.snowman.common.entity.view.View;
import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.game.entity.scene.CharacterEntity;
import com.sun.darkstar.example.snowman.game.entity.util.EntityManager;
import com.sun.darkstar.example.snowman.game.entity.view.util.ViewManager;
import com.sun.darkstar.example.snowman.game.task.RealTimeTask;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;

/**
 * <code>CorrectionTask</code> extends <code>RealTimeTask</code> to correct
 * the position of a character.
 * <p>
 * <code>CorrectionTask</code> execution logic:
 * 1. Retrieve the character entity and view based on given ID.
 * 2. Teleport the character to the given coordinates.
 * 3. Stop the movement and set state back to idle.
 * <p>
 * <code>CorrectionTask</code> are considered 'equal' if and only if the
 * <code>CharacterEntity</code> are 'equal'.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 08-07-2008 15:01 EST
 * @version Modified date: 08-07-2008 15:09 EST
 */
public class CorrectionTask extends RealTimeTask {
	/**
	 * The ID number of the character to be corrected.
	 */
	private final int id;
	/**
	 * The x coordinate to correct to.
	 */
	private final float x;
	/**
	 * The z coordinate to correct to.
	 */
	private final float z;

	/**
	 * Constructor of <code>CorrectionTask</code>.
	 * @param game The <code>Game</code> instance.
	 * @param id The ID number of the character to be corrected.
	 * @param x The x coordinate to correct to.
	 * @param z The z coordinate to correct to.
	 */
	public CorrectionTask(Game game, int id, float x, float z) {
		super(ETask.Correction, game);
		this.id = id;
		this.x = x;
		this.z = z;
	}

	@Override
	public void execute() {
		// Step 1.
		CharacterEntity entity = (CharacterEntity)EntityManager.getInstance().getEntity(this.id);
		View view = (View)ViewManager.getInstance().getView(entity);
		// Step 2.
		view.getLocalTranslation().x = this.x;
		view.getLocalTranslation().z = this.z;
		// Step 3.
		entity.resetForce();
		entity.resetVelocity();
		entity.setState(EState.Idle);
		ViewManager.getInstance().markForUpdate(entity);
	}

	@Override
	public boolean equals(Object object) {
		if(super.equals(object)) {
			if(object instanceof CorrectionTask) {
				CorrectionTask given = (CorrectionTask)object;
				return (this.id == given.id);
			}
		}
		return false;
	}
}
