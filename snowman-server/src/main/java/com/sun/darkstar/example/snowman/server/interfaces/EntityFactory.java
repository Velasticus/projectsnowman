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

import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;
import com.sun.darkstar.example.snowman.common.util.Coordinate;
import com.sun.sgs.app.ClientSession;

/**
 * Factory to create entities in the game world.
 * 
 * @author Owen Kellett
 */
public interface EntityFactory 
{
    /**
     * Creates a new {@code SnowmanPlayer} object that is associated with
     * the given {@link ClientSession}.
     * 
     * @param session the session of the connected player
     * @return a {@code SnowmanPlayer} associated with the connected player
     */
    SnowmanPlayer createSnowmanPlayer(ClientSession session);
    
    /**
     * Creates a new {@code SnowmanPlayer} which is an AI robot.
     * 
     * @param name the name of the robot
     * @param delay the time delay to wait before the robot makes its first move
     * @return a {@code SnowmanPlayer} representing a new robot player
     */
    SnowmanPlayer createRobotPlayer(String name,
                                    int delay);
    
    /**
     * Creates a new {@code SnowmanFlag}.
     * 
     * @param game the {@code SnowmanGame} the new flag is to be a part of
     * @param teamColor the color of the team that owns the flag
     * @param flagHome the location of the flag's home position
     * @param flagGoal the location of the flag's goal position
     * @return a new {@code SnowmanFlag}
     */
    SnowmanFlag createSnowmanFlag(SnowmanGame game,
                                  ETeamColor teamColor,
                                  Coordinate flagHome,
                                  Coordinate flagGoal);
}
