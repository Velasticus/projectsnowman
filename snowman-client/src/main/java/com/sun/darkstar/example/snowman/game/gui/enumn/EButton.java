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
package com.sun.darkstar.example.snowman.game.gui.enumn;

import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;

/**
 * <code>EButton</code> defines the enumerations of all buttons that are used
 * in all game scenes.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-09-2008 15:49 EST
 * @version Modified date: 07-09-2008 15:52 EST
 */
public enum EButton {
	/**
	 * The play button in login scene.
	 */
	Connect(EGameState.LoginState);
	
	/**
	 * The <code>EGameState</code> enumeration.
	 */
	private final EGameState state;
	
	/**
	 * Constructor of <code>EButton</code>.
	 * @param state The <code>EGameState</code> enumeration.
	 */
	private EButton(EGameState state) {
		this.state = state;
	}
	
	/**
	 * Retrieve the game state this button is used in.
	 * @return The <code>EGameState</code> enumeration.
	 */
	public EGameState getState() {
		return this.state;
	}
}
