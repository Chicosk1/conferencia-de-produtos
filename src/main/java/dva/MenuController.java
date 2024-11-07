package dva;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private Button btnExecutar, btnEstoque, btnConferencia;
    @FXML
    private TextField txtCodBarra;
    @FXML
    private TextField txtQuantidade;
    GridPane

    int teste;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnExecutar.setOnAction(event -> btnExecutarAction());
        btnEstoque.setOnAction(event -> btnEstoqueAction());
        btnConferencia.setOnAction(event -> btnConferenciaAction());
        teste = Integer.parseInt(txtCodBarra.getText());
        teste = Integer.parseInt(txtQuantidade.getText());
    }
    private void btnExecutarAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Produtos.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Produtos");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void btnEstoqueAction() {}
    private void btnConferenciaAction() {}
}