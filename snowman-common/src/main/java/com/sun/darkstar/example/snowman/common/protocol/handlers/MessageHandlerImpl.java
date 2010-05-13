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

import java.nio.ByteBuffer;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.sun.darkstar.example.snowman.common.protocol.enumn.EMOBType;
import com.sun.darkstar.example.snowman.common.protocol.enumn.EEndState;
import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;
import com.sun.darkstar.example.snowman.common.protocol.enumn.EOPCODE;
import com.sun.darkstar.example.snowman.common.protocol.processor.IProtocolProcessor;
import com.sun.darkstar.example.snowman.common.protocol.processor.IClientProcessor;
import com.sun.darkstar.example.snowman.common.protocol.processor.IServerProcessor;

/**
 * Default implementation of the <code>MessageHandler</code> interface.
 * 
 * @author Yi Wang (Neakor)
 * @author Jeffrey Kesselman
 * @author Owen Kellett
 */
public class MessageHandlerImpl implements MessageHandler
{
    private static MessageHandler instance;
    protected MessageHandlerImpl() {}
    public static MessageHandler getInstance() {
        if(instance == null) {
            MessageHandlerImpl.instance = new MessageHandlerImpl();
        }
        return instance;
    }
    
    /**
     * The <code>Logger</code> instance.
     */
    protected final Logger logger = Logger.getLogger(MessageHandlerImpl.class.toString());
    
    /* {@inheritDoc} */
    public void parseClientPacket(ByteBuffer packet, IClientProcessor processor) {
        EOPCODE code = this.getOpCode(packet);
        this.parseClientPacket(code, packet, processor);
    }
    
    /* {@inheritDoc} */
    public void parseServerPacket(ByteBuffer packet, IServerProcessor processor) {
        EOPCODE code = this.getOpCode(packet);
        this.parseServerPacket(code, packet, processor);
    }
    
    /**
     * Parses the given packet with the given opcode and hands off the data
     * to the appropriate IClientProcessor method with data from the packet
     * sent in as parameters.
     * 
     * @param code opcode of the packet
     * @param packet data packet with the read head at the start of the payload
     * @param unit processing unit to receive and process the data
     */
    protected void parseClientPacket(EOPCODE code, ByteBuffer packet, IClientProcessor unit) {
        switch (code) {
            case NEWGAME:
                int myID = packet.getInt();
                byte[] mapname = new byte[packet.getInt()];
                packet.get(mapname);
                String mapString = new String(mapname);
                logger.log(Level.FINEST, "Processing {0} packet : {1}, {2}", new Object[]{code, myID, mapString});
                unit.newGame(myID, mapString);
                break;
            case STARTGAME:
                unit.startGame();
                logger.log(Level.FINEST, "Processing {0} packet ", code);
                break;
            case ENDGAME:
                EEndState endState = EEndState.values()[packet.getInt()];
                logger.log(Level.FINEST, "Processing {0} packet : {1}", new Object[]{code, endState});
                unit.endGame(endState);
                break;
            case ADDMOB:
                int addId = packet.getInt();
                float addX = packet.getFloat();
                float addY = packet.getFloat();
                EMOBType addType = EMOBType.values()[packet.getInt()];
                ETeamColor addColor = ETeamColor.values()[packet.getInt()];
                byte[] mobNameBytes = new byte[packet.getInt()];
                packet.get(mobNameBytes);
                String mobName = new String(mobNameBytes);
                logger.log(Level.FINEST, "Processing {0} packet : {1}, {2}, {3}, {4}, {5}, {6}", 
                           new Object[]{code, addId, addX, addY, addType, addColor, mobNameBytes});
                unit.addMOB(addId,
                            addX,
                            addY,
                            addType,
                            addColor,
                            mobName);
                break;
            case REMOVEMOB:
                int removeId = packet.getInt();
                logger.log(Level.FINEST, "Processing {0} packet : {1}", new Object[]{code, removeId});
                unit.removeMOB(removeId);
                break;
            case MOVEMOB:
                int moveId = packet.getInt();
                float moveStartX = packet.getFloat();
                float moveStartY = packet.getFloat();
                float moveEndX = packet.getFloat();
                float moveEndY = packet.getFloat();
                logger.log(Level.FINEST, "Processing {0} packet : {1}, {2}, {3}, {4}, {5}",
                           new Object[]{code, moveId, moveStartX, moveStartY, moveEndX, moveEndY});
                unit.moveMOB(moveId,
                             moveStartX,
                             moveStartY,
                             moveEndX,
                             moveEndY);
                break;
            case STOPMOB:
                int stopId = packet.getInt();
                float stopX = packet.getFloat();
                float stopY = packet.getFloat();
                logger.log(Level.FINEST, "Processing {0} packet : {1}, {2}, {3}",
                           new Object[]{code, stopId, stopX, stopY});
                unit.stopMOB(stopId,
                             stopX,
                             stopY);
                break;
            case ATTACHOBJ:
                int attachId1 = packet.getInt();
                int attachId2 = packet.getInt();
                logger.log(Level.FINEST, "Processing {0} packet : {1}, {2}",
                           new Object[]{code, attachId1, attachId2});
                unit.attachObject(attachId1,
                                  attachId2);
                break;
            case ATTACKED:
                int attackId = packet.getInt();
                int attackTarget = packet.getInt();
                int attackHp = packet.getInt();
                logger.log(Level.FINEST, "Processing {0} packet : {1}, {2}, {3}",
                           new Object[]{code, attackId, attackTarget, attackHp});
                unit.attacked(attackId,
                              attackTarget,
                              attackHp);
                break;
            case RESPAWN:
                int respawnId = packet.getInt();
                float respawnX = packet.getFloat();
                float respawnY = packet.getFloat();
                logger.log(Level.FINEST, "Processing {0} packet : {1}, {2}, {3}",
                           new Object[]{code, respawnId, respawnX, respawnY});
                unit.respawn(respawnId,
                             respawnX,
                             respawnY);
                break;
            case CHAT:
                int sourceID = packet.getInt();
                byte[] messageBytes = new byte[packet.getInt()];
                packet.get(messageBytes);
                String message = new String(messageBytes);
                logger.log(Level.FINEST, "Processing {0} packet : {1}, {2}",
                           new Object[]{code, sourceID, message});
                unit.chatMessage(sourceID,
                                 message);
            	break;
            default:
                //divert to common parser
                this.parseCommonPacket(code, packet, unit);
        }
    }
    
