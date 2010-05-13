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

package com.sun.darkstar.example.snowman.common.protocol.handlers;

import com.sun.darkstar.example.snowman.common.protocol.enumn.EMOBType;
import com.sun.darkstar.example.snowman.common.protocol.enumn.EEndState;
import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;
import com.sun.darkstar.example.snowman.common.protocol.processor.IServerProcessor;
import com.sun.darkstar.example.snowman.common.protocol.processor.IClientProcessor;
import com.sun.darkstar.example.snowman.common.protocol.messages.Messages;
import com.sun.darkstar.example.snowman.common.protocol.messages.ServerMessages;
import com.sun.darkstar.example.snowman.common.protocol.messages.ClientMessages;
import java.nio.ByteBuffer;
import org.junit.Test;
import org.junit.Assert;
import org.easymock.EasyMock;

/**
 * Test the MessageHandlerImpl
 * 
 * @author Owen Kellett
 */
public class MessageHandlerImplTest
{
    @Test
    public void parseServerReadyPkt() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        
        ByteBuffer readyPacket = Messages.createReadyPkt();
        
        IServerProcessor mockProcessor = EasyMock.createMock(IServerProcessor.class);
        //record expected processor calls
        mockProcessor.ready();
        EasyMock.replay(mockProcessor);
        
