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
package com.sun.darkstar.example.tool;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.concurrent.Callable;

import javax.swing.event.MouseInputAdapter;

import com.jme.intersection.TrianglePickResults;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.sun.darkstar.example.snowman.common.util.SingletonRegistry;
import com.sun.darkstar.example.tool.WorldEditor.ModeEnum;

public class WorldEditorMouseListener extends MouseInputAdapter {

	private final Ray ray;
	private final Vector3f intersection;
	private final WorldEditor editor;
	private ModeEnum mode = ModeEnum.Select;
	private TrianglePickResults picks = new TrianglePickResults();

	public WorldEditorMouseListener(WorldEditor editor) {
		this.ray = new Ray();
		this.intersection = new Vector3f();
		this.editor = editor;
		picks.setCheckDistance(true);
	}

	@SuppressWarnings("unchecked")
	public void mousePressed(MouseEvent e) {
		if(this.editor.isPressed()) return;
		else if(e.getButton() == 3) {
			GameTaskQueueManager.getManager().update(new Callable(){

				@Override
				public Object call() throws Exception {
					editor.setPressed(true);
					return null;
				}
			
			});
		}
	}

	@SuppressWarnings("unchecked")
	public void mouseReleased(MouseEvent e) {
		if(!this.editor.isPressed()) return;
		else if(e.getButton() == 3) {
			GameTaskQueueManager.getManager().update(new Callable(){

				@Override
				public Object call() throws Exception {
					editor.setPressed(false);
					return null;
				}
			
			});
		}
		if (this.mode == ModeEnum.Select) {
			// do selection. Set tree node selected.
			final MouseEvent event = e; 
			GameTaskQueueManager.getManager().update(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					editor.setSelectedSpatial(null);
					DisplaySystem.getDisplaySystem()
							.getPickRay(
									new Vector2f(event.getX(), event.getY()),
									true, ray);
					picks.clear();
					editor.getWorld().findPick(ray, picks);
					if (picks.getNumber() != 0) {
						Spatial spat = picks.getPickData(0).getTargetMesh();

						// Walk up until we grab the one right under the static
						// root.
						while (spat != null
								&& !editor.getWorld().getStaticRoot().equals(
										spat.getParent())) {
							spat = spat.getParent();
						}
						if (spat != null) {
							editor.setSelected(spat);
						}
					}
					return null;
				}
			});
		}
	}

	@SuppressWarnings("unchecked")
	public void mouseWheelMoved(MouseWheelEvent e){
		if(this.mode == ModeEnum.Raise || this.mode == ModeEnum.Lower || this.mode == ModeEnum.Smooth
				 || this.mode == ModeEnum.Paint || this.mode == ModeEnum.Erase) {
			final MouseWheelEvent event = e; 
			GameTaskQueueManager.getManager().update(new Callable(){

				@Override
				public Object call() throws Exception {
					float percentage = event.getWheelRotation()/100.0f;
					float radius = editor.getBrush().getRadius()*(1-percentage);
					if(radius > 5) radius = 5;
					else if(radius < 1) radius = 1;
					editor.getBrush().setRadius(radius);
					return null;
				}
			});
		}
	}

	public void mouseMoved(MouseEvent e){
		if(this.mode == ModeEnum.Raise || this.mode == ModeEnum.Lower || this.mode == ModeEnum.Smooth
				 || this.mode == ModeEnum.Paint || this.mode == ModeEnum.Erase) {
			final MouseEvent event = e; 
			GameTaskQueueManager.getManager().update(new Callable<Void>(){

				@Override
				public Void call() throws Exception {
			        DisplaySystem.getDisplaySystem().getPickRay(new Vector2f(event.getX(),event.getY()), true, ray);
					SingletonRegistry.getCollisionManager().getIntersection(ray, editor.getTerrain(), intersection, true);
					editor.getBrush().setLocalTranslation(intersection);
					return null;
				}

			});
		}
	}
	
	public void mouseDragged(MouseEvent e){
		final MouseEvent event = e; 
		if(this.mode == ModeEnum.Raise || this.mode == ModeEnum.Lower || this.mode == ModeEnum.Smooth
				 || this.mode == ModeEnum.Paint || this.mode == ModeEnum.Erase) {
			GameTaskQueueManager.getManager().update(new Callable<Void>(){

				@Override
				public Void call() throws Exception {
			        DisplaySystem.getDisplaySystem().getPickRay(new Vector2f(event.getX(),event.getY()), true, ray);
					SingletonRegistry.getCollisionManager().getIntersection(ray, editor.getTerrain(), intersection, true);
					editor.getBrush().setLocalTranslation(intersection);
					return null;
				}

			});
		}
	}

	public void setMode(ModeEnum enumn) {
		this.mode = enumn;
	}
	
	public Vector3f getIntersection() {
		return this.intersection;
	}
}
