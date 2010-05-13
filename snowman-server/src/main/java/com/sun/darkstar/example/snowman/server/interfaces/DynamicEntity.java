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

import com.sun.sgs.app.ManagedObject;

/**
 * A {@code DynamicEntity} represents a movable entity in the snowman
 * world.  This could be a player, a flag, a campfire, etc.
 * 
 * @author Owen Kellett
 */
public interface DynamicEntity extends ManagedObject
{
    /**
     * This method sets the id.
     * @param i the ID
     */
    void setID(int i);
    
    /**
     * This method gets the id.
     * @return the entity's ID
     */
    int getID();
    
    /**
     * Set the location of the entity in the world.
     * @param x the x coordinate of the entity
     * @param y the y coordinate of the entity
     */
    void setLocation(float x, float y);
    
    /**
     * Get the X coordinate of the entity in the world at the given time.
     * @return the X coordinate of the entity
     */
    float getX();
    
    /**
     * Get the Y coordinate of the entity in the world at the given time.
     * @return the Y coordinate of the entity
     */
    float getY();

}
