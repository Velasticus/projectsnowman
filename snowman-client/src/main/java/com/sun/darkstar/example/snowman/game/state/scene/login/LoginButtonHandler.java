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

package com.sun.darkstar.example.snowman.game.state.scene.login;

import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;

import com.sun.darkstar.example.snowman.game.gui.enumn.EButton;
import com.sun.darkstar.example.snowman.game.gui.scene.LoginGUI;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;
import com.sun.darkstar.example.snowman.game.task.util.TaskManager;

/**
 * <code>LoginButtonHandler</code> is responsible for monitoring and processing
 * button pressed events in login scene.
 * 
 * @author Yi Wang (Neakor)
 * @author Owen Kellett
 * @version Creation date: 07-09-2008 15:46 EST
 */
public class LoginButtonHandler implements IButtonPressedListener {
    /**
     * The <code>LoginGUI</code> instance.
     */
    private final LoginGUI gui;

    /**
     * Constructor of <code>LoginButtonHandler</code>.
     * @param gui The <code>LoginGUI</code> instance.
     */
    public LoginButtonHandler(LoginGUI gui) {
        this.gui = gui;
    }

    @Override
    public void buttonPressed(ButtonPressedEvent e) {
        if (e.getTrigger().getText().equalsIgnoreCase(EButton.Connect.toString())) {
            TaskManager.getInstance().createTask(ETask.Authenticate, 
                                                 this.gui.getUsername(), 
                                                 this.gui.getPassword(),
                                                 this.gui.getHost(),
                                                 this.gui.getPort());
        }
    }
}
