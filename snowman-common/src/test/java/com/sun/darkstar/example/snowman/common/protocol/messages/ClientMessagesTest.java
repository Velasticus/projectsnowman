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

import com.sun.darkstar.example.snowman.common.protocol.enumn.EOPCODE;
import java.nio.ByteBuffer;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test the ClientMessages class
 * 
 * @author Owen Kellett
 */
public class ClientMessagesTest extends AbstractTestMessages
{
    
    @Test
    public void testCreateMoveMePkt() {
        ByteBuffer movePacket = ClientMessages.createMoveMePkt(1.0f, 2.0f, 3.0f, 4.0f);
        checkOpcode(movePacket, EOPCODE.MOVEME);
        
        float startx = movePacket.getFloat();
        float starty = movePacket.getFloat();
        float endx = movePacket.getFloat();
        float endy = movePacket.getFloat();
        
        Assert.assertEquals(1.0f, startx, 0);
        Assert.assertEquals(2.0f, starty, 0);
        Assert.assertEquals(3.0f, endx, 0);
        Assert.assertEquals(4.0f, endy, 0);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(movePacket.hasRemaining());
    }
    
    @Test
    public void testCreateAttackPkt() {
        ByteBuffer attackPacket = ClientMessages.createAttackPkt(5, 1.0f, 2.0f);
        checkOpcode(attackPacket, EOPCODE.ATTACK);
        
        int id = attackPacket.getInt();
        float x = attackPacket.getFloat();
        float y = attackPacket.getFloat();
        
        Assert.assertEquals(5, id);
        Assert.assertEquals(1.0f, x, 0);
        Assert.assertEquals(2.0f, y, 0);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(attackPacket.hasRemaining());
    }
    
    @Test
    public void testCreateGetFlagPkg() {
        ByteBuffer getFlagPacket = ClientMessages.createGetFlagPkt(10, 1.0f, 2.0f);
        checkOpcode(getFlagPacket, EOPCODE.GETFLAG);
        
        int id = getFlagPacket.getInt();
        float x = getFlagPacket.getFloat();
        float y = getFlagPacket.getFloat();
        
        Assert.assertEquals(10, id);
        Assert.assertEquals(1.0f, x, 0);
        Assert.assertEquals(2.0f, y, 0);

        //ensure we are at the end of the buffer
        Assert.assertFalse(getFlagPacket.hasRemaining());
    }
    
    @Test
    public void testCreateScorePkt() {
        ByteBuffer packet = ClientMessages.createScorePkt(1.0f, 2.0f);
        checkOpcode(packet, EOPCODE.SCORE);
        
        float x = packet.getFloat();
        float y = packet.getFloat();
        
        Assert.assertEquals(1.0f, x, 0);
        Assert.assertEquals(2.0f, y, 0);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    @Test
    public void testCreateChatPkt() {
        ByteBuffer packet = ClientMessages.createChatPkt("message");
        checkOpcode(packet, EOPCODE.CHAT);
        
        int length = packet.getInt();
        byte[] messageBytes = new byte[length];
        packet.get(messageBytes);
        String messageString = new String(messageBytes);

        Assert.assertEquals(length, "message".length());
        Assert.assertEquals(messageString, "message");
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
}
