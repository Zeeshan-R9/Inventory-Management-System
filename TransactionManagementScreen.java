import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class TransactionManagementScreen extends JPanel {
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JTextField idField, userIdField, typeField, dateField, itemIdField, quantityField, totalAmountField;
    private JButton addButton, refreshButton, updateButton, deleteButton, generateInvoiceButton;
    private TransactionService transactionService;
    private UserService userService;
    private ItemService itemService;

    public TransactionManagementScreen() {
        try {
            this.transactionService = new TransactionService();
            this.userService = new UserService();
            this.itemService = new ItemService();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Transaction Service Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{
                "Transaction ID", "User ID", "Type", "Date", "Item ID", "Quantity", "Total Amount"
        }, 0);
        transactionTable = new JTable(tableModel);

        transactionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && transactionTable.getSelectedRow() != -1) {
                int selectedRow = transactionTable.getSelectedRow();
                idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                userIdField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                typeField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                dateField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                itemIdField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                quantityField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                totalAmountField.setText(tableModel.getValueAt(selectedRow, 6).toString());
            }
        });

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5));

        formPanel.add(new JLabel("Transaction ID:"));
        idField = new JTextField();
        formPanel.add(idField);

        formPanel.add(new JLabel("User ID:"));
        userIdField = new JTextField();
        formPanel.add(userIdField);

        formPanel.add(new JLabel("Type (Sale/Purchase):"));
        typeField = new JTextField();
        formPanel.add(typeField);

        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        formPanel.add(dateField);

        formPanel.add(new JLabel("Item ID:"));
        itemIdField = new JTextField();
        formPanel.add(itemIdField);

        formPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        formPanel.add(quantityField);

        formPanel.add(new JLabel("Total Amount:"));
        totalAmountField = new JTextField();
        formPanel.add(totalAmountField);

        add(formPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        generateInvoiceButton = new JButton("Generate Invoice");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(generateInvoiceButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addTransaction());

        refreshButton.addActionListener(e -> loadTransactions());

        updateButton.addActionListener(e -> updateTransaction());

        deleteButton.addActionListener(e -> deleteTransaction());

        generateInvoiceButton.addActionListener(e -> generateInvoice());

        loadTransactions();
    }

    private void loadTransactions() {
        try {
            tableModel.setRowCount(0);
            List<Transaction> transactions = transactionService.getAllTransactions();
            for (Transaction transaction : transactions) {
                tableModel.addRow(new Object[]{
                        transaction.getTransactionId(),
                        transaction.getUserId(),
                        transaction.getType(),
                        transaction.getDate(),
                        transaction.getItemId(),
                        transaction.getQuantity(),
                        transaction.getTotalAmount()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading transactions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTransaction() {
        try {
            int userId = Integer.parseInt(userIdField.getText());
            String type = typeField.getText();
            java.sql.Date date = java.sql.Date.valueOf(dateField.getText());
            int itemId = Integer.parseInt(itemIdField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            double totalAmount = Double.parseDouble(totalAmountField.getText());

            Transaction transaction = new Transaction(0, userId, type, date, itemId, quantity, totalAmount);
            transactionService.addTransaction(transaction);
            loadTransactions();
            clearFields();
            JOptionPane.showMessageDialog(this, "Transaction added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding transaction: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTransaction() {
        try {
            int transactionId = Integer.parseInt(idField.getText());
            int userId = Integer.parseInt(userIdField.getText());
            String type = typeField.getText();
            java.sql.Date date = java.sql.Date.valueOf(dateField.getText());
            int itemId = Integer.parseInt(itemIdField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            double totalAmount = Double.parseDouble(totalAmountField.getText());

            Transaction transaction = new Transaction(transactionId, userId, type, date, itemId, quantity, totalAmount);
            transactionService.updateTransaction(transaction);
            loadTransactions();
            clearFields();
            JOptionPane.showMessageDialog(this, "Transaction updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating transaction: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTransaction() {
        try {
            int transactionId = Integer.parseInt(idField.getText());
            transactionService.deleteTransaction(transactionId);
            loadTransactions();
            clearFields();
            JOptionPane.showMessageDialog(this, "Transaction deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting transaction: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText("");
        userIdField.setText("");
        typeField.setText("");
        dateField.setText("");
        itemIdField.setText("");
        quantityField.setText("");
        totalAmountField.setText("");
    }

    private void generateInvoice() {
        try {
            int selectedRow = transactionTable.getSelectedRow();
            if (selectedRow != -1) {
                String transactionId = tableModel.getValueAt(selectedRow, 0).toString();
                String userId = tableModel.getValueAt(selectedRow, 1).toString();
                String type = tableModel.getValueAt(selectedRow, 2).toString();
                String date = tableModel.getValueAt(selectedRow, 3).toString();
                String itemId = tableModel.getValueAt(selectedRow, 4).toString();
                String quantity = tableModel.getValueAt(selectedRow, 5).toString();
                String totalAmount = tableModel.getValueAt(selectedRow, 6).toString();
                String itemName = this.itemService.getItemById(Integer.parseInt(itemId)).getName();
                String userName = this.userService.getUserById(Integer.parseInt(userId)).getUsername();

                String invoice = String.format(
                        "Invoice:\nTransaction ID: %s\nUser Name: %s\nType: %s\nDate: %s\nItem Name: %s\nQuantity: %s\nTotal Amount: %s",
                        transactionId, userName, type, date, itemName, quantity, totalAmount
                );
                JOptionPane.showMessageDialog(this, invoice, "Invoice", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a transaction to generate an invoice.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error Generating an invoice.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
