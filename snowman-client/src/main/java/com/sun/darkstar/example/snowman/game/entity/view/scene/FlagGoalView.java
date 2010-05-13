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

import com.jme.bounding.BoundingBox;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.shape.Disk;
import com.jme.renderer.ColorRGBA;
import com.jme.math.Vector3f;
import com.sun.darkstar.example.snowman.common.interfaces.IDynamicEntity;
import com.sun.darkstar.example.snowman.game.entity.view.DynamicView;
import com.sun.darkstar.example.snowman.common.entity.enumn.EEntity;

/**
 * <code>FlagGoalView</code> extends <code>DynamicView</code> to define the
 * view of a flag goal entity.
 * 
 * @author Owen Kellett
 */
public class FlagGoalView extends DynamicView {
	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of <code>FlagGoalView</code>.
	 * @param entity The flag goal dynamic entity.
	 */
	public FlagGoalView(IDynamicEntity entity, float radius, EEntity type) {
            super(entity);

            Cylinder goal = new Cylinder(this.entity.getEnumn().toString(), 30, 30, radius, 0.1f);
            Disk top = new Disk(this.entity.getEnumn().toString() + "Top", 30, 30, radius);
            goal.setLocalTranslation(0, 0.05f, 0);
            top.setLocalTranslation(0, 0.1f, 0);
            switch (type) {
                case FlagBlueGoal:
                    goal.setSolidColor(ColorRGBA.blue);
                    top.setSolidColor(ColorRGBA.blue);
                    break;
                case FlagRedGoal:
                    goal.setSolidColor(ColorRGBA.red);
                    top.setSolidColor(ColorRGBA.red);
                    break;
                default:
                    goal.setSolidColor(ColorRGBA.lightGray);
                    top.setSolidColor(ColorRGBA.lightGray);
            }
            goal.rotateUpTo(Vector3f.UNIT_Z);
            top.rotateUpTo(Vector3f.UNIT_Z);
            goal.setModelBound(new BoundingBox());
            goal.updateModelBound();
            this.attachChild(goal);
            this.attachChild(top);
    }

    @Override
    public void update(float interpolation) {

    }

    @Override
    public IDynamicEntity getEntity() {
        return (IDynamicEntity) this.entity;
    }
}

