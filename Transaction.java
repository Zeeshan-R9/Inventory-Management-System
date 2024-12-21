import java.sql.Date;

public class Transaction {
    private int transactionId;
    private String type;
    private Date date;
    private int itemId;
    private int quantity;
    private double totalAmount;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int userId;

    public Transaction(int transactionId, int userID, String type, Date date, int itemId, int quantity, double totalAmount) {
        this.transactionId = transactionId;
        this.userId = userID;
        this.type = type;
        this.date = date;
        this.itemId = itemId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }


    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
