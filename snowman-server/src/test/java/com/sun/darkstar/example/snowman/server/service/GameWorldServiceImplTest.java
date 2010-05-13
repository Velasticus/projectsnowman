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

import com.sun.darkstar.example.snowman.common.util.SingletonRegistry;
import com.sun.darkstar.example.snowman.common.util.CollisionManager;
import com.sun.darkstar.example.snowman.common.util.DataImporter;
import com.sun.darkstar.example.snowman.common.util.enumn.EWorld;
import com.sun.darkstar.example.snowman.common.util.Coordinate;
import com.sun.sgs.kernel.ComponentRegistry;
import com.sun.sgs.service.TransactionProxy;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.bounding.BoundingBox;
import com.jme.system.DisplaySystem;
import com.jme.math.Vector3f;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.easymock.EasyMock;
import java.util.Properties;

/**
 * Verify behavior of the <code>GameWorldServiceImpl</code>
 * 
 * @author Owen Kellett
 */
public class GameWorldServiceImplTest
{
    /** mock darkstar kernel interfaces */
    private ComponentRegistry mockRegistry;
    private TransactionProxy mockTxnProxy;
    
    /** mock singletons */
    private DataImporter mockDataImporter;
    private CollisionManager mockCollisionManager;
    
    /** dummy spatial object representing dummy game world */
    private static Spatial dummyWorld;
    
    @Before
    public void mockKernelInterfaces() {
        //create the mock objects to be sent to the constructor
        this.mockRegistry = EasyMock.createMock(ComponentRegistry.class);
        this.mockTxnProxy = EasyMock.createMock(TransactionProxy.class);
        
        //create the dummy world
        dummyWorld = new Box();
        dummyWorld.setModelBound(new BoundingBox());
        dummyWorld.updateModelBound();

        //create the mock singletons
        this.mockDataImporter = EasyMock.createMock(DataImporter.class);
        this.mockCollisionManager = EasyMock.createMock(CollisionManager.class);
        
        //configure behavior of the data importer to return a dummy world
        EasyMock.expect(mockDataImporter.getWorld(EWorld.Battle)).andReturn(dummyWorld);
        EasyMock.replay(mockDataImporter);
        
        //load the singletons into the registry
        SingletonRegistry.setDataImporter(mockDataImporter);
        SingletonRegistry.setCollisionManager(mockCollisionManager);
    }
    
    /**
     * Verify that the trimPath method returns a Coordinate with the
     * the X and Z coordinates of the resulting Vector3f from the
     * CollisionManager
     */
    @Test
    public void testTrimPath() throws Exception {
        //prepare test data
        float startx = 1.0f;
        float starty = 2.0f;
        float endx = 10.0f;
        float endy = 11.0f;
        float realEndx = 5.0f;
        float realEndy = 6.0f;
        Vector3f dummyDestination = new Vector3f(realEndx, 100.0f, realEndy);
        
        //prepare the mock CollisionManager
        EasyMock.expect(mockCollisionManager.getDestination(startx, starty, endx, endy, dummyWorld)).andReturn(dummyDestination);
        EasyMock.replay(mockCollisionManager);
        
        //create the GameWorldService with the mock environment
        GameWorldServiceImpl service = new GameWorldServiceImpl(new Properties(),
                                                                mockRegistry,
                                                                mockTxnProxy);
        
        //call the service
        Coordinate realDestination = service.trimPath(new Coordinate(startx, starty),
                                                      new Coordinate(endx, endy));
        
        Assert.assertEquals(realDestination.getX(), realEndx, 0);
        Assert.assertEquals(realDestination.getY(), realEndy, 0);
    }
    
    /**
     * Verify that the validThrow method returns true
     * when there is no collision returned by the
     * CollisionManager
     */
    @Test
    public void testValidThrowTrue()
    {
        //prepare test data
        float startx = 1.0f;
        float starty = 2.0f;
        float endx = 10.0f;
        float endy = 11.0f;

        //prepare the mock CollisionManager
        EasyMock.expect(mockCollisionManager.validate(startx, starty, endx, endy, dummyWorld)).andReturn(true);
        EasyMock.replay(mockCollisionManager);
        
        //create the GameWorldService with the mock environment
        GameWorldServiceImpl service = new GameWorldServiceImpl(new Properties(),
                                                                mockRegistry,
                                                                mockTxnProxy);
        
        //call the service
        boolean valid = service.validThrow(new Coordinate(startx, starty),
                                           new Coordinate(endx, endy));
        
        Assert.assertTrue(valid);
    }
    
    /**
     * Verify that the validThrow method returns false
     * when there is no collision returned by the
     * CollisionManager
     */
    @Test
    public void testValidThrowFalse()
    {
        //prepare test data
        float startx = 1.0f;
        float starty = 2.0f;
        float endx = 10.0f;
        float endy = 11.0f;

        //prepare the mock CollisionManager
        EasyMock.expect(mockCollisionManager.validate(startx, starty, endx, endy, dummyWorld)).andReturn(false);
        EasyMock.replay(mockCollisionManager);
        
        //create the GameWorldService with the mock environment
        GameWorldServiceImpl service = new GameWorldServiceImpl(new Properties(),
                                                                mockRegistry,
                                                                mockTxnProxy);
        
        //call the service
        boolean valid = service.validThrow(new Coordinate(startx, starty),
                                           new Coordinate(endx, endy));
        
        Assert.assertFalse(valid);
    }
    
    
    @After 
    public void cleanupMocks() {
        this.mockRegistry = null;
        this.mockTxnProxy = null;
        this.dummyWorld = null;
    }
    
    @After
    public void cleanupSingletons() {
        SingletonRegistry.setDataImporter(null);
        SingletonRegistry.setCollisionManager(null);
    }
    
    @After
    public void cleanupJME() {
        DisplaySystem.resetSystemProvider();
    }

}
