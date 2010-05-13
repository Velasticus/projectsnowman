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
package com.sun.darkstar.example.snowman.data.util.export;

import java.io.IOException;
import java.net.URL;

import com.jme.input.KeyBindingManager;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jmex.model.collada.ColladaImporter;
import com.sun.darkstar.example.snowman.data.enumn.EDataType;

/**
 * <code>ModelNodeExporter</code> is responsible for exporting all
 * static <code>SharedMesh</code> models.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-09-2008 17:20 EST
 * @version Modified date: 07-31-2008 12:02 EST
 */
public class ColladaExporter extends Exporter {
	/**
	 * The source model file name without extension.
	 */
	private final String fileName = "Tree";
	/**
	 * The mesh object to be exported.
	 */
	private Node node;
	
	/**
	 * Main method.
	 */
	public static void main(String[] args) {
		new ColladaExporter().start();
	}

	/**
	 * Constructor of <code>ColladaExporter</code>.
	 */
	public ColladaExporter() {
		super("com/sun/darkstar/example/snowman/data/mesh/");
	}
	
	@Override
	protected void initialize() {
		URL url = this.getClass().getClassLoader().getResource(this.sourceDir + this.fileName + ".dae");
		try {
			ColladaImporter.load(url.openStream(), "Model");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.node = ColladaImporter.getModel();
		setFullAmbient(this.node);
		this.node.setLocalScale(.01f);  // Our Collada files were done in 100:1 scale
		this.node.updateRenderState();
		this.rootNode.attachChild(this.node);
	}

	@Override
	protected void simpleUpdate() {
		if(KeyBindingManager.getKeyBindingManager().isValidCommand("out", false)) {
			this.export(this.fileName + EDataType.StaticMesh.getExtension(), this.node);
		}
	}
	

	private void setFullAmbient(Spatial spat) {
		if (spat.getRenderState(RenderState.RS_MATERIAL) != null) {
			MaterialState ms = (MaterialState)spat.getRenderState(RenderState.RS_MATERIAL);
			ms.setAmbient(ColorRGBA.white.clone());
		}
		if (spat instanceof Node) {
			Node node = (Node)spat;
			for (int i = 0; i < node.getQuantity(); i++) {
				Spatial child = node.getChild(i);
				this.setFullAmbient(child);
			}
		}
	}

}
