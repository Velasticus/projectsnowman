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
package com.sun.darkstar.example.snowman.game;

import java.net.URL;

import com.jme.app.BaseGame;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.KeyInputListener;
import com.jme.input.MouseInput;
import com.jme.input.MouseInputListener;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.NanoTimer;
import com.jme.util.Timer;
import com.jmex.game.state.GameStateManager;
import com.sun.darkstar.example.snowman.client.Client;
import com.sun.darkstar.example.snowman.exception.MissingComponentException;
import com.sun.darkstar.example.snowman.game.entity.view.util.ViewManager;
import com.sun.darkstar.example.snowman.game.input.enumn.EInputConverter;
import com.sun.darkstar.example.snowman.game.input.util.InputManager;
import com.sun.darkstar.example.snowman.game.physics.util.PhysicsManager;
import com.sun.darkstar.example.snowman.game.state.GameState;
import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;
import com.sun.darkstar.example.snowman.game.state.scene.BattleState;
import com.sun.darkstar.example.snowman.game.state.scene.LoginState;
import com.sun.darkstar.example.snowman.game.state.scene.EndState;
import com.sun.darkstar.example.snowman.game.stats.StatsManager;
import com.sun.darkstar.example.snowman.game.task.util.TaskManager;
import com.sun.darkstar.example.snowman.interfaces.IComponent;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;

/**
 * <code>Game</code> represents the client end application which maintains
 * the basic game structure including all the <code>GameState</code>, and
 * other utility instances.
 * <p>
 * <code>Game</code> is responsible for updating all the singleton manager
 * systems including <code>GameStateManager</code>, <code>BasicPassManager</code>,
 * <code>PhysicsManager</code> and <code>TaskManager</code>.
 * <p>
 * <code>Game</code> needs to be connected with <code>Client</code> before
 * initialization in order to send out packets to the <code>Server</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 05-23-2008 14:02 EST
 * @version Modified date: 07-16-2008 11:30 EST
 */
public class Game extends BaseGame implements IComponent{
	/**
	 * The flag indicates the activeness of this <code>Component</code>.
	 */
	private boolean active;
	/**
	 * The <code>Client</code> instance.
	 */
	private Client client;
	/**
	 * The <code>Timer</code> instance.
	 */
	private Timer timer;
	/**
	 * The <code>ViewManager</code> instance.
	 */
	private ViewManager viewManager;
	/**
	 * The <code>TaskManager</code> instance.
	 */
	private TaskManager taskManager;
	/**
	 * The <code>PhysicsManager</code> instance.
	 */
	private PhysicsManager physicsManager;
	/**
	 * The <code>GameStateManager</code> instance.
	 */
	private GameStateManager stateManager;
	/**
	 * The <code>BasicPassManager</code> instance.
	 */
	private BasicPassManager passManager;
	/**
	 * The <code>InputManager</code> instance.
	 */
	private InputManager inputManager;
	/**
	 * The update interpolation value.
	 */
	private float interpolation;
	/**
	 * The screen shot count value.
	 */
	private int count;
	
	/**
	 * Constructor of <code>Game</code>.
	 */
	public Game() {
		super();
	}

	@Override
	public void activate() throws MissingComponentException {
		if(this.validate()) {
			this.active = true;
			this.initialize();
			this.start();
		}
	}

	@Override
	public boolean validate() throws MissingComponentException {
		if(this.client == null) {
			throw new MissingComponentException(Client.class.toString());
		}
		return true;
	}

	@Override
	public void initialize() {
	}

	@Override
	public void connect(IComponent component) {
		if(component instanceof Client) {
			this.client = (Client)component;
		}
	}

	@Override	
	protected void initSystem() {
		this.display = DisplaySystem.getDisplaySystem(this.settings.getRenderer());
		this.display.setTitle("Snowman");
		this.timer = new NanoTimer();
//		try {
//			Texture icon = AssetLoader.getInstance().loadTexture(TextureData.Icon_Big);
//			Texture iconSmall = AssetLoader.getInstance().loadTexture(TextureData.Icon_Small);
//			this.display.setIcon(new Image[]{icon.getImage(), iconSmall.getImage()});
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		this.initializeWindow();
		this.initializeCamera();
		this.initializeManagers();
		this.initializeConverters();
		this.initializeHotkey();
	}
	
	/**
	 * Initialize the window. If this is called to apply new game settings, the window
	 * is recreated.
	 */
	private void initializeWindow() {
		this.display.setMinSamples(0);
		this.display.setVSyncEnabled(false);
		this.display.createWindow(this.settings.getWidth(), this.settings.getHeight(), this.settings.getDepth(),
				this.settings.getFrequency(), this.settings.isFullscreen());
		this.display.getRenderer().setBackgroundColor(ColorRGBA.black);
	}

