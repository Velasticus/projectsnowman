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


/**
 * <code>IServerProcessor</code> defines the interface which handles processing
 * messages received by the server from the client.
 * <p>
 * <code>IServerProcessor</code> is directly invoked by <code>ServerProtocol</code>
 * after parsing a received packet.
 * 
 * @author Yi Wang (Neakor)
 * @author Jeffrey Kesselman
 * @author Owen Kellett
 * @version Creation date: 05-29-08 11:44 EST
 * @version Modified date: 11-26-08 21:14 EST
 */
public interface IServerProcessor extends IProtocolProcessor {

    /**
     * <p>
     * Move the sending client to the given position.
     * </p>
     * <p>
     * When the server receives a MOVEME message from the client,
     * this method will be called and it should do the following:
     * <ul>
     * <li>Verify that the client has sent a valid starting location
     * based on the previous known location, move direction and speed, 
     * and server recorded timestamp.</li>
     * <li>If the location is invalid, broadcast a STOPMOB message that
     * gives the server's expected position of the client.</li>
     * <li>If the location is valid, submit the start and end coordinates
     * (given by the client) to the path trimmer of the 
     * <code>GameWorldManager</code>.  Broadcast a MOVEMOB message with the 
     * start position and resulting end position given by the path trimmer.</li>
     * </ul>
     * </p>
     * @param x The x coordinate of the start point.
     * @param y The y coordinate of the start point.
     * @param endx The x coordinate of the end point.
     * @param endy The y coordinate of the end point.
     */
    public void moveMe(float x, float y, float endx, float endy);

    /**
     * <p>
     * Attack the target with given ID number with the sending client.
     * </p>
     * <p>
     * When the server receives a ATTACK message from the client,
     * this method will be called and it should do the following:
     * <ul>
     * <li>Verify that the client has sent a valid attack location
     * based on the previous known location, move direction and speed, 
     * and server recorded timestamp.</li>
     * <li>If the location is invalid, do nothing.</li>
     * <li>If the attacker location is valid, an implicit stop occurs
     * at this location.  Submit the start and end coordinates
     * to the validator of the <code>GameWorldManager</code>.  If there
     * is nothing blocking the snowball throw, and the client is within
     * range of the attackee, broadcast an ATTACKED message with the
     * hp set to the standard HP deduction value. </li>
     * <li>If the attacker location is valid but its determined that
     * the snowball can't be thrown (because it is out of range or there
     * is something in the way), broadcast an ATTACKED message with the
     * HP set to 0.</li>
     * <li>If the attack is a success, deduct the standard HP deduction
     * from the attackee.  Also, check to see if the player is dead and
     * if they hold the flag.  If death, drop the flag and schedule a
     * respawn.</li>
     * </ul>
     * </p>
     * @param targetID The ID number of the target.
     * @param x The x coordinate of the attacker.
     * @param y The y coordinate of the attacker.
     */
    public void attack(int targetID, float x, float y);

    /**
     * <p>
     * Attach the flag with given ID number to the sending client.
     * </p>
     * <p>
     * When the server recieves a GETFLAG message from the client,
     * this method will be called and it should do the following:
     * <ul>
     * <li>Verify that the client is within range of the flag.</li>
     * <li>Verify that the flag is the opponent's flag.</li>
     * <li>Verify that nobody else has the flag.</li>
     * <li>If the client is in range, stop the client at the given
     * position, attach the flag
     * to the client, and broadcast a ATTACHOBJ message.</li>
     * <li>If the client is out of range, ignore the message.</li>
     * </ul>
     * </p>
     * @param flagID The ID number of the flag.
     * @param x The x coordinate of the client position
     * @param y The y coordinate of the client position
     */
    public void getFlag(int flagID, float x, float y);
    
    /**
     * <p>
     * The sending client should be holding the flag and wishes to place
     * the flag down to win the game.
     * </p>
     * <p>
     * When the server recieves a SCORE message from the client,
     * this method will be called and it should do the following:
     * <ul>
     * <li>Verify that the client is holding the flag and is in range
     * of the goal position.</li>
     * <li>If the client is in range, broadcast a ENDGAME message with
     * the results of the game.</li>
     * <li>Otherwise, ignore the message.</li>
     * </ul>
     * </p>
     * @param x The x coordinate of the client position
     * @param y The y coordinate of the client position
     */
    public void score(float x, float y);
    
    /**
     * Broadcast the chat message with inserted source name.
     * @param message The <code>String</code> actual chat message.
     */
    public void chatMessage(String message);
}
