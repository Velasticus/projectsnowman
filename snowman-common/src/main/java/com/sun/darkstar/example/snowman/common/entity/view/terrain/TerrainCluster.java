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
import java.util.ArrayList;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import com.jme.util.geom.BufferUtils;
import com.sun.darkstar.example.snowman.common.entity.view.terrain.enumn.ESculpt;

/**
 * <code>TerrainCluster</code> extends <code>Node</code> to define a cluster
 * of <code>TerrainMeshBlock</code> that represents the entire terrain.
 * <p>
 * <code>TerrainCluster</code> provides the functionality for geometry sculpting
 * and detail texture coordinates duplication during world editing stages. It
 * is then attached to an <code>IStaticView</code> and locked during the world
 * export process.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-01-2008 14:24 EST
 * @version Modified date: 07-07-2008 17:14 EST
 */
public class TerrainCluster extends Node {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = -6194334491013311239L;
	/**
	 * The number <code>TerrainMeshBlock</code> along x axis.
	 */
	private int width;
	/**
	 * The number <code>TerrainMeshBlock</code> along z axis.
	 */
	private int depth;
	/**
	 * The two dimensional array of <code>TerrainMeshBlock</code> attached to the cluster.
	 */
	private TerrainMeshBlock[][] meshes;
	/**
	 * The x and z extent of a single <code>TerrainMeshBlock</code>.
	 */
	private float extent;
	/**
	 * The diagonal distance of a single <code>TerrainMeshBlock</code>.
	 */
	private float diagonal;
	/**
	 * The temporary <code>ArrayList</code> of <code>TerrainMeshBlock</code> to be sculpted.
	 */
	private final ArrayList<TerrainMeshBlock> tempMeshes;
	/**
	 * The temporary <code>Vector3f</code> used to averaging vertices and normals.
	 */
	private final Vector3f tempVector;
	
	/**
	 * Constructor of <code>TerrainCluster</code>.
	 */
	public TerrainCluster() {
		this.tempMeshes = new ArrayList<TerrainMeshBlock>();
		this.tempVector = new Vector3f();
	}

	/**
	 * Constructor of <code>TerrainGroup</code>.
	 * @param name The <code>String</code> name of this cluster.
	 * @param width The number <code>TerrainMeshBlock</code> along x axis.
	 * @param depth The number <code>TerrainMeshBlock</code> along z axis.
	 * @param trianglesPerMesh The number of triangles per <Code>TerrainMeshBlock</code>.
	 */
	public TerrainCluster(String name, int width, int depth, int trianglesPerMesh) {
		super(name);
		this.width = width;
		this.depth = depth;
		this.meshes = new TerrainMeshBlock[width][depth];
		this.tempMeshes = new ArrayList<TerrainMeshBlock>();
		this.tempVector = new Vector3f();
		TerrainMeshBlock mesh = null;
		for(int row = 0; row < width; row++) {
			for(int col = 0; col < depth; col++) {
				Vector3f offset = new Vector3f(2*row*this.extent, 0, 2*col*this.extent);
				int size = (int)FastMath.ceil(FastMath.sqrt(trianglesPerMesh/2) + 1);
				float[] floats = new float[size*size];
				mesh = new TerrainMeshBlock("TerrainBlock_"+row+"_"+col, size, new Vector3f(1,1,1), floats, offset, row, col, width, depth);
				mesh.setModelBound(new BoundingBox());
				mesh.updateModelBound();
				if(row == 0 && col == 0) {
					mesh.updateGeometricState(0, false);
					this.extent = ((BoundingBox)mesh.getWorldBound()).xExtent;
					this.diagonal = FastMath.sqrt(this.extent*this.extent*2);
				}
				this.meshes[row][col] = mesh;
				this.attachChild(mesh);
			}
		}
	}

