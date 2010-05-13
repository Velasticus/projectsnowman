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

package com.sun.darkstar.example.snowman.common.util;

import com.sun.darkstar.example.snowman.common.util.enumn.EStats;
import com.jme.scene.Spatial;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jme.scene.shape.Pyramid;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.math.Ray;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;

/**
 *
 * @author Owen Kellett
 */
public class CollisionManagerTest {
    
    /** Acceptable delta for float comparisons */
    private static float DELTA = 0.01f;
    
    /** Test world of objects */
    private Node testWorld;
    private Box box;
    private Sphere sphere;
    private Pyramid pyramid;
    
    /**
     * The test world is created with three objects lined up in a row
     * A Box with size 10,10,10 with its center at position 0, 0, 20
     * A Sphere with radius 5 with its center at position 0, 0, 40
     * A Pyramid with base size 10x10 and height 10 with center at position 0, 0, 60
     */
    @Before
    public void createTestWorld() {
        //create and position the box
        box = new Box("TestBox", new Vector3f(0f,0f,0f), new Vector3f(10f,10f,10f));
        box.setLocalTranslation(new Vector3f(-5f, -5f, 15f));
        box.setModelBound(new BoundingBox());
        box.updateModelBound();
        
        //create and position the sphere
        sphere = new Sphere("TestSphere", new Vector3f(0f,0f,40f), 10, 10, 5f);
        sphere.setModelBound(new BoundingSphere());
        sphere.updateModelBound();
        
        //create and position the pyramid
        pyramid = new Pyramid("TestPyramid", 10f, 10f);
        pyramid.setLocalTranslation(new Vector3f(0f, -5f, 55f));
        pyramid.setModelBound(new BoundingBox());
        pyramid.updateModelBound();
        
        //create the world and add the objects to it
        testWorld = new Node("TestWorld");
        testWorld.attachChild(box);
        testWorld.attachChild(sphere);
        testWorld.attachChild(pyramid);
        
        //update the bounding geometry for the world
        testWorld.setModelBound(new BoundingBox());
        testWorld.updateModelBound();
        testWorld.updateWorldBound();
    }
    
    /**
     * Move the test world in the world coordinate system
     * 
     * @param transpose vector to use to move the world
     */
    private void moveWorld(Vector3f transpose) {
        testWorld.setLocalTranslation(transpose);
        testWorld.updateGeometricState(0.0f, true);
    }
    
    
    /**
     * This method will check the getIntersectObject method of the
     * CollisionManager by sending a Ray which goes through all objects
     * of the testWorld.  It will look for the given Class type of object,
     * and should return the given result Node
     * 
     * @param reference Class type to check for an intersection
     * @param result result node to expect
     * @param iterate whether or not to iterate through all collided objects
     */
    private void testGetIntersectObject(Class<? extends Spatial> reference, Spatial result, boolean iterate) {
        //create the ray that goes through all objects
        Vector3f origin = new Vector3f(0f,0f,0f);
        Vector3f destination = new Vector3f(0f,0f,100f);
        Vector3f direction = destination.subtractLocal(origin).normalizeLocal();
        
        Ray ray = new Ray(origin, direction);
        
        //call the collision manager
        Spatial actualResult = SingletonRegistry.getCollisionManager().getIntersectObject(ray, testWorld, reference, iterate);
        
        //verify that the resulting node is the same as the expected result
        Assert.assertSame(actualResult, result);
    }

    /**
     * Verify that a Ray which goes through all three objects checking
     * for a box intersection will find the TestBox
     */
    @Test
    public void testGetIntersectObjectIterateBox() {
        testGetIntersectObject(Box.class, box, true);
    }
    
    /**
     * Verify that a Ray which goes through all three objects checking
     * for a sphere intersection will find the TestSphere
     */
    @Test
    public void testGetIntersectObjectIterateSphere() {
        testGetIntersectObject(Sphere.class, sphere, true);
    }
    
