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

import java.net.URL;

import com.jme.bounding.BoundingBox;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.sun.darkstar.example.snowman.data.enumn.EDataType;

/**
 * <code>TextureExporter</code> exports image files into <code>Texture</code>
 * binary files.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-13-2008 10:47 EST
 * @version Modified date: 07-31-2008 12:03 EST
 */
public class TextureExporter extends Exporter {
	/**
	 * The source image file name with extension.
	 */
	private final String inputFile = "Road.jpg";
	/**
	 * The <code>AlphaState</code> for transparency.
	 */
	private BlendState alpha;
	/**
	 * The <code>Texture</code> instance.
	 */
	private Image image;

	/**
	 * Main method.
	 */
	public static void main(String[] args) {
		new TextureExporter().start();
	}
	
	/**
	 * Constructor of <code>TextureExporter</code>.
	 */
	public TextureExporter() {
		super("com/sun/darkstar/example/snowman/data/texture/");
	}

	@Override
	protected void initialize() {
		this.setupBlend();
		this.buildQuad();
	}

	/**
	 * Setup the <code>BlendState</code> for transparency.
	 */
	private void setupBlend() {
		this.alpha = this.display.getRenderer().createBlendState();
		this.alpha.setBlendEnabled(true);
		this.alpha.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		this.alpha.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		this.alpha.setTestEnabled(true);
		this.alpha.setTestFunction(BlendState.TestFunction.GreaterThan);
		this.alpha.setEnabled(true);
	}
	
	/**
	 * Build a <code>Quad</code> to preview the <code>Texture</code>.
	 */
	private void buildQuad() {
		Quad q = new Quad("Export", 20,	20);
		q.setModelBound(new BoundingBox());
		q.updateModelBound();
		q.setRenderState(this.alpha);
		URL url = this.getClass().getClassLoader().getResource(this.sourceDir + this.inputFile);
		Texture t = TextureManager.loadTexture(url, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, 16, true);
		TextureState ts = this.display.getRenderer().createTextureState();
		ts.setTexture(t);
		q.setRenderState(ts);
		this.rootNode.attachChild(q);
		this.image = t.getImage();
	}

	@Override
	protected void simpleUpdate() {
		if(KeyBindingManager.getKeyBindingManager().isValidCommand("out", false)) {
			this.export(this.inputFile.substring(0, this.inputFile.lastIndexOf(".")) + EDataType.Texture.getExtension(), this.image);
		}
	}
}
