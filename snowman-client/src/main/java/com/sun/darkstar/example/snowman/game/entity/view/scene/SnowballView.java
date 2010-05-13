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
import com.jme.bounding.BoundingSphere;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.MaterialState.ColorMaterial;
import com.jme.scene.state.MaterialState.MaterialFace;
import com.jme.system.DisplaySystem;
import com.jme.renderer.ColorRGBA;
import com.sun.darkstar.example.snowman.game.entity.scene.SnowballEntity;
import com.sun.darkstar.example.snowman.game.entity.view.DynamicView;
import com.sun.darkstar.example.snowman.common.interfaces.IDynamicEntity;

/**
 * <code>SnowballView</code> extends <code>DynamicView</code> to define
 * the graphical presentation of a snow ball.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-25-2008 16:48 EST
 * @version Modified date: 07-25-2008 16:50 EST
 */
public class SnowballView extends DynamicView {
    /**
     * Serial version.
     */
    private static final long serialVersionUID = -370362057381803248L;
    
    private Sphere ball;

    /**
     * Constructor of <code>SnowballView</code>.
     * @param entity The <code>SnowballEntity</code>.
     */
    public SnowballView(SnowballEntity entity) {
        super(entity);
    }
    
    public void show() {
        ball = new Sphere("Snowball", 32, 32, 0.05f);
        ball.setSolidColor(ColorRGBA.white.clone());
        ball.setModelBound(new BoundingSphere());
        ball.updateModelBound();
        MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
        ms.setEmissive(ColorRGBA.lightGray.clone());
        ms.setMaterialFace(MaterialFace.FrontAndBack);
        ball.setRenderState(ms);
        this.attachChild(ball);
        this.updateRenderState();
    }
    
    public Sphere getBall() {
        return ball;
    }

    @Override
    public void update(float interpolation) {
    }
    
    @Override
    public IDynamicEntity getEntity() {
        return (IDynamicEntity) this.entity;
    }
}