    /**
     * Parses the given packet with the given opcode and hands off the data
     * to the appropriate IServerProcessor method with data from the packet
     * sent in as parameters.
     * 
     * @param code opcode of the packet
     * @param packet data packet with the read head at the start of the payload
     * @param unit processing unit to receive and process the data
     */
    protected void parseServerPacket(EOPCODE code, ByteBuffer packet, IServerProcessor unit) {
        switch (code) {
            case MOVEME:
                float moveStartX = packet.getFloat();
                float moveStartY = packet.getFloat();
                float moveEndX = packet.getFloat();
                float moveEndY = packet.getFloat();
                logger.log(Level.FINEST, "Processing {0} packet : {1}, {2}, {3}, {4}",
                           new Object[]{code, moveStartX, moveStartY, moveEndX, moveEndY});
                unit.moveMe(moveStartX,
                            moveStartY,
                            moveEndX,
                            moveEndY);
                break;
            case ATTACK:
                int attackId = packet.getInt();
                float attackX = packet.getFloat();
                float attackY = packet.getFloat();
                logger.log(Level.FINEST, "Processing {0} packet : {1}, {2}, {3}",
                           new Object[]{code, attackId, attackX, attackY});
                unit.attack(attackId,
                            attackX,
                            attackY);
                break;
            case GETFLAG:
                int getflagId = packet.getInt();
                float getflagX = packet.getFloat();
                float getflagY = packet.getFloat();
                logger.log(Level.FINEST, "Processing {0} packet : {1}, {2}, {3}",
                           new Object[]{code, getflagId, getflagX, getflagY});
                unit.getFlag(getflagId,
                             getflagX,
                             getflagY);
                break;
            case SCORE:
                float scoreX = packet.getFloat();
                float scoreY = packet.getFloat();
                logger.log(Level.FINEST, "Processing {0} packet : {1}, {2}",
                           new Object[]{code, scoreX, scoreY});
                unit.score(scoreX,
                           scoreY);
                break;
            case CHAT:
                byte[] messageBytes = new byte[packet.getInt()];
                packet.get(messageBytes);
                String message = new String(messageBytes);
                logger.log(Level.FINEST, "Processing {0} packet : {1}",
                           new Object[]{code, message});
                unit.chatMessage(message);
            	break;
            default:
                //divert to common parser
                this.parseCommonPacket(code, packet, unit);
        }
    }
    
    
    /**
     * Parse the given <code>ByteBuffer</code> packet then invoke the corresponding
     * method in given <code>ProtocolProcessor</code>.
     * @param packet The <code>ByteBuffer</code> packet to be parsed.
     * @param processor The <code>ProtocolProcessor</code> to be invoked.
     */
    private void parseCommonPacket(EOPCODE code, ByteBuffer packet, IProtocolProcessor processor) {
        // Parse common code.
        switch (code) {
            case READY:
                logger.log(Level.FINEST, "Processing {0} packet", code);
                processor.ready();
                break;
            default:
                this.logger.warning("Unsupported OPCODE: " + code.toString());
        }
    }
    
    /**
     * Get the OPCODE from the packet
     * @param packet
     * @return
     */
    private EOPCODE getOpCode(ByteBuffer packet) 
    {
        byte opbyte = packet.get();
        if ((opbyte < 0) || (opbyte > EOPCODE.values().length - 1)) {
            this.logger.severe("Unknown op value: " + opbyte);
            return null;
        }
        EOPCODE code = EOPCODE.values()[opbyte];
        
        return code;
    }
}
