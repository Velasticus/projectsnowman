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
package com.sun.darkstar.example.snowman.server.impl;

import com.sun.darkstar.example.snowman.common.protocol.enumn.EEndState;
import com.sun.darkstar.example.snowman.common.protocol.enumn.EMOBType;
import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;
import com.sun.darkstar.example.snowman.common.protocol.messages.ServerMessages;
import com.sun.darkstar.example.snowman.common.util.Coordinate;
import com.sun.darkstar.example.snowman.server.exceptions.SnowmanFullException;
import com.sun.darkstar.example.snowman.server.interfaces.EntityFactory;
import com.sun.darkstar.example.snowman.server.interfaces.SnowmanFlag;
import com.sun.darkstar.example.snowman.server.interfaces.SnowmanGame;
import com.sun.darkstar.example.snowman.server.interfaces.SnowmanPlayer;
import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.Delivery;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;
import com.sun.sgs.app.ObjectNotFoundException;
import com.sun.sgs.app.util.ScalableHashMap;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * This object represents an actual running game session of Project Snowman.
 * 
 * @author Jeffrey Kesselman
 * @author Owen Kellett
 * @author Yi Wang (Neakor)
 */
public class SnowmanGameImpl implements SnowmanGame, Serializable {

    /** The version of the serialized form. */
    public static final long serialVersionUID = 1L;
    private static final Logger logger = 
            Logger.getLogger(SnowmanGameImpl.class.getName());
    
    /**
     * A prefix that is appended to the darkstar bound name for
     * all game channels.
     */
    public static final String CHANPREFIX = "_GAMECHAN_";
    private static final int PLAYERIDSTART = 1;
    private static final int CLEANUPDELAYMS = 5 * 1000;
    
    /**
     * A reference to a channel that is used to send game packets to
     * all the players in this game session
     */
    private final ManagedReference<Channel> channelRef;
    private int numPlayers;
    private int realPlayers = 0;
    private int readyPlayers = 0;
    private int nextPlayerId = PLAYERIDSTART;
    private String gameName;
    private boolean ending = false;
    /**
     * List of flags in the game
     */
    private final ManagedReference
            <Map<Integer, ManagedReference<SnowmanFlag>>> flagRefs;
    /**
     * Map of player IDs to players that are part of this game
     */
    private final ManagedReference
            <Map<Integer, ManagedReference<SnowmanPlayer>>> playerRefs;
    private final EntityFactory entityFactory;
    /**
     * Keeps track of how many players from each team have joined the game
     */
    private int[] teamPlayers = new int[ETeamColor.values().length];
    /**
     * Keeps track of the maximum number of players per team
     */
    private int[] maxTeamPlayers = new int[ETeamColor.values().length];

    /**
     * Creates a new instance of a game with the give name and maximum
     * number of players.
     * 
     * @param gameName the name of the game
     * @param numPlayers the maximum number of players that can join the game
     * @param entityFactory the factory used to create artifacts for the game
     */
    public SnowmanGameImpl(String gameName,
                           int numPlayers,
                           EntityFactory entityFactory) {
        this.gameName = gameName;
        this.numPlayers = numPlayers;
        initMaxTeamPlayers();

        Map<Integer, ManagedReference<SnowmanFlag>> f =
                new ScalableHashMap<Integer, ManagedReference<SnowmanFlag>>(
                ETeamColor.values().length);
        this.flagRefs = AppContext.getDataManager().createReference(f);
        Map<Integer, ManagedReference<SnowmanPlayer>> p =
                new ScalableHashMap<Integer, ManagedReference<SnowmanPlayer>>(
                numPlayers);
        this.playerRefs = AppContext.getDataManager().createReference(p);

        this.entityFactory = entityFactory;
        this.channelRef = AppContext.getDataManager().createReference(
                AppContext.getChannelManager().createChannel(
                CHANPREFIX + gameName, null, Delivery.RELIABLE));
        initFlags();
    }

