// Updated TransactionDAO
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private final Connection connection;

    public TransactionDAO() throws Exception {
        this.connection = ApplicationState.getConnection();
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        String query = "SELECT * FROM Transactions";
        List<Transaction> transactions = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Transaction transaction = mapResultSetToTransaction(resultSet);
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    private Transaction mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        int transactionId = resultSet.getInt("transaction_id");
        int userId = resultSet.getInt("user_id");
        String type = resultSet.getString("transaction_type");
        Date date = resultSet.getDate("transaction_date");
        int itemId = resultSet.getInt("item_id");
        int quantity = resultSet.getInt("quantity");
        double totalAmount = calculateTotalAmount(itemId, quantity);

        return new Transaction(transactionId, userId, type, date, itemId, quantity, totalAmount);
    }

    private double calculateTotalAmount(int itemId, int quantity) throws SQLException {
        String query = "SELECT price FROM Items WHERE item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double price = resultSet.getDouble("price");
                    return price * quantity;
                }
            }
        }
        return 0.0;
    }

    public boolean addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO Transactions (item_id, user_id, transaction_type, quantity, transaction_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transaction.getItemId());
            statement.setInt(2, transaction.getUserId());
            statement.setString(3, transaction.getType());
            statement.setInt(4, transaction.getQuantity());
            statement.setTimestamp(5, new java.sql.Timestamp(transaction.getDate().getTime()));
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean updateTransaction(Transaction transaction) throws SQLException {
        String sql = "UPDATE Transactions SET item_id = ?, user_id = ?, transaction_type = ?, quantity = ?, transaction_date = ? WHERE transaction_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transaction.getItemId());
            statement.setInt(2, transaction.getUserId());
            statement.setString(3, transaction.getType());
            statement.setInt(4, transaction.getQuantity());
            statement.setTimestamp(5, new java.sql.Timestamp(transaction.getDate().getTime()));
            statement.setInt(6, transaction.getTransactionId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteTransaction(int transactionId) throws SQLException {
        String sql = "DELETE FROM Transactions WHERE transaction_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transactionId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
