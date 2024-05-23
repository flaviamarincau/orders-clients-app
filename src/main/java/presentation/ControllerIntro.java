package presentation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
/**
 * The ControllerIntro class is a controller for the intro view.
 * It includes methods for handling button actions and changing scenes.
 */
public class ControllerIntro {
    @FXML
    private Button clientOperationsButton;

    /**
     * Handles the action of the client operations button.
     * It changes the scene to the client operations view.
     */
    @FXML
    public void clientOperationsButtonOnAction() {
        changeScene("clientOperations.fxml", "Client operations");
    }

    /**
     * Handles the action of the product operations button.
     * It changes the scene to the product operations view.
     */
    @FXML
    public void productOperationsButtonOnAction() {
        changeScene("productOperations.fxml", "Product operations");
    }

    /**
     * Handles the action of the create product order button.
     * It changes the scene to the create product order view.
     */
    @FXML
    public void createProductOrderButtonOnAction() {
        changeScene("createProductOrder.fxml", "Create product order");
    }

    /**
     * Changes the scene to the specified fxml file and sets the title of the stage.
     *
     * @param fxml the fxml file to change to
     * @param title the title of the stage
     */
    public void changeScene(String fxml, String title) {
        try {
            String resourcePath = "/" + fxml;
            System.out.println("Loading FXML from path: " + resourcePath);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            if (loader == null) {
                throw new IOException("FXML resource not found: " + resourcePath);
            }
            Parent root = loader.load();
            Stage stage = (Stage) clientOperationsButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
