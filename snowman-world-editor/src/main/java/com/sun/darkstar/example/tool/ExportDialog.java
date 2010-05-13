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
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ExportDialog extends JDialog{
	JCheckBox textureExport = new JCheckBox();
	JTextField fileNameField;
	Boolean canceled = false;
	private File file;
	private boolean hasFile;
	
	public ExportDialog(final WorldEditor parent){
		super(parent,"Export");
		setModal(true);
		setLayout(new BorderLayout());
		textureExport = new JCheckBox("Export Textures");
		JPanel jpanel = new JPanel();
		jpanel.setLayout(new GridLayout(2,1));
		jpanel.add(textureExport);
		JPanel jp2 = new JPanel();
		jp2.setLayout(new GridLayout(1,2));
		JButton exportButton = new JButton("Export");
		exportButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				canceled = false;
				setVisible(false);
			}});
		jp2.add(exportButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				canceled = true;
				setVisible(false);
			}});
		jp2.add(cancelButton);
		jpanel.add(jp2);
		add(jpanel,BorderLayout.SOUTH);
		jpanel = new JPanel();
		jpanel.setLayout(new BorderLayout());
		jpanel.add(new JLabel("Export As: "),BorderLayout.WEST);
		fileNameField = new JTextField();
		fileNameField.setEditable(false);
		fileNameField.setColumns(15);
		jpanel.add(fileNameField,BorderLayout.CENTER);
		JButton browseButton = new JButton("Choose File");
		jpanel.add(browseButton,BorderLayout.EAST);
		add(jpanel,BorderLayout.NORTH);
		browseButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser("Pick a file");
				chooser.setCurrentDirectory(parent.lastDirectory);
				int retval = chooser.showSaveDialog(ExportDialog.this);
				parent.lastDirectory = chooser.getCurrentDirectory();
				if (retval == JFileChooser.APPROVE_OPTION){
					if(chooser.getSelectedFile() != null) {
						hasFile = true;
						setFileNameField(chooser.getSelectedFile().getName());
						file = chooser.getSelectedFile();
					} else hasFile = false;
				}
				
			}});
		pack();
		setLocation(new Point(parent.getWidth()/2,parent.getHeight()/2));
	}
	
	public boolean showDialog(){
		setVisible(true);
		return !canceled;
	}

	protected void setFileNameField(String name) {
		fileNameField.setText(name);
		fileNameField.repaint();
	}
	
	public String getFilenameField(){
		return fileNameField.getText();
	}
	
	public Boolean exportTextures(){
		return textureExport.isSelected();
	}

	public File getFile() {
		return this.file;
	}
	
	public boolean hasFile() {
		return this.hasFile;
	}
}
