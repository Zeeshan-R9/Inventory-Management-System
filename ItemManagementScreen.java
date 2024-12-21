import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ItemManagementScreen extends JPanel {
    private JTable itemsTable;
    private JTextField nameField, priceField, quantityField;
    private JComboBox<String> categoryComboBox, supplierComboBox;
    private JButton addButton, purchaseButton, sellButton, refreshButton, updateButton, deleteButton, filterByCategoryButton, filterBySupplierButton, filterLowStockButton;
    private ItemService itemService;
    private CategoryService categoryService;
    private SupplierService supplierService;
    private TransactionService transactionService;

    public ItemManagementScreen() {
        try {
            this.itemService = new ItemService();
            this.categoryService = new CategoryService();
            this.supplierService = new SupplierService();
            this.transactionService = new TransactionService();
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Items Management Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Item Details"));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        formPanel.add(priceField);

        formPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        formPanel.add(quantityField);

        formPanel.add(new JLabel("Category:"));
        categoryComboBox = new JComboBox<>();
        formPanel.add(categoryComboBox);

        formPanel.add(new JLabel("Supplier:"));
        supplierComboBox = new JComboBox<>();
        formPanel.add(supplierComboBox);

        addButton = new JButton("Add");
        formPanel.add(addButton);

        updateButton = new JButton("Update");
        formPanel.add(updateButton);

        deleteButton = new JButton("Delete");
        formPanel.add(deleteButton);

        filterByCategoryButton = new JButton("Filter by Category");
        formPanel.add(filterByCategoryButton);

        filterBySupplierButton = new JButton("Filter by Supplier");
        formPanel.add(filterBySupplierButton);

        filterLowStockButton = new JButton("Filter Low Stock");
        formPanel.add(filterLowStockButton);

        refreshButton = new JButton("Refresh");
        formPanel.add(refreshButton);

        purchaseButton = new JButton("Purchase Item");
        formPanel.add(purchaseButton);

        sellButton = new JButton("Sell Item");
        formPanel.add(sellButton);


        add(formPanel, BorderLayout.NORTH);

        TableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Price", "Category", "Supplier", "Quantity"}, 0);
        itemsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(itemsTable);
        add(scrollPane, BorderLayout.CENTER);

        itemsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && itemsTable.getSelectedRow() != -1) {
                int selectedRow = itemsTable.getSelectedRow();
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                priceField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                categoryComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
                supplierComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4).toString());
                quantityField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            }
        });

        loadCategories();
        loadSuppliers();
        loadItems();

        addButton.addActionListener(e -> addItem());
        updateButton.addActionListener(e -> updateItem());
        deleteButton.addActionListener(e -> deleteItem());
        filterByCategoryButton.addActionListener(e -> filterByCategory());
        filterBySupplierButton.addActionListener(e -> filterBySupplier());
        filterLowStockButton.addActionListener(e -> filterLowStock());
        refreshButton.addActionListener(e -> {loadItems(); loadCategories(); loadSuppliers();});
        purchaseButton.addActionListener(e -> purchaseItems());
        sellButton.addActionListener(e -> sellItems());

    }

    private void loadCategories() {
        try {
            categoryComboBox.removeAllItems();
            List<Category> categories = categoryService.getAllCategories();
            for (Category category : categories) {
                categoryComboBox.addItem(category.getName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load categories: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSuppliers() {
        try {
            supplierComboBox.removeAllItems();
            List<Supplier> suppliers = supplierService.getAllSuppliers();
            for (Supplier supplier : suppliers) {
                supplierComboBox.addItem(supplier.getName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load suppliers: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadItems() {
        try {
            DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
            model.setRowCount(0); // Clear table
            List<Item> items = itemService.getAllItems();

            for (Item item : items) {
                Supplier supplier = this.supplierService.getSupplierById(item.getSupplierId());
                Category category = this.categoryService.getCategoryById(item.getCategoryId());
                model.addRow(new Object[]{item.getItemId(), item.getName(), item.getPrice(), supplier.getName(), category.getName(), item.getQuantity()});
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load items: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addItem() {
        try {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            String category = (String) categoryComboBox.getSelectedItem();
            String supplier = (String) supplierComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());

            if (name.isEmpty() || category == null || supplier == null) {
                throw new Exception("All fields must be filled out.");
            }

            itemService.addItem(new Item(0, name, quantity, price, this.categoryService.getCategoryByName(category).getCategoryId(),
                    this.supplierService.getSupplierByName(supplier).getSupplierId())); // 0 for quantity (default)
            loadItems();
            JOptionPane.showMessageDialog(this, "Item added successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to add item: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateItem() {
        try {
            int selectedRow = itemsTable.getSelectedRow();
            if (selectedRow == -1) {
                throw new Exception("No item selected for update.");
            }

            int itemId = (int) itemsTable.getValueAt(selectedRow, 0);
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            String category = (String) categoryComboBox.getSelectedItem();
            String supplier = (String) supplierComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());
            if (name.isEmpty() || category == null || supplier == null) {
                throw new Exception("All fields must be filled out.");
            }

            itemService.updateItem(new Item(itemId, name, quantity, price, this.categoryService.getCategoryByName(category).getCategoryId(),
                    this.supplierService.getSupplierByName(supplier).getSupplierId())); // 0 for quantity (default)
            loadItems();
            JOptionPane.showMessageDialog(this, "Item updated successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update item: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteItem() {
        try {
            int selectedRow = itemsTable.getSelectedRow();
            if (selectedRow == -1) {
                throw new Exception("No item selected for deletion.");
            }

            int itemId = (int) itemsTable.getValueAt(selectedRow, 0);
            itemService.deleteItem(itemId);
            loadItems();
            JOptionPane.showMessageDialog(this, "Item deleted successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to delete item: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterByCategory() {
        try {
            String selectedCategoryName = (String) categoryComboBox.getSelectedItem();
            Category selectedCategory = this.categoryService.getCategoryByName(selectedCategoryName);
            List<Item> items = itemService.getItemsByCategory(selectedCategory);
            updateItemTable(items);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to filter by category: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterBySupplier() {
        try {
            String selectedSupplierName = (String) supplierComboBox.getSelectedItem();
            Supplier selectedSupplier = this.supplierService.getSupplierByName(selectedSupplierName);
            List<Item> items = itemService.getItemsBySupplier(selectedSupplier);
            updateItemTable(items);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to filter by supplier: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterLowStock() {
        try {
            List<Item> items = itemService.getLowStockItems(5);
            updateItemTable(items);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to filter low stock items: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void updateItemTable(List<Item> items) {
        try {
            DefaultTableModel model = (DefaultTableModel) itemsTable.getModel();
            model.setRowCount(0);
            for (Item item : items) {
                Supplier supplier = this.supplierService.getSupplierById(item.getSupplierId());
                Category category = this.categoryService.getCategoryById(item.getCategoryId());
                model.addRow(new Object[]{item.getItemId(), item.getName(), item.getPrice(), category.getName(), supplier.getName(), item.getQuantity()});
            }
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void purchaseItems() {
        try {
            int selectedRow = itemsTable.getSelectedRow();
            int itemId = Integer.parseInt(itemsTable.getModel().getValueAt(selectedRow, 0).toString());
            int quantity = Integer.parseInt(quantityField.getText());
            this.itemService.purchaseItem(itemId, quantity);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentdate = currentDate.format(formatter);

        this.transactionService.addTransaction(new Transaction(-1,
                ApplicationState.getLoggedInUSer().getUserId(),
                "Purchase",
                Date.valueOf(currentdate),
                itemId,
                quantity,
                -1));
            loadItems();
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sellItems() {
        try {
            int selectedRow = itemsTable.getSelectedRow();
            int itemId = Integer.parseInt(itemsTable.getModel().getValueAt(selectedRow, 0).toString());
            int quantity = Integer.parseInt(quantityField.getText());
            this.itemService.sellItem(itemId, quantity);

            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String currentdate = currentDate.format(formatter);
            this.transactionService.addTransaction(new Transaction(-1,
                    ApplicationState.getLoggedInUSer().getUserId(),
                    "Sale",
                    Date.valueOf(currentdate),
                    itemId,
                    quantity,
                    -1));
            loadItems();
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

}
