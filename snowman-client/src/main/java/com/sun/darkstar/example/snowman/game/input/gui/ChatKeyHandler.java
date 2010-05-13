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

package com.sun.darkstar.example.snowman.game.input.gui;

import com.jme.input.KeyInput;
import com.jme.input.KeyInputListener;
import com.sun.darkstar.example.snowman.client.Client;
import com.sun.darkstar.example.snowman.common.protocol.messages.ClientMessages;
import com.sun.darkstar.example.snowman.game.gui.scene.ChatGUI;
import com.sun.darkstar.example.snowman.game.task.util.TaskManager;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;

/**
 * <code>ChatKeyHandler</code>
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 11-12-2008 15:28 EST
 * @version Modified date: 11-19-2008 10:49 EST
 */
public class ChatKeyHandler implements KeyInputListener {

    /**
     * The <code>ChatGUI</code> instance.
     */
    private final ChatGUI gui;
    /**
     * The <code>Client</code> instance.
     */
    private final Client client;

    /**
     * Constructor of <code>ChatKeyHandler</code>
     * @param gui The <code>ChatGUI</code> instance.
     * @param client The <code>Client</code> instance.
     */
    public ChatKeyHandler(ChatGUI gui, Client client) {
        this.gui = gui;
        this.client = client;
    }

    @Override
    public void onKey(char character, int keyCode, boolean pressed) {
        if (keyCode == KeyInput.KEY_RETURN && pressed) {
            String message = this.gui.getChatMessage().trim();
            if (message.length() > 0) {
                TaskManager.getInstance().createTask(ETask.Chat, client.getHandler().getProcessor().getID(), message, true);
            }
            this.gui.cleanupChatInput();
            this.gui.setInputEnabled(!this.gui.isChatInputEnabled());
        }
    }
}