    /**
     * Initialize the maximum number of players per team
     * The total number of players is divided up evenly among the teams.
     * If the division is not even, the last team will have the remainder
     */
    private void initMaxTeamPlayers() {
        //special case when there are fewer players than teams
        if (this.numPlayers < ETeamColor.values().length) {
            for (int j = 0; j < maxTeamPlayers.length; j++) {
                if (j < numPlayers) {
                    maxTeamPlayers[j] = 1;
                } else {
                    maxTeamPlayers[j] = 0;
                }
            }
            return;
        }

        int playersPerTeam = this.numPlayers / ETeamColor.values().length;
        int remainder = this.numPlayers % ETeamColor.values().length;
        for (int i = 0; i < maxTeamPlayers.length - 1; i++) {
            maxTeamPlayers[i] = playersPerTeam;
        }
        if (remainder == 0) {
            maxTeamPlayers[maxTeamPlayers.length - 1] = playersPerTeam;
        } else {
            maxTeamPlayers[maxTeamPlayers.length - 1] = remainder;
        }
    }

    /**
     * Initialize the list of flags and their locations
     */
    private void initFlags() {
        for (ETeamColor color : ETeamColor.values()) {
            Coordinate flagStart = SnowmanMapInfo.getFlagStart(
                    SnowmanMapInfo.DEFAULT, color);
            Coordinate flagGoal = SnowmanMapInfo.getFlagGoal(
                    SnowmanMapInfo.DEFAULT, color);
            SnowmanFlag flag =
                    entityFactory.createSnowmanFlag(this,
                                                    color,
                                                    flagStart,
                                                    flagGoal);
            flag.setLocation(flagStart.getX(), flagStart.getY());
            ManagedReference<SnowmanFlag> ref =
                    AppContext.getDataManager().createReference(flag);
            flagRefs.get().put(flag.getID(), ref);
        }
    }

    /** {@inheritDoc} */
    public void send(ByteBuffer buff) {
        channelRef.get().send(null, buff);
    }

    /** {@inheritDoc} */
    public void addPlayer(SnowmanPlayer player, ETeamColor color) {
        AppContext.getDataManager().markForUpdate(this);
        //ensure we are not going over the limit
        if (teamPlayers[color.ordinal()] == maxTeamPlayers[color.ordinal()]) {
            throw new SnowmanFullException("Player " + player.getName() + 
                                           " cannot be added to game " +
                                           this.getName() + 
                                           " : too many players");
        }

        //get a reference to the player and add to the list
        ManagedReference<SnowmanPlayer> playerRef =
                AppContext.getDataManager().createReference(player);
        Integer playerId = Integer.valueOf(nextPlayerId++);
        playerRefs.get().put(playerId, playerRef);

        //increment the total team players in this game
        teamPlayers[color.ordinal()]++;

        //update player information
        player.setID(playerId.intValue());
        Coordinate position = 
                SnowmanMapInfo.getSpawnPosition(
                SnowmanMapInfo.DEFAULT,
                color,
                teamPlayers[color.ordinal()],
                maxTeamPlayers[color.ordinal()]);
        player.setLocation(position.getX(), position.getY());
        player.setTeamColor(color);
        player.setGame(this);

        //add the real players session to the channels.
        if (player.getSession() != null) {
            realPlayers++;
            channelRef.get().join(player.getSession());
        }
    }

    /** {@inheritDoc} */
    public void removePlayer(SnowmanPlayer player) {
        AppContext.getDataManager().markForUpdate(this);
        player.dropFlag();
        playerRefs.get().remove(player.getID());
        Channel channel = channelRef.get();
        if (player.getSession() != null) {
            realPlayers--;
            channel.leave(player.getSession());
        }

        // if all real players have gone, end the game
        if (!channel.hasSessions()) {
            endGame(EEndState.Draw);
        } else {
            send(ServerMessages.createRemoveMOBPkt(player.getID()));
        }
    }

