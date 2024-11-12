package dva;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ConferenciaController implements Initializable {

    @FXML
    private Button btnExecutar, btnEstoque;
    @FXML
    private TextField txtCodBarra, txtQuantidade;
    @FXML
    private ListView<String> listaConferencia;

    private List<Produto> listaDeProdutos = new ArrayList<>();
    private Stage stage;

    public ConferenciaController() {
        listaDeProdutos.add(new Produto("123", "SAPATO", 20.99, 2));
        listaDeProdutos.add(new Produto("234", "CHINELOS", 19.99, 3));
        listaDeProdutos.add(new Produto("456", "BOTA", 5.00, 4));
        listaDeProdutos.add(new Produto("567", "TENIS", 7.00, 7));
        listaDeProdutos.add(new Produto("678", "MEIA", 3.99, 1));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaConferencia.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    if (item.contains("(Saldo insuficiente)")) {
                        setStyle("-fx-text-fill: red;");
                    } else if (item.contains("(Saldo disponível)")) {
                        setStyle("-fx-text-fill: blue;");
                    } else {
                        setStyle("");
                    }
                    setText(item);
                }
            }
        });

        btnExecutar.setOnAction(event -> adicionarItem());
        btnEstoque.setOnAction(event -> irParaEstoque());
    }

    public void adicionarItem() {
        String codBarrasConferencia = txtCodBarra.getText().trim();
        String quantidadeConferencia = txtQuantidade.getText().trim();

        if (quantidadeConferencia.isEmpty() || !quantidadeConferencia.matches("\\d+")) {
            showAlert("Erro", "Quantidade Inválida", "A quantidade informada não é válida.");
            return;
        }

        int quantidade = Integer.parseInt(quantidadeConferencia);
        boolean produtoEncontrado = false;

        for (Produto produto : listaDeProdutos) {
            if (produto.getCodBarras().equals(codBarrasConferencia)) {
                String descricao = produto.getDescricao();
                double valor = produto.getValor();
                String item = "Código: " + codBarrasConferencia + ", Descrição: " + descricao +
                        ", Valor: " + valor + ", Quantidade: " + quantidade;

                if (quantidade > produto.getSaldo()) {
                    item += " (Saldo insuficiente)";
                } else {
                    item += " (Saldo disponível)";
                }

                for (int i = 0; i < listaConferencia.getItems().size(); i++) {
                    if (listaConferencia.getItems().get(i).contains("Código: " + codBarrasConferencia)) {
                        listaConferencia.getItems().remove(i);
                        break;
                    }
                }

                listaConferencia.getItems().add(item);
                produtoEncontrado = true;
                break;
            }
        }

        if (!produtoEncontrado) {
            showAlert("Erro", "Código de Barras Inválido", "O código de barras " + codBarrasConferencia + " não foi encontrado no sistema.");
        }

        txtCodBarra.clear();
        txtQuantidade.clear();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void irParaEstoque() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Estoque.fxml"));
            Scene estoqueScene = new Scene(loader.load());

            stage.setScene(estoqueScene);
            stage.setTitle("Estoque");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
