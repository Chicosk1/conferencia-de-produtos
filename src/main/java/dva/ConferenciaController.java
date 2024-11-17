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

    private int numeroDeContagens = 0; // Contador para rastrear quantas contagens foram realizadas


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
                    primeiraContagem.add(produtoConferido);
                    numeroDeContagens = 1; // Atualiza para indicar que a primeira contagem foi realizada
                } else if (segundaContagem.stream().noneMatch(p -> p.getCodBarras().equals(codBarrasConferencia))) {
                    segundaContagem.add(produtoConferido);
                    numeroDeContagens = 2; // Atualiza para indicar que a segunda contagem foi realizada
                } else if (terceiraContagem.stream().noneMatch(p -> p.getCodBarras().equals(codBarrasConferencia))) {
                    terceiraContagem.add(produtoConferido);
                    numeroDeContagens = 3; // Atualiza para indicar que a terceira contagem foi realizada
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
        List<Produto> produtosDivergentes = new ArrayList<>(); // Lista para produtos em divergência

        for (Produto produto : listaParaConferir) {
            double quantidadeRegistrada = produto.getSaldo();
            boolean produtoConferidoNaPrimeira = false;

            for (List<Produto> conferencias : contagens) {
                for (Produto conferido : conferencias) {
                    if (conferido.getCodBarras().equals(produto.getCodBarras())) {
                        if (conferido.getSaldo() != quantidadeRegistrada) {
                            primeiraContagem.add(conferido);
                            produtosDivergentes.add(conferido); // Adiciona produto divergente à lista
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
                produtosDivergentes.add(produto); // Adiciona produto não conferido à lista
            }
        }

        listaConferencia.getItems().clear();
        listaParaConferir = new ArrayList<>(listaDeProdutos);
        contagens.clear();
        listaConferencia.refresh();

        if (!inventarioConcluidoComSucesso) {
            if (numeroDeContagens == 1) {
                verificarDivergencias(primeiraContagem, "Divergencia");
            }
            if (numeroDeContagens == 2) {
                verificarDivergencias(segundaContagem, "Divergencia");
            }
            if (numeroDeContagens == 3) {
                verificarDivergencias(terceiraContagem, "Divergencia");
            }
        }

    }

    private void verificarDivergencias(List<Produto> contagem, String tipo) {
        List<Produto> produtosDivergentes = new ArrayList<>();

        StringBuilder mensagemErro = new StringBuilder("Os seguintes produtos possuem divergências:\n");

        for (Produto produto : produtosDivergentes) {
            mensagemErro.append("- Descrição: ")
                    .append(produto.getDescricao())
                    .append("- Saldo Total: ")
                    .append(produto.getSaldo())
                    .append("\n");
        }
        if (numeroDeContagens == 1){
            if (primeiraContagem == listaDeProdutos) {
                mostrarAlerta("Sucesso", "Inventário Concluído", "O inventário foi concluído com sucesso!", true);
                System.exit(0);
            } else {
                mostrarAlerta("Erro", "Inventário Incompleto", "Alguns produtos possuem quantidade incorreta ou não foram conferidos. Iniciando Proxima conferencia (2)", false);
            }
        }
        if (numeroDeContagens == 2) {
            if (segundaContagem == listaDeProdutos) {
                mostrarAlerta("Sucesso", "Inventário Concluído", "O inventário foi concluído com sucesso!", true);
                System.exit(0);
            } else if (segundaContagem == primeiraContagem) {
                    mostrarAlerta("Erro", "Divergencia", "Segunda Contagem Exatamente Igual a primeira... Encerrando com Divergencias.", false);
                    mostrarAlerta("Erro", "Inventário Finalizado com Divergências", mensagemErro.toString(), false);
                    System.exit(0);
                } else {
                    mostrarAlerta("Erro", "Inventário Incompleto", "Alguns produtos possuem quantidade incorreta ou não foram conferidos. Iniciando Proxima conferencia (3)", false);
                }
            }

        if (numeroDeContagens == 3){
            if (segundaContagem != listaDeProdutos && segundaContagem != primeiraContagem) {
                if (terceiraContagem == listaDeProdutos) {
                    mostrarAlerta("Sucesso", "Inventário Concluído", "O inventário foi concluído com sucesso!", true);
                    System.exit(0);
                } else {
                    mostrarAlerta("Erro", "Inventário Finalizado com Divergências", mensagemErro.toString(), false);
                    System.exit(0);
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
