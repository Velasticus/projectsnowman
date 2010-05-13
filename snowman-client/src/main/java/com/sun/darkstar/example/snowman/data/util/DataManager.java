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
package com.sun.darkstar.example.snowman.data.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import com.jme.image.Image;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.util.ImageLoader;
import com.jme.util.TextureManager;
import com.jme.util.export.Savable;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.resource.MultiFormatResourceLocator;
import com.jme.util.resource.ResourceLocatorTool;
import com.model.md5.JointAnimation;
import com.model.md5.ModelNode;
import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity;
import com.sun.darkstar.example.snowman.common.interfaces.IInfluence;
import com.sun.darkstar.example.snowman.common.interfaces.IStaticEntity;
import com.sun.darkstar.example.snowman.common.interfaces.IStaticView;
import com.sun.darkstar.example.snowman.common.interfaces.IWorld;
import com.sun.darkstar.example.snowman.common.util.enumn.EWorld;
import com.sun.darkstar.example.snowman.data.enumn.EAnimation;
import com.sun.darkstar.example.snowman.data.enumn.EDataType;
import com.sun.darkstar.example.snowman.data.enumn.ESystemData;
import com.sun.darkstar.example.snowman.game.entity.influence.util.InfluenceManager;
import com.sun.darkstar.example.snowman.game.entity.util.EntityManager;
import com.sun.darkstar.example.snowman.game.entity.view.util.ViewManager;
import com.sun.darkstar.example.snowman.unit.Manager;
import com.sun.darkstar.example.snowman.unit.enumn.EManager;

/**
 * <code>DataManager</code> is a <code>Manager</code> which is responsible
 * for managing all types of data resources used by the game.
 * <p>
 * <code>DataManager</code> internally manages several asset pools based on
 * the types of the assets. These asset pools allow caching and cloning of
 * assets that may be shared by multiple <code>Entity</code> in the game.
 * <p>
 * <code>DataManager</code> allows caching assets for a particular game state
 * during the initialization process of the game state. The cached assets are
 * stored in the various asset pools which can then be retrieved, cloned or
 * destroyed later on.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 06-09-2008 14:48 EST
 * @version Modified date: 07-17-2008 11:35 EST
 */
public class DataManager extends Manager {
	/**
	 * The <code>DataManager</code> instance.
	 */
	private static DataManager instance;
	/**
	 * The <code>JointAnimation</code> asset pool. 
	 */
	private final HashMap<EAnimation, JointAnimation> animationPool;
	/**
	 * The <code>Spatial</code> asset pool.
	 */
	private final HashMap<EEntity, Spatial> spatialPool;
	/**
	 * The character <code>ModelNode</code> asset pool.
	 */
	private final HashMap<EEntity, ModelNode> characterPool;
	/**
	 * The <code>ClassLoader</code> instance.
	 */
	private final ClassLoader loader;

	/**
	 * Constructor of <code>DataManager</code>.
	 */
	private DataManager() {
		super(EManager.DataManager);
		this.animationPool = new HashMap<EAnimation, JointAnimation>();
		this.spatialPool = new HashMap<EEntity, Spatial>();
		this.characterPool = new HashMap<EEntity, ModelNode>();
		this.loader = this.getClass().getClassLoader();
		this.setupLocator();
	}
	
