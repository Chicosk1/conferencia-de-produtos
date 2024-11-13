package dva;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
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

    private List<List<Produto>> contagens = new ArrayList<>();
    private List<Produto> primeiraContagem = null;
    private List<Produto> segundaContagem = null;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public ConferenciaController() {
        listaDeProdutos.add(new Produto("123", "SAPATO", 20.99, 2));
        listaDeProdutos.add(new Produto("234", "CHINELOS", 19.99, 3));
        listaDeProdutos.add(new Produto("456", "BOTA", 5.00, 4));
        listaDeProdutos.add(new Produto("567", "TENIS", 7.00, 7));
        listaDeProdutos.add(new Produto("678", "MEIA", 3.99, 1));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnExecutar.setOnAction(event -> adicionarItem());

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
    }

    public void adicionarItem() {
        String codBarrasConferencia = txtCodBarra.getText().trim();
        String quantidadeConferencia = txtQuantidade.getText().trim();

        if (quantidadeConferencia.isEmpty() || !quantidadeConferencia.matches("\\d+")) {
            mostrarAlerta("Erro", "Quantidade Inválida", "A quantidade informada não é válida.", false);
            return;
        }

        int quantidade = Integer.parseInt(quantidadeConferencia);
        boolean produtoEncontrado = false;

        for (Produto produto : listaDeProdutos) {
            if (produto.getCodBarras().equals(codBarrasConferencia)) {
                String descricao = produto.getDescricao();
                double valor = produto.getValor();
                String item = "Código: " + codBarrasConferencia + ", Descrição: " + descricao + ", Valor: " + valor + ", Quantidade: " + quantidade;

                if (quantidade != produto.getSaldo()) {
                    item += " (Saldo insuficiente)";
                } else {
                    item += " (Saldo disponível)";
                }

                listaConferencia.getItems().removeIf(i -> i.contains("Código: " + codBarrasConferencia));
                listaConferencia.getItems().add(item);

                Produto produtoConferido = new Produto(codBarrasConferencia, descricao, valor, quantidade);
                contagens.add(new ArrayList<>(List.of(produtoConferido)));

                produtoEncontrado = true;
                break;
            }
        }

        if (!produtoEncontrado) {
            mostrarAlerta("Erro", "Código de Barras Inválido", "O código de barras " + codBarrasConferencia + " não foi encontrado no sistema.", false);
        }

        if (todosProdutosConferidos()) {
            finalizarInventario();
        }

        txtCodBarra.clear();
        txtQuantidade.clear();
    }

    private boolean todosProdutosConferidos() {
        for (Produto produto : listaDeProdutos) {
            boolean produtoConferido = false;

            for (List<Produto> conferencias : contagens) {
                for (Produto conferido : conferencias) {
                    if (conferido.getCodBarras().equals(produto.getCodBarras())) {
                        produtoConferido = true;
                        break;
                    }
                }
                if (produtoConferido) break;
            }

            if (!produtoConferido) {
                return false;
            }
        }
        return true;
    }
    public void finalizarInventario() {
        boolean inventarioConcluidoComSucesso = true;
        
        primeiraContagem = new ArrayList<>();
        segundaContagem = new ArrayList<>();

        for (Produto produto : listaDeProdutos) {
            double quantidadeRegistrada = produto.getSaldo();
            boolean produtoConferidoNaPrimeira = false;
            boolean produtoConferidoNaSegunda = false;

            for (List<Produto> conferencias : contagens) {
                for (Produto conferido : conferencias) {
                    if (conferido.getCodBarras().equals(produto.getCodBarras())) {
                        if (conferido.getSaldo() != quantidadeRegistrada) {
                            primeiraContagem.add(conferido);
                            inventarioConcluidoComSucesso = false;
                        }
                        produtoConferidoNaPrimeira = true;
                        break;
                    }
                }
                if (produtoConferidoNaPrimeira) break;
            }

            for (Produto produtoPrimeira : primeiraContagem) {
                if (produtoPrimeira.getCodBarras().equals(produto.getCodBarras())) {
                    if (produtoPrimeira.getSaldo() == quantidadeRegistrada) {
                        segundaContagem.add(produtoPrimeira);
                    } else if (segundaContagem.contains(produtoPrimeira)) {
                        inventarioConcluidoComSucesso = false;
                    }
                    produtoConferidoNaSegunda = true;
                    break;
                }
            }

            if (!produtoConferidoNaPrimeira || !produtoConferidoNaSegunda) {
                inventarioConcluidoComSucesso = false;
            }
        }
        if (inventarioConcluidoComSucesso) {
            mostrarAlerta("Sucesso", "Inventário Concluído", "O inventário foi concluído com sucesso!", true);
        } else if (segundaContagem.equals(primeiraContagem)) {
            mostrarAlerta("Divergência", "Inventário Concluído com Divergências", "A segunda contagem é idêntica à primeira, mas diferente do sistema.", false);

            // Limpa a ListView visualmente e insere os produtos novamente
            listaConferencia.getItems().clear();  // Limpa a lista visual
            contagens.clear();  // Limpa as contagens para tentar novamente
        } else {
            mostrarAlerta("Erro", "Inventário Incompleto", "Alguns produtos possuem quantidade incorreta ou não foram conferidos.", false);

            // Limpa a ListView visualmente e insere os produtos novamente
            listaConferencia.getItems().clear();  // Limpa a lista visual
            contagens.clear();  // Limpa as contagens para tentar novamente
        }
    }


    private void mostrarAlerta(String title, String header, String content, boolean sucesso) {
        Alert alert;

        if (sucesso) {
            alert = new Alert(AlertType.CONFIRMATION);
        } else {
            alert = new Alert(AlertType.ERROR);
        }

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void irParaEstoque(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Estoque.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void Troca (){
        txtCodBarra.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                txtQuantidade.requestFocus();
            }
        });
    }
    public void Troca2 (){
        txtQuantidade.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                btnExecutar.requestFocus();
            }
        });
    }
}