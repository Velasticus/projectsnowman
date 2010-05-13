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

package com.sun.darkstar.example.snowman.server.tasks;

import com.sun.darkstar.example.snowman.server.interfaces.SnowmanPlayer;
import com.sun.darkstar.example.snowman.server.interfaces.SnowmanGame;
import com.sun.darkstar.example.snowman.server.interfaces.GameFactory;
import com.sun.darkstar.example.snowman.server.interfaces.EntityFactory;
import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;
import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Task;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.ObjectNotFoundException;
import java.io.Serializable;
import java.util.Deque;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * This task is a self re-scheduling task that polls the front of the
 * login deques and uses the players to start a new game.
 * 
 * During each execution of the task, each individual deque will be
 * polled at most once.  The polled players will be added to a local
 * list of waiting players.  If the local list fills up, a game
 * will be started, the local list will be emptied,
 * and the matchmaker will be rescheduled.
 * If local list does not fill up,
 * the task is rescheduled to fill it.
 * 
 * @author Owen Kellett
 */
public class MatchmakerTask implements Task, Serializable {
    
    /** The version of the serialized form. */
    public static final long serialVersionUID = 1L;
    private static final Logger logger = 
            Logger.getLogger(MatchmakerTask.class.getName());
    
    private static final int POLLINGINTERVAL = 2 * 1000;
    private static final String NAME_PREFIX = "Game";
    
    private int gameCount = 0;
    private final int numPlayersPerGame;
    private final int numRobotsPerGame;
    private final int robotDelay;
    
    private GameFactory gameFactory;
    private EntityFactory entityFactory;
    
    private List<ManagedReference<SnowmanPlayer>> waitingPlayers;
    private ManagedReference<Deque<ManagedReference<SnowmanPlayer>>>[] 
            waitingDeques;
    
    /**
     * Constructs a {@code MatchmakerTask} with the given attributes
     * and components.
     * 
     * @param numPlayersPerGame number of human players to include in a game
     * @param numRobotsPerGame number of server controlled robots per game
     * @param robotDelay configured delay between robot actions
     * @param gameFactory factory to create game objects
     * @param entityFactory factory to create in-game artifacts
     * @param waitingDeques array of queues which contain connecting clients
     */
    public MatchmakerTask(int numPlayersPerGame,
                          int numRobotsPerGame,
                          int robotDelay,
                          GameFactory gameFactory,
                          EntityFactory entityFactory,
                          ManagedReference<Deque<ManagedReference<SnowmanPlayer>>>[] waitingDeques) {
        this.numPlayersPerGame = numPlayersPerGame;
        this.numRobotsPerGame = numRobotsPerGame;
        this.robotDelay = robotDelay;
        
        this.gameFactory = gameFactory;
        this.entityFactory = entityFactory;
        
        this.waitingPlayers = new ArrayList<ManagedReference<SnowmanPlayer>>();
        this.waitingDeques = waitingDeques;
    }

    /** {@inheritDoc} */
    public void run() throws Exception {
        boolean playersFound = false;
        //cycle through the front of each queue, adding players to the waiting
        //list as they are found
        for (int i = 0; i < waitingDeques.length; i++) {
            ManagedReference<SnowmanPlayer> nextPlayer = 
                    waitingDeques[i].get().poll();
            if (nextPlayer != null) {
                playersFound = true;
                waitingPlayers.add(nextPlayer);
            }
            if (waitingPlayers.size() == numPlayersPerGame) {
                startGame();
                break;
            }
        }
        
        // if no players are found in the queue during this iteration
        // schedule a delay for the next polling cycle
        // otherwise, schedule the next cycle to occur immediately
        if (playersFound) {
            AppContext.getTaskManager().scheduleTask(this);
        } else {
            AppContext.getTaskManager().scheduleTask(this, POLLINGINTERVAL);
        }
    }
    
    private void startGame() {
        //remove players from waiting list if they have disconnected
        boolean needMore = false;
        for (Iterator<ManagedReference<SnowmanPlayer>> ip =
                waitingPlayers.iterator(); ip.hasNext(); ) {
            try {
                ip.next().get();
            } catch (ObjectNotFoundException e) {
                ip.remove();
                needMore = true;
            }
        }
        if (needMore) {
            return;
        }
        
        //start the game if all waiting players are still connected
        String gameName = NAME_PREFIX + (gameCount++);
        SnowmanGame game = 
                gameFactory.createSnowmanGame(gameName,
                                              numPlayersPerGame + 
                                              numRobotsPerGame,
                                              entityFactory);
        ETeamColor color = ETeamColor.values()[0];
        for (Iterator<ManagedReference<SnowmanPlayer>> ip = 
                waitingPlayers.iterator(); ip.hasNext(); ) {
            game.addPlayer(ip.next().get(), color);
            color = ETeamColor.values()[(color.ordinal() + 1) % 
                    ETeamColor.values().length];
        }
        for (int i = 0; i < numRobotsPerGame; i++) {
            game.addPlayer(entityFactory.createRobotPlayer(gameName + 
                                                           "_robot" + i,
                                                           robotDelay),
                           color);
            color = ETeamColor.values()[(color.ordinal() + 1) % 
                    ETeamColor.values().length];
        }
        game.sendMapInfo();
        
        //clear out the waiting list
        waitingPlayers.clear();
    }
}
