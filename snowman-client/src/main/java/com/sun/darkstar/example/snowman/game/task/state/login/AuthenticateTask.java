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
package com.sun.darkstar.example.snowman.game.task.state.login;

import java.util.Properties;

import com.jmex.game.state.GameStateManager;
import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.game.gui.scene.LoginGUI;
import com.sun.darkstar.example.snowman.game.input.util.InputManager;
import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;
import com.sun.darkstar.example.snowman.game.state.scene.LoginState;
import com.sun.darkstar.example.snowman.game.task.RealTimeTask;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;

/**
 * <code>AuthenticateTask</code> extends <code>RealTimeTask</code> to login the
 * user into the server application.
 * <p>
 * <code>AuthenticateTask</code> execution logic:
 * 1. Deactivate all input.
 * 2. Updates the status label of <code>LoginGUI</code>.
 * 3. Change 'Play' button text to 'Please wait...'.
 * 4. Invoke <code>Client</code> to login to the server.
 * 5. Invoke <code>ClientHandler</code> to create <code>PasswordAuthentication</code>.
 * <p>
 * <code>AuthenticateTask</code> does not have a more detailed 'equals'
 * comparison. All <code>AuthenticateTask</code> are considered 'equal',
 * therefore, a newer version can always replace the older one.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-09-2008 17:02 EST
 * @version Modified date: 07-16-2008 11:33 EST
 */
public class AuthenticateTask extends RealTimeTask {
    /**
     * The <code>String</code> user name to login with.
     */
    private final String username;
    /**
     * The <code>String</code> password to login with.
     */
    private final String password;
    /**
     * The host to connect to
     */
    private final String host;
    /**
     * The port to connect to
     */
    private final String port;

    /**
     * Constructor of <code>AuthenticateTask</code>.
     * @param game The <code>Game</code> instance.
     * @param username The <code>String</code> user name to login with.
     * @param password The <code>String</code> password to login with.
     */
    public AuthenticateTask(Game game,
                            String username,
                            String password,
                            String host,
                            String port) {
        super(ETask.Authenticate, game);
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    @Override
    public void execute() {
        final LoginGUI gui = ((LoginState) GameStateManager.getInstance().getChild(EGameState.LoginState.toString())).getGUI();
        gui.setStatus(gui.getConnectingStatus());
        InputManager.getInstance().setInputActive(false);
        this.game.getClient().getHandler().authenticate(this.username, this.password);
        Properties properties = new Properties();
        properties.setProperty("host", host);
        properties.setProperty("port", port);
        this.game.getClient().login(properties);
    }
}
