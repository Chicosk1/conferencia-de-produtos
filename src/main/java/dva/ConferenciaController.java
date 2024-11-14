package dva;

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
import java.net.URL;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ConferenciaController implements Initializable {

    @FXML
    private Button btnExecutar;
    @FXML
    private TextField txtCodBarra, txtQuantidade;
    @FXML
    private ListView<String> listaConferencia;

    private List<Produto> listaDeProdutos = new ArrayList<>();

    public ConferenciaController() {

        listaDeProdutos.add(new Produto("1", "SAPATO", 20.99, 1));
        listaDeProdutos.add(new Produto("2", "CHINELOS", 19.99, 2));
        listaDeProdutos.add(new Produto("3", "CHINELOS", 19.99, 3));
        listaDeProdutos.add(new Produto("4", "CHINELOS", 19.99, 4));
        listaDeProdutos.add(new Produto("5", "CHINELOS", 19.99, 5));

    }

    private List<Produto> listaParaConferir = listaDeProdutos;

    private List<List<Produto>> contagens = new ArrayList<>();
    private List<Produto> primeiraContagem = new ArrayList<>();
    private List<Produto> segundaContagem = new ArrayList<>();
    private List<Produto> terceiraContagem = new ArrayList<>();

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
                } else {
                    setText(null);
                    setStyle("");
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

        for (Produto produto : listaParaConferir) {
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

                if (primeiraContagem.stream().noneMatch(p -> p.getCodBarras().equals(codBarrasConferencia))) {
                    primeiraContagem.add(produtoConferido); // Adiciona o produto na primeira contagem
                } else if (segundaContagem.stream().noneMatch(p -> p.getCodBarras().equals(codBarrasConferencia))) {
                    segundaContagem.add(produtoConferido); // Adiciona o produto na segunda contagem
                }else if (terceiraContagem.stream().noneMatch(p -> p.getCodBarras().equals(codBarrasConferencia))) {
                    terceiraContagem.add(produtoConferido);
                }

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
        for (Produto produto : listaParaConferir) {
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

        for (Produto produto : listaParaConferir) {
            double quantidadeRegistrada = produto.getSaldo();
            boolean produtoConferidoNaPrimeira = false;



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

            if (!produtoConferidoNaPrimeira) {
                inventarioConcluidoComSucesso = false;
            }
        }


        if (inventarioConcluidoComSucesso) {
            mostrarAlerta("Sucesso", "Inventário Concluído", "O inventário foi concluído com sucesso!", true);
        } else {
            mostrarAlerta("Erro", "Inventário Incompleto", "Alguns produtos possuem quantidade incorreta ou não foram conferidos. Faça uma nova contagem.", false);
        }
        listaConferencia.getItems().clear();
        listaParaConferir = new ArrayList<>(listaDeProdutos);
        contagens.clear();
        listaConferencia.refresh();

        if (primeiraContagem == listaParaConferir){
            mostrarAlerta("Divergencia", "Divergencia no Inv", "Houve uma divergencia no inventario", false);
        }else{
            mostrarAlerta("Divergencia", "Divergencia no Inv", "Erro no Inv", false);
        }

        if (segundaContagem == listaParaConferir){
            mostrarAlerta("Divergencia", "Divergencia no Inv", "Houve uma divergencia no inventario", false);
        }else {
            mostrarAlerta("Divergencia", "Divergencia no Inv", "Erro no Inv", false);
            if (segundaContagem == primeiraContagem){
                mostrarAlerta("Divergencia", "Divergencia no Inv", "Houve uma divergencia no inventario", false);
            }else{
                mostrarAlerta("Divergencia", "Divergencia no Inv", "Erro no Inv", false);
            }
        }

        if (terceiraContagem == listaParaConferir){
            mostrarAlerta("Divergencia", "Divergencia no Inv", "Houve uma divergencia no inventario", false);

        }else{
            mostrarAlerta("Divergencia", "Divergencia no Inv", "Erro no Inv", false);
            if(terceiraContagem == primeiraContagem){
                mostrarAlerta("Divergencia", "Divergencia no Inv", "Houve uma divergencia no inventario", false);

            }else{
                mostrarAlerta("Divergencia", "Divergencia no Inv", "Erro no Inv", false);
                if(terceiraContagem == segundaContagem) {
                    mostrarAlerta("Divergencia", "Divergencia no Inv", "Houve uma divergencia no inventario", false);

                }else {
                    mostrarAlerta("Divergencia", "Divergencia no Inv", "Erro no Inv", false);

                }
            }
        }



    }

    private void mostrarAlerta(String title, String header, String content, boolean sucesso) {
        Alert alert = sucesso ? new Alert(AlertType.CONFIRMATION) : new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Stage stage;
    private Scene scene;

    @FXML
    public void irParaEstoque(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Estoque.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void Troca() {
        txtCodBarra.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                txtQuantidade.requestFocus();
            }
        });
    }

    public void Troca2() {
        txtQuantidade.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                btnExecutar.requestFocus();
            }
        });
    }

    public void Troca3(){
        btnExecutar.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                txtCodBarra.requestFocus();
            }
        });
    }
}
