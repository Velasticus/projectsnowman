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
package com.sun.darkstar.example.snowman.common.entity.terrain;

import java.io.IOException;

import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.sun.darkstar.example.snowman.common.entity.EditableEntity;
import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity;

/**
 * <code>TerrainEntity</code> extends <code>EditableEntity</code> to define
 * the editable terrain during world editing stages. It maintains the basic
 * data of the <code>TerrainCluster</code> including the width, the depth
 * and the number of triangles per <code>TerrainMesh</code> value of the
 * <code>TerrainCluster</code>.
 * 
 * @author Yi Wang (Neakor)
 * @author Tim Poliquin (Weenahmen)
 * @version Creation date: 07-01-2008 23:35 EST
 * @version Modified date: 07-02-2008 24:08 EST
 */
public class TerrainEntity extends EditableEntity {
	/**
	 * The width of the <code>TerrainCluster</code>.
	 */
	private int width;
	/**
	 * The depth of the <code>TerrainCluster</code>.
	 */
	private int depth;
	/**
	 * The number of triangles per <code>TerrainMesh</code>.
	 */
	private int trianglesPerMesh;
	
	/**
	 * Constructor of <code>TerrainEntity</code>.
	 */
	public TerrainEntity() {
		super();
	}
	
	/**
	 * Constructor of <code>TerrainEntity</code>.
	 * @param id The integer ID number of this entity.
	 */
	public TerrainEntity(int id) {
		super(EEntity.Terrain, id);
	}
	
	/**
	 * Set the width of the terrain cluster.
	 * @param width The width value to be set.
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * Set the depth of the terrain cluster.
	 * @param depth The depth value to be set.
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	/**
	 * Set the number of triangles per terrain mesh.
	 * @param trianglesPerMesh The number of triangles value.
	 */
	public void setTrianglesPerMesh(int trianglesPerMesh) {
		this.trianglesPerMesh = trianglesPerMesh;
	}
	
	/**
	 * Retrieve the width of the terrain cluster.
	 * @return The integer width value.
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * Retrieve the depth of the terrain cluster.
	 * @return The integer depth value.
	 */
	public int getDepth() {
		return this.depth;
	}
	
	/**
	 * Retrieve the number of triangles per terrain mesh.
	 * @return The integer number of triangles value.
	 */
	public int getTrianglesPerMesh() {
		return this.trianglesPerMesh;
	}
	
	@Override
	public void write(JMEExporter ex) throws IOException {
		super.write(ex);
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(this.width, "Width", 0);
		oc.write(this.depth, "Depth", 0);
		oc.write(this.trianglesPerMesh, "TrianglesPerMesh", 0);
	}
	
	@Override
	public void read(JMEImporter im) throws IOException {
		super.read(im);
		InputCapsule ic = im.getCapsule(this);
		this.width = ic.readInt("Width", 0);
		this.depth = ic.readInt("Depth", 0);
		this.trianglesPerMesh = ic.readInt("TrianglesPerMesh", 0);
	}
}