        parser.parseServerPacket(readyPacket, mockProcessor);
        
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(readyPacket.hasRemaining());
    }
    
    @Test
    public void parseClientReadyPkt() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        
        ByteBuffer readyPacket = Messages.createReadyPkt();
        
        IClientProcessor mockProcessor = EasyMock.createMock(IClientProcessor.class);
        //record expected processor calls
        mockProcessor.ready();
        EasyMock.replay(mockProcessor);
        
        parser.parseClientPacket(readyPacket, mockProcessor);
        
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(readyPacket.hasRemaining());
    }
    
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseNewgame() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IClientProcessor mockProcessor = EasyMock.createMock(IClientProcessor.class);

        // generate packet
        ByteBuffer newgame = ServerMessages.createNewGamePkt(10, "map");
        // record expected processor calls
        mockProcessor.newGame(10, "map");
        EasyMock.replay(mockProcessor);
        // send it to the parser
        parser.parseClientPacket(newgame, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(newgame.hasRemaining());
    }
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseStartgame() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IClientProcessor mockProcessor = EasyMock.createMock(IClientProcessor.class);

        // generate packet
        ByteBuffer packet = ServerMessages.createStartGamePkt();
        // record expected processor calls
        mockProcessor.startGame();
        EasyMock.replay(mockProcessor);
        // send it to the parser
        parser.parseClientPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseEndgame() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IClientProcessor mockProcessor = EasyMock.createMock(IClientProcessor.class);

        // generate packet
        ByteBuffer packet = ServerMessages.createEndGamePkt(EEndState.RedWin);
        // record expected processor calls
        mockProcessor.endGame(EEndState.RedWin);
        EasyMock.replay(mockProcessor);        
        // send it to the parser
        parser.parseClientPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseAddMOB() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IClientProcessor mockProcessor = EasyMock.createMock(IClientProcessor.class);

        // generate packet
        ByteBuffer packet = ServerMessages.createAddMOBPkt(10, 1.0f, 2.0f, EMOBType.SNOWMAN, ETeamColor.Red, "name");
        // record expected processor calls
        mockProcessor.addMOB(10, 1.0f, 2.0f, EMOBType.SNOWMAN, ETeamColor.Red, "name");
        EasyMock.replay(mockProcessor);        
        // send it to the parser
        parser.parseClientPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseRemoveMOB() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IClientProcessor mockProcessor = EasyMock.createMock(IClientProcessor.class);

        // generate packet
        ByteBuffer packet = ServerMessages.createRemoveMOBPkt(10);
        // record expected processor calls
        mockProcessor.removeMOB(10);
        EasyMock.replay(mockProcessor);        
        // send it to the parser
        parser.parseClientPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseMoveMOB() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IClientProcessor mockProcessor = EasyMock.createMock(IClientProcessor.class);

        // generate packet
        ByteBuffer packet = ServerMessages.createMoveMOBPkt(10, 1.0f, 2.0f, 3.0f, 4.0f);
        // record expected processor calls
        mockProcessor.moveMOB(10, 1.0f, 2.0f, 3.0f, 4.0f);
        EasyMock.replay(mockProcessor);        
        // send it to the parser
        parser.parseClientPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseStopMOB() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IClientProcessor mockProcessor = EasyMock.createMock(IClientProcessor.class);

        // generate packet
        ByteBuffer packet = ServerMessages.createStopMOBPkt(10, 1.0f, 2.0f);
        // record expected processor calls
        mockProcessor.stopMOB(10, 1.0f, 2.0f);
        EasyMock.replay(mockProcessor);        
        // send it to the parser
        parser.parseClientPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseAttachObj() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IClientProcessor mockProcessor = EasyMock.createMock(IClientProcessor.class);

        // generate packet
        ByteBuffer packet = ServerMessages.createAttachObjPkt(10, 20);
        // record expected processor calls
        mockProcessor.attachObject(10, 20);
        EasyMock.replay(mockProcessor);        
        // send it to the parser
        parser.parseClientPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseAttacked() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IClientProcessor mockProcessor = EasyMock.createMock(IClientProcessor.class);

        // generate packet
        ByteBuffer packet = ServerMessages.createAttackedPkt(10, 20, 99);
        // record expected processor calls
        mockProcessor.attacked(10, 20, 99);
        EasyMock.replay(mockProcessor);        
        // send it to the parser
        parser.parseClientPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseRespawn() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IClientProcessor mockProcessor = EasyMock.createMock(IClientProcessor.class);

        // generate packet
        ByteBuffer packet = ServerMessages.createRespawnPkt(10, 1.0f, 2.0f);
        // record expected processor calls
        mockProcessor.respawn(10, 1.0f, 2.0f);
        EasyMock.replay(mockProcessor);        
        // send it to the parser
        parser.parseClientPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseMoveMe() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IServerProcessor mockProcessor = EasyMock.createMock(IServerProcessor.class);

        // generate packet
        ByteBuffer packet = ClientMessages.createMoveMePkt(1.0f, 2.0f, 3.0f, 4.0f);
        // record expected processor calls
        mockProcessor.moveMe(EasyMock.eq(1.0f),
                             EasyMock.eq(2.0f),
                             EasyMock.eq(3.0f),
                             EasyMock.eq(4.0f));
        EasyMock.replay(mockProcessor);        
        // send it to the parser
        parser.parseServerPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseAttack() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IServerProcessor mockProcessor = EasyMock.createMock(IServerProcessor.class);

        // generate packet
        ByteBuffer packet = ClientMessages.createAttackPkt(10, 1.0f, 2.0f);
        // record expected processor calls
        mockProcessor.attack(EasyMock.eq(10),
                             EasyMock.eq(1.0f),
                             EasyMock.eq(2.0f));
        EasyMock.replay(mockProcessor);        
        // send it to the parser
        parser.parseServerPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseGetFlag() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IServerProcessor mockProcessor = EasyMock.createMock(IServerProcessor.class);

        // generate packet
        ByteBuffer packet = ClientMessages.createGetFlagPkt(10, 1.0f, 2.0f);
        // record expected processor calls
        mockProcessor.getFlag(10, 1.0f, 2.0f);
        EasyMock.replay(mockProcessor);        
        // send it to the parser
        parser.parseServerPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseScore() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IServerProcessor mockProcessor = EasyMock.createMock(IServerProcessor.class);

        // generate packet
        ByteBuffer packet = ClientMessages.createScorePkt(1.0f, 2.0f);
        // record expected processor calls
        mockProcessor.score(EasyMock.eq(1.0f),
                            EasyMock.eq(2.0f));
        EasyMock.replay(mockProcessor);        
        // send it to the parser
        parser.parseServerPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseServerChat() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IServerProcessor mockProcessor = EasyMock.createMock(IServerProcessor.class);

        // generate packet
        ByteBuffer packet = ClientMessages.createChatPkt("message");
        // record expected processor calls
        mockProcessor.chatMessage("message");
        EasyMock.replay(mockProcessor);
        
        // send it to the parser
        parser.parseServerPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }
    
    /**
     * Test that the proper processor methods are called when
     * packets are sent to the ClientProtocol
     */
    @Test
    public void parseClientChat() {
        MessageHandlerImpl parser = new MessageHandlerImpl();
        IClientProcessor mockProcessor = EasyMock.createMock(IClientProcessor.class);

        // generate packet
        ByteBuffer packet = ServerMessages.createChatPkt(10, "message");
        // record expected processor calls
        mockProcessor.chatMessage(10, "message");
        EasyMock.replay(mockProcessor);        
        // send it to the parser
        parser.parseClientPacket(packet, mockProcessor);
        //verify
        EasyMock.verify(mockProcessor);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(packet.hasRemaining());
    }

    
}
