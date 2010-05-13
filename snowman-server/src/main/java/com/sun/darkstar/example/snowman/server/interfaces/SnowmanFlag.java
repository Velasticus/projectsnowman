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

/**
 * The {@code SnowmanFlag} interface defines the basic behavior
 * of a flag in the snowman game.
 * 
 * @author Owen Kellett
 */
public interface SnowmanFlag extends DynamicEntity
{
    /**
     * Returns the color of the team that owns this flag.
     * @return the flag's color
     */
    ETeamColor getTeamColor();

    /**
     * This method sets the flag as held by a snowman.  Passing null sets it as 
     * held by no snowman.
     * @param player the snowman who holds the flag, or null
     */
    void setHeldBy(SnowmanPlayer player);
    
    /**
     * Relocates the flag to its original home position.
     */
    void returnFlag();
    
    /**
     * Called when a player holding the flag dies, or disconnects from the game.
     * The flag is dropped and placed in the given fixed position in the world.
     * 
     * @param x the x coordinate to drop the flag
     * @param y the y coordinate to drop the flag
     */
    void drop(float x, float y);

    /**
     * Checks if the flag is currently being held by a player.
     * 
     * @return true if the flag is currently held
     */
    boolean isHeld();
    
    /**
     * Returns the x coordinate of the goal location.
     * 
     * @return the x coordinat of the goal location
     */
    float getGoalX();
    
    /**
     * Returns the y coordinate of the goal location.
     * 
     * @return the y coordinate of the goal location
     */
    float getGoalY();
    
}
