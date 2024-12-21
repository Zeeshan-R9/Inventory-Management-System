import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class CategoryManagementScreen extends JPanel {
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField categoryIdField, categoryNameField;
    private JButton addButton, updateButton, deleteButton;
    private CategoryService categoryService;

    public CategoryManagementScreen() {
        try {
            categoryService = new CategoryService();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error initializing CategoryService: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        categoryIdField = new JTextField();
        categoryIdField.setEditable(false);
        categoryNameField = new JTextField();

        topPanel.add(new JLabel("Category ID:"));
        topPanel.add(categoryIdField);
        topPanel.add(new JLabel("Category Name:"));
        topPanel.add(categoryNameField);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name"}, 0);
        categoryTable = new JTable(tableModel);
        loadCategories();
        categoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && categoryTable.getSelectedRow() != -1) {
                int selectedRow = categoryTable.getSelectedRow();
                categoryIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                categoryNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            }
        });

        add(new JScrollPane(categoryTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        bottomPanel.add(addButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);

        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCategory();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCategory();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCategory();
            }
        });
    }

    private void loadCategories() {
        try {
            tableModel.setRowCount(0);
            List<Category> categories = categoryService.getAllCategories();
            for (Category category : categories) {
                tableModel.addRow(new Object[]{category.getCategoryId(), category.getName()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCategory() {
        String categoryName = categoryNameField.getText().trim();
        if (categoryName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            categoryService.addCategory(categoryName);
            loadCategories();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding category: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCategory() {
        String categoryIdText = categoryIdField.getText().trim();
        String categoryName = categoryNameField.getText().trim();
        if (categoryIdText.isEmpty() || categoryName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a category and enter a valid name.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int categoryId = Integer.parseInt(categoryIdText);
            categoryService.updateCategory(categoryId, categoryName);
            loadCategories();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating category: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCategory() {
        String categoryIdText = categoryIdField.getText().trim();
        if (categoryIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a category to delete.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int categoryId = Integer.parseInt(categoryIdText);
            categoryService.removeCategory(categoryId);
            loadCategories();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting category: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        categoryIdField.setText("");
        categoryNameField.setText("");
    }
}
