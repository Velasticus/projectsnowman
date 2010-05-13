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

package com.sun.darkstar.example.snowman.common.protocol.processor;

import com.sun.darkstar.example.snowman.common.protocol.enumn.EEndState;
import com.sun.darkstar.example.snowman.common.protocol.enumn.EMOBType;
import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;

/**
 * <code>IClientProcessor</code> defines the interface which handles processing
 * messages received by the client from the server.
 * <p>
 * <code>IClientProcessor</code> is directly invoked by <code>ClientProtocol</code>
 * after parsing a received packet.
 * 
 * @author Yi Wang (Neakor)
 * @author Jeffrey Kesselman
 * @author Owen Kellett
 * @version Creation date: 05-29-08 11:23 EST
 * @version Modified date: 11-26-08 21:08 EST
 */
public interface IClientProcessor extends IProtocolProcessor {
	
    /**
     * Create a new battle with given map name and assign me with given ID number.
     * @param myID The ID number the server assigned me to.
     * @param mapname The name of the map for the battle.
     */
    public void newGame(int myID, String mapname);// Certified.

    /**
     * Start the current battle.
     */
    public void startGame();// Certified.

    /**
     * End a battle with given <code>EndState</code>.
     * @param endState The <cod>EndState</code> of the battle.
     */
    public void endGame(EEndState endState);// Certified.

    /**
     * Add a MOB with given ID and <code>MOBType</code> at the given position.
     * @param objectID The ID number of the newly added MOB.
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     * @param objType The <code>MOBType</code> of the newly added MOB.
     * @param team The <code>TeamColor</code> of the newly added MOB.
     * @param mobName The name of the newly added MOB
     */
    public void addMOB(int objectID, float x, float y, EMOBType objType, ETeamColor team, String mobName);// Certified.

    /**
     * Move the MOB with given ID from given starting position towards given ending position.
     * @param objectID The ID number of the MOB to be moved.
     * @param startx The x coordinate of the starting position.
     * @param starty The y coordinate of the starting position.
     * @param endx The x coordinate of the ending position.
     * @param endy The y coordinate of the ending position.
     */
    public void moveMOB(int objectID, float startx, float starty, float endx, float endy);// RealTime.

    /**
     * Remove the MOB with given ID number.
     * @param objectID The ID number of the MOB to be removed.
     */
    public void removeMOB(int objectID);// Certified.
    
    /**
     * Stop the MOB with given ID number at the given position.
     * If the MOB is not already at this location, it is effectively
     * teleported to the new stop location.
     * @param objectID the ID number of the MOB to be stopped
     * @param x The x coordinate of the stop position
     * @param y The y coordinate of the stop position
     */
    public void stopMOB(int objectID, float x, float y);

    /**
     * Attach the object with given source ID to the object with given target ID. 
     * @param sourceID The ID number of the object to be moved.
     * @param targetID The ID number of the new parent.
     */
    public void attachObject(int sourceID, int targetID);// Certified.

    /**
     * Attack the target with given target ID with the object with given source ID.
     * @param sourceID The ID number of the attacker.
     * @param targetID The ID number of the target.
     * @param hp The hit points of the target (-1 if a miss)
     */
    public void attacked(int sourceID, int targetID, int hp);// RealTime.

    /**
     * Respawn the object with given ID number to the given position
     * @param objectID The ID number of the object to be set.
     * @param x The x coordinate of the respawn position
     * @param y The y coordinate of the respawn position
     */
    public void respawn(int objectID, float x, float y);// RealTime.
    
    /**
     * Display the chat message on given channel with given source.
     * @param sourceID The <code>Integer</code> ID of the sender.
     * @param message The <code>String</code> actual message.
     */
    public void chatMessage(int sourceID, String message);
}
