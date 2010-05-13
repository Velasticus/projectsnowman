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
package com.sun.darkstar.example.snowman.game.input.gui;

import org.fenggui.event.Key;
import org.lwjgl.input.Keyboard;

import com.jme.input.KeyInputListener;
import com.sun.darkstar.example.snowman.game.input.InputConverter;
import com.sun.darkstar.example.snowman.game.input.enumn.EInputConverter;

/**
 * <code>KeyInputConverter</code> extends <code>InputConverter</code> and
 * implements <code>KeyInputListener</code> to define the utility class which
 * converts jME key inputs into FengGUI events for the GUI
 * systems to process.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 05-28-2008 12:14 EST
 * @version Modified date: 07-21-2008 12:11 EST
 */
public class KeyInputConverter extends InputConverter implements KeyInputListener {

	/**
	 * Constructor of <code>KeyInputConverter</code>.
	 */
	public KeyInputConverter() {
		super(EInputConverter.KeyboardConverter);
	}

	/**
	 * Invoked whenever a key is pressed or released.
	 * @param character The character associated with pressed key, 0 if not applicable.
	 * @param keyCode The key code of the pressed/released key.
	 * @param pressed True if key was pressed, false if released.
	 */
	public void onKey(char character, int keyCode, boolean pressed) {
		if(!this.active || this.display == null) return;
		// Convert the last pressed LWJGL key to FengGUI key event.
		Key keyEvent = this.convertKeyEvent();
		// If the key was pressed, fire both KeyPressedEvent and KeyTypedEvent.
		if(pressed) {
			this.display.fireKeyPressedEvent(character, keyEvent);
			this.display.fireKeyTypedEvent(character);
			// Otherwise fire the KeyReleasedEvent.
		} else {
			this.display.fireKeyReleasedEvent(character, keyEvent);
		}
	}

	/**
	 * Convert the last pressed LWJGL key to FengGUI key event.
	 * @return The GUI <code>Key</code> event of the last pressed key.
	 */
	private Key convertKeyEvent() {
		Key GUIKey;
		// FIXME: Should be able to break hard ref to lwjgl here.
		switch(Keyboard.getEventKey()) {
		case Keyboard.KEY_BACK: GUIKey = Key.BACKSPACE;	break;
		case Keyboard.KEY_RETURN: GUIKey = Key.ENTER; break;
		case Keyboard.KEY_DELETE: GUIKey = Key.DELETE; break;
		case Keyboard.KEY_UP: GUIKey = Key.UP; break;
		case Keyboard.KEY_RIGHT: GUIKey = Key.RIGHT; break;
		case Keyboard.KEY_LEFT:	GUIKey = Key.LEFT; break;
		case Keyboard.KEY_DOWN: GUIKey = Key.DOWN; break;
		case Keyboard.KEY_SCROLL: GUIKey = Key.SHIFT; break;
		case Keyboard.KEY_LMENU: GUIKey = Key.ALT; break;
		case Keyboard.KEY_RMENU: GUIKey = Key.ALT; break;
		case Keyboard.KEY_LCONTROL: GUIKey = Key.CTRL; break;
		case Keyboard.KEY_RSHIFT: GUIKey = Key.SHIFT; break;     
		case Keyboard.KEY_LSHIFT: GUIKey = Key.SHIFT; break;              
		case Keyboard.KEY_RCONTROL: GUIKey = Key.CTRL; break;
		case Keyboard.KEY_INSERT: GUIKey = Key.INSERT; break;
		case Keyboard.KEY_TAB: GUIKey = Key.TAB; break;
		case Keyboard.KEY_F12: GUIKey = Key.F12; break;
		case Keyboard.KEY_F11: GUIKey = Key.F11; break;
		case Keyboard.KEY_F10: GUIKey = Key.F10; break;
		case Keyboard.KEY_F9: GUIKey = Key.F9; break;
		case Keyboard.KEY_F8: GUIKey = Key.F8; break;
		case Keyboard.KEY_F7: GUIKey = Key.F7; break;
		case Keyboard.KEY_F6: GUIKey = Key.F6; break;
		case Keyboard.KEY_F5: GUIKey = Key.F5; break;
		case Keyboard.KEY_F4: GUIKey = Key.F4; break;
		case Keyboard.KEY_F3: GUIKey = Key.F3; break;
		case Keyboard.KEY_F2: GUIKey = Key.F2; break;
		case Keyboard.KEY_F1: GUIKey = Key.F1; break;
		default:
			if("1234567890".indexOf(Keyboard.getEventCharacter()) != -1)  GUIKey = Key.DIGIT;
			else GUIKey = Key.LETTER;
		break;
		}
		return GUIKey;
	}
}
