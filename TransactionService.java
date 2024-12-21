import java.sql.SQLException;
import java.util.List;

public class TransactionService {
    private final TransactionDAO transactionDAO;

    public TransactionService() throws Exception{
        this.transactionDAO = new TransactionDAO();
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        return transactionDAO.getAllTransactions();
    }


    public void addTransaction(Transaction transaction) throws SQLException {
        transactionDAO.addTransaction(transaction);
    }

    public void updateTransaction(Transaction transaction) throws SQLException , InsufficientPermissions {
        if (!(ApplicationState.getLoggedInUSer().getRole().equals(UserRole.ADMIN)))
            throw new InsufficientPermissions("You are not allowed to perform this action!");
        transactionDAO.updateTransaction(transaction);
    }

    public void deleteTransaction(int transactionId) throws SQLException, InsufficientPermissions {
        if (!(ApplicationState.getLoggedInUSer().getRole().equals(UserRole.ADMIN)))
            throw new InsufficientPermissions("You are not allowed to perform this action!");
        transactionDAO.deleteTransaction(transactionId);
    }
}
