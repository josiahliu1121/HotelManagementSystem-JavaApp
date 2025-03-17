package com.component;

import com.exception.DeletionRejectException;
import com.form.FormInterface;
import com.mapper.Mapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private Mapper mapper;
    private JPanel panel;
    private JButton editButton;
    private JButton deleteButton;
    private JTable table;
    private int row;
    private FormInterface form;

    public ButtonEditor(JTable table, Mapper mapper, FormInterface form) {
        this.table = table;
        this.mapper = mapper;
        this.form = form;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0)); // Fix button alignment

        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        panel.add(editButton);
        panel.add(deleteButton);

        // Action for Edit Button
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                form.setValues(table, row);
                form.setVisible(true);

                fireEditingStopped();
            }
        });

        // Action for Delete Button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.convertRowIndexToModel(table.getEditingRow());

                if (row >= 0 && row < table.getRowCount()) {
                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Are you sure you want to delete this row?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        DefaultTableModel model = (DefaultTableModel) table.getModel();

                        // Stop editing before deletion
                        if (table.isEditing()) {
                            table.getCellEditor().stopCellEditing();
                        }

                        try {
                            mapper.delete((Long)table.getValueAt(row, 0));
                        } catch (DeletionRejectException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        model.removeRow(row);

                        fireEditingStopped(); // Ensure JTable stops referencing deleted row
                    }
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}