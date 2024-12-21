import javax.swing.*;
import java.awt.*;

public class MainDashboardScreen extends JFrame {
    private JPanel contentPanel;

    public MainDashboardScreen() {
        setTitle("Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton itemsButton = new JButton("Manage Items");
        JButton suppliersButton = new JButton("Manage Suppliers");
        JButton categoriesButton = new JButton("Manage Categories");
        JButton transactionsButton = new JButton("Manage Transactions");
        JButton usersButton = new JButton("Manager Users");
        JButton logoutButton = new JButton("Logout");

        headerPanel.add(new JLabel("User: " + ApplicationState.getLoggedInUSer().getUsername()));
        buttonPanel.add(itemsButton);
        buttonPanel.add(suppliersButton);
        buttonPanel.add(categoriesButton);
        buttonPanel.add(transactionsButton);
        buttonPanel.add(usersButton);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        if (!(ApplicationState.getLoggedInUSer().getRole().equals(UserRole.ADMIN)))
            usersButton.setEnabled(false);

        contentPanel = new JPanel(new CardLayout());
        contentPanel.add(new ItemManagementScreen(), "ItemManagement");
        contentPanel.add(new SupplierManagementScreen(), "SupplierManagement");
        contentPanel.add(new CategoryManagementScreen(), "CategoryManagement");
        contentPanel.add(new UserManagementScreen(), "UserManagement");
        contentPanel.add(new TransactionManagementScreen(), "TransactionManagement");

        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);

        itemsButton.addActionListener(e -> showPanel("ItemManagement"));
        usersButton.addActionListener(e -> showPanel("UserManagement"));
        transactionsButton.addActionListener(e -> showPanel("TransactionManagement"));
        suppliersButton.addActionListener(e -> showPanel("SupplierManagement"));
        categoriesButton.addActionListener(e -> showPanel("CategoryManagement"));
        logoutButton.addActionListener(e-> logout());
    }

    private void logout() {
        ApplicationState.setLoggedInUSer(null);
        this.dispose();
        new LoginScreen().setVisible(true);
    }

    private void showPanel(String panelName) {
        CardLayout layout = (CardLayout) contentPanel.getLayout();
        layout.show(contentPanel, panelName);
    }
}
