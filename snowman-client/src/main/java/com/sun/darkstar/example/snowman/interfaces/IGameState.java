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
package com.sun.darkstar.example.snowman.interfaces;

import com.sun.darkstar.example.snowman.common.world.World;
import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;

/**
 * <code>IGameState</code> defines the interface for all types of game states
 * that represent a particular stage of the game. It maintains and manages the
 * associated scene graph for the game stage.
 * <p>
 * <code>IGameState</code> is constructed by <code>Game</code> and initialized
 * by <code>ITask</code>. It is updated within the main update render loop every
 * frame. However, it does not provide any direct rendering functionalities.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-29-2008 15:35 EST
 * @version Modified date: 07-17-2008 12:06 EST
 */
public interface IGameState {

	/**
	 * Initialize the game state and its corresponding scene graph.
	 */
	public void initialize();
	
	/**
	 * Retrieve the type of this game state.
	 * @return The <code>EGameState</code> enumeration.
	 */
	public EGameState getType();
	
	/**
	 * Retrieve the world of this game state.
	 * @return The <code>World</code> data.
	 */
	public World getWorld();
	
	/**
	 * Check if this game state has been initialized.
	 * @return True if this game state has been initialized. False otherwise.
	 */
	public boolean isInitialized();
}