	/**
	 * Setup the <code>ResourceLocator</code> used to locate resource files
	 * for <code>MD5Importer</code>.
	 */
	private void setupLocator() {
		URL base = this.loader.getResource(EDataType.Texture.getDirectory());
		try {
			MultiFormatResourceLocator locator = new MultiFormatResourceLocator(base, EDataType.Texture.getExtension());
			ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, locator);
			TextureManager.registerHandler(EDataType.Texture.getExtension(), new ImageLoader() {
				@Override
				public Image load(InputStream is) throws IOException {
					return (Image)BinaryImporter.getInstance().load(is);
				}
			});
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieve the <code>DataManager</code> instance.
	 * @return The <code>DataManager</code> instance.
	 */
	public static DataManager getInstance() {
		if(DataManager.instance == null) {
			DataManager.instance = new DataManager();
		}
		return DataManager.instance;
	}
	
	/**
	 * Retrieve a copy of the cached animation resource with given enumeration.
	 * @param enumn The <code>EAnimation</code> enumeration.
	 * @return The copy of the cached <code>JointAnimation</code> with given enumeration.
	 */
	public JointAnimation getAnimation(EAnimation enumn) {
		JointAnimation animation = this.animationPool.get(enumn);
		if(animation == null) {
			animation = (JointAnimation)this.getResource(EDataType.Animation.toPath(enumn.toString()));
			this.animationPool.put(enumn, animation);
		}
		return animation.clone();
	}

	/**
	 * Retrieve the cached static mesh resource with given enumeration.
	 * @param enumn The <code>EEntity</code> enumeration.
	 * @return The cached <code>SharedMesh</code> with given enumeration.
	 */
	public Spatial getStaticSpatial(EEntity enumn) {
		Spatial mesh = this.spatialPool.get(enumn);
		if(mesh == null) {
			mesh = (Spatial)this.getResource(EDataType.StaticMesh.toPath(enumn.toString()));
			this.spatialPool.put(enumn, mesh);
		}
		return mesh;
	}

	/**
	 * Retrieve a copy of the dynamic character mesh resource with given enumeration.
	 * @param enumn The <code>EEntity</code> enumeration.
	 * @return The copy of the cached character <code>ModelNode</code> with given enumeration.
	 */
	public ModelNode getDynamicMesh(EEntity enumn) {
		ModelNode character = this.characterPool.get(enumn);
		if(character == null) {
			character = (ModelNode)this.getResource(EDataType.DynamicMesh.toPath(enumn.toString()));
			character.setLightCombineMode(LightCombineMode.Off);
			character.updateRenderState();
			this.characterPool.put(enumn, character);
		}
		return character.clone();
	}

	/**
	 * Retrieve the URL points to the system data with given enumeration.
	 * @param enumn The <code>ESystemData</code> enumeration.
	 * @return The <code>URL</code> points to the system data with given enumeration.
	 */
	public URL getSystemData(ESystemData enumn) {
		return this.loader.getResource(EDataType.SystemData.toPath(enumn.toString()));
	}

	/**
	 * Retrieve a world resource and register the information it maintains with
	 * all the managers. The world resource is not cached or cloned.
	 * @param enumn The <code>EWorld</code> enumeration.
	 * @return The <code>IWorld</code> resource with given enumeration.
	 */
	public IWorld getWorld(EWorld enumn) {
		IWorld world = (IWorld)this.getResource(EDataType.World.toPath(enumn.toString()));
		EntityManager entityManager = EntityManager.getInstance();
		ViewManager viewManager = ViewManager.getInstance();
		InfluenceManager influenceManager = InfluenceManager.getInstance();
		for(IStaticView view : world.getViews()) {
			IStaticEntity entity = (IStaticEntity)view.getEntity();
			entityManager.registerEntity(entity);
			viewManager.registerView(view);
			for(IInfluence influence : entity.getInfluences()) {
				influenceManager.registerInfluence(influence);
			}
		}
		return world;
	}
	
	/**
	 * Retrieve a binary resource which is pointed by the given path.
	 * @param path The path to the resource in <code>String</code> form.
	 * @return The loaded <code>Savable</code> resource.
	 */
	private Savable getResource(String path) {
		URL url = this.loader.getResource(path);
		try {
			return BinaryImporter.getInstance().load(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Clear all the cached resources.
	 */
	@Override
	public void cleanup() {
		this.animationPool.clear();
		this.spatialPool.clear();
		this.characterPool.clear();
		TextureManager.clearCache();
	}
}
