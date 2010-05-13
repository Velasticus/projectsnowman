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
package com.sun.darkstar.example.tool;

import java.io.IOException;
import java.util.ArrayList;

import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;
import com.sun.darkstar.example.snowman.common.world.EditableWorld;

public class SavableWorld implements Savable {

	private EditableWorld world = null;
	private ArrayList<TextureLayer> layers = null;
	private int idcount = 0;

    public EditableWorld getWorld() {
		return world;
	}

	public void setWorld(EditableWorld world) {
		this.world = world;
	}

	public ArrayList<TextureLayer> getLayers() {
		return layers;
	}

	public void setLayers(ArrayList<TextureLayer> layers) {
		this.layers = layers;
	}

	public int getIdcount() {
		return idcount;
	}

	public void setIdcount(int idcount) {
		this.idcount = idcount;
	}

	public Class<? extends SavableWorld> getClassTag() {
        return this.getClass();
    }
    
    public void write(JMEExporter ex) throws IOException {
    	OutputCapsule cap = ex.getCapsule(this);
    	cap.write(world, "world", null);
    	cap.writeSavableArrayList(layers, "layers", null);
    	cap.write(idcount, "idcount", 0);
    }

    @SuppressWarnings("unchecked")
	public void read(JMEImporter im) throws IOException {
    	InputCapsule cap = im.getCapsule(this);
    	world = (EditableWorld) cap.readSavable("world", null);
    	layers = cap.readSavableArrayList("layers", null);
    	idcount = cap.readInt("idcount", 0);
    }
}
