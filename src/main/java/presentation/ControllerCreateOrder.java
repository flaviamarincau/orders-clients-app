package presentation;

import dataAcessLayer.ClientDAO;
import dataAcessLayer.OrderDAO;
import dataAcessLayer.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Client;
import model.Orders;
import model.Product;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * The ControllerCreateOrder class is a controller for the create order view.
 * It includes methods for initializing the view, handling button actions, and changing scenes.
 */
public class ControllerCreateOrder {
    @FXML
    private ComboBox<Product> productComboBox;
    @FXML
    private ComboBox<Client> clientComboBox;
    @FXML
    private TextField quantityField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button goBackButton;

    /**
     * The ControllerCreateOrder class is a controller for the create order view.
     * It includes methods for initializing the view, handling button actions, and changing scenes.
     */
    @FXML
    public void initialize() {
        populateClients();
        populateProducts();
    }

    /**
     * Populates the client combo box with all clients from the database.
     */
    private void populateClients() {
        ClientDAO clientDAO = new ClientDAO();
        List<Client> clients = clientDAO.getAllClients();
        ObservableList<Client> observableList = FXCollections.observableArrayList(clients);
        clientComboBox.setItems(observableList);
    }

    /**
     * Populates the product combo box with all products from the database.
     */
    private void populateProducts() {
        ProductDAO productDAO = new ProductDAO();
        List<Product> products = productDAO.getAllProducts();
        ObservableList<Product> observableList = FXCollections.observableArrayList(products);
        productComboBox.setItems(observableList);
    }

    /**
     * Handles the action of the finalize order button.
     * It finalizes the order based on the selected client, product, and quantity.
     */
    @FXML
    public void finalizeOrderButtonOnAction() {
        if (clientComboBox.getValue() == null || productComboBox.getValue() == null || quantityField.getText().trim().isEmpty()) {
            errorLabel.setText("Please select both a client and a product and enter a quantity");
            return;
        }

        Client selectedClient = clientComboBox.getValue();
        Product selectedProduct = productComboBox.getValue();

        if (selectedClient == null || selectedProduct == null) {
            errorLabel.setText("Please select a valid client and product");
            return;
        }

        int desiredQuantity;
        try {
            desiredQuantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            errorLabel.setText("Quantity must be a valid integer");
            return;
        }

        if (desiredQuantity <= 0) {
            errorLabel.setText("Quantity must be greater than 0");
            return;
        }

        if (selectedProduct.getStock_quantity() < desiredQuantity || selectedProduct.getStock_quantity() == 0) {
            errorLabel.setText("Not enough stock or the quantity is not correct");
            return;
        }
        ProductDAO productDAO = new ProductDAO();
        productDAO.updateProductStock(selectedProduct, selectedProduct.getStock_quantity() - desiredQuantity);
        createOrder();
        errorLabel.setText("Order finalized");
    }

    private void createOrder() {
        Orders orders = new Orders();
        orders.setClient_id(clientComboBox.getValue().getClient_id());
        orders.setProduct_id(productComboBox.getValue().getProduct_id());
        orders.setQuantity(Integer.parseInt(quantityField.getText()));
        orders.setOrder_date(new Date());

        OrderDAO orderDAO = new OrderDAO();
        orderDAO.insertOrder(orders);
    }

    /**
     * Changes the scene to the specified fxml file and sets the title of the stage.
     *
     * @param fxml the fxml file to change to
     * @param title the title of the stage
     */
    public void changeScene(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxml));
            Parent root = loader.load();

            Stage stage = (Stage) goBackButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);

            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading scene: " + e.getMessage());
        }
    }

    /**
     * Handles the action of the go back button.
     */
    @FXML
    void goBackButtonOnAction() {
        changeScene("intro.fxml", "Intro");
    }

    /**
     * Handles the action of the see orders button.
     * It changes the scene to the order operations view.
     */
    @FXML
    private void seeOrdersButtonOnAction(){
        changeScene("orderOperations.fxml", "Orders operations");
    }
}