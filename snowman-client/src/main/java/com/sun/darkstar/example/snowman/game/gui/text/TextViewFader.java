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

package com.sun.darkstar.example.snowman.game.gui.text;

import org.fenggui.util.Color;

/**
 * <code>TextViewFader</code> simulates the fading effect on texts
 * displayed by <code>TextView</code>.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 11-07-2008 18:10 EST
 * @version Modified date: 11-10-2008 14:39 EST
 */
public class TextViewFader extends Thread{
	/**
	 * The text view object.
	 */
	private TextView view;
	/**
	 * The boolean flag indicates if the fading is processing.
	 */
	private boolean fading;
	
	/**
	 * Constructor of TextViewFader.
	 * @param view The text view object.
	 */
	public TextViewFader(TextView view) {
		super();
		this.setDaemon(true);
		// Store the text view.
		this.view = view;
		// Start the fading thread.
		this.start();
	}
	
	/**
	 * Start fading out texts of the text view.
	 */
	public void startFading() {
		this.stopFading();
		this.fading = true;
		synchronized(this) {
			this.notifyAll();
		}
	}
	
	/**
	 * Stop fading out texts of the text view and reset them back to normal transparency.
	 */
	public void stopFading() {
		this.fading = false;
		this.setTextAlpha(1);
	}

	/**
	 * Called by start method.
	 */
	public void run() {
		while(true) {
			int size = this.view.getRuns().size();
			while(size <= 0) {
				try {
					synchronized(this) {
						this.wait(100);
						size = this.view.getRuns().size();
					}
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			int cycled = 0;
			// Wait for 10 seconds before fading out texts.
			while(cycled < 1000 && this.fading) {
				try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
				// If there is new text added to the view, start back from 0.
				if(size != this.view.getRuns().size()) {
					cycled = 0;
					size = this.view.getRuns().size();
					this.setTextAlpha(1);
				// Otherwise advance the cycled number.	
				} else {
					cycled++;
				}
			}
			// Fading out texts.
			float alpha = this.view.getRuns().peek().getColor().getAlpha();
			final float change = 20.0f/3000.0f;
			while(alpha > 0 && cycled == 1000 && this.fading) {
				try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
				// If there is new text added to the view, start back from 0.
				if(size != this.view.getRuns().size()) {
					cycled = 0;
					this.setTextAlpha(1);
				// Otherwise decrease the alpha of text color.
				} else {
					alpha = alpha - change;
					if(alpha < 0) alpha = 0;
					this.setTextAlpha(alpha);
				}
			}
			// If not fading, let the thread wait.
			if(!this.fading) {
				synchronized(this){
					try {this.wait();}
					catch (InterruptedException e) {e.printStackTrace();}
				}
			}
		}
	}
	
	/**
	 * Set the alpha of all the texts in the text view.
	 * @param alpha The new alpha value to be set.
	 */
	private void setTextAlpha(float alpha) {
		Color color = null;
		for(TextRun run : this.view.getRuns()) {
			color = run.getColor();
			run.setTextColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
		}
	}
}