    /**
     * Verify that a Ray which goes through all three objects checking
     * for a pyramid intersection will find the TestPyramid
     */
    @Test
    public void testGetIntersectObjectIteratePyramid() {
        testGetIntersectObject(Pyramid.class, pyramid, true);
    }
    
    /**
     * Verify that a Ray which goes through all three objects checking
     * for a box intersection will find the TestBox when iterating (since
     * the box is the first collision)
     */
    @Test
    public void testGetIntersectObjectNoIterateBox() {
        testGetIntersectObject(Box.class, box, false);
    }
    
    /**
     * Verify that a Ray which goes through all three objects checking
     * for a sphere intersection will not find the TestSphere (since the
     * sphere is not the first collision)
     */
    @Test
    public void testGetIntersectObjectNoIterateSphere() {
        testGetIntersectObject(Sphere.class, null, false);
    }
    
    /**
     * Verify that a Ray which goes through all three objects checking
     * for a pyramid intersection will not find the TestPyramid (since
     * the pyramid is not the first collision)
     */
    @Test
    public void testGetIntersectObjectNoIteratePyramid() {
        testGetIntersectObject(Pyramid.class, null, false);
    }
    
    /**
     * Verify that if a Ray is sent that does not intersect with
     * any objects in the world, null will be returned from
     * getIntersectObject
     */
    @Test
    public void testGetIntersectObjectNoCollision() {
        //create the ray that doesn't go through any objects
        Vector3f origin = new Vector3f(0f,0f,0f);
        Vector3f destination = new Vector3f(0f,0f,-100f);
        
        Ray ray = new Ray(origin, destination);
        
        //call the collision manager
        Spatial actualResult = SingletonRegistry.getCollisionManager().getIntersectObject(ray, testWorld, Box.class, true);
        
        //verify that the resulting node is null
        Assert.assertSame(actualResult, null);
    }
    
    
    /**
     * Verify the getIntersection method works on a positive test
     * against the box in the test world.  If the local parameter is set to true,
     * the testWorld will be moved relative to the world so that its local
     * coordinates are different than the world's.
     * 
     * @param nullVector true if we should use a null vector as a parameter in the test
     * @param local true if the result should be converted to local coordinates
     */
    private void testGetIntersectionWithBox(boolean nullVector, boolean local) {
        //create the ray that goes through all objects
        Vector3f origin = new Vector3f(0f,0f,0f);
        Vector3f destination = new Vector3f(0f,0f,100f);
        Vector3f direction = destination.subtractLocal(origin).normalizeLocal();
        Ray ray = new Ray(origin, direction);
        
        //intersection should always be at this point
        Vector3f intersection = new Vector3f(0f,0f,15f);
        
        //configure the store
        Vector3f store = null;
        if(!nullVector)
            store = new Vector3f();
        
        //if we are converting to local coordinates, move the test world and
        //the ray so that we can test the transposition of the result
        if(local) {
            Vector3f transpose = new Vector3f(50f,50f,50f);
            origin.addLocal(transpose);
            destination.addLocal(transpose);
            direction = destination.subtractLocal(origin).normalizeLocal();
            ray = new Ray(origin, direction);
            moveWorld(transpose);
        }
        
        //call the collision manager
        Vector3f result = SingletonRegistry.getCollisionManager().getIntersection(ray, testWorld, store, local);
        
        //verify that the resulting intersection is the same as the expected result
        Assert.assertNotNull(result);
        Assert.assertEquals(intersection.getX(), result.getX(), DELTA);
        Assert.assertEquals(intersection.getY(), result.getY(), DELTA);
        Assert.assertEquals(intersection.getZ(), result.getZ(), DELTA);
        //verity that the store is actually used for the result
        if(!nullVector)
            Assert.assertSame(result, store);
    }
    
