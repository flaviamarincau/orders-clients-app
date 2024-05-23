package dataAcessLayer;

import connection.ConnectionFactory;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * The AbstractDAO class provides a generic data access object implementation.
 * It includes methods for common database operations such as insert, update, delete, and find.
 *
 * @param <T> the type of the object mapped to a database table
 */
public class AbstractDAO<T> {

    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;
    private final String tableName;

    /**
     * Constructs an AbstractDAO for the given type.
     * The table name is determined by the simple name of the class in lowercase.
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.tableName = type.getSimpleName().toLowerCase();
    }

    /**
     * Inserts a new record into the database.
     *
     * @param t the object to be inserted
     * @param query the SQL query to be executed
     * @param values the values to be inserted
     */
    public void insert(T t, String query, Object... values) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            for (int i = 0; i < values.length; i++) {
                statement.setObject(i + 1, values[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getSimpleName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Retrieves all records from the database.
     *
     * @return a list of all records
     */
    public List<T> findAll() {
        List<T> entities = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                entities.add(createObject(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error fetching all records from " + tableName, e);
        }
        return entities;
    }

    /**
     * Retrieves a record by its id.
     *
     * @param id the id of the record
     * @param tableName the name of the table
     * @return the record with the given id
     */
    public T findById(int id, String tableName) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE id = ?")
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createObject(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error finding record by id in " + tableName, e);
        }
        return null;
    }

    /**
     * Deletes a record by its id.
     *
     * @param id the id of the record
     * @param tableName the name of the table
     */
    public void delete(int id, String tableName) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE "+tableName+"_id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error deleting record from " + tableName, e);
        }
    }

    /**
     * Creates an object from a ResultSet.
     *
     * @param resultSet the ResultSet
     * @return the created object
     * @throws SQLException if a database access error occurs
     */
    private T createObject(ResultSet resultSet) throws SQLException {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance = constructor.newInstance();
            for (Field field : type.getDeclaredFields()) {
                String fieldName = field.getName();
                Object value = resultSet.getObject(fieldName);
                if (value != null) {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method setter = propertyDescriptor.getWriteMethod();
                    setter.invoke(instance, value);
                }
            }
            return instance;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | IllegalArgumentException | SecurityException | SQLException e) {
            LOGGER.log(Level.WARNING, "Error creating object from ResultSet for " + tableName, e);
            throw new SQLException("Error creating object from ResultSet for " + tableName, e);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a record in the database.
     *
     * @param t the object to be updated
     * @param query the SQL query to be executed
     * @param values the new values
     */
    public void update(T t, String query, Object... values) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            for (int i = 0; i < values.length; i++) {
                statement.setObject(i + 1, values[i]);
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getSimpleName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

}
