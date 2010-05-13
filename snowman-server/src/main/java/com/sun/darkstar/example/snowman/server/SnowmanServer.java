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
package com.sun.darkstar.example.snowman.server;

import com.sun.darkstar.example.snowman.server.interfaces.GameFactory;
import com.sun.darkstar.example.snowman.server.interfaces.EntityFactory;
import com.sun.darkstar.example.snowman.server.interfaces.SnowmanPlayer;
import com.sun.darkstar.example.snowman.server.impl.EntityFactoryImpl;
import com.sun.darkstar.example.snowman.server.impl.GameFactoryImpl;
import com.sun.darkstar.example.snowman.server.tasks.MatchmakerTask;
import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.AppListener;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.util.ScalableDeque;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Deque;
import java.math.BigInteger;

/**
 * This class is the app listener for Project Snowman.
 * It is sort of like a "main class" in a traditional Java applciation
 * 
 * @author Jeffrey Kesselman
 * @author Owen Kellett
 * @author Keith Thompson
 */
public class SnowmanServer implements ManagedObject, Serializable, AppListener {

    /** The version of the serialized form. */
    public static final long serialVersionUID = 1L;
    private static final Logger logger = 
            Logger.getLogger(SnowmanServer.class.getName());
    
    /**
     * Number of queues to use for the matchmaking system
     */
    private static final int NUMDEQUES = 10;
    /**
     * Name of the property used to define number of players per game
     */
    private static final String PLAYERS_PER_GAME_PROP = "numPlayersPerGame";
    /**
     * Default number of players per game
     */
    private static final int DEFAULT_PLAYERS_PER_GAME = 2;
    /**
     * Name of the property used to define number of robots per game
     */
    private static final String ROBOTS_PER_GAME_PROP = "numRobotsPerGame";
    /**
     * Default number of robots per game
     */
    private static final int DEFAULT_ROBOTS_PER_GAME = 2;
    /**
     * Name of the property used to delay until robots make first move
     */
    private static final String ROBOT_DELAY_PROP = "robotDelay";
    /**
     * Default first move delay for robots
     */
    private static final int DEFAULT_ROBOT_DELAY = 2000;
    
    private int numPlayersPerGame;
    private int numRobotsPerGame;
    private int robotDelay;
    
    private ManagedReference<Deque<ManagedReference<SnowmanPlayer>>>[] 
            waitingDeques;
    private GameFactory gameFactory;
    private EntityFactory entityFactory;

    /**
     * Initializes a Project Snowman server upon first bootup. This involves:
     * <ol>
     * <li>Initializing a list of queues that connecting players are placed
     * into upon connecting to wait to be matched into a game.</li>
     * <li>Initializing the self-rescheduling {@code link MatchmakerTask} which
     * is responsible for pulling players off of the waiting queues and
     * matching them into games.</li>
     * </ol>
     * Configuration parameters such as number of players in a game, number
     * of robots in a game, and robot move delay are also parsed and 
     * established from the given set of properties.
     * 
     * @param props a set of {@code Properties} used to configure the 
     *        runtime state of the game
     */
    @SuppressWarnings("unchecked")
    public void initialize(Properties props) {
        this.gameFactory = new GameFactoryImpl();
        this.entityFactory = new EntityFactoryImpl();
        this.waitingDeques = new ManagedReference[NUMDEQUES];
        for (int i = 0; i < waitingDeques.length; i++) {
            Deque<ManagedReference<SnowmanPlayer>> deque = 
                    new ScalableDeque<ManagedReference<SnowmanPlayer>>();
            waitingDeques[i] = 
                    AppContext.getDataManager().createReference(deque);
        }

        this.config(props);
        AppContext.getTaskManager().scheduleTask(
                new MatchmakerTask(numPlayersPerGame,
                                   numRobotsPerGame,
                                   robotDelay,
                                   gameFactory,
                                   entityFactory,
                                   waitingDeques));
    }

    private void config(Properties props) {
        numPlayersPerGame = getPropertyAsInteger(props,
                                                 PLAYERS_PER_GAME_PROP,
                                                 DEFAULT_PLAYERS_PER_GAME);
        if (numPlayersPerGame <= 0) {
            throw new IllegalArgumentException(PLAYERS_PER_GAME_PROP + 
                                               " must be > 0");
        }
        logger.log(Level.CONFIG,
                   "Number of players required to start a game set to {0}",
                   numPlayersPerGame);
        
        numRobotsPerGame = getPropertyAsInteger(props,
                                                ROBOTS_PER_GAME_PROP,
                                                DEFAULT_ROBOTS_PER_GAME);
        if (numRobotsPerGame < 0) {
            throw new IllegalArgumentException(ROBOTS_PER_GAME_PROP + 
                                               " must be >= 0");
        }
        
        robotDelay = getPropertyAsInteger(props,
                                          ROBOT_DELAY_PROP,
                                          DEFAULT_ROBOT_DELAY);
        if (robotDelay < 0) {
            throw new IllegalArgumentException(ROBOT_DELAY_PROP + 
                                               " must be >= 0");
        }
        logger.log(Level.CONFIG,
                   "Number of robots per game: {0}, " +
                   "with delay of {1} milliseconds",
                   new Object[]{numRobotsPerGame, robotDelay});
    }

    /**
     * When a player logs in, it is randomly added to one of the waiting
     * deques.  The {@link MatchmakerTask} is responsible for pulling players
     * off of these deques and matching them into games.
     * 
     * @param session the {@code ClientSession} of the connecting player
     * @return a {@link SnowmanPlayerListener} associated with the connected
     *         player
     */
    public ClientSessionListener loggedIn(ClientSession session) {
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Player {0} logged in", session.getName());
        }
        SnowmanPlayerListener player =
                new SnowmanPlayerListener(
                entityFactory.createSnowmanPlayer(session));
        BigInteger id = player.getSnowmanPlayerRef().getId();
        BigInteger index = id.mod(BigInteger.valueOf((long) NUMDEQUES));

        waitingDeques[index.intValue()].get().add(player.getSnowmanPlayerRef());
        return player;
    }
    
    /**
     * Retrieves a property with the given key from the {@link Properties}
     * object as an Integer value.  If the property does not exist, or it is
     * an invalid number format, the {@code defaultValue} is returned instead.
     * 
     * @param props the {@code Properties} object
     * @param key the key to get the property of
     * @param defaultValue the default value if the property does not exist
     * 
     * @return the value of the property with the given key as an
     *         {@code Integer} if it exists and is a valid number format,
     *         otherwise, returns defaultValue
     */
    private static Integer getPropertyAsInteger(Properties props,
                                                String key,
                                                Integer defaultValue) {
        try {
            return Integer.valueOf(props.getProperty(key));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
}
