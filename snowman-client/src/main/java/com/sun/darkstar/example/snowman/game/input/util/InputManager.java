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
package com.sun.darkstar.example.snowman.game.input.util;

import java.util.HashMap;
import java.util.LinkedList;

import com.jme.input.KeyInput;
import com.jme.input.KeyInputListener;
import com.jme.input.MouseInput;
import com.jme.input.MouseInputListener;
import com.sun.darkstar.example.snowman.common.interfaces.IDynamicEntity;
import com.sun.darkstar.example.snowman.game.entity.controller.CharacterController;
import com.sun.darkstar.example.snowman.game.entity.controller.SnowballController;
import com.sun.darkstar.example.snowman.game.entity.controller.SnowmanController;
import com.sun.darkstar.example.snowman.game.entity.scene.CharacterEntity;
import com.sun.darkstar.example.snowman.game.entity.scene.SnowballEntity;
import com.sun.darkstar.example.snowman.game.entity.scene.SnowmanEntity;
import com.sun.darkstar.example.snowman.game.input.enumn.EInputConverter;
import com.sun.darkstar.example.snowman.game.input.enumn.EInputType;
import com.sun.darkstar.example.snowman.game.input.gui.KeyInputConverter;
import com.sun.darkstar.example.snowman.game.input.gui.MouseInputConverter;
import com.sun.darkstar.example.snowman.interfaces.IController;
import com.sun.darkstar.example.snowman.interfaces.IInputConverter;
import com.sun.darkstar.example.snowman.unit.Manager;
import com.sun.darkstar.example.snowman.unit.enumn.EManager;

/**
 * <code>InputManager</code> is a <code>Manager</code> that is responsible
 * for managing all <code>IInputConverter</code> and <code>IController</code>
 * instances. It is responsible for the creation, retrieval and destruction
 * of all the input handling instances.
 * <p>
 * <code>InputManager</code> is invoked by <code>Game</code> every frame
 * to update all the maintained <code>IController</code> instances.
 * <p>
 * <code>InputManager</code> maintains all <code>IInputConverter</code> as
 * singleton instances. There can only exist a single instance of one type
 * of <code>IInputConverter</code> at any given time. These converters are
 * maintained by their <code>EInputHandler</code> enumerations.
 * <p>
 * <code>InputManager</code> allows multiple instances of the same type of
 * <code>IController</code>. These controllers are maintained by their
 * corresponding <code>IEntity</code>.
 * <p>
 * <code>InputManager</code> provides convenient method for activating or
 * deactivating all input handlers via a single invocation.
 * 
 * @author Yi Wang (Neakor)
 * @author Tim Poliquin (Weenahmen)
 * @version Creation date: 07-15-2008 23:18 EST
 * @version Modified date: 07-30-2008 11:23 EST
 */
public final class InputManager extends Manager {
	/**
	 * The <code>InputManager</code> instance.
	 */
	private static InputManager instance;
	/**
	 * The <code>HashMap</code> of <code>EInputHandler</code> enumeration
	 * and <code>IInputConverter</code> pair singletons.
	 */
	private final HashMap<EInputConverter, IInputConverter> converters;
	/**
	 * The <code>HashMap</code> of <code>IDynamicEntity</code> key and
	 * <code>IController</code> pairs.
	 */
	private final HashMap<IDynamicEntity, IController> controllers;
	/**
	 * The temporary <code>LinkedList</code> of entities whose controllers are
	 * to be removed.
	 */
	private final LinkedList<IDynamicEntity> removed;

	/**
	 * Constructor of <code>InputManager</code>.
	 */
	private InputManager() {
		super(EManager.InputManager);
		this.converters = new HashMap<EInputConverter, IInputConverter>();
		this.controllers = new HashMap<IDynamicEntity, IController>();
		this.removed = new LinkedList<IDynamicEntity>();
	}
	
	/**
	 * Retrieve the <code>InputManager</code> instance.
	 * @return The <code>InputManager</code> instance.
	 */
	public static InputManager getInstance() {
		if(InputManager.instance == null) {
			InputManager.instance = new InputManager();
		}
		return InputManager.instance;
	}
	
	/**
	 * Update the entity controllers.
	 * @param interpolation The frame rate interpolation value.
	 */
	public void update(float interpolation) {
		while(!this.removed.isEmpty()) {
			IDynamicEntity entity = this.removed.poll();
			Object result = this.controllers.remove(entity);
			if(result == null) this.logger.fine("Controller of: " + entity.toString() + " does not exist.");
			else this.logger.fine("Removed controller of entity: " + entity.toString());
		}
		// Update all controllers.
		for(IController controller : this.controllers.values()) {
			if(controller.isActive()) controller.update(interpolation);
		}
	}
	
