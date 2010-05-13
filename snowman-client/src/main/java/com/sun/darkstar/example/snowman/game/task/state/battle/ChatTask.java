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
package com.sun.darkstar.example.snowman.game.task.state.battle;

import com.sun.darkstar.example.snowman.common.protocol.messages.ClientMessages;
import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.game.entity.scene.CharacterEntity;
import com.sun.darkstar.example.snowman.game.entity.util.EntityManager;
import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;
import com.sun.darkstar.example.snowman.game.state.scene.BattleState;
import com.sun.darkstar.example.snowman.game.task.RealTimeTask;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;
import com.sun.darkstar.example.snowman.exception.ObjectNotFoundException;

/**
 * <code>ChatTask</code> extends <code>RealTimeTask</code> to define the
 * task that processes a chat message
 */
public class ChatTask extends RealTimeTask {

    /**
     * The ID number of the sender
     */
    private final int sourceID;
    /**
     * The chat message
     */
    private final String message;
    /**
     * True if initiated by the client side
     */
    private final boolean local;

    /**
     * Constructor of <code>AttachTask</code>.
     * @param game The <code>Game</code> instance.
     * @param sourceID The ID number of the sending player
     * @param message The chat message
     * @param local true if initiated by client side
     */
    public ChatTask(Game game, int sourceID, String message, boolean local) {
        super(ETask.Attach, game);
        this.sourceID = sourceID;
        this.message = message;
        this.local = local;
    }

    @Override
    public void execute() {
        if (this.local) {
            this.game.getClient().send(ClientMessages.createChatPkt(message));
        }

        try {
            CharacterEntity sender = (CharacterEntity) EntityManager.getInstance().getEntity(sourceID);
            ((BattleState) this.game.getGameState(EGameState.BattleState)).getGUI().appendChatMessage(sender.getName(), message);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            if (object instanceof ChatTask) {
                ChatTask given = (ChatTask) object;
                return (given.sourceID == this.sourceID && given.message.equals(this.message));
            }
        }
        return false;
    }
}

