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
package com.sun.darkstar.example.snowman.common.interfaces;

import com.jme.scene.Node;
import com.jme.util.export.Savable;
import com.sun.darkstar.example.snowman.common.util.enumn.EWorld;

/**
 * <code>IAbstractWorld</code> defines the interface of a basic abstraction
 * of the game world. It maintains all the <code>IEntity</code>, their
 * corresponding <code>IView</code> and <code>IInfluence</code> of the entities
 * for registration purpose during import process. <code>IAbstractWorld</code>
 * is defined by its unique <code>EWorld</code> enumeration.
 * <p>
 * <code>IAbstractWorld</code> extends <code>Savable</code> which allows it
 * to be exported into a jME binary format that can be imported later
 * on for either editing purpose or at game run time.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-01-2008 24:04 EST
 * @version Modified date: 08-11-2008 16:26 EST
 */
public interface IAbstractWorld extends Savable {
	
	/**
	 * Retrieve the enumeration of this world.
	 * @return The <code>EWorld</code> enumeration.
	 */
	public EWorld getWorldEnumn();
	
	/**
	 * Retrieve the root node of all static entities.
	 * @return The static entity root <code>Node</code>.
	 */
	public Node getStaticRoot();

	/**
	 * Retrieve the root node of all dynamic entities.
	 * @return The dynamic entity root <code>Node</code>.
	 */
	public Node getDynamicRoot();
}
