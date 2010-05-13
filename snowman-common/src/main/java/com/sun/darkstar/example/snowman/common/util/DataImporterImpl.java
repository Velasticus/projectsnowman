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

import java.io.IOException;
import java.net.URL;

import com.jme.scene.Spatial;
import com.jme.util.export.binary.BinaryImporter;
import com.sun.darkstar.example.snowman.common.util.enumn.EWorld;

/**
 * <code>DataImporter</code> is a singleton utility class that is responsible
 * for importing binary data files.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-16-2008 16:44 EST
 * @version Modified date: 07-16-2008 16:57 EST
 */
public class DataImporterImpl implements DataImporter{
    /**
     * The <code>DataImporter</code> instance.
     */
    private static DataImporter instance;
    /**
     * The <code>String</code> directory.
     */
    private final String dir;
    /**
     * The world data extension.
     */
    private final String extWorld;

    /**
     * Constructor of <code>DataImporter</code>.
     */
    protected DataImporterImpl() {
        this.dir = "com/sun/darkstar/example/snowman/common/data/world/";
        this.extWorld = ".wld";
    }

    /**
     * Retrieve the <code>DataImporter</code> instance.
     * @return The <code>DataImporter</code> instance.
     */
    public static DataImporter getInstance() {
        if (DataImporterImpl.instance == null) {
            DataImporterImpl.instance = new DataImporterImpl();
        }
        return DataImporterImpl.instance;
    }

    /**
     * Retrieve the world geometry data.
     * @param enumn The <code>EWorld</code> enumeration.
     * @return The loaded <code>Spatial</code> world data.
     */
    public Spatial getWorld(EWorld enumn) {
        URL url = this.getClass().getClassLoader().getResource(this.dir + enumn.toString() + this.extWorld);
        try {
            return (Spatial) BinaryImporter.getInstance().load(url.openStream());
        } catch (IOException e) {
            throw new NullPointerException("Cannot find world data: " + enumn.toString());
        }
    }
}
