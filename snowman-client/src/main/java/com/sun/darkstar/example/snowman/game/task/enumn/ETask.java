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
package com.sun.darkstar.example.snowman.game.task.enumn;

/**
 * <code>ETask</code> defines the enumerations of all <code>ITask</code>.
 * <p>
 * The enumeration of an <code>ITask</code> implies the execution logic type
 * of the <code>ITask</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-03-2008 11:07 EST
 * @version Modified date: 08-14-2008 16:23 EST
 */
public enum ETask {
	/**
	 * The task used to authenticate the user inputs with the server.
	 */
	Authenticate(ETaskType.RealTime),
	/**
	 * The task used to reset the login state after login attempt failed.
	 */
	ResetLogin(ETaskType.RealTime),
        /**
	 * The task used to notify of a login success
	 */
	LoginSuccess(ETaskType.RealTime),
	/**
	 * The task used to change game state.
	 */
	GameState(ETaskType.RealTime),
	/**
	 * The task used to create a new game.
	 */
	NewGame(ETaskType.RealTime),
	/**
	 * The task used to add a MOB.
	 */
	AddMOB(ETaskType.RealTime),
	/**
	 * The task used to initialize chase camera.
	 */
	Ready(ETaskType.RealTime),
	/**
	 * The task used to start the battle.
	 */
	StartGame(ETaskType.RealTime),
	/**
	 * The task used to update all value associated with mouse position.
	 */
	UpdateCursorState(ETaskType.RealTime),
	/**
	 * The task used to initiate the movement of a snowman.
	 */
	MoveCharacter(ETaskType.RealTime),
	/**
	 * The task used to start the attacking process.
	 */
	Attack(ETaskType.RealTime),
	/**
	 * The task used to attach entity.
	 */
	Attach(ETaskType.RealTime),
	/**
	 * The task used to create snow balls.
	 */
	CreateSnowball(ETaskType.Certified),
	/**
	 * The task used to update the motion of snow balls.
	 */
	MoveSnowball(ETaskType.RealTime),
	/**
	 * The task used to correct character position.
	 */
	Correction(ETaskType.RealTime),
	/**
	 * The task used to respawn a character.
	 */
	Respawn(ETaskType.RealTime),
	/**
	 * The task used to remove a MOB.
	 */
	Remove(ETaskType.RealTime),
        /**
         * The task used to score a win
         */
        Score(ETaskType.RealTime),
        /**
         * The task used to process a chat message
         */
        Chat(ETaskType.RealTime);
	
	/**
	 * The <code>ETaskType</code> enumeration.
	 */
	private final ETaskType type;
	
	/**
	 * Constructor of <code>ETask</code>.
	 * @param type The <code>ETaskType</code> enumeration.
	 */
	private ETask(ETaskType type) {
		this.type = type;
	}
	
	/**
	 * Retrieve the type of this task.
	 * @return The <code>ETaskType</code> enumeration.
	 */
	public ETaskType getType() {
		return this.type;
	}
	
	/**
	 * <code>ETaskType</code> defines all types of <code>ITask</code> managed by the
	 * <code>TaskManager</code>.
	 * 
	 * @author Yi Wang (Neakor)
	 * @author Tim Poliquin (Weenahmen)
	 * @version Creation date: 06-02-2008 15:48 EST
	 * @version Modified date: 06-02-2008 15:50 EST
	 */
	public enum ETaskType {
		/**
		 * The real-time task type.
		 */
		RealTime,
		/**
		 * The certified task type.
		 */
		Certified
	}
}
