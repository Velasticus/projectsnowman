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

import com.sun.darkstar.example.snowman.common.protocol.processor.IServerProcessor;
import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;
import com.sun.darkstar.example.snowman.common.util.Coordinate;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedObject;
import java.nio.ByteBuffer;

/**
 * The {@code SnowmanPlayer} interface defines the basic behavior
 * of a player in the snowman game.
 * 
 * @author Owen Kellett
 */
public interface SnowmanPlayer extends DynamicEntity, ManagedObject
{
    /**
     * Get the name of the player.
     * @return the name of the player
     */
    String getName();

    /**
     * Set the location of the player.
     * 
     * @param x the x coordinate of the player
     * @param y the y coordinate of the player
     */
    void setLocation(float x, float y);
    
    /**
     * Get the expected position of the player at the given time.
     * 
     * @param time the time to check the player's position
     * @return the position of the player at the given time
     */
    Coordinate getExpectedPositionAtTime(long time);

    /**
     * Set the team of the player.
     * 
     * @param color the color of the player's team
     */
    void setTeamColor(ETeamColor color);
    
    /**
     * Return the player's team color.
     * 
     * @return the player's team color
     */
    ETeamColor getTeamColor();

    /**
     * Set the game that the player is in.
     * 
     * @param game the game the player is in
     */
    void setGame(SnowmanGame game);
    
    /**
     * Return the game that the player is playing in.
     * 
     * @return the game the player is in
     */
    SnowmanGame getGame();
    
    /**
     * Indicate that this player has received all game initialization info
     * on the client side and is ready to begin play.
     * 
     * @param readyToPlay true if ready to play
     */
    void setReadyToPlay(boolean readyToPlay);
    
    /**
     * Return whether this player is ready to play.
     * 
     * @return whether this player is ready to play
     */
    boolean getReadyToPlay();
    
    /**
     * Revive player, set hitpoints back to maximum value,
     * and relocate it to a respawn position.
     */
    void respawn();
    
    /**
     * Hit this snowman with the given hit point value.
     * 
     * @param hp the hit point value
     * @param attackX the x coordinate of the player
     * @param attackY the y coordinate of the player
     * @return the number of hit points deducted from the player's value
     */
    int hit(int hp, float attackX, float attackY);
    
    /**
     * If this snowman is holding the flag, drop the flag.
     */
    void dropFlag();
    
    /**
     * Return the current hit point value for this player.
     * 
     * @return the current hit point value
     */
    int getHitPoints();
    
    /**
     * Return the {@code ClientSession} associated with this player.
     * 
     * @return the {@code ClientSession} associated with this player or
     *         {@code null} if it is a robot
     */
    ClientSession getSession();
    
    /**
     * Returns whether or not this player is controlled by a human
     * or by a server side robot.
     * 
     * @return true if this player is a server side robot
     */
    boolean isServerSide();
    
    /**
     * Send a message to the player's connected {@code ClientSession}.
     * 
     * @param buff the message to send
     */
    void send(ByteBuffer buff);
    
    /**
     * Get the protocol processor that processes messages for this player.
     * 
     * @return the processor used to process incoming messages for the player
     */
    IServerProcessor getProcessor();
}
