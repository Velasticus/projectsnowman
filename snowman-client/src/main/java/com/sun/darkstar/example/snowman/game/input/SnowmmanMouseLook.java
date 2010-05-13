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

import com.jme.input.KeyInput;
import com.jme.input.Mouse;
import com.jme.input.MouseInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;
import com.sun.darkstar.example.snowman.ClientApplication;
import com.sun.darkstar.example.snowman.common.entity.enumn.ECursorState;

/**
 * Handles mouse interaction for the snowman camera system.
 * 
 * @author Joshua Slack
 */
public class SnowmmanMouseLook extends MouseInputAction {

	private float zoomPerClick = 2f;
	private boolean looking;
	private int cursorX, cursorY;
	private final SnowmanCameraHandler camHandler;
	
	public SnowmmanMouseLook(final Mouse aMouse, final SnowmanCameraHandler camHandler) {
		this.mouse = aMouse;
		this.camHandler = camHandler;
	}

	public void performAction(final InputActionEvent evt) {
		float scale = 0.008f;

		int wheelDelta = MouseInput.get().getWheelDelta();
		if (wheelDelta != 0) {
			camHandler.doZoom((wheelDelta > 0 ? zoomPerClick : -zoomPerClick));
		}

		// right
		boolean btn2 = MouseInput.get().isButtonDown(1);

		float mouseX = mouse.getLocalTranslation().x;
		float mouseY = mouse.getLocalTranslation().y;
        
		// Hold and reset cursor position
        if (!looking && btn2) {
            cursorX = MouseInput.get().getXAbsolute();
            cursorY = MouseInput.get().getYAbsolute();
        	MouseInput.get().setCursorVisible(false);
            looking = true;
        } else if (looking && !btn2) {
        	MouseInput.get().setCursorVisible(true);
			MouseInput.get().setHardwareCursor(ClientApplication.class.getClassLoader().getResource(ECursorState.TryingToMove.getIconLocation()));
        	MouseInput.get().setCursorPosition(cursorX, cursorY);
            looking = false;
        }

		if (btn2) {
			if (KeyInput.get().isControlDown()) {
				// Zoom
				if (mouseY != 0) {
					camHandler.doZoom(.25f * (mouseY > 0 ? zoomPerClick : -zoomPerClick));
				}
			} else {
				// rotate camera horizontally/vertically using scale and x/y mouse
				// shift
				if (mouseX != 0 || mouseY != 0) {
					camHandler.doRotate(scale * -mouseX, scale * mouseY);
				}
			}
		}
	}

	/**
	 * @return the _zoomUnitsPerClick
	 */
	public float getZoomUnitsPerClick() {
		return zoomPerClick;
	}

	/**
	 * @param unitsPerClick
	 *            the _zoomUnitsPerClick to set
	 */
	public void setZoomUnitsPerClick(final float unitsPerClick) {
		zoomPerClick = unitsPerClick;
	}
}
