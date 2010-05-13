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

import com.jme.input.MouseInput;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.sun.darkstar.example.snowman.ClientApplication;
import com.sun.darkstar.example.snowman.common.entity.enumn.ECursorState;
import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity;
import com.sun.darkstar.example.snowman.common.entity.view.StaticView;
import com.sun.darkstar.example.snowman.common.interfaces.IEntity;
import com.sun.darkstar.example.snowman.common.util.CollisionManager;
import com.sun.darkstar.example.snowman.common.util.SingletonRegistry;
import com.sun.darkstar.example.snowman.common.util.enumn.EStats;
import com.sun.darkstar.example.snowman.common.world.World;
import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.game.entity.scene.CharacterEntity;
import com.sun.darkstar.example.snowman.game.entity.scene.SnowmanEntity;
import com.sun.darkstar.example.snowman.game.entity.view.scene.CharacterView;
import com.sun.darkstar.example.snowman.game.entity.view.scene.FlagView;
import com.sun.darkstar.example.snowman.game.entity.view.util.ViewManager;
import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;
import com.sun.darkstar.example.snowman.game.task.RealTimeTask;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;

/**
 * <code>UpdateStateTask</code> extends <code>RealTimeTask</code> to update
 * all the corresponding values associated with mouse position.
 * <p>
 * <code>UpdateStateTask</code> execution logic:
 * 1. Create a <code>Ray</code> goes into the screen based on mouse screen
 * coordinates.
 * 2. If the mouse is intersecting a <code>StaticView</code>:
 *    1. Change the <code>SnowmanEntity</code> state to idle.
 *    2. Change the mouse cursor to walking cursor.
 * 3. If the mouse is intersecting a <code>SnowmanView</code>:
 *    1. Check if the target is in range.
 *    2. Check if there is anything blocking the target.
 *    3. Change the <code>SnowmanEntity</code> state to targeting.
 *    4. Change the mouse cursor to targeting cursor.
 * 4. If the mouse is intersecting a <code>DynamicView</code>:
 *    1. Change the <code>SnowmanEntity</code> state to grabbing.
 *    2. Change the mouse cursor to grabbing cursor.
 * <p>
 * <code>UpdateStateTask</code> does not have detailed 'equal' comparison.
 * All instances of <code>UpdateStateTask</code> are considered 'equal'.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-18-2008 11:36 EST
 * @version Modified date: 08-12-2008 11:45 EST
 */
public class UpdateCursorStateTask extends RealTimeTask {
	/**
	 * The <code>SnowmanEntity</code> instance.
	 */
	private final SnowmanEntity snowman;
	/**
	 * The new x screen coordinate of the mouse.
	 */
	private final int x;
	/**
	 * The new y screen coordinate of the mouse.
	 */
	private final int y;

	/**
	 * Constructor of <code>UpdateStateTask</code>.
	 * @param game The <code>Game</code> instance.
	 * @param snowman The <code>SnowmanEntity</code> to be updated.
	 * @param x The new x screen coordinate of the mouse.
	 * @param y The new y screen coordinate of the mouse.
	 */
	public UpdateCursorStateTask(Game game, SnowmanEntity snowman, int x, int y) {
		super(ETask.UpdateCursorState, game);
		this.snowman = snowman;
		this.x = x;
		this.y = y;
	}

