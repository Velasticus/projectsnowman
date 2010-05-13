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
package com.sun.darkstar.example.snowman.game.entity.util;

import java.util.HashMap;

import com.sun.darkstar.example.snowman.common.entity.EditableEntity;
import com.sun.darkstar.example.snowman.common.entity.StaticEntity;
import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity;
import com.sun.darkstar.example.snowman.common.entity.terrain.TerrainEntity;
import com.sun.darkstar.example.snowman.common.interfaces.IEntity;
import com.sun.darkstar.example.snowman.exception.DuplicatedIDException;
import com.sun.darkstar.example.snowman.exception.ObjectNotFoundException;
import com.sun.darkstar.example.snowman.game.entity.DynamicEntity;
import com.sun.darkstar.example.snowman.game.entity.scene.CharacterEntity;
import com.sun.darkstar.example.snowman.game.entity.scene.SnowballEntity;
import com.sun.darkstar.example.snowman.game.entity.scene.SnowballTrailEntity;
import com.sun.darkstar.example.snowman.game.entity.scene.SnowmanEntity;
import com.sun.darkstar.example.snowman.unit.Manager;
import com.sun.darkstar.example.snowman.unit.enumn.EManager;

/**
 * <code>EntityManager</code> is a <code>Manager</code> that is responsible for
 * managing all the <code>IEntity</code> in the game world.
 * <p>
 * <code>EntityManager</code> is responsible for all aspects of entity management
 * including entity creation, retrieving and destruction.
 * <p>
 * <code>EntityManager</code> maintains all the entities by their ID number. This
 * allows multiple entities with the same type defined by <code>EEntity</code>
 * exist. Positive ID numbers are assigned by the server and negative ID numbers
 * are assigned by the client. Entities with negative ID numbers are usually static
 * environment entities.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-05-2008 11:15 EST
 * @version Modified date: 08-12-2008 11:16 EST
 */
public class EntityManager extends Manager {
	/**
	 * The <code>EntityManager</code> instance.
	 */
	private static EntityManager instance;
	/**
	 * The <code>HashMap</code> of ID number and <code>IEntity</code> pair.
	 */
	private final HashMap<Integer, IEntity> entities;
	/**
	 * The client side current entity ID number.
	 */
	private int idcount;
	
	/**
	 * Constructor of <code>EntityManager</code>.
	 */
	private EntityManager() {
		super(EManager.EntityManager);
		this.entities = new HashMap<Integer, IEntity>();
		this.idcount = 0;
	}
	
	/**
	 * Retrieve the <code>EntityManager</code> instance.
	 * @return The <code>EntityManager</code> instance.
	 */
	public static EntityManager getInstance() {
		if(EntityManager.instance == null) {
			EntityManager.instance = new EntityManager();
		}
		return EntityManager.instance;
	}
	
	/**
	 * Register the given entity with <code>EntityManager</code>. This method
	 * should only be invoked by <code>IWorld</code> when it is first loaded.
	 * @param entity The <code>IEntity</code> to be registered.
	 * @return True if the given entity is successfully registered. False otherwise.
	 */
	public boolean registerEntity(IEntity entity) {
		final int id = entity.getID();
		if(this.entities.containsKey(id)) {
			this.logger.fine("ID: " + id + " already in use");
			return false;
		}
		if(this.idcount > id) this.idcount = id;
		this.entities.put(Integer.valueOf(id), entity);
		return true;
	}
	
	/**
	 * Create an entity with given type and ID type.
	 * @param enumn The <code>EEntity</code> enumeration of the entity.
	 * @return The newly created <code>IEntity</code>.
	 */
	public IEntity createEntity(EEntity enumn) {
		this.idcount--;
		// This exception should never be thrown.
		try {return this.createEntity(enumn, this.idcount, "unknown");} catch (DuplicatedIDException e) {return null;}
	}
	
	/**
	 * Create an entity with given type and ID type.
	 * @param enumn The <code>EEntity</code> enumeration of the entity.
	 * @param id The integer ID number of the entity.
	 * @return The newly created <code>IEntity</code>.
	 * @throws DuplicatedIDException If the given ID number is already in use.
	 */
	public IEntity createEntity(EEntity enumn, int id, String name) throws DuplicatedIDException {
		if(this.entities.containsKey(Integer.valueOf(id))) throw new DuplicatedIDException(id);
		if(enumn == EEntity.Terrain) id = 0;
		IEntity entity = null;
		switch(enumn) {
		case Terrain: entity = new TerrainEntity(id); break;
		case SnowmanLocalRed: entity = new SnowmanEntity(enumn, id, name); break;
		case SnowmanDistributedRed: entity = new CharacterEntity(enumn, id, name); break;
		case SnowmanLocalBlue: entity = new SnowmanEntity(enumn, id, name); break;
		case SnowmanDistributedBlue: entity = new CharacterEntity(enumn, id, name); break;
		case Snowball: entity = new SnowballEntity(id); break;
                    case SnowballTrail: entity = new SnowballTrailEntity(id); break;
		default:
			switch(enumn.getType()) {
			case Static: entity = new StaticEntity(enumn, id); break;
			case Editable: entity = new EditableEntity(enumn, id); break;
			case Dynamic: entity = new DynamicEntity(enumn, id); break;
			}
			break;
		}
		this.entities.put(Integer.valueOf(id), entity);
		this.logger.fine("Created entity " + enumn.toString() + "with ID number: " + id);
		return entity;
	}
	
	/**
	 * Remove the entity with given ID number.
	 * @param id The ID number of the entity to be destroyed.
	 * @return True if the entity is removed. False if it does not exist.
	 */
	public boolean removeEntity(int id) {
		final IEntity entity = this.entities.remove(Integer.valueOf(id));
		if(entity == null) {
			this.logger.fine("Entity with ID number: " + id + " does not exist.");
			return false;
		}
		this.logger.fine("Destroyed entity with ID number: " + id);
		return true;
	}
	
	/**
	 * Retrieve the entity with given ID number.
	 * @param id The ID number of the entity.
	 * @return The <code>IEntity</code> with given ID number.
	 * @throws ObjectNotFoundException If the entity with given ID number does not exist.
	 */
	public IEntity getEntity(int id) throws ObjectNotFoundException {
		final IEntity entity = this.entities.get(Integer.valueOf(id));
		if(entity == null) throw new ObjectNotFoundException("Entity " + String.valueOf(id));
		return entity;
	}
	
	/**
	 * Clears the <code>EntityManager</code> by first clear the entity
	 * pool the reset the ID number count to 0. This method should be
	 * invoked whenever a new <code>World</code> is loaded.
	 */
	@Override
	public void cleanup() {
		this.entities.clear();
		this.idcount = 0;
	}
	
	public void setCount(final int count) {
		this.idcount = count;
	}
	public int getCount() {
		return idcount;
	}
}
