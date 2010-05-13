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
package com.sun.darkstar.example.snowman.client.handler;

import java.net.PasswordAuthentication;

import com.sun.darkstar.example.snowman.client.handler.message.MessageListener;
import com.sun.darkstar.example.snowman.client.handler.message.MessageProcessor;
import com.sun.darkstar.example.snowman.exception.MissingComponentException;
import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.interfaces.IComponent;
import com.sun.darkstar.example.snowman.unit.Component;

/**
 * <code>ClientHandler</code> is a composed <code>Component</code> that is
 * responsible for handling all the messages received from the server.
 * <p>
 * <code>ClientHandler</code> is composed by a single <code>MessageListener</code>
 * and a single <code>MessageProcessor</code>. The sub-components are created
 * at construction time since they define <code>ClientHandler</code>.
 * <p>
 * <code>ClientHandler</code> needs to be connected with <code>Game</code> before
 * initialization.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 05-28-2008 16:40 EST
 * @version Modified date: 08-11-2008 16:55 EST
 */
public class ClientHandler extends Component {
	/**
	 * The single <code>MessageListener</code> instance.
	 */
	private final MessageListener listener;
	/**
	 * The <code>MessageProcessor</code> instance.
	 */
	private final MessageProcessor processor;
	/**
	 * The <code>Game</code> instance.
	 */
	private Game game;
	/**
	 * The <code>PasswordAuthentication</code> instance.
	 */
	private PasswordAuthentication authentication;
	/**
	 * The expected number of MOBs.
	 */
	private int expected;
	
	/**
	 * Constructor of <code>ClientHandler</code>.
	 */
	public ClientHandler() {
		this.listener = new MessageListener(this);
		this.processor = new MessageProcessor(this);
	}

	@Override
	public boolean validate() throws MissingComponentException {
		if(this.game == null) {
			throw new MissingComponentException(Game.class.toString());
		}
		return true;
	}

	@Override
	public void initialize() {
		this.expected = 0;
	}

	@Override
	public void connect(IComponent component) {
		if(component instanceof Game) {
			this.game = (Game)component;
		}
	}
	
	/**
	 * Create the authentication object for authentication purpose.
	 * @param username The <code>String</code> user name.
	 * @param password The <code>String</code> password.
	 */
	public void authenticate(String username, String password) {
		this.authentication = new PasswordAuthentication(username ,password.toCharArray());
	}
	
	/**
	 * Increment the expected number of entities.
	 */
	public void incrementExpected() {
		this.expected++;
	}
	
	/**
	 * Retrieve the <code>Game</code> <code>Component</code>.
	 * @return The <code>Game</code> instance.
	 */
	public Game getGame() {
		return this.game;
	}
	
	/**
	 * Retrieve the <code>MessageListener</code> sub-component.
	 * @return The <code>MessageListener</code> instance.
	 */
	public MessageListener getListener() {
		return this.listener;
	}
	
	/**
	 * Retrieve the <code>MessageProcessor</code> sub-component.
	 * @return The <code>MessageProcessor</code> instance.
	 */
	public MessageProcessor getProcessor() {
		return this.processor;
	}
	
	/**
	 * Retrieve the <code>PasswordAuthentication</code> instance.
	 * @return The <code>PasswordAuthentication</code> instance.
	 */
	public PasswordAuthentication getAuthentication() {
		return this.authentication;
	}
	
	/**
	 * Retrieve the expected number of added entities.
	 * @return The expected number of added entities.
	 */
	public int getExpected() {
		return this.expected;
	}
}
