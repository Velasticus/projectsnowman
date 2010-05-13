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

import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity;
import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity.EEntityType;
import com.sun.darkstar.example.snowman.common.interfaces.IEntity;

/**
 * <code>Entity</code> defines the base abstraction of an entity in the game
 * world. It maintains the basic information about the object including the
 * <code>IView</code> reference, the <code>EEntityType</code> and the
 * <code>EEntity</code>.
 * <p>
 * <code>Entity</code> is constructed by the <code>EntityManager</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-04-2008 15:07 EST
 * @version Modified date: 07-01-2008 15:28 EST
 */
public abstract class Entity implements IEntity {
	/**
	 * The <code>EEntity</code> enumeration of this <code>Entity</code>.
	 */
	private EEntity enumn;
	/**
	 * The integer ID number of this <code>Entity</code>.
	 */
	private int idNumber;
	
	/**
	 * Constructor of <code>Entity</code>.
	 */
	public Entity() {
		super();
	}
	
	/**
	 * Constructor of <code>Entity</code>
	 * @param enumn The <code>EEntity</code> enumeration.
	 * @param id The integer ID number of this entity.
	 */
	public Entity(EEntity enumn, int id) {
		this.enumn = enumn;
		this.idNumber = id;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof IEntity) {
			IEntity given = (IEntity)object;
			return given.getID() == this.idNumber;
		}
		return false;
	}
        
        @Override
        public int hashCode() {
            return Integer.valueOf(idNumber).hashCode();
        }

	@Override
	public int getID() {
		return this.idNumber;
	}

	@Override
	public EEntityType getType() {
		return this.enumn.getType();
	}

	@Override
	public EEntity getEnumn() {
		return this.enumn;
	}
	
	@SuppressWarnings("unchecked")
	public Class getClassTag() {
		return this.getClass();
	}
	
	@Override
	public String toString() {
		return this.enumn.toString() + this.idNumber;
	}
	
	@Override
	public void write(JMEExporter ex) throws IOException {
		OutputCapsule oc = ex.getCapsule(this);
		oc.write(this.enumn.toString(), "Enumeration", null);
		oc.write(this.idNumber, "ID", 0);
	}
	
	@Override
	public void read(JMEImporter im) throws IOException {
		InputCapsule ic = im.getCapsule(this);
		this.enumn = EEntity.valueOf(ic.readString("Enumeration", null));
		this.idNumber = ic.readInt("ID", 0);
	}
}
