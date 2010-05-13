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

import java.net.PasswordAuthentication;
import java.nio.ByteBuffer;

import com.sun.darkstar.example.snowman.client.handler.ClientHandler;
import com.sun.darkstar.example.snowman.common.util.SingletonRegistry;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;
import com.sun.darkstar.example.snowman.game.task.util.TaskManager;
import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.client.simple.SimpleClientListener;

/**
 * <code>MessageListener</code> is a listener which monitors packets received
 * from the the <code>Server</code>.
 * <p>
 * <code>MessageListener</code> passes received packets to <code>MessageProcessor</code>
 * for logic processing.
 * <p>
 * <code>MessageListener</code> is created and attached to its parent
 * <code>ClientHandler</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 05-23-2008 15:24 EST
 * @version Modified date: 07-14-2008 16:20 EST
 */
public class MessageListener implements SimpleClientListener, ClientChannelListener {
	/**
	 * The <code>ClientHandler</code> this listener is attached to.
	 */
	private final ClientHandler handler;
	
	/**
	 * Constructor of <code>MessageListener</code>.
	 * @param handler The <code>ClientHandler</code> this listener is attached to.
	 */
	public MessageListener(ClientHandler handler) {
		this.handler = handler;
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return this.handler.getAuthentication();
	}

	@Override
	public void loggedIn() {
            TaskManager.getInstance().createTask(ETask.LoginSuccess);
        }

	@Override
	public void loginFailed(String reason) {
		TaskManager.getInstance().createTask(ETask.ResetLogin, reason);
	}

	@Override
	public void disconnected(boolean graceful, String reason) {
            this.handler.getGame().finish();
	}

	@Override
	public ClientChannelListener joinedChannel(ClientChannel channel) {
		return this;
	}

	@Override
	public void receivedMessage(ByteBuffer message) {
		SingletonRegistry.getMessageHandler().parseClientPacket(message, this.handler.getProcessor());
	}

	@Override
	public void reconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reconnecting() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leftChannel(ClientChannel channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receivedMessage(ClientChannel channel, ByteBuffer message) {
		SingletonRegistry.getMessageHandler().parseClientPacket(message, this.handler.getProcessor());
	}
}
