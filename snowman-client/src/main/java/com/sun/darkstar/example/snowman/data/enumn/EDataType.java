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
package com.sun.darkstar.example.snowman.data.enumn;

/**
 * <code>EDataType</code> defines enumerations of all the types of data files
 * utilized by the data system. Each <code>EDataType</code> enumeration is
 * paired with a specific file extension and a directory of that data type.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-17-2008 11:14 EST
 * @version Modified date: 07-17-2008 11:30 EST
 */
public enum EDataType {
	/**
	 * The animation data type.
	 */
	Animation(".anim", "com/sun/darkstar/example/snowman/data/animation/"),
	/**
	 * The static mesh data type.
	 */
	StaticMesh(".mesh", "com/sun/darkstar/example/snowman/data/mesh/"),
	/**
	 * The character dynamic mesh data type.
	 */
	DynamicMesh(".char", "com/sun/darkstar/example/snowman/data/mesh/character/"),
	/**
	 * The system data type.
	 */
	SystemData(".data", "com/sun/darkstar/example/snowman/data/system/"),
	/**
	 * The texture data type.
	 */
	Texture(".tex", "com/sun/darkstar/example/snowman/data/texture/"),
	/**
	 * The world data type.
	 */
	World(".wld", "com/sun/darkstar/example/snowman/data/world/");
	
	/**
	 * The <code>String</code> extension.
	 */
	private final String ext;
	/**
	 * The <code>String</code> directory of the extension.
	 */
	private final String dir;
	
	/**
	 * Constructor of <code>EDataType</code>.
	 * @param ext The <code>String</code> extension.
	 * @param dir The <code>String</code> directory.
	 */
	private EDataType(String ext, String dir) {
		this.ext = ext;
		this.dir = dir;
	}
	
	/**
	 * Retrieve the actual extension of the data type.
	 * @return The <code>String</code> extension.
	 */
	public String getExtension() {
		return this.ext;
	}
	
	/**
	 * Retrieve the directory associated with this data type.
	 * @return The <code>String</code> directory of the data type.
	 */
	public String getDirectory() {
		return this.dir;
	}
	
	/**
	 * Convert the given file name into a complete path of that file.
	 * @param file The name of the file.
	 * @return The <code>String</code> path to the file with given file name.
	 */
	public String toPath(String file) {
		return this.dir + file + this.ext;
	}
}
