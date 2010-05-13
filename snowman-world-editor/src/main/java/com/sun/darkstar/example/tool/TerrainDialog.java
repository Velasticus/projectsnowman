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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class TerrainDialog extends JDialog {
	private boolean doit = false;
	JSpinner xSpinner;
	JSpinner ySpinner;
	JSpinner trisPerMesh;
	public TerrainDialog(Frame parent){
		super(parent,"Set Terrain Size",true);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,2));
		xSpinner = new JSpinner(new SpinnerNumberModel(1,1,Integer.MAX_VALUE,1));
		ySpinner = new JSpinner(new SpinnerNumberModel(1,1,Integer.MAX_VALUE,1));
		trisPerMesh = new JSpinner(new SpinnerNumberModel(2000,100,Integer.MAX_VALUE,100));
		panel.add(new JLabel("Width"));
		panel.add(xSpinner);
		panel.add(new JLabel("Depth"));
		panel.add(ySpinner);
		panel.add(new JLabel("Triangles per mesh"));
		panel.add(trisPerMesh);
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				doit = true;
				TerrainDialog.this.setVisible(false);
				
			}});
		JButton cancelButton = new JButton("CANCEL");
		cancelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				doit = false;
				TerrainDialog.this.setVisible(false);				
			}});
		panel.add(okButton);
		panel.add(cancelButton);
		setLayout(new BorderLayout());
		add(panel,BorderLayout.CENTER);
		setLocation(parent.getWidth()/2,parent.getHeight()/2);
		pack();
		setVisible(true);
	}
	
	public boolean wasCanceled(){
		return !doit;
	}
	
	public Dimension getTerrainSize(){
		return new Dimension((Integer)xSpinner.getModel().getValue(),
				(Integer)ySpinner.getModel().getValue());
	}
	
	public int getTrisPerMesh(){
		return (Integer)trisPerMesh.getModel().getValue();
	}
	
}
