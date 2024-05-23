package presentation;

import businessLayer.ProductBLL;
import dataAcessLayer.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Client;
import model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * The ControllerProductOperations class is a controller for the product operations view.
 * It includes methods for initializing the view, handling button actions, and changing scenes.
 */
public class ControllerProductOperations implements Initializable {
    @FXML
    private Button goBackButton;
    @FXML
    private TableView<Product> tableProducts;
    @FXML
    private TableColumn<Client, Integer> productIdTableColumn;
    @FXML
    private TableColumn<Client, String> nameTableColumn;
    @FXML
    private TableColumn<Client, String> quantityTableColumn;
    @FXML
    private TableColumn<Client, String> descriptionTableColumn;
    @FXML
    private TableColumn<Client, Integer> priceTableColumn;
    @FXML
    private TextField editNameField;
    @FXML
    private TextField editDescriptionField;
    @FXML
    private TextField editPriceField;
    @FXML
    private TextField editQuantityField;

    private ObservableList<Product> productsList;

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
        } catch (Exception e) {
            e.printStackTrace();
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
     * Handles the action of the add product button.
     * It adds a product to the database based on the input fields.
     */
    @FXML
    void addProductButtonOnAction() {
        if (areFieldsEmpty()) {
            showAlert("Empty Fields", "Please fill all the fields before adding a product.");
            return;
        }
        String name = editNameField.getText().trim();
        String description = editDescriptionField.getText().trim();
        double price = Double.parseDouble(editPriceField.getText().trim());
        int quantity = Integer.parseInt(editQuantityField.getText().trim());

        Product product = new Product(0, name, description, price, quantity);

        try {
            new ProductBLL().validate(product);
            ProductDAO productDAO = new ProductDAO();
            productDAO.insertProduct(product);
            fetchProductFromDatabase();
            clearEditFields();

            System.out.println("Product Insertion success");
        } catch (IllegalArgumentException e) {
            showAlert("Validation Error", e.getMessage());
        }
    }

    /**
     * Handles the action of the delete product button.
     * It deletes the selected product from the database.
     */
    @FXML
    void deleteProductButtonOnAction() {
        Product selectedProduct = tableProducts.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            ProductDAO productDAO = new ProductDAO();
            productDAO.delete(selectedProduct);
            fetchProductFromDatabase();

            System.out.println("Product Delete success");
        } else {
            showAlert("Select product", "Please select a product to delete.");
        }
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityTableColumn.setCellValueFactory(new PropertyValueFactory<>("stock_quantity"));
        fetchProductFromDatabase();
    }

    /**
     * Fetches all products from the database and updates the product list.
     */
    private void fetchProductFromDatabase() {
        ProductDAO productDAO = new ProductDAO();
        productsList = FXCollections.observableArrayList(productDAO.getAllProducts());
        tableProducts.setItems(productsList);
    }

    /**
     * Handles the action of the update product button.
     * It updates the selected product's information in the database.
     */
    @FXML
    void updateProductButtonOnAction() {
        Product selectedProduct = tableProducts.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            String updatedName = editNameField.getText().trim();
            String updatedDescription = editDescriptionField.getText().trim();
            double updatedPrice = editPriceField.getText().isEmpty() ? selectedProduct.getPrice() : Double.parseDouble(editPriceField.getText().trim());
            int updatedQuantity = editQuantityField.getText().isEmpty() ? selectedProduct.getStock_quantity() : Integer.parseInt(editQuantityField.getText().trim());

            if (!updatedName.isEmpty() || !updatedDescription.isEmpty() || !editPriceField.getText().isEmpty() || !editQuantityField.getText().isEmpty()) {
                Product updatedProduct = new Product(selectedProduct.getProduct_id(),
                        updatedName.isEmpty() ? selectedProduct.getName() : updatedName,
                        updatedDescription.isEmpty() ? selectedProduct.getDescription() : updatedDescription,
                        updatedPrice,
                        updatedQuantity
                );
                try {
                    new ProductBLL().validate(updatedProduct);
                    ProductDAO productDAO = new ProductDAO();
                    productDAO.updateProduct(updatedProduct);
                    fetchProductFromDatabase();
                    clearEditFields();

                    System.out.println("Product Update success");
                } catch (IllegalArgumentException e) {
                    showAlert("Validation Error", e.getMessage());
                }
            } else {
                showAlert("No Changes", "No changes made to update.");
            }
        } else {
            showAlert("Select Product", "Please select a product to update.");
        }
    }

    /**
     * Clears the edit fields.
     */
    private void clearEditFields() {
        editNameField.clear();
        editDescriptionField.clear();
        editPriceField.clear();
        editQuantityField.clear();
    }

    /**
     * Checks if the input fields are empty.
     *
     * @return true if any of the fields are empty, false otherwise
     */
    private boolean areFieldsEmpty() {
        return editNameField.getText().trim().isEmpty() ||
                editDescriptionField.getText().trim().isEmpty() ||
                editPriceField.getText().trim().isEmpty() ||
                editQuantityField.getText().trim().isEmpty();
    }

    /**
     * Shows an alert with the specified title and content.
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
}
