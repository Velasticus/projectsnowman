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
package com.sun.darkstar.example.snowman.game.task;

import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;
import com.sun.darkstar.example.snowman.interfaces.IRealTimeTask;

/**
 * <code>RealTimeTask</code> implements <code>IRealTimeTask</code> to provide
 * the most basic implementation for time dependent tasks. It maintains the
 * construction time stamp for time stamp check purpose.
 * <p>
 * Subclasses of <code>RealTimeTask</code> need to implement the actual
 * execution logic details.
 * <p>
 * <code>RealTimeTask</code> uses the <code>ETask</code> enumeration for the
 * 'equals' comparison. Subclasses need to provide more detailed comparison
 * logic based on the task specific information.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-02-2008 16:48 EST
 * @version Modified date: 07-09-2008 14:59 EST
 */
public abstract class RealTimeTask extends Task implements IRealTimeTask {
	/**
	 * The time stamp of this <code>RealTimeTask</code>
	 */
	protected final long timestamp;

	/**
	 * Constructor of <code>RealTimeTask</code>
	 * @param enumn The <code>ETask</code> enumeration of this </code>Task</code>.
	 * @param game The <code>Game</code> instance.
	 */
	public RealTimeTask(ETask enumn, Game game) {
		super(enumn, game);
		this.timestamp = System.currentTimeMillis();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof RealTimeTask) {
			RealTimeTask task = (RealTimeTask)o;
			return this.enumn == task.enumn;
		}
		return false;
	}
	
	@Override
	public long getTimestamp() {
		return this.timestamp;
	}
	
	@Override
	public boolean isLaterThan(IRealTimeTask task) {
		// Consider equal to be later than.
		return (this.timestamp >= task.getTimestamp());
	}
}
