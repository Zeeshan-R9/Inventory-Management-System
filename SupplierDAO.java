import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private Connection connection;

    public SupplierDAO() throws Exception {
        this.connection = ApplicationState.getConnection();
    }

    public boolean addSupplier(Supplier supplier) throws SQLException {
        String query = "INSERT INTO Suppliers (supplier_name, contact_info, address) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getContactNumber());
            statement.setString(3, supplier.getAddress());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteSupplier(int supplierId) throws SQLException {
        String query = "DELETE FROM Suppliers WHERE supplier_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, supplierId);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean updateSupplier(Supplier supplier) throws SQLException {
        String query = "UPDATE Suppliers SET supplier_name = ?, contact_info = ?, address = ? WHERE supplier_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, supplier.getName());
            preparedStatement.setString(2, supplier.getContactNumber());
            preparedStatement.setString(3, supplier.getAddress());
            preparedStatement.setInt(4, supplier.getSupplierId());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    public Supplier getSupplierById(int supplierId) throws SQLException {
        String query = "SELECT * FROM Suppliers WHERE supplier_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, supplierId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Supplier(
                            resultSet.getInt("supplier_id"),
                            resultSet.getString("supplier_name"),
                            resultSet.getString("contact_info"),
                            resultSet.getString("address")
                    );
                }
            }
        }
        return null;
    }

    public Supplier getSupplierByName(String supplierName) throws SQLException {
        String query = "SELECT * FROM Suppliers WHERE supplier_name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, supplierName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Supplier(
                            resultSet.getInt("supplier_id"),
                            resultSet.getString("supplier_name"),
                            resultSet.getString("contact_info"),
                            resultSet.getString("address")
                    );
                }
            }
        }
        return null;
    }

    public List<Supplier> getAllSuppliers() throws SQLException {
        String query = "SELECT * FROM Suppliers";
        List<Supplier> supplierList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Supplier supplier = new Supplier(
                        resultSet.getInt("supplier_id"),
                        resultSet.getString("supplier_name"),
                        resultSet.getString("contact_info"),
                        resultSet.getString("address")
                );
                supplierList.add(supplier);
            }
        }
        return supplierList;
    }
}
