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
package com.sun.darkstar.example.snowman.common.entity.enumn;


/**
 * <code>ECursorState</code> defines the enumerations of all the possible states
 * that the targeting cursor can be in.
 * 
 * @author Owen Kellett
 */
public enum ECursorState {
    /**
     * Invalid operation
     */
    Invalid("com/sun/darkstar/example/snowman/data/icons/IconWalking.png"),
    /**
     * The trying to move state
     */
    TryingToMove("com/sun/darkstar/example/snowman/data/icons/IconWalking.png"),
    /**
     * The targeting other snowman state.
     */
    Targeting("com/sun/darkstar/example/snowman/data/icons/IconTarget.png"),
    /**
     * The trying to grab flag state.
     */
    TryingToGrab("com/sun/darkstar/example/snowman/data/icons/IconGrab.png");

    final String iconLocation;
    
    private ECursorState(String location) {
		this.iconLocation = location;
	}
    
	public String getIconLocation() {
		return iconLocation;
	}
}
