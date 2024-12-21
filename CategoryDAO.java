import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private Connection connection;

    public CategoryDAO() throws Exception {
        this.connection = ApplicationState.getConnection();
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM Categories";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Category category = new Category(
                        resultSet.getInt("category_id"),
                        resultSet.getString("category_name")
                );
                categories.add(category);
            }
        }
        return categories;
    }

    public boolean addCategory(Category category) throws SQLException {
        String query = "INSERT INTO Categories (category_name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category.getName());
            return statement.executeUpdate() > 0;
        }
    }

    public Category getCategoryById(int Id) throws SQLException {
        String query = "SELECT * FROM Categories WHERE category_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, Id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Category(
                            resultSet.getInt("category_id"),
                            resultSet.getString("category_name")
                    );
                }
            }
        }
        return null;
    }

    public Category getCategoryByName(String name) throws SQLException {
        String query = "SELECT * FROM Categories WHERE category_name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Category(
                            resultSet.getInt("category_id"),
                            resultSet.getString("category_name")
                    );
                }
            }
        }
        return null;
    }

    public boolean updateCategory(Category category) throws SQLException {
        String query = "UPDATE Categories SET category_name = ? WHERE category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category.getName());
            statement.setInt(2, category.getCategoryId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean removeCategory(int categoryId) throws SQLException {
        String query = "DELETE FROM Categories WHERE category_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            return statement.executeUpdate() > 0;
        }
    }
}
