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
package com.sun.darkstar.example.snowman.common.util;

/**
 * <code>IHPConverter</code> defines the interface of the singleton utility
 * class that is responsible for converting HP values into scale, speed,
 * and attack range values.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-21-2008 17:50 EST
 * @version Modified date: 07-25-2008 12:08 EST
 */
public interface IHPConverter {

	/**
	 * Convert the given HP value into scale value.
	 * @param hp The integer HP value to be converted
	 * @return The float scale converted from the given HP value.
	 */
	public float convertScale(int hp);
	
	/**
	 * Convert the given HP value into mass value.
	 * @param hp The integer HP value to be converted
	 * @return The float mass converted from the given HP value.
	 */
	public float convertMass(int hp);
	
	/**
	 * Convert the given HP value into range value.
	 * @param hp The integer HP value to be converted
	 * @return The float range converted from the given HP value.
	 */
	public float convertRange(int hp);
	
	/**
	 * Retrieve the maximum HP value.
	 * @return The integer maximum HP value.
	 */
	public int getMaxHP();
	
	/**
	 * Retrieve the default mass value.
	 * @return The float default mass value.
	 */
	public float getDefaultMass();
	
	/**
	 * Retrieve the default range value.
	 * @return The float default range value.
	 */
	public float getDefaultRange();
}
