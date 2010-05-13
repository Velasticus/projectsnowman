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
package com.sun.darkstar.example.snowman.exception;

/**
 * <code>ObjectNotFoundException</code> is thrown in response to attempting to
 * retrieve an object such as an <code>IEntity</code> or <code>IInfluence</code>
 * that is not created yet.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-06-2008 11:47 EST
 * @version Modified date: 06-06-2008 17:34 EST
 */
public class ObjectNotFoundException extends RuntimeException {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = -3361707271776252063L;
	
	/**
	 * Constructor of <code>ObjectNotFoundException</code>.
	 */
	public ObjectNotFoundException() {
		super();
	}
	
	/**
	 * Constructor of <code>ObjectNotFoundException</code>.
	 * @param objectID The ID of the object attempted to retrieve.
	 */
	public ObjectNotFoundException(String objectID) {
		super("Object " + objectID + " not found.");
	}
}