	/**
	 * Sculpt this terrain cluster with given sculpting type and brush.
	 * @param enumn The <code>ESculpt</code> enumeration.
	 * @param boundCenter The <code>Vector3f</code> of brush bounding center.
	 * @param worldcoords The <code>Vector3f</code> of brush world coordinates.
	 * @param radius The float radius of the brush.
	 * @param intentisy The float intensity of the brush.
	 */
	public void sculptCluster(ESculpt enumn, Vector3f boundCenter, Vector3f worldcoords, float radius, float intentisy) {
		this.tempMeshes.clear();
		for(int row = 0; row < this.meshes.length; row++) {
			for(int col = 0; col < this.meshes[row].length; col++) {
				float distance = TerrainCluster.getPlanarDistance(this.meshes[row][col].getWorldBound().getCenter(), boundCenter);
				if(distance < this.diagonal + radius) {
					this.tempMeshes.add(this.meshes[row][col]);
				}
			}
		}
		switch(enumn) {
		case Raise:
			for(TerrainMeshBlock block : this.tempMeshes) {
				block.modifyHeight(worldcoords, radius, intentisy);
			}
			break;
		case Lower:
			for(TerrainMeshBlock block : this.tempMeshes) {
				block.modifyHeight(worldcoords, radius, -intentisy);
			}
			break;
		case Smooth:
			this.smoothCluster(worldcoords, radius, intentisy);
			break;
		}
		this.averageVertices();
		this.averageNormals();
	}

	/**
	 * Smooth the the entire terrain cluster.
	 * @param center The center position of modification in world coordinate system.
	 * @param radius The radius of modification.
	 * @param intensity The intensity of modification.
	 */
	private void smoothCluster(Vector3f center, float radius, float intensity) {
		float total = 0;
		float count = 0;
		ArrayList<Vector3f> values = null;
		for(TerrainMeshBlock mesh : this.tempMeshes) {
			values = mesh.populateVertices(center, radius);
			for(Vector3f vector : values) {
				total += vector.y;
			}
			count += values.size();
		}
		float average = total / count;
		for(TerrainMeshBlock mesh : this.tempMeshes) {
			mesh.smoothHeight(average, radius, intensity);
		}
	}

	/**
	 * Average the vertices of the right and down edges based on the meshes to
	 * the right below the mesh.
	 */
	private void averageVertices() {
		for(TerrainMeshBlock mesh : this.tempMeshes) {
			int size = mesh.getSize();
			TerrainMeshBlock right = this.getRightMesh(mesh.getRowIndex(), mesh.getColumnIndex());
			TerrainMeshBlock down = this.getDownMesh(mesh.getRowIndex(), mesh.getColumnIndex());
			TerrainMeshBlock cross = this.getCrossMesh(mesh.getRowIndex(), mesh.getColumnIndex());
			// First average the overlapping vertex between right, down and cross meshes.
			if(right != null && down != null && cross != null) {
				BufferUtils.populateFromBuffer(this.tempVector, right.getVertexBuffer(), (size*size) - size);
				float heightRight = this.tempVector.y;
				BufferUtils.populateFromBuffer(this.tempVector, down.getVertexBuffer(), size - 1);
				float heightDown = this.tempVector.y;
				BufferUtils.populateFromBuffer(this.tempVector, cross.getVertexBuffer(), 0);
				float heightCross = this.tempVector.y;
				float average = (heightRight + heightDown + heightCross) / 3.0f;
				this.tempVector.y = average;
				BufferUtils.setInBuffer(this.tempVector, cross.getVertexBuffer(), 0);
				BufferUtils.populateFromBuffer(this.tempVector, down.getVertexBuffer(), size - 1);
				this.tempVector.y = average;
				BufferUtils.setInBuffer(this.tempVector, down.getVertexBuffer(), size - 1);
				BufferUtils.populateFromBuffer(this.tempVector, right.getVertexBuffer(), (size*size) - size);
				this.tempVector.y = average;
				BufferUtils.setInBuffer(this.tempVector, right.getVertexBuffer(), (size*size) - size);
			}
			// Set the vertices on the right edge according to the left edge of the right mesh.
			if(right != null) {
				for(int i = 1; i <= size; i++) {
					int index1 = i * size - 1;
					int index2 = index1 - (size - 1);
					BufferUtils.populateFromBuffer(this.tempVector, right.getVertexBuffer(), index2);
					float height = this.tempVector.y;
					BufferUtils.populateFromBuffer(this.tempVector, mesh.getVertexBuffer(), index1);
					this.tempVector.y = height;
					BufferUtils.setInBuffer(this.tempVector, mesh.getVertexBuffer(), index1);
				}
			}
			// Set the vertices on the down edge according to the up edge of the down mesh.
			if(down != null) {
				int sizeSqr = size*size;
				for(int i = size; i > 0; i--) {
					int index1 = sizeSqr - i;
					int index2 = index1 - (sizeSqr - size);
					BufferUtils.populateFromBuffer(this.tempVector, down.getVertexBuffer(), index2);
					float height = this.tempVector.y;
					BufferUtils.populateFromBuffer(this.tempVector, mesh.getVertexBuffer(), index1);
					this.tempVector.y = height;
					BufferUtils.setInBuffer(this.tempVector, mesh.getVertexBuffer(), index1);
				}
			}
		}
	}

