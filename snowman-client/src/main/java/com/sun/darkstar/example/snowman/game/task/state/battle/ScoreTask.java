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
import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;
import com.sun.darkstar.example.snowman.common.util.enumn.EStats;
import com.sun.darkstar.example.snowman.common.interfaces.IEntity;
import com.sun.darkstar.example.snowman.common.entity.view.View;
import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.game.task.RealTimeTask;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;
import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;
import com.sun.darkstar.example.snowman.game.state.scene.BattleState;
import com.sun.darkstar.example.snowman.game.entity.util.EntityManager;
import com.sun.darkstar.example.snowman.game.entity.view.util.ViewManager;

/**
 * <code>ScoreTask</code> extends <code>RealTimeTask</code> to score a win
 * for the character.
 * <p>
 * <code>ScoreTask</code> execution logic:
 * 1. Check to see if the position is within the goal radius of the color's goal
 * 2. Send 'score' packet to server if check succeeds
 * 
 * @author Owen Kellett
 */
public class ScoreTask extends RealTimeTask {

    /**
     * The color of the scoring snowman
     */
    private ETeamColor color;
    /**
     * The x coordinate to score at.
     */
    private final float x;
    /**
     * The z coordinate to score at.
     */
    private final float z;

    /**
     * Constructor of <code>ScoreTask</code>.
     * @param game The <code>Game</code> instance.
     * @param x The x coordinate to score at.
     * @param z The z coordinate to score at.
     */
    public ScoreTask(Game game, ETeamColor color, float x, float z) {
        super(ETask.Score, game);
        this.color = color;
        this.x = x;
        this.z = z;
    }

    @Override
    public void execute() {
        BattleState state = ((BattleState) this.game.getGameState(EGameState.BattleState));
        Integer goalId = state.getFlagGoalId(color);
        IEntity goalEntity = EntityManager.getInstance().getEntity(goalId);
        View goalView = (View) ViewManager.getInstance().getView(goalEntity);
        float goalX = goalView.getLocalTranslation().x;
        float goalZ = goalView.getLocalTranslation().z;
        
        float distanceSqd = (goalX - x)*(goalX - x) + (goalZ - z)*(goalZ - z);
        if(distanceSqd < EStats.GoalRadius.getValue()*EStats.GoalRadius.getValue())
            this.game.getClient().send(ClientMessages.createScorePkt(x, z));
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            if (object instanceof ScoreTask) {
                ScoreTask given = (ScoreTask) object;
                return (this.x == given.x && this.z == given.z);
            }
        }
        return false;
    }
}