	/**
	 * Initialize the camera.
	 */
	private void initializeCamera() {
		// Create the camera.
		Camera camera = this.display.getRenderer().createCamera(this.display.getWidth(), this.display.getHeight());
		camera.setFrustumPerspective(45.0f, this.display.getWidth()/this.display.getHeight(), .1f, 500);
		Vector3f location = new Vector3f(0.0f, 0.0f, 0.0f);
		Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		Vector3f direction = new Vector3f(0.0f, 0.0f, -1.0f);
		camera.setFrame(location, left, up, direction);
		camera.setParallelProjection(false);
		camera.update();
		// Assign Camera.
		this.display.getRenderer().setCamera(camera);
	}

	/**
	 * Initialize all the system managers.
	 */
	private void initializeManagers() {
		this.viewManager = ViewManager.getInstance();
		this.taskManager = TaskManager.create(this);
		this.physicsManager = PhysicsManager.getInstance();
		this.stateManager = GameStateManager.create();
		this.passManager = new BasicPassManager();
		this.inputManager = InputManager.getInstance();
	}

	/**
	 * Initialize the GUI input converters.
	 */
	private void initializeConverters() {
		KeyInput.get().addListener((KeyInputListener)this.inputManager.getConverter(EInputConverter.KeyboardConverter));
		MouseInput.get().addListener((MouseInputListener)this.inputManager.getConverter(EInputConverter.MouseConverter));
	}

	/**
	 * Initialize the hot keys.
	 */
	private void initializeHotkey() {
		KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
		KeyBindingManager.getKeyBindingManager().set("screenshot", KeyInput.KEY_F1);
		KeyBindingManager.getKeyBindingManager().set("flip_stats-", KeyInput.KEY_F5);
		KeyBindingManager.getKeyBindingManager().set("flip_stats+", KeyInput.KEY_F6);
	}
	
	@Override
	protected void initGame() {
            LoginState login = new LoginState(this);
            this.stateManager.attachChild(login);
            login.setActive(true);
            login.initialize();

            BattleState battle = new BattleState(this);
            battle.setActive(false);
            this.stateManager.attachChild(battle);
            
            EndState end = new EndState(this);
            end.setActive(false);
            this.stateManager.attachChild(end);

            // reinit stats as needed
            StatsManager.getInstance().recreateStatsDisplay();
	}
	
	@Override
	protected void update(float unused) {
		// Update the timer to get the frame rate.
		this.timer.update();
		this.interpolation = this.timer.getTimePerFrame();

        /** update stats, if enabled. */
        StatsManager.getInstance().updateStats(interpolation);

		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).execute();

		//Update view manager.
        this.viewManager.update(this.interpolation);
        // Update input manager.
		this.inputManager.update(this.interpolation);
		// Execute tasks.
		this.taskManager.update();
		// Update physics.
		this.physicsManager.update(this.interpolation);
		// Update the game states.
		this.stateManager.update(this.interpolation);
		// Update the pass manager to update special effect passes.
		this.passManager.updatePasses(this.interpolation);
		// Update basic hot keys.
		if(KeyBindingManager.getKeyBindingManager().isValidCommand("exit", false)) {
			this.finish();
		} else if (KeyBindingManager.getKeyBindingManager().isValidCommand("flip_stats-", false)) {
	        StatsManager.getInstance().flipBack();
		} else if (KeyBindingManager.getKeyBindingManager().isValidCommand("flip_stats+", false)) {
	        StatsManager.getInstance().flipForward();
		} else if(KeyBindingManager.getKeyBindingManager().isValidCommand("screenshot", false)) {
			this.display.getRenderer().takeScreenShot("Snowman" + this.count);
			this.count++;
//			this.client.getHandler().getProcessor().newGame(-1, null);
		}
	}

	@Override
	protected void render(float unused) {
		this.display.getRenderer().clearBuffers();
		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER).execute();

		// Render all render passes.
		this.passManager.renderPasses(this.display.getRenderer());
        
        // Init stats as needed
        StatsManager.getInstance().renderStats();
	}

	@Override
	protected void reinit() {}

	@Override
	protected void cleanup() {
		if(this.stateManager != null) this.stateManager.cleanup();
		if(this.passManager != null) this.passManager.cleanUp();
	}

	@Override
	public void deactivate() {
		this.active = false;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}
	
	public float getInterpolation() {
		return this.interpolation;
	}
	
	/**
	 * Retrieve the <code>Client</code> instance.
	 * @return The <code>Client</code> instance.
	 */
	public Client getClient() {
		return this.client;
	}
	
	/**
	 * Retrieve the render pass manager.
	 * @return The <code>BasicPassManager</code> instance.
	 */
	public BasicPassManager getPassManager() {
		return this.passManager;
	}
	
	/**
	 * Retrieve the game state with given enumeration.
	 * @param enumn The <code>EGameState</code> enumeration.
	 * @return the GameState of the given type
	 */
	public GameState getGameState(EGameState enumn) {
		return (GameState)this.stateManager.getChild(enumn.toString());
	}
        
        public void deactivateAllGameStates() {
            for(EGameState s : EGameState.values()) {
                ((GameState)this.stateManager.getChild(s.toString())).setActive(false);
            }
        }
}
