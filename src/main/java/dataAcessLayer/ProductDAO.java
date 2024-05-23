package dataAcessLayer;

import connection.ConnectionFactory;
import model.Product;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
/**
 * The ProductDAO class provides data access object implementation for Product objects.
 * It includes methods for common database operations such as insert, update, delete, and find.
 */
public class ProductDAO extends AbstractDAO<Product> {
    /**
     * Retrieves all products from the database.
     *
     * @return a list of all products
     */
    public List<Product> getAllProducts() {
        return super.findAll();
    }

    /**
     * Inserts a new product into the database.
     *
     * @param product the product to be inserted
     */
    public void insertProduct(Product product) {
        String query = "INSERT INTO product (name, description, price, stock_quantity) VALUES (?, ?, ?, ?)";
        super.insert(product, query, product.getName(), product.getDescription(), product.getPrice(), product.getStock_quantity());
    }

    /**
     * Updates a product in the database.
     *
     * @param product the product to be updated
     */
    public void updateProduct(Product product) {
        String query = "UPDATE product SET name = ?, description = ?, price = ?, stock_quantity = ? WHERE product_id = ?";
        super.update(product, query, product.getName(), product.getDescription(), product.getPrice(), product.getStock_quantity(), product.getProduct_id());
    }

    /**
     * Deletes a product from the database by its id.
     *
     * @param product the product to be deleted
     */
    public void delete(Product product) {
        super.delete(product.getProduct_id(), "product");
    }

    /**
     * Updates the stock quantity of a product in the database.
     *
     * @param selectedProduct the product to be updated
     * @param newStockQuantity the new stock quantity
     */
    public void updateProductStock(Product selectedProduct, int newStockQuantity) {
        String query = "UPDATE product SET stock_quantity = ? WHERE product_id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newStockQuantity);
            statement.setInt(2, selectedProduct.getProduct_id());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO:updateProductStock " + e.getMessage());
        }
    }
}