	@Override
	public void execute() {
		DisplaySystem display = DisplaySystem.getDisplaySystem();
		CollisionManager collisionManager = SingletonRegistry.getCollisionManager();
		Vector3f worldCoords = new Vector3f();
		Vector3f camLocation = display.getRenderer().getCamera().getLocation().clone();
		display.getWorldCoordinates(new Vector2f(this.x, this.y), 1, worldCoords);
		Ray ray = new Ray();
		ray.setOrigin(camLocation);
		ray.setDirection(worldCoords.subtractLocal(camLocation).normalizeLocal());
		World world = this.game.getGameState(EGameState.BattleState).getWorld();
		Spatial result = collisionManager.getIntersectObject(ray, world, StaticView.class, false);
		if(result != null) {
			this.snowman.setCursorState(ECursorState.TryingToMove);
			MouseInput.get().setHardwareCursor(ClientApplication.class.getClassLoader().getResource(ECursorState.TryingToMove.getIconLocation()));
			return;
		}
		result = collisionManager.getIntersectObject(ray, world, CharacterView.class, false);
		if(result != null) {
			CharacterView view = (CharacterView)result;
			if(view.getEntity() == this.snowman || !this.validateTeam(view.getEntity())) return;
			if(this.validateDeath((CharacterView)result) && this.validateAttackRange(result) && this.validateBlocking(result)) {
				this.snowman.setCursorState(ECursorState.Targeting);
				this.snowman.setTarget((CharacterEntity)view.getEntity());
				MouseInput.get().setHardwareCursor(ClientApplication.class.getClassLoader().getResource(ECursorState.Targeting.getIconLocation()));
			}
			return;
		}
		result = collisionManager.getIntersectObject(ray, world, FlagView.class, false);
		if(result != null) {
			FlagView view = (FlagView)result;
			if(this.validateGrabRange(result) && this.validateTeam(view.getEntity())) {
				this.snowman.setCursorState(ECursorState.TryingToGrab);
				this.snowman.setTarget(view.getEntity());
				MouseInput.get().setHardwareCursor(ClientApplication.class.getClassLoader().getResource(ECursorState.TryingToGrab.getIconLocation()));
			}
			return;
		}
                
        this.snowman.setCursorState(ECursorState.Invalid);
		MouseInput.get().setHardwareCursor(ClientApplication.class.getClassLoader().getResource(ECursorState.Invalid.getIconLocation()));
	}

	/**
	 * Validate if the given entity can be targeted.
	 * @param entity The <code>IEntity</code> to be validated.
	 * @return True of the entity can be targeted. False otherwise.
	 */
	private boolean validateTeam(IEntity entity) {
		switch(entity.getEnumn()) {
		case SnowmanDistributedBlue: return (this.snowman.getEnumn() != EEntity.SnowmanLocalBlue);
		case SnowmanDistributedRed: return (this.snowman.getEnumn() != EEntity.SnowmanLocalRed);
		case FlagBlue: return (this.snowman.getEnumn() != EEntity.SnowmanLocalBlue);
		case FlagRed: return (this.snowman.getEnumn() != EEntity.SnowmanLocalRed);
		}
		return false;
	}

	/**
	 * Validate if the given target is still alive.
	 * @param view The <code>CharacterView</code> instance.
	 * @return True if the target is still alive. False otherwise.
	 */
	private boolean validateDeath(CharacterView view) {
		return ((CharacterEntity)view.getEntity()).isAlive();
	}

	/**
	 * Validate if the given target is within attack range.
	 * @param target The <code>Spatial</code> target to check.
	 * @return True if the given target is in range. False otherwise.
	 */
	private boolean validateAttackRange(Spatial target) {
		float range = SingletonRegistry.getHPConverter().convertRange(this.snowman.getHP());
		float distance = this.getPlanarDistance((Spatial)ViewManager.getInstance().getView(this.snowman), target);
		if(range * range >= distance) return true;
		return false;
	}

	/**
	 * Validate if the given target is within grab range.
	 * @param target The <code>Spatial</code> target to check.
	 * @return True if the given target is in range. False otherwise.
	 */
	private boolean validateGrabRange(Spatial target) {
		float distance = this.getPlanarDistance((Spatial)ViewManager.getInstance().getView(this.snowman), target);
		if(EStats.GrabRange.getValue() * EStats.GrabRange.getValue() >= distance) return true;
		return false;
	}

	/**
	 * Validate if the given target is blocked by a static entity.
	 * @param target The <code>Spatial</code> target to check.
	 * @return True if the target is valid. False otherwise.
	 */
	private boolean validateBlocking(Spatial target) {
		Spatial snowman = (Spatial) ViewManager.getInstance().getView(this.snowman);
		Vector3f start = snowman.getLocalTranslation();
		Vector3f end = target.getLocalTranslation();
		World world = this.game.getGameState(EGameState.BattleState).getWorld();
		return SingletonRegistry.getCollisionManager().validate(start.x, start.z, end.x, end.z, world.getStaticRoot());
	}

	/**
	 * Retrieve the planar distance squared between the given spatials.
	 * @param spatial The starting <code>Spatial</code>.
	 * @param target The targeting <code>Spatial</code>.
	 * @return The planar distance squared between the given spatials.
	 */
	private float getPlanarDistance(Spatial spatial, Spatial target) {
		Vector3f start = spatial.getLocalTranslation();
		Vector3f end = target.getLocalTranslation();
		float dx = start.x - end.x;
		float dz = start.z - end.z;
		return (dx * dx) + (dz * dz);
	}
}
