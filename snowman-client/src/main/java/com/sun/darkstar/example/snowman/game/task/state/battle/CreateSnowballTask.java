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

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity;
import com.sun.darkstar.example.snowman.common.entity.view.View;
import com.sun.darkstar.example.snowman.common.util.enumn.EStats;
import com.sun.darkstar.example.snowman.exception.ObjectNotFoundException;
import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.game.entity.scene.CharacterEntity;
import com.sun.darkstar.example.snowman.game.entity.scene.SnowballEntity;
import com.sun.darkstar.example.snowman.game.entity.scene.SnowballTrailEntity;
import com.sun.darkstar.example.snowman.game.entity.util.EntityManager;
import com.sun.darkstar.example.snowman.game.entity.view.scene.SnowballView;
import com.sun.darkstar.example.snowman.game.entity.view.scene.SnowballTrailView;
import com.sun.darkstar.example.snowman.game.entity.view.util.ViewManager;
import com.sun.darkstar.example.snowman.game.input.util.InputManager;
import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;
import com.sun.darkstar.example.snowman.game.task.Task;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;
import com.sun.darkstar.example.snowman.interfaces.IController;

/**
 * <code>CreateSnowballTask</code> extends <code>Task</code> to define the task
 * that generates a throwing snow ball action.
 * <p>
 * <code>CreateSnowballTask</code> execution logic:
 * 1. Retrieve attacker and target view based on entity.
 * 2. Retrieve attacker and target positions.
 * 3. Set target to be in hit state and top the character.
 * 4. Create snow ball entity and view.
 * 5. Attach snow ball at correct location.
 * 6. Create and activate a snow ball controller.
 * 7. update snow ball render state.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-25-2008 15:36 EST
 * @version Modified date: 08-06-2008 11:30 EST
 */
public class CreateSnowballTask extends Task {
	/**
	 * The ID number of the attacker.
	 */
	private final CharacterEntity attackerEntity;
	/**
	 * The ID number of the target.
	 */
	private final CharacterEntity targetEntity;

	/**
	 * Constructor of <code>CreateSnowballTask</code>.
	 * @param game The <code>Game</code> instance.
	 * @param attacker The attacker <code>CharacterEntity</code> instance.
	 * @param target The target <code>CharacterEntity</code> instance.
	 */
	public CreateSnowballTask(Game game, CharacterEntity attacker, CharacterEntity target) {
		super(ETask.CreateSnowball, game);
		this.attackerEntity = attacker;
		this.targetEntity = target;
	}

	@Override
	public void execute() {
		try {
			// Step 1.
			View attacker = (View)ViewManager.getInstance().getView(this.attackerEntity);
			View target = (View)ViewManager.getInstance().getView(this.targetEntity);
			// Step 2.
			Vector3f attackerPosition = attacker.getLocalTranslation().clone();
			Vector3f targetPosition = target.getLocalTranslation().clone();
			// Step 3.
			
			// Step 4. create the snowball
			SnowballEntity snowball = (SnowballEntity)EntityManager.getInstance().createEntity(EEntity.Snowball);
			snowball.setDestination(targetPosition);
                        snowball.setTarget(targetEntity);
			SnowballView snowballView = (SnowballView)ViewManager.getInstance().createView(snowball);
			// Step 5. position the snowball
			snowballView.setLocalTranslation(attackerPosition);
			Vector3f right = new Vector3f();
			attacker.getLocalRotation().getRotationColumn(0, right);
			right.normalizeLocal();
			right.multLocal(((BoundingBox)attacker.getWorldBound()).xExtent*-0.75f);
                        if(Vector3f.isValidVector(right)) {
                            snowballView.getLocalTranslation().y += ((BoundingBox) attacker.getWorldBound()).yExtent * 1.6f;
                            snowballView.getLocalTranslation().addLocal(right);
                        }
                        else {
                            //workaround for NaN issue with world bounds
                            snowballView.getLocalTranslation().y += EStats.SnowmanHeight.getValue();
                        }
                        
                        //create the snowball trail
                        SnowballTrailEntity snowballTrail = (SnowballTrailEntity)EntityManager.getInstance().createEntity(EEntity.SnowballTrail);
                        snowball.setTrail(snowballTrail);
                        SnowballTrailView snowballTrailView = (SnowballTrailView)ViewManager.getInstance().createView(snowballTrail);
                        
                        //attach the views
			this.game.getGameState(EGameState.BattleState).getWorld().attachChild(snowballView);
                        this.game.getGameState(EGameState.BattleState).getWorld().attachChild(snowballTrailView);
                        
			// Step 6. setup the snowball controller
			IController controller = InputManager.getInstance().getController(snowball);
			controller.setActive(true);
			// Step 7.
			snowballView.updateRenderState();
                        snowballTrailView.updateRenderState();
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		}
	}
        
        @Override
	public boolean equals(Object object) {
            if (object instanceof CreateSnowballTask) {
                CreateSnowballTask given = (CreateSnowballTask) object;
                return given == this;
            }
            return false;
	}
}
