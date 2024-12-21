import java.sql.SQLException;
import java.util.List;

public class ItemService {
    private final ItemDAO itemDAO;

    public ItemService() throws Exception{
        this.itemDAO = new ItemDAO();
    }

    public List<Item> getAllItems() throws SQLException {
        return itemDAO.getAllItems();
    }

    public Item getItemById(int id) throws SQLException{
        return itemDAO.getItemById(id);
    }

    public List<Item> getLowStockItems(int threshold) throws SQLException {
        return itemDAO.getLowStockItems(threshold);
    }


    public void purchaseItem(int itemId, int quantity) throws SQLException {
        Item item = itemDAO.getItemById(itemId);
        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
            itemDAO.updateItem(item);
        } else {
            throw new IllegalArgumentException("Item not found.");
        }
    }

    public void updateItem(Item item) throws Exception , InsufficientPermissions {
        if (!(ApplicationState.getLoggedInUSer().getRole().equals(UserRole.ADMIN)))
            throw new InsufficientPermissions("You are not allowed to perform this action!");
        itemDAO.updateItem(item);
    }

    public void sellItem(int itemId, int quantity) throws SQLException {
        Item item = itemDAO.getItemById(itemId);
        if (item != null) {
            if (item.getQuantity() < quantity) {
                throw new IllegalArgumentException("Insufficient stock.");
            }
            item.setQuantity(item.getQuantity() - quantity);
            itemDAO.updateItem(item);
        } else {
            throw new IllegalArgumentException("Item not found.");
        }
    }


    public List<Item> getItemsByCategory(Category category) throws SQLException {
        return itemDAO.filterItemsByCategory(category);
    }

    public List<Item> getItemsBySupplier(Supplier supplier) throws SQLException {
        return itemDAO.filterItemsBySupplier(supplier);
    }

    public void addItem(Item item) throws SQLException , InsufficientPermissions {
        if (!(ApplicationState.getLoggedInUSer().getRole().equals(UserRole.ADMIN)))
            throw new InsufficientPermissions("You are not allowed to perform this action!");
        itemDAO.addItem(item);
    }

    public void deleteItem(int itemId) throws SQLException , InsufficientPermissions {
        if (!(ApplicationState.getLoggedInUSer().getRole().equals(UserRole.ADMIN)))
            throw new InsufficientPermissions("You are not allowed to perform this action!");
        itemDAO.deleteItem(itemId);
    }
}