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

import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.sun.darkstar.example.snowman.common.entity.enumn.EState;
import com.sun.darkstar.example.snowman.common.physics.enumn.EForce;
import com.sun.darkstar.example.snowman.common.protocol.messages.ClientMessages;
import com.sun.darkstar.example.snowman.common.util.CollisionManager;
import com.sun.darkstar.example.snowman.common.util.SingletonRegistry;
import com.sun.darkstar.example.snowman.common.world.World;
import com.sun.darkstar.example.snowman.exception.ObjectNotFoundException;
import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.game.entity.scene.CharacterEntity;
import com.sun.darkstar.example.snowman.game.entity.util.EntityManager;
import com.sun.darkstar.example.snowman.game.entity.view.util.ViewManager;
import com.sun.darkstar.example.snowman.game.physics.util.PhysicsManager;
import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;
import com.sun.darkstar.example.snowman.game.task.RealTimeTask;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;

/**
 * <code>MoveCharacterTask</code> extends <code>RealTimeTask</code> to
 * set the destination of the user controlled <code>CharacterEntity</code>.
 * <p>
 * <code>MoveCharacterTask</code> execution logic:
 * 1. Reset the velocity of the character.
 * 2. Find the click position based on screen coordinates.
 * 3. Find and set the valid destination based on clicked position.
 * 4. Store the destination in the character.
 * 5. Send server a 'moveme' packet.
 * 6. Rotate the character to face the destination.
 * 7. Add a movement force to the character based on the direction.
 * 8. Mark the character with <code>PhysicsManager</code> for update.
 * 9. Set the character state to moving and mark for update.
 * <p>
 * Two <code>MoveCharacterTask</code> are considered 'equal' if and only
 * if both of them are setting on the same <code>CharacterEntity</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-21-2008 15:58 EST
 * @version Modified date: 07-24-2008 16:33 EST
 */
public class MoveCharacterTask extends RealTimeTask {
	/**
	 * The <code>CharacterEntity</code> to be set.
	 */
	private CharacterEntity character;
	/**
	 * The flag indicates if the entity being set is locally controlled.
	 */
	private boolean local;
	/**
	 * The x coordinate of the clicked screen position.
	 */
	private int x;
	/**
	 * The y coordinate of the clicked screen position.
	 */
	private int y;
	/**
	 * The x coordinate of the starting position.
	 */
	private float startX;
	/**
	 * The z coordinate of the starting position.
	 */
	private float startZ;
	/**
	 * The x coordinate of the destination position.
	 */
	private float endX;
	/**
	 * The z coordinate of the destination position.
	 */
	private float endZ;

	/**
	 * Constructor of <code>SetDestinationTask</code>.
	 * @param game The <code>Game</code> instance.
	 * @param character The <code>CharacterEntity</code> to be set.
	 * @param x The x coordinate of the clicked screen position.
	 * @param y The y coordinate of the clicked screen position.
	 */
	public MoveCharacterTask(Game game, CharacterEntity character, int x, int y) {
		super(ETask.MoveCharacter, game);
		this.character = character;
		this.local = true;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Constructor of <code>SetDestinationTask</code>.
	 * @param game The <code>Game</code> instance.
	 * @param id The ID number of the character entity.
	 * @param startX The x coordinate of the starting position.
	 * @param startZ The z coordinate of the starting position.
	 * @param endX The x coordinate of the destination position.
	 * @param endZ The z coordinate of the destination position.
	 */
	public MoveCharacterTask(Game game, int id, float startX, float startZ, float endX, float endZ) {
		super(ETask.MoveCharacter, game);
		try {
			this.character = (CharacterEntity)EntityManager.getInstance().getEntity(id);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		}
		this.local = false;
		this.startX = startX;
		this.startZ = startZ;
		this.endX = endX;
		this.endZ = endZ;
	}

	@Override
	public void execute() {
		if (this.character == null) return;
		try {
			this.character.resetVelocity();
			this.character.resetForce();
			Vector3f destination = this.getDestination();
			if (destination != null) {
				this.character.setDestination(destination);
				Spatial view = (Spatial)ViewManager.getInstance().getView(this.character);
				if(!this.local) {
					view.getLocalTranslation().x = this.startX;
					view.getLocalTranslation().z = this.startZ;
				}
				destination.y = 0;
				Vector3f lcoal = view.getLocalTranslation().clone();
				lcoal.y = 0;
				Vector3f direction = destination.subtract(lcoal);
				direction.y = 0;
				direction.normalizeLocal();
				view.getLocalRotation().lookAt(direction, Vector3f.UNIT_Y);
				Vector3f force = direction.multLocal(EForce.Movement.getMagnitude());
				this.character.addForce(force);
				PhysicsManager.getInstance().markForUpdate(this.character);
				// Step 9.
				this.character.setState(EState.Moving);
				ViewManager.getInstance().markForUpdate(this.character);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Vector3f getDestination() {
		if(this.local) {
			DisplaySystem display = DisplaySystem.getDisplaySystem();
			CollisionManager collisionManager = SingletonRegistry.getCollisionManager();
			Ray ray = new Ray();
			display.getPickRay(new Vector2f(this.x, this.y), false, ray);
			World world = this.game.getGameState(EGameState.BattleState).getWorld();
			Vector3f click = collisionManager.getIntersection(ray, world, new Vector3f(), true);
			if(click == null) return null;
			try {
				Spatial view = (Spatial)ViewManager.getInstance().getView(this.character);
				Vector3f local = view.getLocalTranslation().clone();
				this.game.getClient().send(ClientMessages.createMoveMePkt(local.x, local.z, click.x, click.z));
				return collisionManager.getDestination(local.x, local.z, click.x, click.z, world.getStaticRoot());
			} catch (ObjectNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return new Vector3f(this.endX, 0, this.endZ);
		}
	}
	
	@Override
	public boolean equals(Object object) {
		if(super.equals(object)) {
			if(object instanceof MoveCharacterTask) {
				MoveCharacterTask given = (MoveCharacterTask)object;
				if (given.character != null)
					return given.character.equals(this.character);
				else
					return this.character == null;
			}
		}
		return false;
	}
}
