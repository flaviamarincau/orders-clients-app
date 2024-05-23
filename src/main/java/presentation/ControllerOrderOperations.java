package presentation;

import businessLayer.OrderBLL;
import dataAcessLayer.OrderDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Orders;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
/**
 * The ControllerOrderOperations class is a controller for the order operations view.
 * It includes methods for handling button actions, changing scenes, and interacting with the Orders table in the database.
 */
public class ControllerOrderOperations implements Initializable {

    @FXML
    private TableView<Orders> tableOrders;
    @FXML
    private TableColumn<Orders, Integer> orderIdTableColumn;
    @FXML
    private TableColumn<Orders, Integer> clientIdTableColumn;
    @FXML
    private TableColumn<Orders, Integer> productIdTableColumn;
    @FXML
    private TableColumn<Orders, Integer> quantityTableColumn;
    @FXML
    private TableColumn<Orders, Date> orderDateTableColumn;
    @FXML
    private DatePicker editDateDatePicker;
    @FXML
    private Button goBackButton;
    @FXML
    private Label errorLabel;

    private ObservableList<Orders> ordersList;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orderIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        clientIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("client_id"));
        productIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        quantityTableColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("order_date"));
        fetchOrdersFromDatabase();
    }

    /**
     * Fetches all orders from the database and sets them in the orders table.
     */
    private void fetchOrdersFromDatabase() {
        OrderDAO orderDAO = new OrderDAO();
        ordersList = FXCollections.observableArrayList(orderDAO.getAllOrders());
        tableOrders.setItems(ordersList);
    }

    /**
     * Handles the action of the go back button.
     * It changes the scene to the create product order view.
     *
     * @param event the action event
     */
    @FXML
    void goBackButtonOnAction(ActionEvent event) {
        changeScene("/createProductOrder.fxml", "Intro");
    }

    /**
     * Handles the action of the add order button.
     * It changes the scene to the create product order view.
     */
    @FXML
    void addOrderButtonOnAction() {
       changeScene("/createProductOrder.fxml", "Create Order");
    }

    /**
     * Handles the action of the update order button.
     * It updates the selected order's information in the database.
     */
    @FXML
    void updateOrderButtonOnAction() {
        Orders selectedOrders = tableOrders.getSelectionModel().getSelectedItem();
        if (selectedOrders != null) {
            LocalDate localDate = editDateDatePicker.getValue();
            if (localDate != null && localDate.isAfter(LocalDate.now())) {
                errorLabel.setText("The date cannot be later than the current date.");
                return;
            }
            Date updatedOrderDate = localDate == null ? selectedOrders.getOrder_date() : java.sql.Date.valueOf(localDate);

            Orders updatedOrders = new Orders(selectedOrders.getOrder_id(), selectedOrders.getClient_id(), selectedOrders.getProduct_id(), selectedOrders.getQuantity(), updatedOrderDate);

            try {
                new OrderBLL().validate(updatedOrders);
                OrderDAO orderDAO = new OrderDAO();
                orderDAO.updateOrder(updatedOrders);
                fetchOrdersFromDatabase();
                clearEditFields();

                System.out.println("Orders Update success");
            } catch (IllegalArgumentException e) {
                showAlert("Validation Error", e.getMessage());
            }
        } else {
            showAlert("Select Orders", "Please select an order to update.");
        }
    }

    /**
     * Handles the action of the delete order button.
     * It deletes the selected order from the database.
     */
    @FXML
    void deleteOrderButtonOnAction() {
        Orders selectedOrders = tableOrders.getSelectionModel().getSelectedItem();

        if (selectedOrders != null) {
            OrderDAO orderDAO = new OrderDAO();
            orderDAO.delete(selectedOrders);
            fetchOrdersFromDatabase();

            System.out.println("Orders Delete success");
        } else {
            showAlert("Select Orders", "Please select an order to delete.");
        }
    }

    /**
     * Clears the edit fields.
     */
    private void clearEditFields() {
        editDateDatePicker.setValue(null);
    }

    /**
     * Shows an alert with the given title and content.
     *
     * @param title the title of the alert
     * @param content the content of the alert
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Changes the scene to the specified fxml file with the given title.
     *
     * @param fxml the fxml file to change to
     * @param title the title of the scene
     */
    public void changeScene(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
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
}