    /**
     * Verify negative test of getIntersection method of the CollisionManager
     * A Ray that does not intersect the world is passed in and null should
     * be returned.
     * 
     * @param nullVector true if we should use a null vector as a parameter in the test
     * @param local true if the result should be converted to local coordinates
     */
    private void testGetIntersectionNegative(boolean nullVector, boolean local) {
        //create the ray that misses the objects
        Vector3f origin = new Vector3f(0f,0f,0f);
        Vector3f destination = new Vector3f(0f,0f,-100f);
        Vector3f direction = destination.subtractLocal(origin).normalizeLocal();
        Ray ray = new Ray(origin, direction);
        
        //configure the store
        Vector3f store = null;
        if(!nullVector)
            store = new Vector3f();
        
        //if we are converting to local coordinates, move the test world and
        //the ray so that we can test the transposition of the result
        if(local) {
            Vector3f transpose = new Vector3f(50f,50f,50f);
            origin.addLocal(transpose);
            destination.addLocal(transpose);
            direction = destination.subtractLocal(origin).normalizeLocal();
            ray = new Ray(origin, direction);
            moveWorld(transpose);
        }
        
        //call the collision manager
        Vector3f result = SingletonRegistry.getCollisionManager().getIntersection(ray, testWorld, store, local);
        
        //verify that the resulting intersection is null
        Assert.assertNull(result);
    }
    
    /**
     * Verify locally transposed result vector with null
     * store works properly
     */
    @Test
    public void testGetIntersectionWithBoxNullLocal() {
        testGetIntersectionWithBox(true, true);
    }
    
    /**
     * Verify global result vector with null
     * store works properly
     */
    @Test
    public void testGetIntersectionWithBoxNullWorld() {
        testGetIntersectionWithBox(true, false);
    }
    
    /**
     * Verify locally transposed result vector with not null
     * store works properly
     */
    @Test
    public void testGetIntersectionWithBoxNotNullLocal() {
        testGetIntersectionWithBox(false, true);
    }
    
    /**
     * Verify global result vector with not null
     * store works properly
     */
    @Test
    public void testGetIntersectionWithBoxNotNullWorld() {
        testGetIntersectionWithBox(false, false);
    }
    
    
    /**
     * Verify locally transposed result vector with null
     * store works properly
     */
    @Test
    public void testGetIntersectionNegativeNullLocal() {
        testGetIntersectionNegative(true, true);
    }
    
    /**
     * Verify global result vector with null
     * store works properly
     */
    @Test
    public void testGetIntersectionNegativeNullWorld() {
        testGetIntersectionNegative(true, false);
    }
    
    /**
     * Verify locally transposed result vector with not null
     * store works properly
     */
    @Test
    public void testGetIntersectionNegativeNotNullLocal() {
        testGetIntersectionNegative(false, true);
    }
    
    /**
     * Verify global result vector with not null
     * store works properly
     */
    @Test
    public void testGetIntersectionNegativeNotNullWorld() {
        testGetIntersectionNegative(false, false);
    }
    
    
    