	/**
	 * Average the normals of the right and down edges based on the meshes to
	 * the right below the mesh.
	 */
	private void averageNormals() {
		for(TerrainMeshBlock mesh : this.tempMeshes) {
			int size = mesh.getSize();
			TerrainMeshBlock right = this.getRightMesh(mesh.getRowIndex(), mesh.getColumnIndex());
			TerrainMeshBlock down = this.getDownMesh(mesh.getRowIndex(), mesh.getColumnIndex());
			TerrainMeshBlock cross = this.getCrossMesh(mesh.getRowIndex(), mesh.getColumnIndex());
			// First average the overlapping normal between right, down and cross meshes.
			if(right != null && down != null && cross != null) {
				BufferUtils.populateFromBuffer(this.tempVector, right.getNormalBuffer(), (size*size) - size);
				float[] normalRight = new float[]{this.tempVector.x, this.tempVector.y, this.tempVector.z};
				BufferUtils.populateFromBuffer(this.tempVector, down.getNormalBuffer(), size - 1);
				float[] normalDown = new float[]{this.tempVector.x, this.tempVector.y, this.tempVector.z};
				BufferUtils.populateFromBuffer(this.tempVector, cross.getNormalBuffer(), 0);
				float[] normalCross = new float[]{this.tempVector.x, this.tempVector.y, this.tempVector.z};
				float averageX = (normalRight[0] + normalDown[0] + normalCross[0]) / 3.0f;
				float averageY = (normalRight[1] + normalDown[1] + normalCross[1]) / 3.0f;
				float averageZ = (normalRight[2] + normalDown[2] + normalCross[2]) / 3.0f;
				this.tempVector.x = averageX;
				this.tempVector.y = averageY;
				this.tempVector.z = averageZ;
				BufferUtils.setInBuffer(this.tempVector, cross.getNormalBuffer(), 0);
				BufferUtils.setInBuffer(this.tempVector, down.getNormalBuffer(), size - 1);
				BufferUtils.setInBuffer(this.tempVector, right.getNormalBuffer(), (size*size) - size);
			}
			// Set the normals on the right edge according to the left edge of the right mesh.
			if(right != null) {
				for(int i = 1; i <= size; i++) {
					int index1 = i * size - 1;
					int index2 = index1 - (size - 1);
					BufferUtils.populateFromBuffer(this.tempVector, right.getNormalBuffer(), index2);
					BufferUtils.setInBuffer(this.tempVector, mesh.getNormalBuffer(), index1);
				}
			}
			// Set the normals on the down edge according to the up edge of the down mesh.
			if(down != null) {
				int sizeSqr = size*size;
				for(int i = size; i > 0; i--) {
					int index1 = sizeSqr - i;
					int index2 = index1 - (sizeSqr - size);
					BufferUtils.populateFromBuffer(this.tempVector, down.getNormalBuffer(), index2);
					BufferUtils.setInBuffer(this.tempVector, mesh.getNormalBuffer(), index1);
				}
			}
		}
	}

