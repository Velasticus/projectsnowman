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
package com.sun.darkstar.example.snowman.game.input;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.input.InputHandler;
import com.jme.math.FastMath;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.sun.darkstar.example.snowman.common.util.CollisionManager;
import com.sun.darkstar.example.snowman.common.util.SingletonRegistry;
import com.sun.darkstar.example.snowman.common.world.World;
import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;

/**
 * InputHandler handling camera interactions.
 * 
 * @author Joshua Slack
 */
public class SnowmanCameraHandler extends InputHandler {

	private static final Logger logger = Logger
			.getLogger(SnowmanCameraHandler.class.getName());

	private static final float MAX_AZIMUTH = FastMath.DEG_TO_RAD * 45f;
	private static final float MIN_AZIMUTH = FastMath.DEG_TO_RAD * -15f;
	private static final float MAX_ZOOM = 6;
	private static final float MIN_ZOOM = 2;

	private final SnowmanMouseHandler mouseLookHandler;
	private final SnowmanKeyHandler keyLookHandler;

	private final Camera camera;
	private float zoomDistance = 5;

	private final Ray checkRay = new Ray();
	private final Vector3f camSphereCoords = new Vector3f(1, 0,
			15 * FastMath.DEG_TO_RAD);
	private final Vector3f tempVec = new Vector3f();

	private final Node target;
	private final Vector3f targetLocation = new Vector3f();

	private final Vector3f targetOffset = new Vector3f();
	private final Vector3f distStore = new Vector3f();

	
	/**
	 * The <code>Game</code> instance.
	 */
	protected final Game game;

	public SnowmanCameraHandler(final Camera camera, final Node center,
			final Game game) {
		mouseLookHandler = new SnowmanMouseHandler(this);
		addToAttachedHandlers(mouseLookHandler);
		keyLookHandler = new SnowmanKeyHandler(this);
		addToAttachedHandlers(keyLookHandler);
		this.camera = camera;
		this.target = center;
		this.game = game;
	}

	@Override
	public void update(final float time) {
		try {
			super.update(time);
			updateCamera();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Input Handler caught!", ex);
		}
	}

	public SnowmanMouseHandler getMouseLookHandler() {
		return mouseLookHandler;
	}

	void doRotate(float factorH, float factorV) {
		// grab current spherical position of camera and increase angle
		camSphereCoords.y -= factorH;
		camSphereCoords.z -= factorV;
		if (camSphereCoords.z > MAX_AZIMUTH) {
			camSphereCoords.z = MAX_AZIMUTH;
		} else if (camSphereCoords.z < MIN_AZIMUTH) {
			camSphereCoords.z = MIN_AZIMUTH;
		}
	}

	void doZoom(float factor) {
		// grab current spherical position of camera and alter x
		zoomDistance -= factor * zoomDistance / 25f;
		if (zoomDistance < MIN_ZOOM) {
			zoomDistance = MIN_ZOOM;
		} else if (zoomDistance > MAX_ZOOM) {
			zoomDistance = MAX_ZOOM;
		}

	}

	/**
	 * Apply the current sphere coords to the camera and update our location.
	 */
	public void updateCamera() {
		targetLocation.set(target.getWorldTranslation()).addLocal(targetOffset);
		FastMath.sphericalToCartesian(camSphereCoords, tempVec);
		Vector3f direction = camera.getDirection().set(tempVec).negateLocal();

		// Keep camera from going underground. (ideally this would also check
		// scenery)
		checkRay.getOrigin().set(targetLocation);
		checkRay.getDirection().set(direction).negateLocal();

		CollisionManager collisionManager = SingletonRegistry
				.getCollisionManager();
		World world = this.game.getGameState(EGameState.BattleState).getWorld();
		distStore.zero();
		Vector3f pick1 = collisionManager.getIntersection(checkRay, world
				.getStaticRoot(), distStore, false);
		float actualZoomDistance = zoomDistance;
		if (pick1 != null) {
			float pLength = pick1.subtractLocal(targetLocation).length() - 1.0f;
			if (pLength > .5f && pLength < actualZoomDistance) {
				actualZoomDistance = pLength;
			}
		}

		Vector3f loc = tempVec.multLocal(actualZoomDistance).addLocal(
				targetLocation);
		Vector3f up = camera.getUp().set(0, 1, 0);
		Vector3f left = camera.getLeft().set(up).crossLocal(direction)
				.normalizeLocal();
		if (left.equals(Vector3f.ZERO)) {
			if (direction.x != 0) {
				left.set(direction.y, -direction.x, 0f);
			} else {
				left.set(0f, direction.z, -direction.y);
			}
		}
		up.set(direction).crossLocal(left).normalizeLocal();

		camera.getLeft().set(left.x, left.y, left.z);
		camera.getLocation().set(loc.x, loc.y, loc.z);
		camera.onFrameChange();
		camera.apply();
	}

	public void setTargetOffset(final Vector3f offset) {
		targetOffset.set(offset);
	}
	
	public void setAzimuth(float radians) {
		camSphereCoords.y = radians;
	}
}
