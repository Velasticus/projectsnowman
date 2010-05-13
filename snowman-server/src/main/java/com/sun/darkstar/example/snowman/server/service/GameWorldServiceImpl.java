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

import java.util.Properties;
import java.util.logging.Logger;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jme.system.dummy.DummySystemProvider;
import com.sun.darkstar.example.snowman.common.util.Coordinate;
import com.sun.darkstar.example.snowman.common.util.SingletonRegistry;
import com.sun.darkstar.example.snowman.common.util.enumn.EWorld;
import com.sun.darkstar.example.snowman.server.impl.SnowmanMapInfo;
import com.sun.sgs.kernel.ComponentRegistry;
import com.sun.sgs.service.TransactionProxy;


/**
 * The {@code GameWorldServiceImpl} is an implementation of a Project
 * Darkstar service that provides utility methods to calculate
 * collision detection and spatial information against the static
 * game world geometry.
 * 
 * @author Owen Kellett
 */
public class GameWorldServiceImpl implements GameWorldService {
    
    /** The logger for this class. */
    private static final Logger logger = 
            Logger.getLogger(GameWorldServiceImpl.class.getName());
    
    /** Current game world **/
    private Spatial gameWorld;

    /**
     * Constructs a {@code GameWorldService} that is initialized to
     * use a static game world to calculated collisions.
     * 
     * @param properties application properties
     * @param registry registry of darkstar components
     * @param txnProxy transaction proxy
     */
    public GameWorldServiceImpl(Properties properties,
                                ComponentRegistry registry,
                                TransactionProxy txnProxy) {
        //create dummy display system so that the JME importer doesn't complain
        DummySystemProvider provider = new DummySystemProvider();
	DisplaySystem.setSystemProvider(provider);
        
        this.gameWorld = 
                SingletonRegistry.getDataImporter().getWorld(EWorld.Battle);
        this.gameWorld.updateGeometricState(0, false);
        SnowmanMapInfo.setDimensions(
                gameWorld.getWorldBound().getCenter().getX() * 2.0f,
                gameWorld.getWorldBound().getCenter().getZ() * 2.0f);
    }

    /** {@inheritDoc} **/
    public String getName() {
        return this.getClass().getName();
    }

    /** {@inheritDoc} **/
    public void ready() throws Exception {

    }

    /** {@inheritDoc} **/
    public void shutdown() {

    }
    
    /** {@inheritDoc} */
    public Coordinate trimPath(Coordinate start,
                               Coordinate end) {
        Vector3f destination = SingletonRegistry.getCollisionManager().
                getDestination(start.getX(), 
                               start.getY(),
                               end.getX(),
                               end.getY(),
                               gameWorld);
        return new Coordinate(destination.getX(), destination.getZ());

    }
    
    /** {@inheritDoc} */
    public boolean validThrow(Coordinate start,
                              Coordinate end) {
        return SingletonRegistry.getCollisionManager().validate(start.getX(),
                                                                start.getY(), 
                                                                end.getX(), 
                                                                end.getY(), 
                                                                gameWorld);
    }
}
