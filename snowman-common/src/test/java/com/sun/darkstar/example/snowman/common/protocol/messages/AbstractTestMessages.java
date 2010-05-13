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
 * Test the Messages class
 * 
 * @author Owen Kellett
 */
public class AbstractTestMessages 
{
    /**
     * Verify that READY packet contains ready opcode and
     * timestamp
     */
    @Test
    public void testCreateReadyPkt() {
        ByteBuffer readyPacket = Messages.createReadyPkt();
        checkOpcode(readyPacket, EOPCODE.READY);
        
        //ensure we are at the end of the buffer
        Assert.assertFalse(readyPacket.hasRemaining());
    }
    
    /**
     * Verify the standard header of the packet.
     * First check that the given opcode matches the first byte of
     * the packet. 
     * @param packet packet to check
     * @param opcode opcode to verify against
     */
    protected void checkOpcode(ByteBuffer packet, EOPCODE opcode) {
        byte opbyte = packet.get();
        Assert.assertTrue((opbyte >= 0) && (opbyte < EOPCODE.values().length));

        EOPCODE code = EOPCODE.values()[opbyte];
        Assert.assertTrue(code == opcode);
    }
}
