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
package com.sun.darkstar.example.snowman.common.entity;

import java.io.IOException;
import java.util.ArrayList;

import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity;
import com.sun.darkstar.example.snowman.common.interfaces.IEditableEntity;
import com.sun.darkstar.example.snowman.common.interfaces.IFinal;
import com.sun.darkstar.example.snowman.common.interfaces.IInfluence;

/**
 * <code>EditableEntity</code> extends <code>Entity</code> and implements
 * <code>IEditableEntity</code> to define the actual editable object
 * in the world during editing stages.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-01-2008 15:40 EST
 * @version Modified date: 07-01-2008 21:06 EST
 */
public class EditableEntity extends Entity implements IEditableEntity {
	/**
	 * The <code>ArrayList</code> of <code>IInfluence</code>.
	 */
	private ArrayList<IInfluence> influences;
	
	/**
	 * Constructor of <code>EditableEntity</code>.
	 */
	public EditableEntity() {
		super();
	}
	
	/**
	 * Constructor of <code>EditableEntity</code>.
	 * @param enumn The <code>EEntity</code> enumeration.
	 * @param id The integer ID number of this entity.
	 */
	public EditableEntity(EEntity enumn, int id) {
		super(enumn, id);
		this.influences = new ArrayList<IInfluence>();
	}

	@Override
	public IFinal constructFinal() {
		StaticEntity entity = new StaticEntity(this.getEnumn(), this.getID());
		entity.process(this);
		return entity;
	}
	
	@Override
	public boolean attachInfluence(IInfluence influence) {
		if(this.influences.contains(influence)) return false;
		this.influences.add(influence);
		return true;
	}

	@Override
	public boolean detachInfluence(IInfluence influence) {
		return this.influences.remove(influence);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<IInfluence> getInfluences() {
		return (ArrayList<IInfluence>)this.influences.clone();
	}
	
	@Override
	public void write(JMEExporter ex) throws IOException {
		super.write(ex);
		OutputCapsule oc = ex.getCapsule(this);
		oc.writeSavableArrayList(this.influences, "Influences", null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void read(JMEImporter im) throws IOException {
		super.read(im);
		InputCapsule ic = im.getCapsule(this);
		this.influences = (ArrayList<IInfluence>)ic.readSavableArrayList("Influences", null);
	}
}
