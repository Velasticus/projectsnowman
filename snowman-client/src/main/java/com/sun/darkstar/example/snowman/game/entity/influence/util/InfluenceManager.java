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
package com.sun.darkstar.example.snowman.game.entity.influence.util;

import java.util.HashMap;

import com.sun.darkstar.example.snowman.common.entity.influence.enumn.EInfluence;
import com.sun.darkstar.example.snowman.common.interfaces.IInfluence;
import com.sun.darkstar.example.snowman.game.entity.influence.BurnedInfluence;
import com.sun.darkstar.example.snowman.game.entity.influence.SlipperyInfluence;
import com.sun.darkstar.example.snowman.unit.Manager;
import com.sun.darkstar.example.snowman.unit.enumn.EManager;

/**
 * <code>InfluenceManager</code> is a <code>Manager</code> that is responsible
 * for managing all <code>IInfluence</code> of static entities.
 * <p>
 * <code>InfluenceManager</code> manages the creation of an <code>IInfluence</code>.
 * It maintains an <code>IInfluence</code> pool which allows the system to reuse
 * created <code>IInfluence</code> for multiple static entities. This means that
 * <code>InfluenceManager</code> does not allow multiple <code>IInfluence</code>
 * with the same <code>EInfluence</code> enumeration exist.
 * <p>
 * <code>InfluenceManager</code> does not allow destruction of an influence, since
 * <code>IInfluence</code> objects are generally not resource intensive and the
 * static entities which share these <code>IInfluence</code> always exist.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-06-2008 11:35 EST
 * @version Modified date: 06-29-2008 17:53 EST
 */
public class InfluenceManager extends Manager {
	/**
	 * The <code>InfluenceManager</code> instance.
	 */
	private static InfluenceManager instance;
	/**
	 * The <code>HashMap</code> of <code>EInfluence</code> and <code>IInfluence</code> pair.
	 */
	private final HashMap<EInfluence, IInfluence> influences;
	
	/**
	 * Constructor of <code>InfluenceManager</code>.
	 */
	private InfluenceManager() {
		super(EManager.InfluenceManager);
		this.influences = new HashMap<EInfluence, IInfluence>();
	}
	
	/**
	 * Retrieve the <code>InfluenceManager</code> instance.
	 * @return The <code>InfluenceManager</code> instance.
	 */
	public static InfluenceManager getInstance() {
		if(InfluenceManager.instance == null) {
			InfluenceManager.instance = new InfluenceManager();
		}
		return InfluenceManager.instance;
	}
	
	/**
	 * Register the given influence with the <code>InfluenceManager</code>.
	 * @param influence The <code>IInfluence</code> to be registered.
	 * @return True if the given influence is successfully registered. False otherwise.
	 */
	public boolean registerInfluence(IInfluence influence) {
		final EInfluence enumn = influence.getEnumn();
		if(this.influences.containsKey(enumn)) {
			this.logger.fine("Influence has already been registered.");
			return false;
		}
		this.influences.put(enumn, influence);
		return true;
	}
	
	/**
	 * Retrieve the influence with given influence ID.
	 * @param enumn The <code>EInfluence</code> enumeration.
	 * @return The <code>IInfluence</code> with given ID.
	 */
	public IInfluence getInfluence(EInfluence enumn) {
		IInfluence influence = this.influences.get(enumn);
		if(influence == null) return this.createInfluence(enumn);
		return influence;
	}
	
	/**
	 * Create an influence based on given influence ID.
	 * @param enumn The <code>EInfluence</code> enumeration.
	 * @return The <code>IInfluence</code> with given ID.
	 */
	private IInfluence createInfluence(EInfluence enumn) {
		IInfluence influence = null;
		switch(enumn) {
		case Burned:
			influence = new BurnedInfluence();
			break;
		case Slippery:
			influence = new SlipperyInfluence();
			break;
		}
		this.influences.put(enumn, influence);
		this.logger.fine("Created " + enumn.toString() + " influence");
		return influence;
	}
	
	/**
	 * Clean up <code>InfluenceManager</code> by removing all influences.
	 */
	@Override
	public void cleanup() {
		this.influences.clear();
	}
}