    /** 
     * {@inheritDoc}
     * 
     * Since each player receives an individually customized NEWGAME message
     * as private session messages, the game channel is not used to send
     * the AddMOB packets.  The AddMOB messages are sent as private session
     * messages instead.  This is done so as to preserve ordering.
     */
    public void sendMapInfo() {
        for (ManagedReference<SnowmanPlayer> ref : playerRefs.get().values()) {
            SnowmanPlayer player = ref.get();
            if (player.getSession() != null) {
                player.send(ServerMessages.createNewGamePkt(player.getID(),
                                                            "default_map"));
            }
        }
        for (ManagedReference<SnowmanPlayer> ref : playerRefs.get().values()) {
            SnowmanPlayer player = ref.get();
            multiSend(ServerMessages.createAddMOBPkt(
                      player.getID(), player.getX(), player.getY(),
                      EMOBType.SNOWMAN, player.getTeamColor(), 
                      player.getName()));
        }
        for (ManagedReference<SnowmanFlag> flagRef : flagRefs.get().values()) {
            SnowmanFlag flag = flagRef.get();
            multiSend(ServerMessages.createAddMOBPkt(
                      flag.getID(), flag.getX(), flag.getY(), EMOBType.FLAG, 
                      flag.getTeamColor(), 
                      flag.getTeamColor().toString() + "Flag"));

            //TODO - encode goal color in the flag
            //currently the add mob should swap the goal colors so that
            //it is more intuitive for the players
            multiSend(ServerMessages.createAddMOBPkt(
                      flag.getID() + flagRefs.get().size(), 
                      flag.getGoalX(), flag.getGoalY(), EMOBType.FLAGGOAL,
                      flag.getTeamColor() == ETeamColor.Red 
                      ? ETeamColor.Blue : ETeamColor.Red, "Goal"));
        }
        multiSend(ServerMessages.createReadyPkt());
    }

    /**
     * Send a message to all players in the game without using the game
     * {@code Channel}.
     * @param buff
     */
    private void multiSend(ByteBuffer buff) {
        for (ManagedReference<SnowmanPlayer> ref : playerRefs.get().values()) {
            // send a wrapped buffer
            ref.get().send(buff.asReadOnlyBuffer());
        }
    }

    /** {@inheritDoc} */
    public void startGameIfReady() {
        AppContext.getDataManager().markForUpdate(this);
        readyPlayers++;
        if (readyPlayers >= realPlayers) {
            send(ServerMessages.createStartGamePkt());
        }
    }

    /** {@inheritDoc} */
    public Set<Integer> getPlayerIds() {
        return playerRefs.get().keySet();
    }

    /** {@inheritDoc} */
    public SnowmanPlayer getPlayer(int id) {
        ManagedReference<SnowmanPlayer> playerRef =
                playerRefs.get().get(Integer.valueOf(id));
        if (playerRef != null) {
            return playerRef.get();
        }
        return null;
    }

    /** {@inheritDoc} */
    public void endGame(EEndState endState) {
        //if this method has already been called, skip
        if(ending) {
            return;
        }
        ending = true;
        
        send(ServerMessages.createEndGamePkt(endState));

        // Attempt to clean up the game objects, including the channel, later
        // so that the EndGame message is sent ASAP
        AppContext.getTaskManager().scheduleTask(
                new ObjectRemovalTask(
                AppContext.getDataManager().createReference(this)),
                CLEANUPDELAYMS);
    }

    /**
     * Asyncronously removes an object from the datastore.
     */
    private static class ObjectRemovalTask implements Task, Serializable {
        
        /** The version of the serialized form. */
        public static final long serialVersionUID = 1L;

        final ManagedReference ref;

        ObjectRemovalTask(ManagedReference ref) {
            this.ref = ref;
        }

        /** {@inheritDoc} */
        public void run() throws Exception {
            try {
                AppContext.getDataManager().removeObject(ref.get());
            } catch (ObjectNotFoundException alreadyRemoved) {
            }
        }
    }

    /** {@inheritDoc} */
    public void removingObject() {
        Channel c = getGameChannel();
        c.leaveAll();
        AppContext.getDataManager().removeObject(c);

        //only remove server side robots
        //player listener is responsible for cleaning up client
        //connected players
        for (ManagedReference<SnowmanPlayer> ref : playerRefs.get().values()) {
            try {
                SnowmanPlayer p = ref.get();
                if (p.isServerSide()) {
                    AppContext.getDataManager().removeObject(p);
                }
            } catch (ObjectNotFoundException alreadyRemoved) {
            }
        }

        for (ManagedReference<SnowmanFlag> ref : flagRefs.get().values()) {
            AppContext.getDataManager().removeObject(ref.get());
        }
    }

    /** {@inheritDoc} */
    public Set<Integer> getFlagIds() {
        return flagRefs.get().keySet();
    }

    /** {@inheritDoc} */
    public SnowmanFlag getFlag(int id) {
        return flagRefs.get().get(id).get();
    }

    /** {@inheritDoc} */
    public String getName() {
        return gameName;
    }

    /** {@inheritDoc} */
    public Channel getGameChannel() {
        return channelRef.get();
    }
}
