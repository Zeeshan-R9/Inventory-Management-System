import java.sql.SQLException;
import java.util.List;

public class SupplierService {
    private SupplierDAO supplierDAO;

    public SupplierService() throws Exception{
        this.supplierDAO = new SupplierDAO();
    }

    public Supplier getSupplierById(int Id) throws Exception {
        return this.supplierDAO.getSupplierById(Id);
    }

    public Supplier getSupplierByName(String name) throws Exception {
        return this.supplierDAO.getSupplierByName(name);
    }

    public void updateSupplier(int id, String name, String contactInfo, String address) throws SQLException , InsufficientPermissions {
        if (!(ApplicationState.getLoggedInUSer().getRole().equals(UserRole.ADMIN)))
            throw new InsufficientPermissions("You are not allowed to perform this action!");
        Supplier existingSupplier = supplierDAO.getSupplierById(id);
        if (existingSupplier == null) {
            throw new SQLException("Supplier with ID " + id + " does not exist.");
        }

        existingSupplier.setName(name);
        existingSupplier.setContactNumber(contactInfo);
        existingSupplier.setAddress(address);

        boolean isUpdated = supplierDAO.updateSupplier(existingSupplier);
        if (!isUpdated) {
            throw new SQLException("Failed to update supplier with ID " + id);
        }
    }

    public List<Supplier> getAllSuppliers() throws SQLException {
        return supplierDAO.getAllSuppliers();
    }

    public void addSupplier(String name, String contactNo, String supplierAddress) throws SQLException , InsufficientPermissions {
        if (!(ApplicationState.getLoggedInUSer().getRole().equals(UserRole.ADMIN)))
            throw new InsufficientPermissions("You are not allowed to perform this action!");
        supplierDAO.addSupplier(new Supplier(-1, name, contactNo, supplierAddress));
    }

    public void deleteSupplier(int supplierId) throws SQLException , InsufficientPermissions {
        if (!(ApplicationState.getLoggedInUSer().getRole().equals(UserRole.ADMIN)))
            throw new InsufficientPermissions("You are not allowed to perform this action!");
        supplierDAO.deleteSupplier(supplierId);
    }

}