    /**
     * Verify getDestination works properly when there is a standard
     * collision (distance is greater than BACKOFFDISTANCE value).
     * 
     * @param local if the testWorld should be transposed to verify world to local mapping
     */
    private void testGetDestinationCollision(boolean local) {
        //move the box up by the PATHHEIGHT
        Vector3f c = box.getLocalTranslation();
        box.setLocalTranslation(c.getX(), EStats.SnowmanHeight.getValue()/2.0f + c.getY(), c.getZ());
        
        //calculate the starting and ending positions
        //starting position should be backed off by the BACKOFFDISTANCE
        float startx = 0.0f;
        float startz = 0.0f - EStats.BackoffDistance.getValue();
        float endx = 0.0f;
        float endz = 50.0f;

        //in the test world, trimmed destination should always be:
        Vector3f trimmed = new Vector3f(0.0f, EStats.SnowmanHeight.getValue()/2.0f, 15.0f - EStats.BackoffDistance.getValue());
        
        //transpose the world if necessary
        if(local)
            moveWorld(new Vector3f(50f, 50f, 50f));
        
        //calculate the actual result
        Vector3f result = SingletonRegistry.getCollisionManager().getDestination(startx, startz, endx, endz, testWorld);
        
        //verify
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getX(), trimmed.getX(), DELTA);
        Assert.assertEquals(result.getY(), trimmed.getY(), DELTA);
        Assert.assertEquals(result.getZ(), trimmed.getZ(), DELTA);
    }
    
    @Test public void testGetDestinationCollisionLocal() {
        testGetDestinationCollision(true);
    }
    @Test public void testGetDestinationCollisionNotLocal() {
        testGetDestinationCollision(false);
    }
    
    /**
     * Verify getDestination works properly when there is no collision
     * 
     * @param local if the testWorld should be transposed to verify world to local mapping
     */
    private void testGetDestinationMiss(boolean local) {
        //move the box up by the PATHHEIGHT
        Vector3f c = box.getLocalTranslation();
        box.setLocalTranslation(c.getX(), EStats.SnowmanHeight.getValue()/2.0f + c.getY(), c.getZ());
        
        //calculate the starting and ending positions
        float startx = 0.0f;
        float startz = 0.0f;
        float endx = 0.0f;
        float endz = -50.0f;

        //in the test world, trimmed destination should always be:
        Vector3f trimmed = new Vector3f(0.0f, EStats.SnowmanHeight.getValue()/2.0f, -50.0f);
        
        //transpose the world if necessary
        if(local)
            moveWorld(new Vector3f(50f, 50f, 50f));
        
        //calculate the actual result
        Vector3f result = SingletonRegistry.getCollisionManager().getDestination(startx, startz, endx, endz, testWorld);
        
        //verify
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getX(), trimmed.getX(), DELTA);
        Assert.assertEquals(result.getY(), trimmed.getY(), DELTA);
        Assert.assertEquals(result.getZ(), trimmed.getZ(), DELTA);
    }
    
    @Test public void testGetDestinationMissLocal() {
        testGetDestinationMiss(true);
    }
    @Test public void testGetDestinationMissNotLocal() {
        testGetDestinationMiss(false);
    }
    
    /**
     * Verify getDestination works properly when there is no collision but
     * the end point is within the BACKOFFDISTANCE
     * 
     * @param local if the testWorld should be transposed to verify world to local mapping
     */
    private void testGetDestinationNearMiss(boolean local) {
        //move the box up by the PATHHEIGHT
        Vector3f c = box.getLocalTranslation();
        box.setLocalTranslation(c.getX(), EStats.SnowmanHeight.getValue()/2.0f + c.getY(), c.getZ());
        
        //calculate the starting and ending positions
        //starting position should be backed off by the BACKOFFDISTANCE
        float startx = 0.0f;
        float startz = 0.0f - EStats.BackoffDistance.getValue();
        float endx = 0.0f;
        float endz = 15.0f - EStats.BackoffDistance.getValue()/2.0f;

        //in the test world, trimmed destination should always be:
        Vector3f trimmed = new Vector3f(0.0f, EStats.SnowmanHeight.getValue()/2.0f, 15.0f - EStats.BackoffDistance.getValue());
        
        //transpose the world if necessary
        if(local)
            moveWorld(new Vector3f(50f, 50f, 50f));
        
        //calculate the actual result
        Vector3f result = SingletonRegistry.getCollisionManager().getDestination(startx, startz, endx, endz, testWorld);
        
        //verify
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getX(), trimmed.getX(), DELTA);
        Assert.assertEquals(result.getY(), trimmed.getY(), DELTA);
        Assert.assertEquals(result.getZ(), trimmed.getZ(), DELTA);
    }
    
    @Test public void testGetDestinationNearMissLocal() {
        testGetDestinationNearMiss(true);
    }
    @Test public void testGetDestinationNearMissNotLocal() {
        testGetDestinationNearMiss(false);
    }
    
    /**
     * Verify validate works properly when there is a basic perpendicular
     * collision.
     * 
     * @param local if the testWorld should be transposed to verify world to local mapping
     */
    private void testValidateBasicHit(boolean local) {
        //move the box up by the THROWHEIGHT
        Vector3f c = box.getLocalTranslation();
        box.setLocalTranslation(c.getX(), EStats.SnowballHeight.getValue() + c.getY(), c.getZ());
        
        //start point and end point
        float startx = 0.0f;
        float startz = 0.0f;
        float endx = 0.0f;
        float endz = 100.0f;

        //transpose the world if necessary
        if(local)
            moveWorld(new Vector3f(50f, 50f, 50f));
        
        //calculate the actual result
        boolean result = SingletonRegistry.getCollisionManager().validate(startx, startz, endx, endz, testWorld);
        
        //verify
        Assert.assertFalse(result);
    }
    
    @Test public void testValidateBasicHitLocal() {
        testValidateBasicHit(true);
    }
    @Test public void testValidateBasicHitNotLocal() {
        testValidateBasicHit(false);
    }
    
    
    /**
     * Verify validate works properly when there is no collision
     * 
     * @param local if the testWorld should be transposed to verify world to local mapping
     */
    private void testValidateMiss(boolean local) {
        //move the box up by the THROWHEIGHT
        Vector3f c = box.getLocalTranslation();
        box.setLocalTranslation(c.getX(), EStats.SnowballHeight.getValue() + c.getY(), c.getZ());
        
        //start point and end point
        float startx = 0.0f;
        float startz = 0.0f;
        float endx = 0.0f;
        float endz = -100.0f;

        //transpose the world if necessary
        if(local)
            moveWorld(new Vector3f(50f, 50f, 50f));
        
        //calculate the actual result
        boolean result = SingletonRegistry.getCollisionManager().validate(startx, startz, endx, endz, testWorld);
        
        //verify
        Assert.assertTrue(result);
    }
    
    @Test public void testValidateMissLocal() {
        testValidateMiss(true);
    }
    @Test public void testValidateMissNotLocal() {
        testValidateMiss(false);
    }
    
    
    /**
     * Verify validate works properly when there is a non-perpendicular collision
     * 
     * @param local if the testWorld should be transposed to verify world to local mapping
     */
    private void testValidateComplexHit(boolean local) {
        //move the box up by the THROWHEIGHT
        Vector3f c = box.getLocalTranslation();
        box.setLocalTranslation(c.getX(), EStats.SnowballHeight.getValue() + c.getY(), c.getZ());
        
        //start point and end point
        float startx = -5.0f;
        float startz = -5.0f;
        float endx = 0.0f;
        float endz = 25.0f;

        //transpose the world if necessary
        if(local)
            moveWorld(new Vector3f(50f, 50f, 50f));
        
        //calculate the actual result
        boolean result = SingletonRegistry.getCollisionManager().validate(startx, startz, endx, endz, testWorld);
        
        //verify
        Assert.assertFalse(result);
    }
    
    @Test public void testValidateComplexHitLocal() {
        testValidateComplexHit(true);
    }
    @Test public void testValidateComplexHitNotLocal() {
        testValidateComplexHit(false);
    }
    
    /**
     * Verify validate works properly when there is a no collision
     * but the detection ray collides with an object that is behind the
     * target point
     * 
     * @param local if the testWorld should be transposed to verify world to local mapping
     */
    private void testValidateBehindNoHit(boolean local) {
        //move the box up by the THROWHEIGHT
        Vector3f c = box.getLocalTranslation();
        box.setLocalTranslation(c.getX(), EStats.SnowballHeight.getValue() + c.getY(), c.getZ());
        
        //start point and end point
        float startx = 0.0f;
        float startz = 0.0f;
        float endx = 0.0f;
        float endz = 10.0f;

        //transpose the world if necessary
        if(local)
            moveWorld(new Vector3f(50f, 50f, 50f));
        
        //calculate the actual result
        boolean result = SingletonRegistry.getCollisionManager().validate(startx, startz, endx, endz, testWorld);
        
        //verify
        Assert.assertTrue(result);
    }
    
    @Test public void testValidateBehindNoHitLocal() {
        testValidateBehindNoHit(true);
    }
    @Test public void testValidateBehindHitNotLocal() {
        testValidateBehindNoHit(false);
    }
    
    @After
    public void cleanupTestWorld() {
        box = null;
        sphere = null;
        pyramid = null;
        testWorld = null;
    }

}
