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

import java.util.ArrayList;

/**
 * <code>IStaticEntity</code> defines the interface for all types of static
 * entities in the game world.
 * <p>
 * <code>IStaticEntity</code> is constructed from <code>IEditableEntity</code>
 * during the world export process. It extends <code>IFinal</code> to allow
 * construction based on <code>IEditable</code>.
 * <p>
 * <code>IStaticEntity</code> maintains a list of <code>IInfluence</code> of
 * the static entity. This list defines the ways this static entity affects
 * other <code>IDynamicEntity</code>.
 * <p>
 * <code>IStaticEntity</code> can perform all the <code>IInfluence</code> it
 * maintains on a given <code>IDynamicEntity</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-04-2008 15:08 EST
 * @version Modified date: 07-01-2008 14:02 EST
 */
public interface IStaticEntity extends IEntity, IFinal {
	
	/**
	 * Attach the given influence to this static entity.
	 * @param influence The <code>IInfluence</code> to be added.
	 * @return True if the given influence is added. False if influence already exists.
	 */
	public boolean attachInfluence(IInfluence influence);

	/**
	 * Detach the influence with given ID.
	 * @param influence The <code>IInfluence</code> to be removed.
	 * @return True if the given influence is removed. False if it does not exist.
	 */
	public boolean detachInfluence(IInfluence influence);
	
	/**
	 * Perform all the influences on the given dynamic entity.
	 */
	public void performInfluence(IDynamicEntity entity);
	
	/**
	 * Retrieve the list of influences of this static entity.
	 * @return The <code>ArrayList</code> of <code>IInfluence</code>.
	 */
	public ArrayList<IInfluence> getInfluences();
}
