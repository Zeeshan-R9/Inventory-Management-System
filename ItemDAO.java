// Updated ItemDAO
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    private Connection connection;

    public ItemDAO() throws Exception {
        this.connection = ApplicationState.getConnection();
    }

    public boolean addItem(Item item) throws SQLException {
        String query = "INSERT INTO items (item_name, category_id, supplier_id, quantity, price) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, item.getName());
            statement.setInt(2, item.getCategoryId());
            statement.setInt(3, item.getSupplierId());
            statement.setInt(4, item.getQuantity());
            statement.setDouble(5, item.getPrice());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean updateItem(Item item) throws SQLException {
        String query = "UPDATE items SET item_name = ?, category_id = ?, supplier_id = ?, quantity = ?, price = ? WHERE item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, item.getName());
            statement.setInt(2, item.getCategoryId());
            statement.setInt(3, item.getSupplierId());
            statement.setInt(4, item.getQuantity());
            statement.setDouble(5, item.getPrice());
            statement.setInt(6, item.getItemId());
            return statement.executeUpdate() > 0;
        }
    }

    public List<Item> getAllItems() throws SQLException {
        String query = "SELECT * FROM items";
        List<Item> itemList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Item item = mapResultSetToItem(resultSet);
                itemList.add(item);
            }
        }
        return itemList;
    }

    public List<Item> getLowStockItems(int threshold) throws SQLException {
        String query = "SELECT * FROM items WHERE quantity < ?";
        List<Item> itemList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, threshold);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = mapResultSetToItem(resultSet);
                    itemList.add(item);
                }
            }
        }
        return itemList;
    }

    public Item getItemById(int itemId) throws SQLException {
        String query = "SELECT * FROM items WHERE item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToItem(resultSet);
                }
            }
        }
        return null;
    }

    public boolean deleteItem(int itemId) throws SQLException {
        String query = "DELETE FROM items WHERE item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            return statement.executeUpdate() > 0;
        }
    }

    public List<Item> filterItemsBySupplier(Supplier supplier) throws SQLException {
        String query = "SELECT * FROM items WHERE supplier_id = ?";
        List<Item> itemList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, supplier.getSupplierId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = mapResultSetToItem(resultSet);
                    itemList.add(item);
                }
            }
        }
        return itemList;
    }

    public List<Item> filterItemsByCategory(Category category) throws SQLException {
        String query = "SELECT * FROM items WHERE category_id = ?";
        List<Item> itemList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, category.getCategoryId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = mapResultSetToItem(resultSet);
                    itemList.add(item);
                }
            }
        }
        return itemList;
    }

    private Item mapResultSetToItem(ResultSet resultSet) throws SQLException {
        return new Item(
                resultSet.getInt("item_id"),
                resultSet.getString("item_name"),
                resultSet.getInt("quantity"),
                resultSet.getDouble("price"),
                resultSet.getInt("category_id"),
                resultSet.getInt("supplier_id")
        );
    }
}
