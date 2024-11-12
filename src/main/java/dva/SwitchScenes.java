package dva;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class SwitchScenes {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void irParaEstoque(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Estoque.fxml"));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void irParaConferencia(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Estoque.fxml"));
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
