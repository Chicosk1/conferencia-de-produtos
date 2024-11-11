package dva;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ConferenciaController implements Initializable {

    @FXML
    private Button btnExecutar, btnEstoque, btnConferencia;
    @FXML
    private TextField txtCodBarra, txtQuantidade;
    @FXML
    private ListView<String> listaConferencia;

    private List<Produto> listaDeProdutos = new ArrayList<>();

    public ConferenciaController() {

        listaDeProdutos.add(new Produto("123", "SAPATO", 2, 20.99));
        listaDeProdutos.add(new Produto("234", "CHINELOS", 3, 19.99));
        listaDeProdutos.add(new Produto("456", "BOTA", 4, 5.00));
        listaDeProdutos.add(new Produto("567", "TENIS", 7, 7.00));
        listaDeProdutos.add(new Produto("678", "MEIA", 1, 3.99));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnExecutar.setOnAction(event -> adicionarItem());
        btnEstoque.setOnAction(event -> btnEstoqueAction());
        btnConferencia.setOnAction(event -> btnConferenciaAction());
    }

    public void adicionarItem() {

        String codBarrasConferencia = txtCodBarra.getText().trim();
        String quantidadeConferencia = txtQuantidade.getText().trim();

        boolean produtoEncontrado = false;

        //Erros do Sistema

        Alert alert = new Alert(AlertType.ERROR);

        if (!produtoEncontrado) {

            alert.setTitle("Erro");
            alert.setHeaderText("Código de Barras Inválido");
            alert.setContentText("O código de barras " + codBarrasConferencia + " não foi encontrado no sistema.");

            alert.showAndWait();
        }

        if (quantidadeConferencia.isEmpty() || !quantidadeConferencia.matches("\\d+")) {

            alert.setTitle("Erro");
            alert.setHeaderText("Quantidade Inválida");
            alert.setContentText("A quantidade " + quantidadeConferencia + " não foi encontrado no sistema.");

            alert.showAndWait();
        }

        //Adição na Lista

        int quantidade = Integer.parseInt(quantidadeConferencia);

        for (Produto produto : listaDeProdutos) {

            if (produto.getCodBarras().equals(codBarrasConferencia)) {

                String descricao = produto.getDescricao();
                double valor = produto.getValor();
                String item = "Código: " + codBarrasConferencia + ", Descrição: " + descricao + ", Valor: " + valor + ", Quantidade: " + quantidade;

                listaConferencia.getItems().add(item);
                produtoEncontrado = true;
                break;

            }
        }

        txtCodBarra.clear();
        txtQuantidade.clear();

    }

    private void btnEstoqueAction() {}
    private void btnConferenciaAction() {}
}