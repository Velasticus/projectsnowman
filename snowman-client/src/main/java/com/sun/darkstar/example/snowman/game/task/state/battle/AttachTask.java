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

import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity;
import com.sun.darkstar.example.snowman.common.entity.view.View;
import com.sun.darkstar.example.snowman.common.interfaces.IEntity;
import com.sun.darkstar.example.snowman.common.protocol.messages.ClientMessages;
import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.game.entity.DynamicEntity;
import com.sun.darkstar.example.snowman.game.entity.scene.CharacterEntity;
import com.sun.darkstar.example.snowman.game.entity.util.EntityManager;
import com.sun.darkstar.example.snowman.game.entity.view.scene.CharacterView;
import com.sun.darkstar.example.snowman.game.entity.view.util.ViewManager;
import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;
import com.sun.darkstar.example.snowman.game.task.RealTimeTask;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;

/**
 * <code>AttachTask</code> extends <code>RealTimeTask</code> to define the
 * task that grabs a flag.
 * <p>
 * <code>AttachTask</code> execution logic:
 * 1. Retrieve the views of both the target and flag.
 * 2. Attach the flag view to the target view with appropriate translation.
 * 3. Send out 'grab' packet.
 * <p>
 * <code>AttachTask</code> are considered 'equal' if and only if the grabbing
 * source ID and the flag ID are 'equal'.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 08-14-2008 12:09 EST
 * @version Modified date: 08-14-2008 12:50 EST
 */
public class AttachTask extends RealTimeTask {
    /**
     * The ID number of the flag.
     */
    private final int flagID;
    /**
     * The ID number of the target to attach to.
     */
    private final int targetID;
    /**
     * True if initiated by the client side
     */
    private final boolean local;


    /**
     * Constructor of <code>AttachTask</code>.
     * @param game The <code>Game</code> instance.
     * @param flagID The ID number of the flag.
     * @param targetID The ID number of the target to attach to.
     * @param local true if initiated by client side
     */
    public AttachTask(Game game, int flagID, int targetID, boolean local) {
        super(ETask.Attach, game);
        this.flagID = flagID;
        this.targetID = targetID;
        this.local = local;
    }

    @Override
    public void execute() {
        IEntity target = EntityManager.getInstance().getEntity(this.targetID);
        IEntity flag = EntityManager.getInstance().getEntity(this.flagID);
        View targetView = (View) ViewManager.getInstance().getView(target);
        View flagView = (View) ViewManager.getInstance().getView(flag);

        if(this.local) {
            this.game.getClient().send(ClientMessages.createGetFlagPkt(this.flagID, targetView.getLocalTranslation().x, targetView.getLocalTranslation().z));
        }
        else {
            if (target instanceof CharacterEntity) {
                flagView.setLocalTranslation(0.3f, 0.5f, 0);
                ((CharacterEntity) target).setFlag((DynamicEntity) flag);
                flagView.detachFromParent();
                flagView.attachTo(targetView);
            } else if (target.getEnumn() == EEntity.Terrain && flagView.getParent() instanceof CharacterView) {
                flagView.getLocalTranslation().set(flagView.getParent().getLocalTranslation());
                flagView.detachFromParent();
                flagView.attachTo(this.game.getGameState(EGameState.BattleState).getWorld().getDynamicRoot());
            }
        }
    }

    @Override
    public boolean equals(Object object) {
            if (super.equals(object)) {
                if (object instanceof AttachTask) {
                    AttachTask given = (AttachTask) object;
                    return (given.flagID == this.flagID) && (given.targetID == this.targetID) && 
                            (given.local == this.local);
                }
            }
            return false;
    }
}
