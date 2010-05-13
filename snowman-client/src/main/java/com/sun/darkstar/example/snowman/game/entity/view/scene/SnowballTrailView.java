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

import com.jme.scene.shape.Sphere;
import com.jme.system.DisplaySystem;
import com.jmex.effects.TrailMesh;
import com.jme.image.Texture;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.BlendState;
import com.jme.util.TextureManager;
import com.sun.darkstar.example.snowman.game.entity.scene.SnowballTrailEntity;
import com.sun.darkstar.example.snowman.game.entity.view.DynamicView;
import com.sun.darkstar.example.snowman.common.interfaces.IDynamicEntity;

/**
 * <code>SnowballTrailView</code> extends <code>DynamicView</code> to define
 * the graphical presentation of a snow ball trail.
 * 
 * @author Owen Kellett
 */
public class SnowballTrailView extends DynamicView {
    /**
     * Serial version.
     */
    private static final long serialVersionUID = 1L;
    
    private TrailMesh trail;
    private Sphere ball;
    
    private boolean started = false;

    /**
     * Constructor of <code>SnowballTrailView</code>.
     * @param entity The <code>SnowballTrailEntity</code>.
     */
    public SnowballTrailView(SnowballTrailEntity entity) {
        super(entity);
    }
    
    public void show(Sphere ball) {
        this.ball = ball;
        
        trail = new TrailMesh("TrailMesh", 5);
        trail.setUpdateSpeed(60.0f);
        trail.setFacingMode(TrailMesh.FacingMode.Billboard);
        trail.setUpdateMode(TrailMesh.UpdateMode.Step);
        
        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setEnabled(true);
        Texture t = TextureManager.loadTexture(
                getClass().getClassLoader().getResource(
                "com/sun/darkstar/example/snowman/data/texture/environment/snowballTrail.png"),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);
        ts.setTexture(t);
        trail.setRenderState(ts);
        
        BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        bs.setBlendEnabled(true);
        bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        bs.setDestinationFunction(BlendState.DestinationFunction.One);
        bs.setTestEnabled(true);
        trail.setRenderState(bs);
        
        this.attachChild(trail);
        this.updateRenderState();
    }

    @Override
    public void update(float interpolation) {
        if(!started) {
            trail.resetPosition(ball.getWorldTranslation());
            started = true;
        }
        
        trail.setTrailFront(ball.getWorldTranslation(),
                    ball.getRadius()*1.5f, interpolation);
        trail.update(DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation());
    }
    
    @Override
    public IDynamicEntity getEntity() {
        return (IDynamicEntity) this.entity;
    }
}
