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
package com.sun.darkstar.example.snowman.client.handler.message;

import com.sun.darkstar.example.snowman.client.handler.ClientHandler;
import com.sun.darkstar.example.snowman.common.protocol.enumn.EEndState;
import com.sun.darkstar.example.snowman.common.protocol.enumn.EMOBType;
import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;
import com.sun.darkstar.example.snowman.common.protocol.processor.IClientProcessor;
import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;
import com.sun.darkstar.example.snowman.game.state.scene.BattleState;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;
import com.sun.darkstar.example.snowman.game.task.util.TaskManager;

/**
 * <code>MessageProcessor</code> is a processing unit responsible for
 * processing all the messages received by the <code>MessageListener</code>.
 * <p>
 * <code>MessageProcessor</code> generates <code>ITask</code> based on
 * the received messages and buffers these <code>ITask</code> inside
 * <code>TaskManager</code> for processing.
 * <p>
 * <code>MessageProcessor</code> is created and attached to its parent
 * <code>ClientHandler</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 05-27-2008 11:57 EST
 * @version Modified date: 11-26-2008 21:10 EST
 */
public class MessageProcessor implements IClientProcessor {
	/**
	 * The <code>ClientHandler</code> this processor is attached to.
	 */
	private final ClientHandler handler;
	/**
	 * The ID number of the local controlled player.
	 */
	private int myID;

	/**
	 * Constructor of <code>MessageProcessor</code>.
	 * @param handler The <code>ClientHandler</code> this processor is attached to.
	 */
	public MessageProcessor(ClientHandler handler) {
		this.handler = handler;
	}

	@Override
	public void ready() {
		TaskManager.getInstance().createTask(ETask.Ready);
	}

	@Override
	public void newGame(int myID, String mapname) {
		this.myID = myID;
		TaskManager.getInstance().createTask(ETask.GameState, EGameState.BattleState);
	}

	@Override
	public void startGame() {
		TaskManager.getInstance().createTask(ETask.StartGame);
	}

	@Override
	public void endGame(EEndState endState) {
            TaskManager.getInstance().createTask(ETask.GameState, EGameState.EndState, endState);
	}

	@Override
	public void addMOB(int objectID, float x, float y, EMOBType objType, ETeamColor team, String mobName) {
		this.handler.incrementExpected();
		TaskManager.getInstance().createTask(ETask.AddMOB, objectID, objType, team, x, y, mobName, (objectID == this.myID));
	}

	@Override
	public void moveMOB(int objectID, float startx, float starty, float endx, float endy) {
		if(objectID == this.myID) return;
		TaskManager.getInstance().createTask(ETask.MoveCharacter, objectID, startx, starty, endx, endy);
	}

	@Override
	public void removeMOB(int objectID) {
		TaskManager.getInstance().createTask(ETask.Remove, objectID);
	}

	@Override
	public void stopMOB(int objectID, float x, float y) {
		TaskManager.getInstance().createTask(ETask.Correction, objectID, x, y);
	}

	@Override
	public void attachObject(int sourceID, int targetID) {
		TaskManager.getInstance().createTask(ETask.Attach, sourceID, targetID, false);
	}

	@Override
	public void attacked(int sourceID, int targetID, int hp) {
		TaskManager.getInstance().createTask(ETask.Attack, sourceID, targetID, hp, (sourceID == this.myID));
	}

	@Override
	public void respawn(int objectID, float x, float y) {
		TaskManager.getInstance().createTask(ETask.Respawn, objectID, x, y, (objectID == this.myID));
	}
	
	@Override
	public void chatMessage(int sourceID, String message) {
            if(sourceID != this.myID) {
                TaskManager.getInstance().createTask(ETask.Chat, sourceID, message, false);
            }
	}

	/**
	 * Retrieve the ID number of this client. 
	 * @return The <code>Integer</code> ID number.
	 */
	public int getID() {
		return this.myID;
	}
}
