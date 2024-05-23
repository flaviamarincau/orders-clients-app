package dataAcessLayer;

import connection.ConnectionFactory;
import model.Client;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
/**
 * The ClientDAO class provides data access object implementation for Client objects.
 * It includes methods for common database operations such as insert, update, delete, and find.
 */
public class ClientDAO extends AbstractDAO<Client> {

    /**
     * Retrieves all clients from the database.
     *
     * @return a list of all clients
     */
    public List<Client> getAllClients() {
        return super.findAll();
    }

    /**
     * Updates a client in the database.
     *
     * @param client the client to be updated
     */
    public void updateClient(Client client) {
        String query = "UPDATE client SET name = ?, email = ?, phone_number = ?, address = ? WHERE client_id = ?";
        super.update(client, query, client.getName(), client.getEmail(), client.getPhone_number(), client.getAddress(), client.getClient_id());
    }

    /**
     * Deletes a client from the database by its id.
     *
     * @param client the client to be deleted
     */
    public void delete(Client client) {
        super.delete(client.getClient_id(), "client");
    }

    /**
     * Inserts a new client into the database.
     *
     * @param client the client to be inserted
     */
    public void insertClient(Client client) {
        String query = "INSERT INTO client (name, email, phone_number, address) VALUES (?, ?, ?, ?)";
        super.insert(client, query, client.getName(), client.getEmail(), client.getPhone_number(), client.getAddress());
    }
}