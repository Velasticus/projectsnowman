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

import com.sun.darkstar.example.snowman.game.task.enumn.ETask;

/**
 * <code>ITask</code> defines the interface for all types of tasks that are
 * generated in response to either user inputs received by various types of
 * <code>IController</code> or packets processed by <code>IProtocolProcessor</code>.
 * <p>
 * <code>ITask</code> is a logic unit which contains execution logic that
 * modifies the state of the <code>Game</code> or an <code>IEntity</code>.
 * It does not contain any pre-calculation logic such as finding target or
 * calculating destination.
 * <p>
 * <code>ITask</code> maintains an <code>ETask</code> which defines the base
 * type and logic type of this <code>ITask</code>.
 * <p>
 * <code>ITask</code> is created separately but maintained and executed inside
 * the main game update/render loop by <code>TaskManager</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-02-2008 15:15 EST
 * @version Modified date: 07-09-2008 13:50 EST
 */
public interface ITask {
	
	/**
	 * Execute this task.
	 */
	public void execute();
	
	/**
	 * Retrieve the enumeration of this task.
	 * @return The <code>ETask</code> enumeration of this <code>ITask</code>.
	 */
	public ETask getEnumn();
}
