package dva;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ConferenciaController implements Initializable {

    @FXML
    private Button btnExecutar, btnEstoque, btnConferencia;
    @FXML
    private TextField txtCodBarra, txtQuantidade;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnExecutar.setOnAction(event -> btnExecutarAction());
        btnEstoque.setOnAction(event -> btnEstoqueAction());
        btnConferencia.setOnAction(event -> btnConferenciaAction());
    }

    private void btnExecutarAction() {}
    private void btnEstoqueAction() {}
    private void btnConferenciaAction() {}
}