package dataAcessLayer;

import connection.ConnectionFactory;
import model.Orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
/**
 * The OrderDAO class provides data access operations for the Orders table in the database.
 * It extends the AbstractDAO class and overrides its methods to perform operations specific to the Orders table.
 */
public class OrderDAO extends AbstractDAO<Orders> {

    /**
     * Retrieves all orders from the Orders table.
     *
     * @return a list of all orders in the Orders table.
     */
    public List<Orders> getAllOrders() {
        return super.findAll();
    }

    /**
     * Inserts a new order into the Orders table.
     *
     * @param order the order to be inserted
     */
    public void insertOrder(Orders order) {
        String query = "INSERT INTO orders (client_id, product_id, quantity, order_date) VALUES (?, ?, ?, ?)";
        super.insert(order, query, order.getClient_id(), order.getProduct_id(), order.getQuantity(), order.getOrder_date());
    }

    /**
     * Updates an order in the Orders table.
     *
     * @param order the order to be updated
     */
    public void updateOrder(Orders order) {
        String query = "UPDATE orders SET client_id = ?, product_id = ?, quantity = ?, order_date = ? WHERE order_id = ?";
        super.update(order, query, order.getClient_id(), order.getProduct_id(), order.getQuantity(), order.getOrder_date(), order.getOrder_id());
    }

    /**
     * Deletes an order from the Orders table.
     *
     * @param order the order to be deleted
     */
    public void delete(Orders order) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM orders WHERE order_id = ?")) {
            statement.setInt(1, order.getOrder_id());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error deleting record from orders" , e);
        }
    }
}