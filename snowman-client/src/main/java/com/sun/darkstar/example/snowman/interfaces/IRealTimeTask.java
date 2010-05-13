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

/**
 * <code>IRealTimeTask</code> defines the interface for time dependent tasks.
 * This means that  the older version of the task can be safely discarded if
 * there is a newer version of the same task available.
 * <p>
 * <code>IRealTimeTask</code> generates a time stamp at construction time that
 * represents the time this <code>IRealTimeTask</code> is created.
 * <p>
 * <code>IRealTimeTask</code> provides the functionality to compare with another
 * <code>IRealTimeTask</code> and determine the order of construction.
 * <p>
 * Realizations of <code>IRealTimeTask</code> are required to override 'equals'
 * method with detailed comparisons based on the information the task maintains.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-05-2008 12:44 EST
 * @version Modified date: 07-09-2008 14:54 EST
 */
public interface IRealTimeTask extends ITask {
	
	/**
	 * Check if the given object is the same as this real time task.
	 * @param object The <code>Object</code> to check against.
	 * @return True if the given object is the same as this task. False otherwise.
	 */
	public boolean equals(Object object);

	/**
	 * Retrieve the time stamp of this real time task.
	 * @return The time stamp of this <code>IRealTimeTask</code>.
	 */
	public long getTimestamp();

	/**
	 * Compare the construction time stamp of this task with the given one.
	 * @param task The <code>IRealTimeTask</code> to be compared with.
	 * @return True if this task is constructed later than the given one.
	 */
	public boolean isLaterThan(IRealTimeTask task);
}
