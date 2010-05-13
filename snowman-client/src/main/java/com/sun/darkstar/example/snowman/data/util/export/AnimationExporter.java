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
import com.model.md5.JointAnimation;
import com.model.md5.ModelNode;
import com.model.md5.importer.MD5Importer;
import com.sun.darkstar.example.snowman.data.enumn.EDataType;

/**
 * <code>AnimationExporter</code> is responsible for exporting all
 * character <code>JointAnimation</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-31-2008 11:35 EST
 * @version Modified date: 07-31-2008 12:00 EST
 */
public class AnimationExporter extends Exporter {
	/**
	 * The source model file name without extension.
	 */
	private final String fileName = "SnowManWalking";
	/**
	 * The <code>JointAnimation</code> to be exported.
	 */
	private JointAnimation animation;
	
	/**
	 * Main method.
	 */
	public static void main(String[] args) {
		new AnimationExporter().start();
	}

	/**
	 * Constructor of <code>AnimationExporter</code>.
	 */
	public AnimationExporter() {
		super("com/sun/darkstar/example/snowman/data/animation/");
	}

	@Override
	protected void initialize() {
		final URL mesh = this.getClass().getClassLoader().getResource(this.sourceDir + this.fileName + ".md5mesh");
		final URL anim = this.getClass().getClassLoader().getResource(this.sourceDir + this.fileName + ".md5anim");
		try {
			MD5Importer.getInstance().load(mesh, this.fileName, anim, this.fileName, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ModelNode model = MD5Importer.getInstance().getModelNode();
		model.setLocalScale(0.01f);
		this.rootNode.attachChild(model);
		this.animation = MD5Importer.getInstance().getAnimation();
		MD5Importer.getInstance().cleanup();
	}

	@Override
	protected void simpleUpdate() {
		if(KeyBindingManager.getKeyBindingManager().isValidCommand("out", false)) {
			this.export(this.fileName + EDataType.Animation.getExtension(), this.animation);
		}
	}
}
