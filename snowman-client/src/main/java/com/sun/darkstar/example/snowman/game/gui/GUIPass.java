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
package com.sun.darkstar.example.snowman.game.gui;

import org.fenggui.Display;
import org.fenggui.render.lwjgl.LWJGLBinding;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.Pass;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.sun.darkstar.example.snowman.game.input.enumn.EInputConverter;
import com.sun.darkstar.example.snowman.game.input.util.InputManager;

/**
 * <code>GUIPass</code> renders FengGUI widgets in its own separate
 * <code>RenderPass</code> to enhance performance and scene graph management.
 * <p>
 * <code>GUIPass</code> has to be added to a <code>BasicPassManager</code>
 * after the root <code>RenderPass</code> and before any additional special
 * effect <code>RenderPass</code> such as <code>BloomRenderPass</code> or
 * <code>ShadowRenderPass</code>. 
 * <p>
 * Subclasses which extends <code>GUIPass</code> need to implement initialize
 * method to create all GUI widgets and add them to the <code>Display</code>.
 * <p>
 * <code>GUIPass</code> automatically invokes <code>IInputConverter</code> to
 * set the FengGUI <code>Display</code> instance for input conversion.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 05-21-2008 17:14 EST
 * @version Modified date: 07-09-2008 11:58 EST
 */
public abstract class GUIPass extends Pass {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = -284580225155562862L;
	/**
	 * The FengGUI <code>Display</code> instance.
	 */
	protected final Display display;
	/**
	 * The temporary <code>TextureState</code> to render FengGUI.
	 */
	private final TextureState tempTextureState;
	
	/**
	 * Constructor of <code>GUIPass</code>
	 */
	public GUIPass() {
		super();
		LWJGLBinding binding = new LWJGLBinding();
		binding.setUseClassLoader(true);
		this.display = new Display(binding);
		this.tempTextureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		Texture texture = TextureState.getDefaultTexture();
		texture.setScale(new Vector3f(1, 1, 1));
		this.tempTextureState.setTexture(texture);
	}
	
	/**
	 * Initialize the graphical user pass.
	 */
	public void initialize() {
		this.buildWidgets();
		InputManager.getInstance().getConverter(EInputConverter.KeyboardConverter).setDisplay(this.display);
		InputManager.getInstance().getConverter(EInputConverter.MouseConverter).setDisplay(this.display);
	}
	
	/**
	 * Build the graphical user interface widgets.
	 */
	protected abstract void buildWidgets();

	@Override
	protected void doRender(Renderer r) {
		this.tempTextureState.apply();
		this.display.display();
	}
	
	/**
	 * Retrieve the FengGU} <code>Display</code> instance.
	 * @return The FengGUI <code>Display</code> instance.
	 */
	public Display getDisplay() {
		return this.display;
	}
}
