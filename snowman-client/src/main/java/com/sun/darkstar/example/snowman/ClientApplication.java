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
package com.sun.darkstar.example.snowman;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.io.File;

import com.jme.app.AbstractGame.ConfigShowMode;
import com.sun.darkstar.example.snowman.client.Client;
import com.sun.darkstar.example.snowman.client.handler.ClientHandler;
import com.sun.darkstar.example.snowman.exception.MissingComponentException;
import com.sun.darkstar.example.snowman.game.Game;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * <code>ClientApplication</code> is the main class for the client program.
 * It initializes all the <code>Component</code> and starts the program.
 * 
 * @author Yi Wang (Neakor)
 * @version Creation date: 05-23-2008 14:00 EST
 * @version Modified date: 06-06-2008 17:31 EST
 */
public class ClientApplication {

    /**
     * Main entry point of client program.
     * @param args Nothing needs to be passed in.
     */
    public static void main(String[] args) throws Exception {
        setLibraryPath();
        
        // Construct components.
        ClientHandler handler = new ClientHandler();
        Client client = new Client();
        Game game = new Game();
        game.setConfigShowMode(ConfigShowMode.ShowIfNoConfig, (URL) null);

        // Establish component connections.
        handler.connect(game);
        client.connect(handler);
        game.connect(client);
        // Initialize components.
        try {
            handler.activate();
            client.activate();
            game.activate();
        } catch (MissingComponentException e) {
            e.printStackTrace();
        }
    }

    private static boolean isWebstart(){
        try {
            final Class serviceManagerClass = Class.forName("javax.jnlp.ServiceManager");
            Method lookupMethod = (Method) AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws Exception {
                    return serviceManagerClass.getMethod("lookup", new Class[] {String.class});
                }
            });
            Object basicService = lookupMethod.invoke(serviceManagerClass, new Object[]{"javax.jnlp.BasicService"});
        } catch (IllegalAccessException ex) {
            return false;
        } catch (IllegalArgumentException ex) {
            return false;
        } catch (InvocationTargetException ex) {
           return false;
        } catch (PrivilegedActionException ex) {
           return false;
        } catch (ClassNotFoundException ex) {
           return false;
        }
        return true;
    }
    
    private static void setLibraryPath() throws Exception {
        if (isWebstart()){
            System.out.println("running in webstart");
            System.getProperties().remove("org.lwjgl.librarypath");
            return;
        }
        if (System.getProperty("org.lwjgl.librarypath") == null) {
            URL jarLocation = ClientApplication.class.
                    getProtectionDomain().getCodeSource().getLocation();
            File jarFile = new File(jarLocation.toURI());
            File jarDirectory = jarFile.getParentFile();
            
            String name = System.getProperty("os.name");
            String arch = System.getProperty("os.arch");

            String nativeDir = "";
            if ("Linux".equals(name) && "i386".equals(arch)) {
                nativeDir = "linux";
            } else if ("Linux".equals(name) &&
                    ("x86_64".equals(arch) || "amd64".equals(arch))) {
                nativeDir = "linux64";
            } else if ("Mac OS X".equals(name) &&
                    ("i386".equals(arch) || "x86_64".equals(arch))) {
                nativeDir = "macosx";
            } else if ("SunOS".equals(name) && "x86".equals(arch)) {
                nativeDir = "solaris";
            } else if (name != null && name.startsWith("Windows")) {
                nativeDir = "win32";
            } else {
                throw new IllegalStateException("Unsupported platform: \n" +
                                                "Name    : " + name + "\n" +
                                                "Arch    : " + arch);
            }
            
            File nativesDirectory = new File(jarDirectory, 
                                             "lib" + File.separator + 
                                             "natives" + File.separator + 
                                             nativeDir);
            System.setProperty("org.lwjgl.librarypath", 
                               nativesDirectory.getAbsolutePath());
        }
    }
}
