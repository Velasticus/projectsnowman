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

package com.sun.darkstar.example.tool.test;

import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.sun.darkstar.example.tool.JKMenuBar;
import com.sun.darkstar.example.tool.WorldEditorMenuListener;

/**
 * 
 * @author Jeffrey Kesselman
 */
public class MenuTest extends JFrame {

	private static final long serialVersionUID = 1L;

	public MenuTest() {
		JKMenuBar menuBar = null;
		menuBar = new JKMenuBar(new InputStreamReader(getClass()
				.getResourceAsStream("res/menubar.sdl")));
		setJMenuBar(menuBar);
		menuBar.addListener(new WorldEditorMenuListener() {

			@Override
			public void doNew() {
				System.out.print("doNew");
			}

			@Override
			public void doLoad() {
				System.out.print("doLoad");
			}

			@Override
			public void doSave() {
				System.out.print("doSave");
			}

			@Override
			public void doExportWorld() {
				System.out.print("doExportScene");
			}

			@Override
			public void doExit() {
				System.out.print("doExit");
			}

			@Override
			public void doCreateWorld() {
				System.out.print("doCreateWorld");
			}

			@Override
			public void doCreateTerrain() {
				System.out.print("doCreateTerrain");
			}

			@Override
			public void doCreateTextureLayer() {
				System.out.print("doCreateTextureLayer");
			}

			@Override
			public void doDeleteTerrain() {
				System.out.print("doDeleteTerrain");
			}

			@Override
			public void doDeleteTextureLayer() {
				System.out.print("doDeleteTextureLayer");
			}

			@Override
			public void doDeleteSelected() {
				System.out.print("doDeleteSelected");
			}

		});
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}

	static public void main(String[] args) {
		new MenuTest();
	}

}
