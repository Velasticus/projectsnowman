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
 * <code>HPConverter</code> implements <code>IHPConverter</code> to define
 * the HP converter utility class.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-21-2008 17:53 EST
 * @version Modified date: 07-25-2008 12:08 EST
 */
public final class HPConverter implements IHPConverter {
	/**
	 * The <code>HPConverter</code> instance.
	 */
	private static HPConverter instance;
	/**
	 * The maximum HP value.
	 */
	private final int maxHP;
	/**
	 * The maximum scale value.
	 */
	private final float maxScale;
	/**
	 * The default mass value.
	 */
	private final float defaultMass;
	/**
	 * The default range value.
	 */
	private final float defaultRange;
	
	/**
	 * Constructor of <code>HPConverter</code>.
	 */
	private HPConverter() {
		this.maxHP = 100;
		this.maxScale = 2;
		this.defaultMass = 10;
		this.defaultRange = 10;
	}
	
	/**
	 * Retrieve the <code>HPConverter</code> instance.
	 * @return The <code>HPConverter</code> instance.
	 */
	public static IHPConverter getInstance() {
		if(HPConverter.instance == null) {
			HPConverter.instance = new HPConverter();
		}
		return HPConverter.instance;
	}

	@Override
	public float convertScale(int hp) {
		return ((this.maxScale * this.maxHP) - hp) / this.maxHP;
	}

	@Override
	public float convertMass(int hp) {
		return this.convertScale(hp) * this.defaultMass;
	}

	@Override
	public float convertRange(int hp) {
		return this.convertScale(hp) * this.defaultRange;
	}

	@Override
	public int getMaxHP() {
		return this.maxHP;
	}

	@Override
	public float getDefaultMass() {
		return this.defaultMass;
	}

	@Override
	public float getDefaultRange() {
		return this.defaultRange;
	}
}