	/**
	 * Register the given input converter to the appropriate input device.
	 * @param converter The <code>IInputConverter</code> to be registered.
	 * @return True if the converter is registered. False if it is already registered.
	 */
	public boolean registerConverter(IInputConverter converter) {
		return this.registerListener(converter, converter.getEnumn().getType());
	}
	
	/**
	 * Register the given entity controller to the appropriate input device.
	 * @param controller The <code>IController</code> to be registered.
	 * @return True if the controller is registered. False if it is already registered.
	 */
	public boolean registerController(IController controller) {
		return this.registerListener(controller, controller.getInputType());
	}
	
	/**
	 * Register the given listener to the input with given type.
	 * @param listener The input listener to be registered.
	 * @param enumn The <code>EInputType</code> enumeration.
	 * @return True if the given listener is registered. False if it is already registered.
	 */
	private boolean registerListener(Object listener, EInputType enumn) {
		if(!(listener instanceof KeyInputListener) && !(listener instanceof MouseInputListener))
			throw new IllegalArgumentException("Invalid handler which does not implement any valid listener interface.");
		switch(enumn) {
		case Keyboard:
			if(KeyInput.get().containsListener((KeyInputListener)listener)) return false;
			else KeyInput.get().addListener((KeyInputListener)listener); return true;
		case Mouse:
			if(MouseInput.get().containsListener((MouseInputListener)listener)) return false;
			else MouseInput.get().addListener((MouseInputListener)listener); return true;
		default: throw new IllegalArgumentException("Invalid input type: " + enumn.toString());
		}
	}
	
	/**
	 * Activate or deactivate all input handlers including all GUI converters
	 * and all entity controllers.
	 * @param active True if input should be activated. False otherwise.
	 */
	public void setInputActive(boolean active) {
		for(IInputConverter converter : this.converters.values()) {
			converter.setActive(active);
		}
		for(IController controller : this.controllers.values()) {
			controller.setActive(active);
		}
	}
	
	/**
	 * Remove the given entity controller.
	 * @param entity The <code>IDynamicEntity</code> controlled by the controller.
	 */
	public void removeController(IDynamicEntity entity) {
		if(entity == null) return;
		this.removed.add(entity);
	}
	
	/**
	 * Retrieve the input converter with given enumeration.
	 * @param enumn The <code>EConverter</code> enumeration.
	 * @return The <code>IInputConverter</code> with given enumeration.
	 */
	public IInputConverter getConverter(EInputConverter enumn) {
		IInputConverter converter = this.converters.get(enumn);
		if(converter == null) converter = this.createConverter(enumn);
		return converter;
	}
	
	/**
	 * Retrieve the entity controller with given enumeration.
	 * @param entity The <code>IDynamicEntity</code> that is being controlled.
	 * @return The <code>IController</code> that controls the given entity.
	 */
	public IController getController(IDynamicEntity entity) {
		IController controller = this.controllers.get(entity);
		if(controller == null) controller = this.createController(entity);
		return controller;
	}
	
	/**
	 * Create an input converter based on given enumeration.
	 * @param enumn The <code>EConverter</code> enumeration.
	 * @return The <code>IInputConverter</code> with given enumeration.
	 */
	private IInputConverter createConverter(EInputConverter enumn) {
		IInputConverter converter = null;
		switch(enumn) {
		case KeyboardConverter: converter = new KeyInputConverter(); break;
		case MouseConverter: converter = new MouseInputConverter(); break;
		default: throw new IllegalArgumentException("Invalid converter enumeration.");
		}
		this.converters.put(enumn, converter);
		return converter;
	}
	
	/**
	 * Create an entity controller based on given enumeration.
	 * @param entity The <code>IDynamicEntity</code> controller by the created controller.
	 * @return The <code>IController</code> with given enumeration.
	 */
	private IController createController(IDynamicEntity entity) {
		IController controller = null;
		switch(entity.getEnumn()) {
		case SnowmanDistributedRed: controller = new CharacterController((CharacterEntity)entity, EInputType.None); break;
		case SnowmanLocalRed: controller = new SnowmanController((SnowmanEntity)entity); break;
		case SnowmanDistributedBlue: controller = new CharacterController((CharacterEntity)entity, EInputType.None); break;
		case SnowmanLocalBlue: controller = new SnowmanController((SnowmanEntity)entity); break;
		case Snowball: controller = new SnowballController((SnowballEntity)entity); break;
		default: throw new IllegalArgumentException("Invalid controller enumeration.");
		}
		this.controllers.put(entity, controller);
		return controller;
	}

	@Override
	public void cleanup() {
		this.converters.clear();
		this.controllers.clear();
	}
}
