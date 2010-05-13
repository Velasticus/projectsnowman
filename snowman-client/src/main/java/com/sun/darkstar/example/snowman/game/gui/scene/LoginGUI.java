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
package com.sun.darkstar.example.snowman.game.gui.scene;

import org.fenggui.Label;
import org.fenggui.Button;
import org.fenggui.TextEditor;
import org.fenggui.util.Alphabet;
import org.fenggui.util.Color;
import org.fenggui.util.fonttoolkit.FontFactory;

import com.sun.darkstar.example.snowman.game.gui.GUIPass;
import com.sun.darkstar.example.snowman.game.gui.enumn.EButton;
import com.sun.darkstar.example.snowman.game.state.scene.login.LoginButtonHandler;

/**
 * <code>LoginGUI</code> extends <code>GUIPass</code> to define the user
 * interface for the login scene. It initializes and maintains all the
 * FengGUI widgets.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 07-09-2008 15:43 EST
 * @version Modified date: 07-10-2008 18:24 EST
 */
public class LoginGUI extends GUIPass {
    /**
     * Serial version.
     */
    private static final long serialVersionUID = -1020458021673923988L;
    
     /**
     * The <code>LoginButtonHandler</code> instance.
     */
    private final LoginButtonHandler buttonHandler;
        
    /**
     * The default status text.
     */
    private final String defaultStatus;
    private final String connectingStatus;
    private final String waitingStatus;
    private final String failedStatus;
    /**
     * The status note.
     */
    private Label labelStatus;
    
    private TextEditor textUsername;
    private TextEditor textPassword;
    private TextEditor textHost;
    private TextEditor textPort;
    private Button connectButton;

    /**
     * Constructor of <code>LoginGUI</code>.
     */
    public LoginGUI() {
        super();
        this.buttonHandler = new LoginButtonHandler(this);
        this.defaultStatus = "Welcome to Project Snowman";
        this.connectingStatus = "Connecting to server, please wait ...";
        this.waitingStatus = "Waiting for server to match you into a game...";
        this.failedStatus = "Login failed";
    }

    @Override
    public void buildWidgets() {
        this.buildStatus();
        this.buildTexts();
        this.buildButton();
    }

    /**
     * Build the status note.
     */
    private void buildStatus() {
        this.labelStatus = new Label(this.defaultStatus);
        java.awt.Font awtFont = new java.awt.Font("Sans", java.awt.Font.BOLD, 16);
        this.labelStatus.getAppearance().setFont(FontFactory.renderStandardFont(awtFont, true, Alphabet.getDefaultAlphabet()));
        this.labelStatus.setSizeToMinSize();
        this.labelStatus.setHeight(60);
        this.labelStatus.setXY(this.display.getWidth() / 2 - this.labelStatus.getWidth() / 2, this.display.getHeight() / 6);
        this.labelStatus.getAppearance().setTextColor(Color.WHITE);
        this.display.addWidget(this.labelStatus);
    }
    
    /**
     * Build the text fields.
     */
    private void buildTexts() {
        Label labelUsername = new Label("Username:  ");
        labelUsername.setSizeToMinSize();
        labelUsername.getAppearance().setTextColor(Color.WHITE);
        labelUsername.setXY(this.display.getWidth() / 4 - (75 + labelUsername.getWidth() / 2),
                            this.display.getHeight() * 7 / 8);
        this.textUsername = new TextEditor();
        this.textUsername.setText(System.getProperty("username", ""));
        this.textUsername.setSize(150, 20);
        this.textUsername.setXY(labelUsername.getX() + labelUsername.getWidth(), labelUsername.getY());
        this.textUsername.setMultiline(false);
        this.display.addWidget(labelUsername);
        this.display.addWidget(this.textUsername);
        
        Label labelPassword = new Label("Password:  ");
        labelPassword.setSize(labelUsername.getSize());
        labelPassword.getAppearance().setTextColor(Color.WHITE);
        labelPassword.setXY(this.display.getWidth() / 4 - (75 + labelPassword.getWidth() / 2),
                            this.display.getHeight() * 7 / 8 - 30);
        this.textPassword = new TextEditor();
        this.textPassword.setText(System.getProperty("password", ""));
        this.textPassword.setSize(150, 20);
        this.textPassword.setXY(labelPassword.getX() + labelPassword.getWidth(), labelPassword.getY());
        this.textPassword.setMultiline(false);
        this.textPassword.setPasswordField(true);
        this.display.addWidget(labelPassword);
        this.display.addWidget(this.textPassword);
        
        Label labelHost = new Label("Host:  ");
        labelHost.setSizeToMinSize();
        labelHost.getAppearance().setTextColor(Color.WHITE);
        labelHost.setXY(this.display.getWidth() * 3 / 4 - (75 + labelHost.getWidth() / 2),
                            this.display.getHeight() * 7 / 8);
        this.textHost = new TextEditor();
        this.textHost.setText(System.getProperty("server.host", "localhost"));
        this.textHost.setSize(150, 20);
        this.textHost.setXY(labelHost.getX() + labelHost.getWidth(), labelHost.getY());
        this.textHost.setMultiline(false);
        this.display.addWidget(labelHost);
        this.display.addWidget(this.textHost);
        
        Label labelPort = new Label("Port:  ");
        labelPort.setSize(labelHost.getSize());
        labelPort.getAppearance().setTextColor(Color.WHITE);
        labelPort.setXY(this.display.getWidth() * 3 / 4 - (75 + labelPort.getWidth() / 2),
                        this.display.getHeight() * 7 / 8 - 30);
        this.textPort = new TextEditor();
        this.textPort.setText(System.getProperty("server.port", "3000"));
        this.textPort.setSize(150, 20);
        this.textPort.setXY(labelPort.getX() + labelPort.getWidth(), labelPort.getY());
        this.textPort.setMultiline(false);
        this.display.addWidget(labelPort);
        this.display.addWidget(this.textPort);
    }
    
    /**
     * Build the button.
     */
    private void buildButton() {
        this.connectButton = new Button(EButton.Connect.toString());
        this.connectButton.setSize(75, 20);
        this.connectButton.setXY(this.display.getWidth() / 2 - this.connectButton.getWidth() / 2,
                                 this.display.getHeight() * 7 / 8 - 60);
        this.connectButton.addButtonPressedListener(this.buttonHandler);
        this.display.addWidget(this.connectButton);
    }

    private void positionLabel(Label label) {
        label.setSizeToMinSize();
        label.setHeight(60);
        label.setXY(this.display.getWidth() / 2 - label.getWidth() / 2, this.display.getHeight() / 6);
    }

    /**
     * Set the status text.
     * @param text The <code>String</code> status text to be set.
     */
    public void setStatus(String text) {
        this.labelStatus.setText(text);
        positionLabel(this.labelStatus);
    }



    /**
     * Retrieve the default status text.
     * @return The default <code>String</code> status text.
     */
    public String getDefaultStatus() {
        return this.defaultStatus;
    }
    
    public String getConnectingStatus() {
        return this.connectingStatus;
    }

    public String getWaitingStatus() {
        return this.waitingStatus;
    }

    public String getFailedStatus() {
        return this.failedStatus;
    }
    
    
    public String getUsername() {
        return textUsername.getText();
    }
    public String getPassword() {
        return textPassword.getText();
    }
    public String getHost() {
        return textHost.getText();
    }
    public String getPort() {
        return textPort.getText();
    }
}
