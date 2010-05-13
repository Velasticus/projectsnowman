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
package com.sun.darkstar.example.snowman.interfaces;

import org.fenggui.Display;

import com.sun.darkstar.example.snowman.game.input.enumn.EInputConverter;

/**
 * <code>IInputConverter</code> defines the interface for converters that
 * converts jME input events to FengGUI events.
 * <p>
 * <code>IInputConverter</code> only converts the input events to the set
 * FengGUI <code>Display</code> instance. It maintains a reference
 * to the <code>Display</code> instance that should receive the converted
 * FengGUI events.
 * <p>
 * <code>IInputConverter</code> can be disabled manually to stop the conversion
 * from jME input events to FengGUI events. This method can
 * be used to disable all the current displaying GUI.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-09-2008 11:01 EST
 * @version Modified date: 07-21-2008 12:10 EST
 */
public interface IInputConverter {

	/**
	 * Set the display instance that should receive the converted events.
	 * @param display The FengGUI <code>Display</code> instance.
	 */
	public void setDisplay(Display display);
	
	/**
	 * Set the activeness of the input conversion process.
	 * @param active True if conversion should be activated. False otherwise.
	 */
	public void setActive(boolean active);
	
	/**
	 * Retrieve the type enumeration of this converter.
	 * @return The <code>EInputConverter</code> enumeration.
	 */
	public EInputConverter getEnumn();
	
	/**
	 * Check if the input conversion process is active.
	 * @return True if conversion is active. False otherwise.
	 */
	public boolean isActive();
}
