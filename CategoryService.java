import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private CategoryDAO categoryDAO;

    public CategoryService() throws Exception {
        this.categoryDAO = new CategoryDAO();
    }

    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.getAllCategories();
    }

    public void addCategory(String categoryName) throws SQLException , InsufficientPermissions {
        if (!(ApplicationState.getLoggedInUSer().getRole().equals(UserRole.ADMIN)))
            throw new InsufficientPermissions("You are not allowed to perform this action!");
        Category category = new Category(0, categoryName); // Assuming ID is auto-generated
        if (!categoryDAO.addCategory(category)) {
            throw new SQLException("Failed to add category.");
        }
    }

    public void updateCategory(int categoryId, String categoryName) throws SQLException , InsufficientPermissions {
        if (!(ApplicationState.getLoggedInUSer().getRole().equals(UserRole.ADMIN)))
            throw new InsufficientPermissions("You are not allowed to perform this action!");
        Category category = new Category(categoryId, categoryName);
        if (!categoryDAO.updateCategory(category)) {
            throw new SQLException("Failed to update category.");
        }
    }

    public Category getCategoryById(int Id) throws SQLException {
        return this.categoryDAO.getCategoryById(Id);
    }

    public Category getCategoryByName(String name) throws SQLException {
        return this.categoryDAO.getCategoryByName(name);
    }

    public void removeCategory(int categoryId) throws SQLException , InsufficientPermissions {
        if (!(ApplicationState.getLoggedInUSer().getRole().equals(UserRole.ADMIN)))
            throw new InsufficientPermissions("You are not allowed to perform this action!");
        if (!categoryDAO.removeCategory(categoryId)) {
            throw new SQLException("Failed to remove category.");
        }
    }
}
