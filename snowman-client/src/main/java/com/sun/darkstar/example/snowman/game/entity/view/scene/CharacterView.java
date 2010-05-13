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
package com.sun.darkstar.example.snowman.game.entity.view.scene;

import com.jme.bounding.CollisionTreeManager;
import com.jme.scene.Spatial;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.model.md5.JointAnimation;
import com.model.md5.ModelNode;
import com.model.md5.controller.JointController;
import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity;
import com.sun.darkstar.example.snowman.common.util.SingletonRegistry;
import com.sun.darkstar.example.snowman.data.enumn.EAnimation;
import com.sun.darkstar.example.snowman.data.util.DataManager;
import com.sun.darkstar.example.snowman.game.entity.scene.CharacterEntity;
import com.sun.darkstar.example.snowman.game.entity.view.DynamicView;

/**
 * <code>CharacterView</code> extends <code>DynamicView</code> to represent
 * a dynamic animated view of <code>CharacterEntity</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-21-2008 14:54 EST
 * @version Modified date: 07-24-2008 11:45 EST
 */
public class CharacterView extends DynamicView {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 1082825580187469809L;
        /**
         * The actual snowman node
         */
        private Node snowmanNode;
	/**
	 * The <code>ModelNode</code> of this snowman view.
	 */
	private ModelNode model;
	/**
	 * The <code>JointController</code> for controlling the character.
	 */
	private JointController jointController;
	/**
	 * The idle animation.
	 */
	private JointAnimation animIdle;
	/**
	 * The movement animation.
	 */
	private JointAnimation animMove;
	/**
	 * The attack animation.
	 */
	private JointAnimation animAttack;
	/**
	 * The hit animation.
	 */
	private JointAnimation animHit;
	/**
	 * The death animation.
	 */
	private JointAnimation animDeath;
        /**
         * Text label of the name of the character
         */
        private Text label;
        /**
         * the label node
         */
        private Node labelNode;

	/**
	 * Constructor of <code>CharacterView</code>.
	 * @param snowman The <code>CharacterEntity</code> instance.
	 */
	public CharacterView(CharacterEntity snowman) {
            super(snowman);
            
            labelNode = new Node();
            labelNode.setLightCombineMode(Spatial.LightCombineMode.Off);
            labelNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
            
            label = Text.createDefaultTextLabel("label", snowman.getName());
            if(snowman.getEnumn() == EEntity.SnowmanDistributedBlue ||
                    snowman.getEnumn() == EEntity.SnowmanLocalBlue) {
                label.setTextColor(ColorRGBA.blue);
            }
            if(snowman.getEnumn() == EEntity.SnowmanDistributedRed ||
                    snowman.getEnumn() == EEntity.SnowmanLocalRed) {
                label.setTextColor(ColorRGBA.red);
            }
            label.setRenderQueueMode(Renderer.QUEUE_ORTHO);
            label.updateRenderState();
            
            labelNode.attachChild(label);
            labelNode.updateRenderState();
            
            snowmanNode = new Node();
            this.attachChild(snowmanNode);
	}

	@Override
	public void attachSpatial(Spatial mesh) {
		if(!(mesh instanceof ModelNode)) throw new IllegalArgumentException("Mesh is not a dynamic ModelNode.");
		snowmanNode.attachChild(mesh);
		this.model = (ModelNode)mesh;
		this.jointController = new JointController(this.model.getJoints());
		this.jointController.setActive(true);
		this.model.addController(this.jointController);
		this.animIdle = DataManager.getInstance().getAnimation(EAnimation.Idle);
		this.animMove = DataManager.getInstance().getAnimation(EAnimation.Move);
		this.animAttack = DataManager.getInstance().getAnimation(EAnimation.Attack);
		this.animHit = DataManager.getInstance().getAnimation(EAnimation.Hit);
		this.animDeath = DataManager.getInstance().getAnimation(EAnimation.Death);
		this.jointController.addAnimation(this.animIdle);
		this.jointController.addAnimation(this.animMove);
		this.jointController.addAnimation(this.animAttack);
		this.jointController.addAnimation(this.animHit);
		this.jointController.addAnimation(this.animDeath);
		this.jointController.setFading(this.animIdle, 0, false);
		this.jointController.setRepeatType(com.jme.scene.Controller.RT_WRAP);
	}

	@Override
	public void update(float interpolation) {
		this.getSnowmanNode().setLocalScale(SingletonRegistry.getHPConverter().convertScale(this.getEntity().getHP()));
		this.getEntity().setMass(SingletonRegistry.getHPConverter().convertMass(this.getEntity().getHP()));
		this.getSnowmanNode().updateWorldBound();
		CollisionTreeManager.getInstance().updateCollisionTree(this.getSnowmanNode());
		switch(this.getEntity().getState()) {
		case Moving:
			if(this.jointController.getActiveAnimation() == this.animMove) return;
			this.jointController.setFading(this.animMove, 0, false);
			this.jointController.setRepeatType(com.jme.scene.Controller.RT_WRAP);
			break;
		case Idle:
			if(this.jointController.getActiveAnimation() == this.animIdle) return;
			this.jointController.setFading(this.animIdle, 0, false);
			this.jointController.setRepeatType(com.jme.scene.Controller.RT_WRAP);
			break;
		case Attacking:
			if(this.jointController.getActiveAnimation() == this.animAttack) return;
			this.jointController.setFading(this.animAttack, 0, false);
			this.jointController.setRepeatType(com.jme.scene.Controller.RT_CLAMP);
			break;
		case Hit:
			if(this.jointController.getActiveAnimation() == this.animHit) return;
			this.jointController.setFading(this.animHit, 0, false);
			this.jointController.setRepeatType(com.jme.scene.Controller.RT_CLAMP);
			break;
		case Death:
			if(this.jointController.getActiveAnimation() == this.animDeath) return;
			this.jointController.setFading(this.animDeath, 0, false);
			this.jointController.setRepeatType(com.jme.scene.Controller.RT_CLAMP);
			break;
		}
	}
        
        public Text getLabel() {
            return this.label;
        }
        
        public Node getLabelNode() {
            return this.labelNode;
        }
	
	@Override
	public CharacterEntity getEntity() {
		return (CharacterEntity)this.entity;
	}

	/**
	 * Retrieve the dynamic mesh.
	 * @return The <code>ModelNode</code> instance.
	 */
	public ModelNode getMesh() {
		return this.model;
	}
        
        public Node getSnowmanNode() {
            return this.snowmanNode;
        }

	/**
	 * Check if the current active animation is half complete.
	 * @return True if the current active animation is half complete. False otherwise.
	 */
	public boolean isCurrentHalf() {
		float time = this.jointController.getActiveAnimation().getAnimationTime();
		float value = (this.jointController.getActiveAnimation().getNextTime()/time);
		return ((value >= 0.3f) && (value <= 0.7f));
	}

	/**
	 * Check if the current active animation is complete.
	 * @return True if the current active animation is complete. False otherwise.
	 */
	public boolean isCurrentComplete() {
		return this.jointController.getActiveAnimation().isCyleComplete();
	}
}
