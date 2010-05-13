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

import org.fenggui.Display;

import com.sun.darkstar.example.snowman.game.input.enumn.EInputConverter;
import com.sun.darkstar.example.snowman.interfaces.IInputConverter;

/**
 * <code>InputConverter</code> implements <code>IInputConverter</code> to
 * define the basic abstraction of an converter that converts jME
 * input events into FengGUI events.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-11-2008 16:53 EST
 * @version Modified date: 07-21-2008 12:11 EST
 */
public abstract class InputConverter implements IInputConverter {
	/**
	 * The <code>EInputConverter</code> enumeration.
	 */
	protected final EInputConverter enumn;
	/**
	 * The FengGUI <code>Display</code> instance.
	 */
	protected Display display;
	/**
	 * The flag indicates if this converter is active.
	 */
	protected boolean active;
	
	/**
	 * Constructor of <code>InputConverter</code>.
	 * @param enumn The <code>EInputConverter</code> enumeration.
	 */
	protected InputConverter(EInputConverter enumn) {
		this.enumn = enumn;
		this.active = true;
	}

	@Override
	public void setDisplay(Display display) {
		if(display == null) throw new IllegalArgumentException("Given display instance is null.");
		this.display = display;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public EInputConverter getEnumn() {
		return this.enumn;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}
}
