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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * This class interprets a JME scene graph as a Swing JTree Model
 * @author Jeffrey Kesselman
 */
class JMonkeyTreeModel implements TreeModel {

    Spatial root;
    List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

    public JMonkeyTreeModel(Spatial sceneRoot) {
        root = sceneRoot;
    }

    @Override
    public Spatial getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((Node) parent).getChild(index);
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof Node) {
            List<Spatial> children = ((Node) parent).getChildren();
            if (children != null) {
                return children.size();
            }
        }
        return 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        return !(node instanceof Node);
    }

    public void addChild(Node parent, Spatial child) {
        parent.attachChild(child);
        for (TreeModelListener l : listeners) {
            l.treeNodesInserted(new TreeModelEvent(this, makePath(parent),
                    new int[]{parent.getChildIndex(child)},
                    new Object[]{child}));
        }
    }

    void deleteNode(Spatial node) {
        Node parent = node.getParent();
        if (parent != null) {
            int idx = parent.getChildIndex(node);
            node.removeFromParent();
            for (TreeModelListener l : listeners) {
                l.treeNodesRemoved(new TreeModelEvent(this, makePath(parent),
                        new int[]{idx}, new Object[]{node}));
            }
        }
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
    	if (parent instanceof DefaultMutableTreeNode && child instanceof DefaultMutableTreeNode) {
	        return ((DefaultMutableTreeNode) parent).getIndex(
	                (DefaultMutableTreeNode) child);
    	} else if (parent instanceof Node && child instanceof Spatial) {
    		return ((Node)parent).getChildIndex((Spatial)child);
    	} else {
    		return -1;
    	}
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listeners.remove(l);
    }

    private Object[] makePath(Spatial node) {
        List<Object> pathList = new LinkedList<Object>();
        pathList.add(node);
        while (node.getParent() != null) {
            node = node.getParent();
            pathList.add(0, node);
        }
        return pathList.toArray();
    }
}
