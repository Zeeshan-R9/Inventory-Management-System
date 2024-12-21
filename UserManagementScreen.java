import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class UserManagementScreen extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField userIdField, usernameField, passwordField, roleField;
    private JButton addButton, updateButton, deleteButton;
    private UserService userService;

    public UserManagementScreen() {
        try {
            userService = new UserService();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error initializing UserService: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        userIdField = new JTextField();
        userIdField.setEditable(false);
        usernameField = new JTextField();
        passwordField = new JTextField();
        roleField = new JTextField();

        topPanel.add(new JLabel("User ID:"));
        topPanel.add(userIdField);
        topPanel.add(new JLabel("Username:"));
        topPanel.add(usernameField);
        topPanel.add(new JLabel("Password:"));
        topPanel.add(passwordField);
        topPanel.add(new JLabel("Role:"));
        topPanel.add(roleField);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Username", "Password", "Role"}, 0);
        userTable = new JTable(tableModel);
        loadUsers();
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && userTable.getSelectedRow() != -1) {
                int selectedRow = userTable.getSelectedRow();
                userIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                usernameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                passwordField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                roleField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            }
        });

        add(new JScrollPane(userTable), BorderLayout.CENTER);

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
                addUser();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
    }

    private void loadUsers() {
        try {
            tableModel.setRowCount(0);
            List<User> users = userService.getAllUsers();
            for (User user : users) {
                tableModel.addRow(new Object[]{user.getUserId(), user.getUsername(), user.getPassword(), user.getRole()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addUser() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleField.getText().trim();
        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            User user = new User(0, username, password, role);
            userService.addUser(user);
            loadUsers();
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        String userIdText = userIdField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleField.getText().trim();
        if (userIdText.isEmpty() || username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user and fill all fields.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int userId = Integer.parseInt(userIdText);
            User user = new User(userId, username, password, role);
            userService.updateUser(user);
            loadUsers();
            clearForm();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        String userIdText = userIdField.getText().trim();
        if (userIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int userId = Integer.parseInt(userIdText);
            userService.deleteUser(userId);
            loadUsers();
            clearForm();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        userIdField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        roleField.setText("");
    }
}
