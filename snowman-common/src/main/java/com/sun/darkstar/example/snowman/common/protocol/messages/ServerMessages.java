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

package com.sun.darkstar.example.snowman.common.protocol.messages;

import java.nio.ByteBuffer;

import com.sun.darkstar.example.snowman.common.protocol.enumn.EEndState;
import com.sun.darkstar.example.snowman.common.protocol.enumn.EMOBType;
import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;
import com.sun.darkstar.example.snowman.common.protocol.enumn.EOPCODE;

/**
 * <code>ServerMessages</code> provides static packet generation methods
 * that are sent by just the server
 * 
 * @author Yi Wang (Neakor)
 * @author Jeffrey Kesselman
 * @author Owen Kellett
 */
public class ServerMessages extends Messages 
{

    /**
     * Create a "new game" packet which notifies the client to enter battle state
     * with given ID number and map name.
     * @param myID The ID number assigned to the client.
     * @param mapname The name of the map to play on.
     * @return The <code>ByteBuffer</code> "new game" packet.
     */
    public static ByteBuffer createNewGamePkt(int myID, String mapname) {
        byte[] bytes = new byte[1 + 4 + 4 + mapname.length()];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte) EOPCODE.NEWGAME.ordinal());
        buffer.putInt(myID);
        buffer.putInt(mapname.length());
        buffer.put(mapname.getBytes());
        
        buffer.flip();
        return buffer;
    }

    /**
     * Create a "start game" packet which notifies the client to start the battle.
     * @return The <code>ByteBuffer</code> "start game" packet.
     */
    public static ByteBuffer createStartGamePkt() {
        byte[] bytes = new byte[1];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte) EOPCODE.STARTGAME.ordinal());
        
        buffer.flip();
        return buffer;
    }

    /**
     * Create a "end game" packet which notifies the client to end the battle
     * with given state.
     * @param state The <code>EndState</code> of the battle.
     * @return The <code>ByteBuffer</code> "end game" packet.
     */
    public static ByteBuffer createEndGamePkt(EEndState state) {
        byte[] bytes = new byte[1 + 4];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte) EOPCODE.ENDGAME.ordinal());
        buffer.putInt(state.ordinal());
        
        buffer.flip();
        return buffer;
    }


    /**
     * Create an "add MOB" packet which notifies the client to add a MOB with
     * given ID number, <code>MOBType</code> at the given position.
     * @param targetID The ID number of the new map object.
     * @param x The X coordinate of the object.
     * @param y The Y coordinate of the object.
     * @param mobType The <code>MOBType</code> of object.
     * @param team The <code>TeamColor</code> of object.
     * @return The <code>ByteBuffer</code> "add MOB" packet.
     */
    public static ByteBuffer createAddMOBPkt(int targetID, float x, float y, EMOBType mobType, ETeamColor team, String mobName) {
        byte[] bytes = new byte[1 + 20 + 4 + mobName.length()];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte) EOPCODE.ADDMOB.ordinal());
        buffer.putInt(targetID);
        buffer.putFloat(x);
        buffer.putFloat(y);
        buffer.putInt(mobType.ordinal());
        buffer.putInt(team.ordinal());
        buffer.putInt(mobName.length());
        buffer.put(mobName.getBytes());
        
        buffer.flip();
        return buffer;
    }
    
    /**
     * Create a "remove MOB" packet which notifies the client to remove
     * the MOB with given ID.
     * @param targetID The ID number of the MOB to be removed.
     * @return The <code>ByteBuffer</code> "remove MOB" packet.
     */
    public static ByteBuffer createRemoveMOBPkt(int targetID) {
        byte[] bytes = new byte[1 + 4];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte) EOPCODE.REMOVEMOB.ordinal());
        buffer.putInt(targetID);
        
        buffer.flip();
        return buffer;
    }

    /**
     * Create a "move MOB" packet which notifies the client to move the MOB
     * with given ID towards the given destination.
     * @param targetID The ID number of the MOB to be moved.
     * @param startx The x coordinate of the starting position.
     * @param starty The y coordinate of the starting position.
     * @param endx The x coordinate of the ending position.
     * @param endy The y coordinate of the ending position.
     * @return The <code>ByteBuffer</code> "move MOB" packet.
     */
    public static ByteBuffer createMoveMOBPkt(int targetID, float startx, float starty, float endx, float endy) {
        byte[] bytes = new byte[1 + 20];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte) EOPCODE.MOVEMOB.ordinal());
        buffer.putInt(targetID);
        buffer.putFloat(startx);
        buffer.putFloat(starty);
        buffer.putFloat(endx);
        buffer.putFloat(endy);
        
        buffer.flip();
        return buffer;
    }
    
    /**
     * Create a "stop MOB" packet which notifies the client to stop the MOB
     * with given ID at the given destination
     * @param targetID The ID number of the MOB to be stopped.
     * @param x The x coordinate of the stop position.
     * @param y The y coordinate of the stop position.
     * @return The <code>ByteBuffer</code> "move MOB" packet.
     */
    public static ByteBuffer createStopMOBPkt(int targetID, float x, float y) {
        byte[] bytes = new byte[1 + 12];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte) EOPCODE.STOPMOB.ordinal());
        buffer.putInt(targetID);
        buffer.putFloat(x);
        buffer.putFloat(y);
        
        buffer.flip();
        return buffer;
    }

    /**
     * Create an "attach object" packet which notifies the client to attach
     * the object with given source ID to the object with given target ID.
     * @param sourceID The ID number of the object to be re-attached.
     * @param targetID the ID number of the object to attach it to.
     * @return The <code>ByteBuffer</code> "attach object" packet.
     */
    public static ByteBuffer createAttachObjPkt(int sourceID, int targetID) {
        byte[] bytes = new byte[1 + 8];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte) EOPCODE.ATTACHOBJ.ordinal());
        buffer.putInt(sourceID);
        buffer.putInt(targetID);
        
        buffer.flip();
        return buffer;
    }

    /**
     * Create an "attacked" packet which notifies the client that the object
     * with given source ID has attacked the object with given target ID.
     * @param sourceID The ID number of the attacker.
     * @param targetID The ID number of the target.
     * @param hp The hit point of the target (-1 if a miss)
     * @return The <code>ByteBuffer</code> "attacked" packet.
     */
    public static ByteBuffer createAttackedPkt(int sourceID, int targetID, int hp) {
        byte[] bytes = new byte[1 + 12];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte) EOPCODE.ATTACKED.ordinal());
        buffer.putInt(sourceID);
        buffer.putInt(targetID);
        buffer.putInt(hp);
        
        buffer.flip();
        return buffer;
    }

    /**
     * Create a "respawn" packet which notifies the client to respawn
     * the object with given ID at the given location.
     * @param objectID The ID number of the object to be respawn.
     * @param x The x coordinate of the respawn position.
     * @param y The y coordinate of the respawn position.
     * @return The <code>ByteBuffer</code> "respawn" packet.
     */
    public static ByteBuffer createRespawnPkt(int objectID, float x, float y) {
        byte[] bytes = new byte[1 + 12];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte) EOPCODE.RESPAWN.ordinal());
        buffer.putInt(objectID);
        buffer.putFloat(x);
        buffer.putFloat(y);
        
        buffer.flip();
        return buffer;
    }
    
    /**
     * Create a chat message packet with given values.
     * @param sourceID The <code>Integer</code> source ID.
     * @param message The <code>String</code> message to be displayed.
     * @return The <code>ByteBuffer</code> 'chat message' packet.
     */
    public static ByteBuffer createChatPkt(int sourceID, String message) {
    	byte[] bytes = new byte[1 + 4 + 4 + message.length()];
    	ByteBuffer buffer = ByteBuffer.wrap(bytes);
    	buffer.put((byte) EOPCODE.CHAT.ordinal());
        buffer.putInt(sourceID);
    	buffer.putInt(message.length());
    	buffer.put(message.getBytes());
        
        buffer.flip();
    	return buffer;
    }
}
