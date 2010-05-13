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

import com.worldwizards.saddl.SADDL;
import com.worldwizards.saddl.Tuple;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;



/**
 * This is a manubar that is configured by a SADDL file of the following form:
 * 
 * Menu
 *      Submenu
 *          Item=methodToCall
 * @author Jeffrey Kesselman
 */
@SuppressWarnings("unchecked")
public class JKMenuBar extends JMenuBar {
    List listeners = new ArrayList();

    /**
     * This is the constructor.  It takes a reader that is reading the SADDL
     * data
     * @param desc The SADDL file reader. (Does not have to actually be a file)
     */
    public JKMenuBar(Reader desc) {
        try {
            List<Tuple> menuLists = SADDL.parse(desc);
            parse(menuLists);
        } catch (IOException ex) {
            Logger.getLogger(JKMenuBar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * This is another construtor.  It takes the data returned from calling
     * SADDL.parse on the reader  in the abovre constructor.
     * @param menuLists A list of Tuples describing the menu structure
     */
    public JKMenuBar(List<Tuple> menuLists){
        parse(menuLists);
    }
    /**
     * This method actually parses the top level Tuple list and makes the
     * JMenu tree.
     * @param menuLists  A list of Tuples describing the menu structure
     */
    private void parse(List<Tuple> menuLists){
            System.out.println(menuLists);
            for (Tuple menuTuple : menuLists) {
                JMenu menu = new JMenu(menuTuple.getKey());
                menu.getPopupMenu().setLightWeightPopupEnabled(false);
                add(menu);
                if (menuTuple.getValue() instanceof List){ // sub menus
                    parseSubMenus(menu,(List<Tuple>)menuTuple.getValue());
                }
            }
       
        
    }

   
    /**
     * This is the recursive routine that descends the menu tree represenetd by 
     * nested Tuple lists.
     * @param menu The parent menu of the passed in sub-list
     * @param list a recursively descended sub-list of Tuples
     */
    private void parseSubMenus(JMenu menu, List<Tuple> list) {
        for (Tuple subMenuTuple : list){
            if (subMenuTuple.getValue() instanceof List) { // is a menus
                JMenu subMenu = new JMenu(subMenuTuple.getKey());
                subMenu.getPopupMenu().setLightWeightPopupEnabled(false);    
                menu.add(subMenu);
                parseSubMenus(subMenu,(List)subMenuTuple.getValue());
            } else {
                menu.add(new JKMenuItem(subMenuTuple.getKey(),
                        (String)subMenuTuple.getValue()));
            }
        }
    }
    
    /**
     * This method adds a listener to the list of listeners for this menu bar.
     * The listener must implement all of the action methods described in the
     * SADDL file or run-time errors will occur.
     * @param listener A listener tp invoke the SADDL file defined action
     * methods on
     */
    public void addListener(Object listener){
        listeners.add(listener);        
    }
    
    /**
     * A private method that invokes a method with the passed in name, no 
     * parameters, and a void return on all the registered listening objects.
     * @param methodName
     */
     private void doMenuItem(String methodName) {
        for(Object l : listeners){
            try {
                Method m = l.getClass().getMethod(methodName);
                m.setAccessible(true);
                m.invoke(l);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(JKMenuBar.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(JKMenuBar.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(JKMenuBar.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(JKMenuBar.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(JKMenuBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
     /**
      * A sub class of JMEnuItem that stores the name of a method to invoke and
      * calls doMenuItem above with that method nsame when it is clicked.
      */
    class JKMenuItem extends JMenuItem implements ActionListener{
        String methodName=null;
        
        JKMenuItem(String name, String methodToInvoke){
            super(name);
            methodName = methodToInvoke;
            this.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            if (methodName != null){
               // System.out.println("Calling "+methodName);
                JKMenuBar.this.doMenuItem(methodName);
            }
        }
    }
}
