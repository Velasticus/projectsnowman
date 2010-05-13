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
package com.sun.darkstar.example.snowman.unit;

import java.util.logging.Logger;

import com.sun.darkstar.example.snowman.unit.enumn.EManager;

/**
 * <code>Manager</code> defines the most basic abstraction for all types of
 * managing systems in the game. All managing systems should follow the
 * singleton design pattern.
 * <p>
 * <code>Manager</code> maintains an unique <code>EManager</code> that defines
 * the type of this managing system and a <code>Logger</code> instance used for
 * logging any useful information.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-09-2008 12:15 EST
 * @version Modified date: 06-29-2008 17:55 EST
 */
public abstract class Manager {
	/**
	 * The <code>EManager</code> enumeration.
	 */
	private final EManager type;
	/**
	 * The <code>Logger</code> instance.
	 */
	protected final Logger logger;
	
	/**
	 * Constructor of <code>Manager</code>
	 * @param type The <code>EManager</code> enumeration.
	 */
	protected Manager(EManager type) {
		this.type = type;
		this.logger = Logger.getLogger(this.type.toString());
	}
	
	/**
	 * Clean up the <code>Manager</code>.
	 */
	public abstract void cleanup();
}
