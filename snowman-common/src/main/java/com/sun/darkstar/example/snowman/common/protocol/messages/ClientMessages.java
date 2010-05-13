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
import com.sun.darkstar.example.snowman.common.protocol.enumn.EOPCODE;

/**
 * <code>ClientMessages</code> provides static packet generation methods
 * that are sent by just the client
 * 
 * @author Yi Wang (Neakor)
 * @author Jeffrey Kesselman
 * @author Owen Kellett
 */
public class ClientMessages extends Messages
{
    /**
     * Create a "move me" packet which notifies the server that this client is trying
     * to move towards the given destination.
     * @param x The x coordinate of the start point.
     * @param y The y coordinate of the start point.
     * @param endx The x coordinate of the clicked position.
     * @param endy The y coordinate of the clicked position.
     * @return The <code>ByteBuffer</code> "move me" packet.
     */
    public static ByteBuffer createMoveMePkt(float x, float y, float endx, float endy) {
        byte[] bytes = new byte[1 + 16];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte) EOPCODE.MOVEME.ordinal());
        buffer.putFloat(x);
        buffer.putFloat(y);
        buffer.putFloat(endx);
        buffer.putFloat(endy);
        
        buffer.flip();
        return buffer;
    }

    /**
     * Create an "attack" packet which notifies the server that the sending
     * client is attacking the object with given ID from the given location.
     * @param targetID The ID number of the target.
     * @param x The x coordinate of the position of the attacker
     * @param y The y coordinate of the position of the attacker
     * @return The <code>ByteBuffer</code> "attack" packet.
     */
    public static ByteBuffer createAttackPkt(int targetID, float x, float y) {
        byte[] bytes = new byte[1 + 12];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte) EOPCODE.ATTACK.ordinal());
        buffer.putInt(targetID);
        buffer.putFloat(x);
        buffer.putFloat(y);
        
        buffer.flip();
        return buffer;
    }


    /**
     * Create a "get flag" packet which notifies the server that this client is
     * trying to pick up the flag with given ID.
     * @param flagID The ID number of the flag the client is picking up.
     * @param x The x coordinate of the client's position
     * @param y The y coordinate of the client's position
     * @return The <code>ByteBuffer</code> "get flag" packet.
     */
    public static ByteBuffer createGetFlagPkt(int flagID, float x, float y) {
        byte[] bytes = new byte[1 + 12];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte) EOPCODE.GETFLAG.ordinal());
        buffer.putInt(flagID);
        buffer.putFloat(x);
        buffer.putFloat(y);
        
        buffer.flip();
        return buffer;
    }
    
    /**
     * Create a "score" packet which notifies the server that this client
     * has reached the goal position with the flag and wishes to place the flag
     * down to win the game.
     * @param x The x coordinate of the client's position
     * @param y The y coordinate of the client's position
     * @return The <code>ByteBuffer</code> "place flag" packet
     */
    public static ByteBuffer createScorePkt(float x, float y) {
        byte[] bytes = new byte[1 + 8];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put((byte) EOPCODE.SCORE.ordinal());
        buffer.putFloat(x);
        buffer.putFloat(y);
        
        buffer.flip();
        return buffer;
    }
    
    /**
     * Create a chat message packet with given values.
     * @param message The <code>String</code> message to be displayed.
     * @return The <code>ByteBUffer</code> 'chat message' packet.
     */
    public static ByteBuffer createChatPkt(String message) {
    	byte[] bytes = new byte[1 + 4 + message.length()];
    	ByteBuffer buffer = ByteBuffer.wrap(bytes);
    	buffer.put((byte) EOPCODE.CHAT.ordinal());
    	buffer.putInt(message.length());
    	buffer.put(message.getBytes());
        
        buffer.flip();
    	return buffer;
    }
}
