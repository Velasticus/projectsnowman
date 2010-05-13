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
package com.sun.darkstar.example.snowman.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Properties;

import com.sun.darkstar.example.snowman.client.handler.ClientHandler;
import com.sun.darkstar.example.snowman.exception.MissingComponentException;
import com.sun.darkstar.example.snowman.interfaces.IComponent;
import com.sun.darkstar.example.snowman.unit.Component;

import com.sun.sgs.client.simple.SimpleClient;

/**
 * <code>Client</code> is a <code>Component</code> which represents the communication
 * protocol between the client application and the server application.
 * <p>
 * <code>Client</code> is responsible for handling outgoing messages from the <code>Game</code>
 * to the server.
 * <p>
 * <code>Client</code> needs to be connected with <code>ClientHandler</code> before
 * initialization in order to establish connection with the server.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 05-23-2008 14:52 EST
 * @version Modified date: 07-10-2008 17:12 EST
 */
public class Client extends Component{
	/**
	 * The <code>ClientHandler</code> <code>Component</code>.
	 */
	private ClientHandler handler;
	/**
	 * The <code>SimpleClient</code> instance.
	 */
	private SimpleClient connection;

	/**
	 * Constructor of <code>Client</code>.
	 */
	public Client() {}

	@Override
	public boolean validate() throws MissingComponentException {
		if(this.handler == null) {
			throw new MissingComponentException(ClientHandler.class.toString());
		}
		return true;
	}

	@Override
	public void initialize() {
		this.connection = new SimpleClient(this.handler.getListener());
	}

	@Override
	public void connect(IComponent component) {
		if(component instanceof ClientHandler) {
			this.handler = (ClientHandler)component;
		}
	}
	
	public void login(Properties properties) {
		try {
			this.connection.login(properties);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
        
        public void logout() {
            this.connection.logout(false);
        }
	
	public void send(ByteBuffer message) {
		try {
			this.connection.send(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieve the <code>ClientHandler</code> instance.
	 * @return The <code>ClientHandler</code> instance.
	 */
	public ClientHandler getHandler() {
		return this.handler;
	}
}
