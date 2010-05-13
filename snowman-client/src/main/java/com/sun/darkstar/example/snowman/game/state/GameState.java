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
package com.sun.darkstar.example.snowman.game.state;

import com.jme.renderer.pass.RenderPass;
import com.jmex.game.state.BasicGameState;
import com.sun.darkstar.example.snowman.common.world.World;
import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;
import com.sun.darkstar.example.snowman.interfaces.IGameState;

/**
 * <code>GameState</code> extends <code>BasicGameState</code> and implements
 * <code>IGameState</code> to define the basic abstraction of a particular
 * state of game.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-02-2008 12:29 EST
 * @version Modified date: 07-17-2008 12:02 EST
 */
public abstract class GameState extends BasicGameState implements IGameState {
	/**
	 * The <code>EGameState</code> enumeration.
	 */
	protected final EGameState enumn;
	/**
	 * The <code>Game</code> instance.
	 */
	protected final Game game;
	/**
	 * The flag indicates the activeness of this game state.
	 */
	protected boolean active;
	/**
	 * The flag indicates if this game state is initialized.
	 */
	protected boolean initialized;
	/**
	 * The <code>World</code> of this game state.
	 */
	protected World world;

	/**
	 * Constructor of <code>GameState</code>.
	 * @param enumn The <code>EGameState</code> enumeration.
	 */
	public GameState(EGameState enumn, Game game) {
		super(enumn.toString());
		this.enumn = enumn;
		this.game = game;
	}

	@Override
	public void initialize() {
		this.buildRootPass();
		this.initializeWorld();
		this.initializeState();
		this.rootNode.attachChild(this.world);
		this.initialized = true;
	}
	
	/**
	 * Build the root node render pass.
	 */
	private void buildRootPass() {
		RenderPass rootPass = new RenderPass();
		rootPass.add(this.rootNode);
		this.game.getPassManager().add(rootPass);
	}
	
	/**
	 * Initialize the world of the game state.
	 */
	protected abstract void initializeWorld();

	/**
	 * Initialize the actual details of the game state.
	 */
	protected abstract void initializeState();
	
	@Override
	public void update(float interpolation) {
		super.update(interpolation);
		if(this.active) this.updateState(interpolation);
	}
	
	/**
	 * Update the actual details of the game state.
	 * @param interpolation The frame rate interpolation value.
	 */
	protected abstract void updateState(float interpolation);

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public EGameState getType() {
		return this.enumn;
	}
	
	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public boolean isInitialized() {
		return this.initialized;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public void cleanup() {
		this.rootNode.detachAllChildren();
	}
}
