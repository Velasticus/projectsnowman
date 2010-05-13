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

import org.fenggui.Button;
import org.fenggui.Container;
import org.fenggui.Span;
import org.fenggui.TextEditor;
import org.fenggui.background.PlainBackground;
import org.fenggui.border.PlainBorder;
import org.fenggui.event.FocusEvent;
import org.fenggui.layout.StaticLayout;
import org.fenggui.text.TextStyle;
import org.fenggui.util.Color;
import org.fenggui.util.fonttoolkit.FontFactory;

import com.sun.darkstar.example.snowman.game.gui.GUIPass;
import com.sun.darkstar.example.snowman.game.gui.text.TextView;
import com.sun.darkstar.example.snowman.game.gui.widget.ScrollContainer;
import com.sun.darkstar.example.snowman.game.input.gui.ChatButtonHandler;

/**
 * <code>ChatGUI</code> defines the concrete implementation of the GUI render
 * pass that displays the chat interface during the battle stage of the game.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 11-12-2008 12:18 EST
 * @version Modified date: 11-12-2008 14:39 EST
 */
public class ChatGUI extends GUIPass {

    /**
     * Serial version.
     */
    private static final long serialVersionUID = 8866591274194274971L;
    /**
     * The chat container.
     */
    private Container mainContainer;
    /**
     * The input container.
     */
    private Container inputContainer;
    /**
     * The chat button.
     */
    private Button chatButton;
    /**
     * The output scroll container
     */
    private ScrollContainer outputContainer;
    /**
     * The text view to display chat messages
     */
    private TextView outputChannel;
    /**
     * The text font style
     */
    private TextStyle outputStyle;
    /**
     * The flag indicates if the chat is currently stay enabled.
     */
    private boolean stayEnabled;

    /**
     * Constructor of <code>BattleGUI</code>
     */
    public ChatGUI() {
    }

    @Override
    protected void buildWidgets() {
        this.buildMainContainer();
        // Add parts to the main container.
        this.mainContainer.addWidget(this.buildChatButton());
        this.mainContainer.addWidget(this.buildOutputField());
        // Build the input field which is not added to the main container initially.
        this.buildInputField();
        // Add the main container to the display.
        this.display.addWidget(this.mainContainer);
    }

    @Override
    protected void doUpdate(float tpf) {
        super.doUpdate(tpf);
    }

    /**
     * Build the main container which contains the whole chat sub GUI system.
     */
    private void buildMainContainer() {
        // Create the main container.
        this.mainContainer = new Container(new StaticLayout());
        this.mainContainer.setSize(460, 216);
        this.mainContainer.setXY(5, 5);
    }

    /**
     * Build the chat button.
     */
    private Button buildChatButton() {
        // Create the chat button.
        this.chatButton = new Button("Chat");
        this.chatButton.setSize(60, 20);
        this.chatButton.setXY(0, 0);
        this.chatButton.addButtonPressedListener(new ChatButtonHandler(this));
        // Return the chat button.
        return this.chatButton;
    }

    /**
     * Build the output text views which display the chat messages.
     * @return The default channel output container.
     */
    private ScrollContainer buildOutputField() {
        // Create the output container
        this.outputContainer = new ScrollContainer(true);
        this.outputContainer.setSize(this.mainContainer.getWidth(), this.mainContainer.getHeight() - this.chatButton.getHeight());
        this.outputContainer.setXY(0, this.chatButton.getHeight());

        // Create the output text channels and add them to the container
        this.outputChannel = new TextView();
        this.outputChannel.setSize(this.outputContainer.getWidth(), this.outputContainer.getHeight());
        this.outputChannel.setXY(0, 0);
        this.outputChannel.setFadingEnabled(true);
        this.outputContainer.setInnerWidget(this.outputChannel);

        // Create the text style
        this.outputStyle = new TextStyle(FontFactory.renderStandardFont(new java.awt.Font("Verdana", java.awt.Font.BOLD, 12)), Color.WHITE);
        
        // Return the default channel container.
        return this.outputContainer;
    }

