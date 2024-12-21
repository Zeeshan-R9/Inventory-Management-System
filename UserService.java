import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    public UserService() throws Exception {
        this.userDAO = new UserDAO();
    }

    public boolean authenticate(String username, String password, String selectedRole) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        return user != null && user.getPassword().equals(password) && user.getRole().equals(selectedRole);
    }

    public boolean addUser(User user) throws SQLException {
        return userDAO.addUser(user);
    }

    public boolean updateUser(User user) throws SQLException {
        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int userId) throws SQLException {
        return userDAO.deleteUser(userId);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    public User getUserById(int id) throws SQLException{
        return userDAO.getUserById(id);
    }

}
