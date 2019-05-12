package com.calendar.main;

import javax.swing.table.AbstractTableModel;

public class CalendarTable extends AbstractTableModel{
	
	public String[] columnName = new String[1];
	public Object[][] data = new Object[1][1];
	
	public int getColumnCount() {
		return columnName.length;
	}
	public int getRowCount() {
		return data.length;
	}
	
	public String getColumnName(int col) {
		return columnName[col];
	}
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

}
