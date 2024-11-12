package dva;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
    private final Stage stage;

    public ConferenciaController(Stage stage) {

        this.stage = stage;

        listaDeProdutos.add(new Produto("123", "SAPATO",20.99, 2));
        listaDeProdutos.add(new Produto("234", "CHINELOS",19.99, 3));
        listaDeProdutos.add(new Produto("456", "BOTA",5.00, 4));
        listaDeProdutos.add(new Produto("567", "TENIS",7.00, 7));
        listaDeProdutos.add(new Produto("678", "MEIA",3.99, 1));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnExecutar.setOnAction(event -> adicionarItem());
        btnEstoque.setOnAction(event -> irParaEstoque());
    }

    public void adicionarItem() {

        String codBarrasConferencia = txtCodBarra.getText().trim();
        String quantidadeConferencia = txtQuantidade.getText().trim();

        boolean produtoEncontrado = false;

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

        txtCodBarra.clear();
        txtQuantidade.clear();

    }

    @FXML
    private void irParaEstoque() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("estoque.fxml"));
            Scene estoqueScene = new Scene(loader.load());

            stage.setScene(estoqueScene);
            stage.setTitle("Tela de Estoque");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}