    /**
     * Build the input container.
     */
    private void buildInputField() {
        // Create the input container.
        this.inputContainer = new Container(new StaticLayout());
        this.inputContainer.setSize(this.mainContainer.getWidth() - this.chatButton.getWidth(), this.chatButton.getHeight());
        this.inputContainer.setXY(this.chatButton.getWidth(), 0);
        this.inputContainer.getAppearance().add(new PlainBorder(1, 1, 1, 1, new Color(255, 255, 255, 255), true, Span.BORDER));
        // Create the general text field for input container.
        TextEditor messageInputText = new TextEditor(false);
        messageInputText.setSize(inputContainer.getWidth(), inputContainer.getHeight());
        messageInputText.setXY(0, 0);
        messageInputText.setMaxCharacters(75);
        inputContainer.addWidget(messageInputText);
    }

    /**
     * Append the given chat message to the correct output fields.
     * @param name The name of the player who sent the message
     * @param chatMessage The chat message needs to be appended.
     */
    public void appendChatMessage(String name, String chatMessage) {
        StringBuilder builder = new StringBuilder();
        builder.append(name).append(": ").append(chatMessage).append("\n");
        // Append the message with correct "to" string in specified channel.
        this.outputChannel.appendText(builder.toString(), this.outputStyle);
        // Layout the output container.
        this.outputContainer.layout();
    }

    /**
     * Enable or disable the input field.
     * @param enabled True if the input field should be enabled. False disabled.
     */
    public void setInputEnabled(boolean enabled) {
        if (this.stayEnabled && !enabled) {
            this.setInputFocused(true);
            return;
        }
        if (enabled) {
            this.mainContainer.addWidget(this.inputContainer);
        } else {
            this.mainContainer.removeWidget(this.inputContainer);
        }
        this.setInputFocused(enabled);
    }

    /**
     * Set if the chat input field should stay enabled.
     * @param stayEnabled True if the chat input field should stay enbled. False otherwise.
     */
    public void setStayEnabled(boolean stayEnabled) {
        this.stayEnabled = stayEnabled;
        this.outputContainer.setVerticalScrollBarEnabled(stayEnabled);
        this.outputChannel.setFadingEnabled(!stayEnabled);

        if (stayEnabled) {
            if (!this.isChatInputEnabled()) {
                this.inputContainer.layout();
                this.mainContainer.addWidget(this.inputContainer);
            }
            this.outputContainer.getAppearance().add(new PlainBackground(new Color(0, 0, 0, 160)));
            // Gain input focus.
            if (!this.isInTypingState()) {
                this.setInputFocused(true);
            }
        } else {
            this.mainContainer.removeWidget(this.inputContainer);
            this.outputContainer.getAppearance().removeAll();
            // Lose input focus.
            if (this.isInTypingState()) {
                this.setInputFocused(false);
            }
        }
        this.mainContainer.layout();
    }

    private void setInputFocused(boolean focused) {
        TextEditor input = (TextEditor) this.inputContainer.getWidget(0);
        if (focused && this.inputContainer.isInWidgetTree()) {
            input.focusChanged(new FocusEvent(input, false));
            this.inputContainer.focusChanged(new FocusEvent(this.inputContainer, false));
        } else if (!focused && this.inputContainer.isInWidgetTree()) {
            input.focusChanged(new FocusEvent(input, true));
            this.inputContainer.focusChanged(new FocusEvent(this.inputContainer, true));
        }
    }

    /**
     * Retrieve the chat message in the chat input field.
     * @return The chat message.
     */
    public String getChatMessage() {
        return ((TextEditor) this.inputContainer.getWidget(0)).getText();
    }

    /**
     * Delete the chat message in the chat input field.
     */
    public void cleanupChatInput() {
        ((TextEditor) this.inputContainer.getWidget(0)).setText("");
    }

    /**
     * Check if the chat input field is enbaled.
     * @return True if the chat input field is enabled. False otherwise.
     */
    public boolean isChatInputEnabled() {
        return this.inputContainer.isInWidgetTree();
    }

    /**
     * Check if the user is typing in the input field.
     * @return True if the user is typing in the input field. False otherwise.
     */
    public boolean isInTypingState() {
        return ((TextEditor) this.inputContainer.getWidget(0)).isInWritingState();
    }
}
