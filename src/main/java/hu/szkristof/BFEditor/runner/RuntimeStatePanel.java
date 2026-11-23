package hu.szkristof.BFEditor.runner;

import hu.szkristof.BFEditor.BFEditor;
import hu.szkristof.BFEngine.BFEngine;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RuntimeStatePanel extends JPanel {

	private JTable table;
	private RuntimeStateModel model;

	public RuntimeStatePanel() {
		setLayout(new BorderLayout());

		model = new RuntimeStateModel();
		table = new JTable(model);

		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	public void updatePanel() {
		BFEngine engine = BFEditor.getInstance().getEngine();
		char[] memory = engine.getMemory();

		model.clearData();
		model.addRow("Program counter", engine.getProgramCounter());
		model.addRow("Data pointer", engine.getDataPointer());
		for (int i = 0; i < memory.length; i++) {
			model.addRow("Memory[" + i + "]", (int) memory[i]);
		}

		model.fireTableDataChanged();
	}

	private static class RuntimeStateModel extends AbstractTableModel {

		private final List<Object[]> data = new ArrayList<>();

		private final String[] columns = {
				"Name", "Value"
		};

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public String getColumnName(int column) {
			return columns[column];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data.get(rowIndex)[columnIndex];
		}

		public void addRow(String name, Integer value) {
			data.add(new Object[]{name, value});
		}

		public void clearData() {
			int size = data.size();
			data.clear();
		}

	}

}
