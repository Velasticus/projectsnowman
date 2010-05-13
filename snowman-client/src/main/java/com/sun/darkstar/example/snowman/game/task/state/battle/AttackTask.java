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

import com.jme.math.Vector3f;
import com.sun.darkstar.example.snowman.common.entity.enumn.EState;
import com.sun.darkstar.example.snowman.common.entity.view.View;
import com.sun.darkstar.example.snowman.common.protocol.messages.ClientMessages;
import com.sun.darkstar.example.snowman.exception.ObjectNotFoundException;
import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.game.entity.scene.CharacterEntity;
import com.sun.darkstar.example.snowman.game.entity.scene.SnowmanEntity;
import com.sun.darkstar.example.snowman.game.entity.util.EntityManager;
import com.sun.darkstar.example.snowman.game.entity.view.util.ViewManager;
import com.sun.darkstar.example.snowman.game.input.util.InputManager;
import com.sun.darkstar.example.snowman.game.task.RealTimeTask;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;
import com.sun.darkstar.example.snowman.game.task.util.TaskManager;

/**
 * <code>AttackTask</code> extends <code>RealTimeTask</code> to define
 * the task that initiates the attacking process.
 * <p>
 * <code>AttackTask</code> execution logic:
 * 1. Retrieve the attacker and target entity and view based on IDs.
 * 2. Set the delta change to the target and check for death.
 * 3. Stop the attacker, create a snowball, and set attacker to attacking state.
 * 4. Rotate attacker towards the target.
 * 5. Set the target ID to the attacker <code>CharacterEntity</code>
 * 6. Send 'attack' packet to server if attacker is locally controlled.
 * <p>
 * <code>AttackTask</code> are considered 'equal' if and only if the attacker
 * and target IDs are exactly the same.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 08-05-2008 14:39 EST
 * @version Modified date: 08-06-2008 11:20 EST
 */
public class AttackTask extends RealTimeTask {
	/**
	 * The ID number of the attacker.
	 */
	private final int attackerID;
	/**
	 * The ID number of the target.
	 */
	private final int targetID;
	/**
	 * The delta HP value.
	 */
	private final int delta;
	/**
	 * The flag indicates if this snow attack is initiated by local controller.
	 */
	private final boolean local;
	/**
	 * The flag indicates if this is echoed back for the locally controlled player.
	 */
	private final boolean self;

	/**
	 * Constructor of <code>AttackingTask</code>.
	 * @param game The <code>Game</code> instance.
	 * @param attackerID The ID number of the attacker.
	 * @param targetID The ID number of the target.
	 */
	public AttackTask(Game game, int attackerID, int targetID) {
		super(ETask.Attack, game);
		this.attackerID = attackerID;
		this.targetID = targetID;
		this.delta = 0;
		this.local = true;
		this.self = false;
	}
	
	/**
	 * Constructor of <code>AttackingTask</code>.
	 * @param game The <code>Game</code> instance.
	 * @param attackerID The ID number of the attacker.
	 * @param targetID The ID number of the target.
	 * @param delta The delta HP value.
	 * @param self True if this is echoed back for the locally controlled player.
	 */
	public AttackTask(Game game, int attackerID, int targetID, int delta, boolean self) {
		super(ETask.Attack, game);
		this.attackerID = attackerID;
		this.targetID = targetID;
		this.delta = delta;
		this.local = false;
		this.self = self;
	}

	@Override
	public void execute() {
            try {
                // Step 1.
                CharacterEntity attackerEntity = (CharacterEntity) EntityManager.getInstance().getEntity(this.attackerID);
                View attacker = (View) ViewManager.getInstance().getView(attackerEntity);
                CharacterEntity targetEntity = (CharacterEntity) EntityManager.getInstance().getEntity(this.targetID);
                View target = (View) ViewManager.getInstance().getView(targetEntity);
                // Step 2.
                targetEntity.addHP(this.delta);
                if(this.delta > 0) {
                    if (targetEntity.isAlive()) {
                        targetEntity.setState(EState.Hit);
                        ViewManager.getInstance().markForUpdate(targetEntity);
                    }
                    targetEntity.resetForce();
                    targetEntity.resetVelocity();
                }
                if (targetEntity.getHP() <= 0) {
                    targetEntity.setState(EState.Death);
                    ViewManager.getInstance().markForUpdate(targetEntity);
                    if (targetEntity instanceof SnowmanEntity) {
                        InputManager.getInstance().getController(targetEntity).setActive(false);
                    }
                    if (targetEntity.isCarrying()) {
                        TaskManager.getInstance().createTask(ETask.Attach, targetEntity.getFlag().getID(), 0, false);
                        targetEntity.setFlag(null);
                    }
                }
                
                if (!this.self) {
                    // Step 3.
                    attackerEntity.setDestination(null);
                    attackerEntity.resetVelocity();
                    TaskManager.getInstance().createTask(ETask.CreateSnowball, attackerEntity, targetEntity);
                    attackerEntity.setState(EState.Attacking);
                    ViewManager.getInstance().markForUpdate(attackerEntity);
                    // Step 4.
                    Vector3f attackerPosition = attacker.getLocalTranslation().clone();
                    Vector3f targetPosition = target.getLocalTranslation().clone();
                    attacker.getLocalRotation().lookAt(targetPosition.subtract(attackerPosition).normalizeLocal(), Vector3f.UNIT_Y);
                    // Step 5.
                    attackerEntity.setTarget(targetEntity);
                    // Step 6.
                    if (this.local) {
                        this.game.getClient().send(ClientMessages.createAttackPkt(this.targetID, attackerPosition.x, attackerPosition.z));
                    }
                } 
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
    }

	@Override
	public boolean equals(Object object) {
		if(super.equals(object)) {
			if(object instanceof AttackTask) {
				AttackTask given = (AttackTask)object;
				return ((given.attackerID == this.attackerID) && (given.targetID == this.targetID) &&
                                        (given.self == this.self));
			}
		}
		return false;
	}
}
