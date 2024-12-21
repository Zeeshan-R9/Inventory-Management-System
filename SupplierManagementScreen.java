import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;


public class SupplierManagementScreen extends JPanel {
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, contactField, addressField, idField;
    private JButton addButton, updateButton, deleteButton, refreshButton;
    private SupplierService supplierService;

    public SupplierManagementScreen() {
        try {
            supplierService = new SupplierService();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error initializing SupplierService: " + e.getMessage()
                    , "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Contact Info", "Address"}, 0);
        supplierTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(supplierTable);
        add(scrollPane, BorderLayout.CENTER);

        supplierTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && supplierTable.getSelectedRow() != -1) {
                int selectedRow = supplierTable.getSelectedRow();
                idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                contactField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                addressField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            }
        });

        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Supplier Management"));

        inputPanel.add(new JLabel("Supplier ID (for Update/Delete):"));
        idField = new JTextField();
        inputPanel.add(idField);


        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Contact Info:"));
        contactField = new JTextField();
        inputPanel.add(contactField);

        inputPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        inputPanel.add(addressField);

        addButton = new JButton("Add Supplier");
        updateButton = new JButton("Update Supplier");
        deleteButton = new JButton("Delete Supplier");
        refreshButton = new JButton("Refresh Table");

        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);
        inputPanel.add(refreshButton);

        add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSupplier();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSupplier();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSupplier();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSuppliers();
            }
        });

        loadSuppliers();
    }

    private void loadSuppliers() {
        try {
            List<Supplier> suppliers = supplierService.getAllSuppliers();
            tableModel.setRowCount(0);
            for (Supplier supplier : suppliers) {
                tableModel.addRow(new Object[]{supplier.getSupplierId(), supplier.getName(), supplier.getContactNumber(), supplier.getAddress()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading suppliers: " + e.getMessage()
                    , "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSupplier() {
        String name = nameField.getText();
        String contactInfo = contactField.getText();
        String address = addressField.getText();

        if (name.isEmpty() || contactInfo.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            supplierService.addSupplier(name, contactInfo, address);
            JOptionPane.showMessageDialog(this, "Supplier added successfully!");
            loadSuppliers();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding supplier: " + e.getMessage()
                    , "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSupplier() {
        String idText = idField.getText();
        String name = nameField.getText();
        String contactInfo = contactField.getText();
        String address = addressField.getText();

        if (idText.isEmpty() || name.isEmpty() || contactInfo.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            supplierService.updateSupplier(id, name, contactInfo, address);
            JOptionPane.showMessageDialog(this, "Supplier updated successfully!");
            loadSuppliers();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating supplier: " + e.getMessage()
                    , "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSupplier() {
        String idText = idField.getText();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the Supplier ID.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            supplierService.deleteSupplier(id);
            JOptionPane.showMessageDialog(this, "Supplier deleted successfully!");
            loadSuppliers();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format."
                    , "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting supplier: " + e.getMessage()
                    , "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