	/**
	 * Retrieve the planar distance between the given two vectors.
	 * @param vec1 The <code>Vector3f</code> to check.
	 * @param vec2 The <code>Vector3f</code> to check.
	 * @return The float planar distance between the given two vectors.
	 */
	public static float getPlanarDistance(Vector3f vec1, Vector3f vec2) {
		float dx = vec1.x - vec2.x;
		float dy = vec1.z - vec2.z;
		return FastMath.sqrt(dx* dx + dy * dy);
	}

	/**
	 * Retrieve the mesh to the right of given index set.
	 * @param row The base row index number.
	 * @param col The base column index number.
	 * @return The <code>TerrainMeshBlock</code> to the right of given index set.
	 */
	private TerrainMeshBlock getRightMesh(int row, int col) {
		if(row == this.meshes.length - 1) return null;
		return this.meshes[row + 1][col];
	}

	/**
	 * Retrieve the mesh below the given index set.
	 * @param row The base row index number.
	 * @param col The base column index number.
	 * @return The <code>TerrainMeshBlock</code> below the given index set.
	 */
	private TerrainMeshBlock getDownMesh(int row, int col) {
		if(col == this.meshes[row].length - 1) return null;
		return this.meshes[row][col + 1];
	}

	/**
	 * Retrieve the mesh below and to the right of the given index set.
	 * @param row The base row index number.
	 * @param col The base column index number.
	 * @return The <code>TerrainMeshBlock</code> below and to the right of the given index set.
	 */
	private TerrainMeshBlock getCrossMesh(int row, int col) {
		if(row == this.meshes.length - 1 || col == this.meshes[row].length - 1) return null;
		return this.meshes[row + 1][col + 1];
	}

	/**
	 * Set the the texture at given unit as detail texture with given repeat number.
	 * @param unit The texture unit of the detail texture.
	 * @param repeat The repeat number to be used.
	 */
	public void setDetailTexture(int unit, int repeat) {
		for(int i = 0; i < this.meshes.length; i++) {
			for(int j = 0; j < this.meshes[i].length; j++) {
				this.meshes[i][j].copyTextureCoordinates(0, unit, repeat);
			}
		}
	}
	
	/**
	 * Retrieve the list of nodes containing terrain meshes.
	 * @return The <code>ArrayList</code> of terrain mesh <code>Node</code>.
	 */
	public ArrayList<Node> getMeshList() {
		ArrayList<Node> list = new ArrayList<Node>();
		for(int i = 0; i < this.meshes.length; i++) {
			for(int j = 0; j < this.meshes[i].length; j++) {
				list.add(this.meshes[i][j].getParent());
			}
		}
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return TerrainCluster.class;
	}

	@Override
	public void write(JMEExporter ex) throws IOException {
		super.write(ex);
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(this.width, "Width", 0);
		oc.write(this.depth, "Depth", 0);
		oc.write(this.meshes, "Meshes", null);
		oc.write(this.extent, "Extent", 0);
		oc.write(this.diagonal, "Diagonal", 0);
	}

	@Override
	public void read(JMEImporter im) throws IOException {
		super.read(im);
		InputCapsule ic = im.getCapsule(this);
		this.width = ic.readInt("Width", 0);
		this.depth = ic.readInt("Depth", 0);
		this.meshes = new TerrainMeshBlock[this.width][this.depth];
		Savable[][] temp = ic.readSavableArray2D("Meshes", null);
		for(int i = 0; i < temp.length; i++) {
			for(int j = 0; j < temp[i].length; j++) {
				this.meshes[i][j] = (TerrainMeshBlock)temp[i][j];
			}
		}
		this.extent = ic.readFloat("Extent", 0);
		this.diagonal = ic.readFloat("Diagonal", 0);
	}
}
