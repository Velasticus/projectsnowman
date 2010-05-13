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

import com.jme.util.export.Savable;
import com.sun.darkstar.example.snowman.common.entity.influence.enumn.EInfluence;

/**
 * <code>IInfluence</code> defines the interface for all types of influences
 * that affect <code>IDynamicEntity</code>.
 * <p>
 * <code>IInfluence</code> has a unique <code>EInfluenceID</code> that defines
 * the type of this <code>IInfluence</code>. Two <code>IInfluence</code> are
 * considered 'equal' if and only if they have the same <code>EInfluenceID</code>.
 * <p>
 * <code>IInfluence</code> can be performed on all entities that implements
 * <code>IDynamicEntity</code>.
 * <p>
 * <code>IInfluence</code> extends <code>Savable</code> interface so it can be
 * directly saved into a jME binary format which can then be imported
 * later on at game run time.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-04-2008 15:17 EST
 * @version Modified date: 06-18-2008 11:25 EST
 */
public interface IInfluence extends Savable {

	/**
	 * Perform this influence on the given entity.
	 * @param entity The <code>IDynamicEntity</code> to be affected.
	 */
	public void perform(IDynamicEntity entity);
	
	/**
	 * Retrieve the ID of this influence.
	 * @return The <code>EInfluence</code> ID enumeration.
	 */
	public EInfluence getEnumn();
}
