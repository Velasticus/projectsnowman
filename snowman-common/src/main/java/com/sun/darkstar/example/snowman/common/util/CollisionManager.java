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

import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * <code>CollisionManager</code> is a <code>Manager</code> that is responsible
 * for processing all collision detection tasks.
 * 
 * @author Yi Wang (Neakor)
 * @author Owen Kellett
 * @version Creation date: 07-02-2008 24:26 EST
 * @version Modified date: 07-16-2008 11:40 EST
 */
public interface CollisionManager {
	
    /**
     * Retrieve the intersecting object under the given node.
     * @param ray The <code>Ray</code> to check with.
     * @param root The root <code>Node</code> to check against.
     * @param reference The <code>Class</code> reference of the expected object.
     * @param iterate True if all intersected objects should be checked. Otherwise only the first is checked.
     * @return The <code>Spatial</code> that is of the given reference <code>Class</code>.
     */
    public Spatial getIntersectObject(Ray ray, Node root, Class<? extends Spatial> reference, boolean iterate);

    /**
     * Retrieve the intersection point with the given ray and spatial in either
     * world coordinate system or local coordinate system of the given spatial
     * based on the given flag value. The intersection result is stored in the
     * given vector and returned. If the given store is null, a new vector instance
     * is created and returned with the intersection result.
     * @param ray The <code>Ray</code> to check with.
     * @param parent The parent <code>Spatial</code> to check against.
     * @param store The <code>Vector3f</code> to store the intersection result in.
     * @param local True if the intersection should be converted to local coordinate system of the parent.
     * @return If hit, the <code>Vector3f</code> intersection is returned. Otherwise <code>null</code> is returned.
     */
    public Vector3f getIntersection(Ray ray, Spatial parent, Vector3f store, boolean local);
    
    /**
     * Retrieve the valid destination point based on the given coordinate values.
     * @param x1 The x coordinate of the starting position in the given spatial's local coordinate system.
     * @param z1 The z coordinate of the starting position in the given spatial's local coordinate system.
     * @param x2 The x coordinate of the clicking position in the given spatial's local coordinate system.
     * @param z2 The z coordinate of the clicking position in the given spatial's local coordinate system.
     * @param spatial The <code>Spatial</code> instance to check against.
     * @return The valid <code>Vector3f</code> destination.
     */
    public Vector3f getDestination(float x1, float z1, float x2, float z2, Spatial spatial);

    /**
     * Validate if there is any static objects between the given points.
     * @param x1 The x coordinate of the starting position in the given spatial's local coordinate system.
     * @param z1 The z coordinate of the starting position in the given spatial's local coordinate system.
     * @param x2 The x coordinate of the clicking position in the given spatial's local coordinate system.
     * @param z2 The z coordinate of the clicking position in the given spatial's local coordinate system.
     * @param spatial The <code>Spatial</code> instance to check against.
     * @return True if there is no occlusion objects. False otherwise.
     */
    public boolean validate(float x1, float z1, float x2, float z2, Spatial spatial);
}
