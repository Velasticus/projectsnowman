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
package com.sun.darkstar.example.snowman.game.task.state.battle;

import com.jme.math.Vector3f;
import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity;
import com.sun.darkstar.example.snowman.common.entity.view.View;
import com.sun.darkstar.example.snowman.common.interfaces.IDynamicEntity;
import com.sun.darkstar.example.snowman.common.interfaces.IEntity;
import com.sun.darkstar.example.snowman.common.interfaces.IView;
import com.sun.darkstar.example.snowman.common.protocol.enumn.EMOBType;
import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;
import com.sun.darkstar.example.snowman.exception.DuplicatedIDException;
import com.sun.darkstar.example.snowman.game.Game;
import com.sun.darkstar.example.snowman.game.entity.util.EntityManager;
import com.sun.darkstar.example.snowman.game.entity.view.DynamicView;
import com.sun.darkstar.example.snowman.game.entity.view.scene.CharacterView;
import com.sun.darkstar.example.snowman.game.entity.view.util.ViewManager;
import com.sun.darkstar.example.snowman.game.input.util.InputManager;
import com.sun.darkstar.example.snowman.game.state.enumn.EGameState;
import com.sun.darkstar.example.snowman.game.state.scene.BattleState;
import com.sun.darkstar.example.snowman.game.task.RealTimeTask;
import com.sun.darkstar.example.snowman.game.task.enumn.ETask;
import com.sun.darkstar.example.snowman.interfaces.IController;

/**
 * <code>AddMOBTask</code> extends <code>RealTimeTask</code> to create and
 * add a MOB to the <code>BattleState</code>.
 * <p>
 * <code>AddMOBTask</code> execution logic:
 * 1. Convert <code>EMOBType</code> to <code>EEntity</code> enumeration.
 * 2. Create corresponding <code>DynamicEntity</code>.
 * 3. Create corresponding <code>DynamicView</code>.
 * 4. Set the x and z coordinates of the view.
 * 5. If it is controlled locally, create corresponding <code>IController</code>.
 * 6. If it is controlled locally, setup chase camera with battle state.
 * 7. Attach the <code>DynamicView</code> to <code>World</code>.
 * <p>
 * <code>AddMOBTask</code> comparison is based on the given ID number and the
 * <code>EMOBType</code>. Two <code>AddMOBTask</code> are considered 'equal'
 * if and only if they have the same ID number and the <code>EMOBType</code>
 * enumeration.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-14-2008 16:30 EST
 * @version Modified date: 07-23-2008 17:41 EST
 */
public class AddMOBTask extends RealTimeTask {
	/**
	 * The ID number of the entity to be added.
	 */
	private final int id;
	/**
	 * The <code>EMOBType</code> enumeration.
	 */
	private final EMOBType enumn;
	/**
	 * The <code>ETeamColor</code> enumeration.
	 */
	private final ETeamColor color;
	/**
	 * The flag indicates if this mob is controlled locally.
	 */
	private final boolean local;
	/**
	 * The x coordinate of the initial position.
	 */
	private final float x;
	/**
	 * The z coordinate of the initial position.
	 */
	private final float z;
        
        private String mobName;
	
	/**
	 * Constructor of <code>AddMOBTask</code>.
	 * @param game The <code>Game</code> instance.
	 * @param id The ID number of the entity to be added.
	 * @param enumn The <code>EMOBType</code> enumeration.
	 * @param color The <code>ETeamColor</code> enumeration.
	 * @param x The x coordinate of the initial position.
	 * @param z The z coordinate of the initial position.
	 * @param local The flag indicates if this mob is controlled locally.
	 */
	public AddMOBTask(Game game, int id, EMOBType enumn, ETeamColor color, float x, float z, String mobName, boolean local) {
		super(ETask.AddMOB, game);
		this.id = id;
		this.enumn = enumn;
		this.color = color;
		this.x = x;
		this.z = z;
                this.mobName = mobName;
		this.local = local;
	}

	@Override
	public void execute() {
            switch (this.enumn) {
                case SNOWMAN:
                    if (this.local) {
                        switch (this.color) {
                            case Red:
                                addMob(EEntity.SnowmanLocalRed);
                                break;
                            case Blue:
                                addMob(EEntity.SnowmanLocalBlue);
                                break;
                        }
                    } else {
                        switch (this.color) {
                            case Red:
                                addMob(EEntity.SnowmanDistributedRed);
                                break;
                            case Blue:
                                addMob(EEntity.SnowmanDistributedBlue);
                                break;
                        }
                    }
                    break;
                case FLAG:
                    switch (this.color) {
                        case Red:
                            addMob(EEntity.FlagRed);
                            break;
                        case Blue:
                            addMob(EEntity.FlagBlue);
                            break;
                    }
                    break;
                case FLAGGOAL:
                    switch (this.color) {
                        case Red:
                            addMob(EEntity.FlagRedGoal);
                            break;
                        case Blue:
                            addMob(EEntity.FlagBlueGoal);
                            break;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid entity type: " + this.enumn.toString());
            }
		
	}
        
        private void addMob(EEntity enumn)
        {
            try {
                BattleState state = ((BattleState) this.game.getGameState(EGameState.BattleState));
                IEntity entity = EntityManager.getInstance().createEntity(enumn, this.id, mobName);
                IView view = ViewManager.getInstance().createView(entity);
                ((View) view).getLocalTranslation().x = this.x;
                ((View) view).getLocalTranslation().z = this.z;

                if (state.getWorld().getWorldBound() == null) {
                    state.getWorld().updateGeometricState(0, true);
                }

                if (state.getWorld().getWorldBound() != null) {
                    Vector3f center = state.getWorld().getWorldBound().getCenter();
                    if (center != null) {
                        // have view look towards center
                        Vector3f direction = center.subtract(this.x, 0, this.z);
                        direction.y = 0;
                        direction.normalizeLocal();
                        ((View) view).getLocalRotation().lookAt(direction, Vector3f.UNIT_Y);
                    }
                }

                if (this.enumn == EMOBType.SNOWMAN) {
                    IController controller = InputManager.getInstance().getController((IDynamicEntity) entity);
                    controller.setActive(true);
                    if (this.local) {
                        InputManager.getInstance().registerController(controller);
                        state.initializeCameraHandler((DynamicView) view);
                    }
                    state.getWorld().getDynamicRoot().attachChild(((CharacterView)view).getLabelNode());
                }
                if(this.enumn == EMOBType.FLAGGOAL) {
                    state.addFlagGoalId(color, id);
                }
                view.attachTo(state.getWorld().getDynamicRoot());
                state.getWorld().updateRenderState();
                state.incrementCount();
            } catch (DuplicatedIDException e) {
                e.printStackTrace();
            }
    }

    @Override
	public boolean equals(Object object) {
		if(!super.equals(object)) return false;
		AddMOBTask given = (AddMOBTask)object;
		return ((this.id == given.id) && (this.enumn == given.enumn));
	}
}
