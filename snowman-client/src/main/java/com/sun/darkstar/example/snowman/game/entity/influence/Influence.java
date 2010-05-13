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
package com.sun.darkstar.example.snowman.game.entity.influence;

import java.io.IOException;

import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.sun.darkstar.example.snowman.common.entity.influence.enumn.EInfluence;
import com.sun.darkstar.example.snowman.common.interfaces.IInfluence;

/**
 * <code>Influence</code> implements <code>IInfluence</code> to define the
 * most basic abstraction of an influence in game world.
 * <p>
 * Subclasses of <code>Influence</code> should implement the actual influence
 * logic inside <code>perform(...)</code> method.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-06-2008 13:42 EST
 * @version Modified date: 06-18-2008 12:05 EST
 */
public abstract class Influence implements IInfluence {
	/**
	 * The <code>EInfluence</code> enumeration of this <code>Influence</code>
	 */
	private EInfluence enumn;
	
	/**
	 * Constructor of <code>Influence</code>.
	 */
	public Influence() {
		super();
	}
	
	/**
	 * Constructor of <code>Influence</code>.
	 * @param enumn The <code>EInfluence</code> enumeration.
	 */
	public Influence(EInfluence enumn) {
		this.enumn = enumn;
	}
	
	@Override
	public EInfluence getEnumn() {
		return this.enumn;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof IInfluence) {
			IInfluence given = (IInfluence)object;
			return this.enumn.equals(given.getEnumn());
		}
		return false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return Influence.class;
	}
	
	@Override
	public void write(JMEExporter ex) throws IOException {
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(this.enumn.toString(), "Enumeration", null);
	}
	
	@Override
	public void read(JMEImporter im) throws IOException {
		InputCapsule ic = im.getCapsule(this);
		this.enumn = EInfluence.valueOf(ic.readString("Enumeration", null));
	}
}
