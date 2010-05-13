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
package com.sun.darkstar.example.snowman.common.entity.view.terrain;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.jme.bounding.CollisionTreeManager;
import com.jme.math.Vector3f;
import com.jme.scene.TexCoords;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.geom.BufferUtils;
import com.jmex.terrain.TerrainBlock;


public class TerrainMeshBlock extends TerrainBlock {

	private static final long serialVersionUID = 1L;

	/**
	 * The row index number in the parent <code>TerrainCluster</code>.
	 */
	private int row;
	/**
	 * The column index number in the parent <code>TerrainCluster</code>.
	 */
	private int col;
	/**
	 * The number <code>TerrainMesh</code> along x axis in the parent <code>TerrainCluster</code>.
	 */
	private int clusterWidth;
	/**
	 * The number <code>TerrainMesh</code> along z axis in the parent <code>TerrainCluster</code>.
	 */
	private int clusterDepth;
	/**
	 * The temporary <code>Vector3f</code> objects.
	 */
	private final Vector3f tempVector = new Vector3f();
	private final Vector3f tempVector2 = new Vector3f();
	private boolean genTextureCoords = false;
	/**
	 * The temporary <code>ArrayList</code> of vertices for smoothing.
	 */
	private ArrayList<Vector3f> tempValues;
	/**
	 * The temporary <code>ArrayList</code> of indices for smoothing.
	 */
	private ArrayList<Integer> tempIndices;
	/**
	 * The temporary <code>ArrayList</code> of distances for smoothing.
	 */
	private ArrayList<Float> tempDistances;

	public TerrainMeshBlock() {
		
	}
	
	public TerrainMeshBlock(String name, int size, Vector3f stepScale,
			float[] heightMap, Vector3f origin, int row, int col, int width, int depth) {
		super(name, size, stepScale, heightMap, origin);
		this.row = row;
		this.col = col;
		this.clusterWidth = width;
		this.clusterDepth = depth;
		genTextureCoords = true;
		getOffset().set(origin.x, origin.z);
		buildTextureCoordinates();
	}

	@Override
	public void buildTextureCoordinates() {
		if (!genTextureCoords) return;
		FloatBuffer textureBuffer = BufferUtils.createVector2Buffer(this.getVertexCount());
		FloatBuffer vertexBuffer = this.getVertexBuffer();
		vertexBuffer.rewind();
		for (int i = 0; i < this.getVertexCount(); i++) {
			textureBuffer.put((vertexBuffer.get() + getOffset().x) / (this.clusterWidth*(this.getSize() - 1)));
			vertexBuffer.get();
			textureBuffer.put((vertexBuffer.get() + getOffset().y) / (this.clusterDepth*(this.getSize() - 1)));
		}
		textureBuffer.flip();
		setTextureCoords(new TexCoords(textureBuffer));
	}


	/**
	 * Raise or lower the height of the this terrain block within the given radius.
	 * @param center The center position of modification in world coordinate system.
	 * @param radius The radius of modification.
	 * @param delta The delta of modification.
	 */
	public void modifyHeight(Vector3f center,
			float radius, float delta) {
		FloatBuffer vertexBuffer = getVertexBuffer();
		for(int i = 0; i < getVertexCount(); i++) {
			BufferUtils.populateFromBuffer(tempVector, vertexBuffer, i);
			localToWorld(tempVector, tempVector2);
			float distance = TerrainCluster.getPlanarDistance(tempVector2, center);
			if(distance < radius) {
				addHeightMapValue((int)tempVector.x, (int)tempVector.z, delta * (1 - distance / radius));
			}
		}
		updateFromHeightMap();
		CollisionTreeManager.getInstance().updateCollisionTree(this);
		updateGeometricState(0, true);
		updateModelBound();
	}

	/**
	 * Populate the vectors of affected vertices, the indices of the vertices and
	 * the distances between the vertices and the center based on given center and radius.
	 * @param center The center position of modification in world coordinate system.
	 * @param radius The radius of modification.
	 * @return The <code>ArrayList</code> of vertices <code>Vector3f</code> values.
	 */
	public ArrayList<Vector3f> populateVertices(Vector3f center, float radius) {
		if (tempValues == null) tempValues = new ArrayList<Vector3f>();
		if (tempIndices == null) tempIndices = new ArrayList<Integer>();
		if (tempDistances == null) tempDistances = new ArrayList<Float>();
		this.tempValues.clear();
		this.tempIndices.clear();
		this.tempDistances.clear();
		FloatBuffer vertexBuffer = this.getVertexBuffer();
		for(int i = 0; i < this.getVertexCount(); i++) {
			BufferUtils.populateFromBuffer(this.tempVector, vertexBuffer, i);
			this.localToWorld(this.tempVector, this.tempVector);
			float distance = TerrainCluster.getPlanarDistance(this.tempVector, center);
			if(distance < radius) {
				this.tempValues.add(this.tempVector.clone());
				this.tempIndices.add(Integer.valueOf(i));
				this.tempDistances.add(Float.valueOf(distance));
			}
		}
		return this.tempValues;
	}

	/**
	 * Smooth the height of this terrain mesh towards the given average value.
	 * @param average The average value to smooth towards.
	 * @param radius The radius of modification.
	 * @param intensity The intensity of modification.
	 */
	public void smoothHeight(float average, float radius, float intensity) {
		Vector3f vector = null;
		for(int i = 0; i < this.tempValues.size(); i++) {
			vector = this.tempValues.get(i);
			float delta = (average - vector.y) * intensity * (1 - this.tempDistances.get(i).floatValue() / radius);
			this.worldToLocal(vector, vector);
			addHeightMapValue((int)vector.x, (int)vector.z, delta);
		}
		updateFromHeightMap();
		CollisionTreeManager.getInstance().updateCollisionTree(this);
		this.updateGeometricState(0, true);
		this.updateModelBound();
	}
	
	
	/**
	 * Retrieve the row index number of this terrain mesh in the cluster.
	 * @return The row index number.
	 */
	public int getRowIndex() {
		return this.row;
	}

	/**
	 * Retrieve the column index number of this terrain mesh in the cluster.
	 * @return The column index number.
	 */
	public int getColumnIndex() {
		return this.col;
	}
	
	@Override
	public void write(JMEExporter ex) throws IOException {
		super.write(ex);
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(this.row, "RowIndex", 0);
		oc.write(this.col, "ColumnIndex", 0);
		oc.write(this.clusterWidth, "ClusterWidth", 1);
		oc.write(this.clusterDepth, "ClusterDepth", 1);
	}

	@Override
	public void read(JMEImporter im) throws IOException {
		super.read(im);
		InputCapsule ic = im.getCapsule(this);
		this.row = ic.readInt("RowIndex", 0);
		this.col = ic.readInt("ColumnIndex", 0);
		this.clusterWidth = ic.readInt("ClusterWidth", 1);
		this.clusterDepth = ic.readInt("ClusterDepth", 1);
	}
}
