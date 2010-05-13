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

package com.sun.darkstar.example.snowman.server.service;

import com.sun.darkstar.example.snowman.common.util.Coordinate;

/**
 * The {@code GameWorldManagerImpl} implements the {@link GameWorldManager}
 * and provides application level access
 * to the {@link GameWorldService} running inside of the 
 * Project Darkstar stack.  
 * 
 * @author Owen Kellett
 */
public class GameWorldManagerImpl implements GameWorldManager {
    
    private final GameWorldService backingService;
    
    /**
     * Constructs a {@code GameWorldManager} backed by the given service.
     * 
     * @param backingService the backing {@code GameWorldService}
     */
    public GameWorldManagerImpl(GameWorldService backingService) {
        this.backingService = backingService;
    }
    
    // The following methods are synchronized to avoid problems with the
    // jme code not being thread-safe. This will single-thread all tasks
    // that do collision detection... so a more perminant solution needs
    // to be found.
    
    /** {@inheritDoc} */
    public synchronized Coordinate trimPath(Coordinate start, 
                               Coordinate end) {
        return backingService.trimPath(start, end);
    }

    /** {@inheritDoc} */
    public synchronized boolean validThrow(Coordinate start,
                              Coordinate end) {
        return backingService.validThrow(start, end);
    }

}
