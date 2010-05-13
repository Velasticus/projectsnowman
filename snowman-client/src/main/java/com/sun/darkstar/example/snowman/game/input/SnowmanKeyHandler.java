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
package com.sun.darkstar.example.snowman.game.input;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;

/**
 * Quick hack for getting keyboard rotate / zoom in.
 * 
 * @author Joshua Slack
 * 
 */
public class SnowmanKeyHandler extends InputHandler {

	private final SnowmanCameraHandler camHandler;

	public SnowmanKeyHandler(SnowmanCameraHandler camHandler) {
		this.camHandler = camHandler;
	}

	@Override
	public void update(float time) {
		float scale = 2f;
		float zoomScale = 20f;

		if (KeyInput.get().isKeyDown(KeyInput.KEY_LEFT)) {
			camHandler.doRotate(time*scale, 0);
		}
		if (KeyInput.get().isKeyDown(KeyInput.KEY_RIGHT)) {
			camHandler.doRotate(time*-scale, 0);
		}
		if (KeyInput.get().isKeyDown(KeyInput.KEY_UP)) {
			camHandler.doRotate(0,time*scale);
		}
		if (KeyInput.get().isKeyDown(KeyInput.KEY_DOWN)) {
			camHandler.doRotate(0,time*-scale);
		}
		if (KeyInput.get().isKeyDown(KeyInput.KEY_LBRACKET)) {
			camHandler.doZoom(time*zoomScale);
		}
		if (KeyInput.get().isKeyDown(KeyInput.KEY_RBRACKET)) {
			camHandler.doZoom(time*-zoomScale);
		}
	}
}
