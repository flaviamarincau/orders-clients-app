package presentation;

import businessLayer.ClientBLL;
import dataAcessLayer.ClientDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Client;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * The ControllerClientOperations class is a controller for the client operations view.
 * It includes methods for initializing the view, handling button actions, and changing scenes.
 */
public class ControllerClientOperations implements Initializable {
    @FXML
    private Button goBackButton;
    @FXML
    private TableView<Client> tableClients;
    @FXML
    private TableColumn<Client, Integer> clientIdTableColumn;
    @FXML
    private TableColumn<Client, String> nameTableColumn;
    @FXML
    private TableColumn<Client, String> emailTableColumn;
    @FXML
    private TableColumn<Client, String> phoneNoTableColumn;
    @FXML
    private TableColumn<Client, String> addressTableColumn;
    @FXML
    private TextField editNameField;
    @FXML
    private TextField editPhoneField;
    @FXML
    private TextField editAddressField;
    @FXML
    private TextField editEmailField;
    private ObservableList<Client> clientList;

    /**
     * Changes the scene to the specified fxml file.
     *
     * @param fxml the fxml file to change the scene to
     * @param title the title of the new scene
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action of the go back button.
     *
     * @param event the action event
     */
    @FXML
    void goBackButtonOnAction(ActionEvent event) {
        changeScene("/intro.fxml", "Intro");
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
        clientIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("client_id"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNoTableColumn.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        fetchClientFromDatabase();
    }

    /**
     * Fetches all clients from the database and updates the client list.
     */
    private void fetchClientFromDatabase() {
        ClientDAO clientDAO = new ClientDAO();
        clientList = FXCollections.observableArrayList(clientDAO.getAllClients());
        tableClients.setItems(clientList);
    }

    /**
     * Handles the action of the update client button.
     * It updates the selected client's information in the database.
     */
    @FXML
    void updateClientButtonOnAction() {
        Client selectedClient = tableClients.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            String updatedName = editNameField.getText().trim();
            String updatedEmail = editEmailField.getText().trim();
            String updatedPhone = editPhoneField.getText().trim();
            String updatedAddress = editAddressField.getText().trim();
            if (!updatedName.isEmpty() || !updatedEmail.isEmpty() || !updatedPhone.isEmpty() || !updatedAddress.isEmpty()) {
                Client updatedClient = new Client(selectedClient.getClient_id(),
                        updatedName.isEmpty() ? selectedClient.getName() : updatedName,
                        updatedEmail.isEmpty() ? selectedClient.getEmail() : updatedEmail,
                        updatedPhone.isEmpty() ? selectedClient.getPhone_number() : updatedPhone,
                        updatedAddress.isEmpty() ? selectedClient.getAddress() : updatedAddress
                );
                try {
                    new ClientBLL().validate(updatedClient);
                    ClientDAO clientDAO = new ClientDAO();
                    clientDAO.updateClient(updatedClient);
                    fetchClientFromDatabase();
                    clearEditFields();

                    System.out.println("Client Update success");
                } catch (IllegalArgumentException e) {
                    showAlert("Validation Error", e.getMessage());
                }
            } else {
                showAlert("No Changes", "No changes made to update.");
            }
        } else {
            showAlert("Select Client", "Please select a client to update.");
        }
    }

    /**
     * Clears the edit fields.
     */
    private void clearEditFields() {
         editNameField.clear();
        editPhoneField.clear();
        editAddressField.clear();
        editEmailField.clear();
    }


    /**
     * Handles the action of the add client button.
     * It adds a new client to the database.
     */
    @FXML
    void addClientButtonOnAction() {
        if (areFieldsEmpty()) {
            showAlert("Empty Fields", "Please fill all the fields before adding a client.");
            return;
        }
        String name = editNameField.getText().trim();
        String email = editEmailField.getText().trim();
        String phoneNumber = editPhoneField.getText().trim();
        String address = editAddressField.getText().trim();

        Client client = new Client(0, name, email, phoneNumber, address);

        try {
            new ClientBLL().validate(client);
            ClientDAO clientDAO = new ClientDAO();
            clientDAO.insertClient(client);
            fetchClientFromDatabase();
            clearEditFields();

            System.out.println("Client Insertion success");
        } catch (IllegalArgumentException e) {
            showAlert("Validation Error", e.getMessage());
        }
    }

    /**
     * Handles the action of the delete client button.
     * It deletes the selected client from the database.
     */
    @FXML
    void deleteClientButtonOnAction() {
        Client selectedClient = tableClients.getSelectionModel().getSelectedItem();

        if (selectedClient != null) {
            ClientDAO clientDAO = new ClientDAO();
            clientDAO.delete(selectedClient);
            fetchClientFromDatabase();

            System.out.println("Client Delete success");
        } else {
            showAlert("Select Client", "Please select a client to delete.");
        }
    }

    /**
     * Checks if the edit fields are empty.
     *
     * @return true if the fields are empty, false otherwise
     */
    private boolean areFieldsEmpty() {
        return editNameField.getText().trim().isEmpty() ||
                editEmailField.getText().trim().isEmpty() ||
                editPhoneField.getText().trim().isEmpty() ||
                editAddressField.getText().trim().isEmpty();
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
