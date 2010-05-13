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
import com.sun.darkstar.example.snowman.common.protocol.enumn.EEndState;
import com.sun.darkstar.example.snowman.common.protocol.enumn.EMOBType;
import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;
import java.nio.ByteBuffer;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test the ServerMessages
 * 
 * @author Owen Kellett
 */
public class ServerMessagesTest extends AbstractTestMessages
{
    
    @Test
    public void testCreateNewgamePkt() {
        ByteBuffer packet = ServerMessages.createNewGamePkt(10, "map");
        checkOpcode(packet, EOPCODE.NEWGAME);
        
        int id = packet.getInt();
        int length = packet.getInt();
        byte[] mapname = new byte[length];
        packet.get(mapname);
        String mapnameString = new String(mapname);
        
        Assert.assertEquals(id, 10);
        Assert.assertEquals(length, "map".length());
        Assert.assertEquals(mapnameString, "map");
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    @Test
    public void testCreateStartgamePkt() {
        ByteBuffer packet = ServerMessages.createStartGamePkt();
        checkOpcode(packet, EOPCODE.STARTGAME);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    @Test
    public void testCreateEndgamePkt() {
        ByteBuffer packet = ServerMessages.createEndGamePkt(EEndState.RedWin);
        checkOpcode(packet, EOPCODE.ENDGAME);
        
        EEndState state = EEndState.values()[packet.getInt()];
        
        Assert.assertEquals(state, EEndState.RedWin);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    @Test
    public void testCreateAddMOBPkt() {
        ByteBuffer packet = ServerMessages.createAddMOBPkt(10, 1.0f, 2.0f, EMOBType.SNOWMAN, ETeamColor.Red, "name");
        checkOpcode(packet, EOPCODE.ADDMOB);
        
        int id = packet.getInt();
        float x = packet.getFloat();
        float y = packet.getFloat();
        EMOBType type = EMOBType.values()[packet.getInt()];
        ETeamColor team = ETeamColor.values()[packet.getInt()];
        int length = packet.getInt();
        byte[] mobNameBytes = new byte[length];
        packet.get(mobNameBytes);
        String mobName = new String(mobNameBytes);
        
        Assert.assertEquals(id, 10);
        Assert.assertEquals(x, 1.0f, 0);
        Assert.assertEquals(y, 2.0f, 0);
        Assert.assertEquals(type, EMOBType.SNOWMAN);
        Assert.assertEquals(team, ETeamColor.Red);
        Assert.assertEquals(length, "name".length());
        Assert.assertEquals(mobName, "name");
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    @Test
    public void testCreateRemoveMOBPkt() {
        ByteBuffer packet = ServerMessages.createRemoveMOBPkt(10);
        checkOpcode(packet, EOPCODE.REMOVEMOB);
        
        int id = packet.getInt();

        Assert.assertEquals(id, 10);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    @Test
    public void testCreateMoveMOBPkt() {
        ByteBuffer packet = ServerMessages.createMoveMOBPkt(10, 1.0f, 2.0f, 3.0f, 4.0f);
        checkOpcode(packet, EOPCODE.MOVEMOB);
        
        int id = packet.getInt();
        float startx = packet.getFloat();
        float starty = packet.getFloat();
        float endx = packet.getFloat();
        float endy = packet.getFloat();

        Assert.assertEquals(id, 10);
        Assert.assertEquals(startx, 1.0f, 0);
        Assert.assertEquals(starty, 2.0f, 0);
        Assert.assertEquals(endx, 3.0f, 0);
        Assert.assertEquals(endy, 4.0f, 0);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    @Test
    public void testCreateStopMOBPkt() {
        ByteBuffer packet = ServerMessages.createStopMOBPkt(10, 1.0f, 2.0f);
        checkOpcode(packet, EOPCODE.STOPMOB);
        
        int id = packet.getInt();
        float x = packet.getFloat();
        float y = packet.getFloat();

        Assert.assertEquals(id, 10);
        Assert.assertEquals(x, 1.0f, 0);
        Assert.assertEquals(y, 2.0f, 0);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    @Test
    public void testCreateAttachObjPkt() {
        ByteBuffer packet = ServerMessages.createAttachObjPkt(10, 20);
        checkOpcode(packet, EOPCODE.ATTACHOBJ);
        
        int source = packet.getInt();
        int target = packet.getInt();

        Assert.assertEquals(source, 10);
        Assert.assertEquals(target, 20);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    @Test
    public void testCreateAttackedPkt() {
        ByteBuffer packet = ServerMessages.createAttackedPkt(10, 20, 99);
        checkOpcode(packet, EOPCODE.ATTACKED);
        
        int source = packet.getInt();
        int target = packet.getInt();
        int hp = packet.getInt();

        Assert.assertEquals(source, 10);
        Assert.assertEquals(target, 20);
        Assert.assertEquals(hp, 99);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    @Test
    public void testCreateRespawnPkt() {
        ByteBuffer packet = ServerMessages.createRespawnPkt(10, 1.0f, 2.0f);
        checkOpcode(packet, EOPCODE.RESPAWN);
        
        int id = packet.getInt();
        float x = packet.getFloat();
        float y = packet.getFloat();

        Assert.assertEquals(id, 10);
        Assert.assertEquals(x, 1.0f, 0);
        Assert.assertEquals(y, 2.0f, 0);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    @Test
    public void testCreateChatPkt() {
        ByteBuffer packet = ServerMessages.createChatPkt(10, "message");
        checkOpcode(packet, EOPCODE.CHAT);
        
        int id = packet.getInt();
        int length = packet.getInt();
        byte[] messageBytes = new byte[length];
        packet.get(messageBytes);
        String messageString = new String(messageBytes);

        Assert.assertEquals(id, 10);
        Assert.assertEquals(length, "message".length());
        Assert.assertEquals(messageString, "message");
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
}
