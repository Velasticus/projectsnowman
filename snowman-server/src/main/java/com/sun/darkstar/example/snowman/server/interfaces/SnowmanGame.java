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

package com.sun.darkstar.example.snowman.server.interfaces;

import com.sun.darkstar.example.snowman.common.protocol.enumn.EEndState;
import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedObjectRemoval;
import java.nio.ByteBuffer;
import java.util.Set;

/**
 * The {@code SnowmanGame} interface describes the basic behavior
 * for a game.
 * 
 * @author Owen Kellett
 * @author Yi Wang (Neakor)
 */
public interface SnowmanGame extends ManagedObject, ManagedObjectRemoval
{
    /**
     * Send a message to all players in the game on the game's Channel.
     * @param buff the message itself
     */
    void send(ByteBuffer buff);
    
    /**
     * Send the AddMOB packets to all of the players in the game.
     * to initiate the game state
     */
    void sendMapInfo();
    
    /**
     * Add a player to the game.
     * @param player the player to add to the game
     * @param color the team of the player
     */
    void addPlayer(SnowmanPlayer player, ETeamColor color);
    
    /**
     * Remove player from the game.
     * @param player the player to remove from the game
     */
    void removePlayer(SnowmanPlayer player);
    
    /**
     * Verify that all players are ready to player and start the game
     * by broadcasting a STARTGAME message if so.
     */
    void startGameIfReady();
    
    /**
     * End the game, notify clients of the results, and cleanup the game state.
     * 
     * @param endState the result of the game
     */
    void endGame(EEndState endState);
    
    /**
     * Return the flag from the game with the given id.
     * @param id the id of the flag
     * @return the flag with the given id
     */
    SnowmanFlag getFlag(int id);

    /**
     * Returns the complete set of flag ids from flags in the game.
     * 
     * @return the set of flag ids in the game
     */
    Set<Integer> getFlagIds();
    
    /**
     * Return the player from the game with the given id.
     * 
     * @param id the id of the player
     * @return the player with the given id
     */
    SnowmanPlayer getPlayer(int id);
    
    /**
     * Return the complete set of player ids from players in the game.
     * 
     * @return the set of player ids in the game
     */
    Set<Integer> getPlayerIds();
    
    /**
     * Return the name of the game.
     * 
     * @return the name of the game
     */
    String getName();
    
    /**
     * Return the channel for this game.
     * @return the game channel
     */
    Channel getGameChannel();
}
