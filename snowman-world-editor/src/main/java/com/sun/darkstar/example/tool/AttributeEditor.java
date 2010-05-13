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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/**
 * This class extends JTable to create a very specific editor for Maps
 * 
 * @author Jeffrey Kesselman
 */
public class AttributeEditor extends JTable {
	List<AttributeEditorListener> listeners = new ArrayList<AttributeEditorListener>();
	public AttributeEditor() {
		super();
		setAttributes(new HashMap<String, String>());
		setColumnModel(new AEColumnModel());
		setCellSelectionEnabled(true);
	}

	/**
	 * This call sets the map that this editor represents/edits
	 * 
	 * @param map
	 */
	public void setAttributes(Map<String, String> map) {
		setModel(new MapModel(map));
		
	}
	
	public void fireAttributeChanged(String key, String value){
		for(AttributeEditorListener l : listeners) {
			l.attributeChanged(key, value);
		}
	}
	
	public void addListener(AttributeEditorListener l){
		listeners.add(l);
	}

	class MapModel extends AbstractTableModel {
		Map<String, String> map;

		public MapModel(Map<String, String> map) {
			this.map = map;
		}

		public int getRowCount() {
			return map.size();
		}

		public int getColumnCount() {
			return 2;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			Entry entry = (Entry) map.entrySet().toArray()[rowIndex];
			if (columnIndex == 0) {
				return entry.getKey();
			} else {
				return entry.getValue();
			}
		}

		public boolean isCellEditable(int row, int col) {
			return col == 1;
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			if (columnIndex == 1) {
				String tag = (String) ((Entry) map.entrySet().toArray()[rowIndex])
						.getKey();
				map.put(tag, (String)value);
				AttributeEditor.this.fireAttributeChanged(tag,(String)value);
			}
		}

	}

	class AEColumnModel extends DefaultTableColumnModel {
		TableColumn[] cols  = new TableColumn[2];
		public AEColumnModel() {
			cols[0] = new TableColumn(0) {
				public Object getHeaderValue() {
					return "Attribute";
				}
			};
			cols[1] = new TableColumn(1) {
				public Object getHeaderValue() {
					return "Value";
				}
			};

		}

		@Override
		public TableColumn getColumn(int columnIndex) {
			// TODO Auto-generated method stub
			return cols[columnIndex];
		}
		
		

	}
}
