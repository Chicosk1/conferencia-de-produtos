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

    private int numeroDeContagens = 0;


    public ConferenciaController() {

        listaDeProdutos.add(new Produto("1", "SAPATO", 20.99, 1));
        listaDeProdutos.add(new Produto("2", "CHINELOS", 19.99, 2));
        listaDeProdutos.add(new Produto("3", "TENIS", 19.99, 3));
        listaDeProdutos.add(new Produto("4", "COMPUTADOR", 19.99, 4));
        listaDeProdutos.add(new Produto("5", "MOUSE", 19.99, 5));

    }

    private List<Produto> listaParaConferir = listaDeProdutos;
    private List<List<Produto>> contagens = new ArrayList<>();
    private List<Produto> primeiraContagem = new ArrayList<>();
    private List<Produto> segundaContagem = new ArrayList<>();
    private List<Produto> terceiraContagem = new ArrayList<>();
    List<Produto> produtosDivergentes = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Chamando metodo adicionarItem ao pressionar botão
        btnExecutar.setOnAction(event -> adicionarItem());

        //Inserindo cor no String do Produto com validação de saldo
        listaConferencia.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    if (item.contains("(Saldo insuficiente)")) {
                        setStyle("-fx-text-fill: red;");
                    } else if (item.contains("(Saldo disponível)")) {
                        setStyle("-fx-text-fill: blue;");
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
                                                                            //Regex de numerais
        if (quantidadeConferencia.isEmpty() || !quantidadeConferencia.matches("\\d+")) {
            mostrarAlerta("Erro", "Quantidade Inválida", "A quantidade informada não é válida.", false);
            return;
        }

        int quantidade = Integer.parseInt(quantidadeConferencia);
        boolean produtoEncontrado = false;

        // Passando por toda a lista
        for (Produto produto : listaParaConferir) {
            //Verificando se o codigo de barras inserido é igual ao da lista de produtos
            if (produto.getCodBarras().equals(codBarrasConferencia)) {
                String descricao = produto.getDescricao();
                double valor = produto.getValor();
                String item = "Código: " + codBarrasConferencia + ", Descrição: " + descricao + ", Valor: " + valor + ", Quantidade: " + quantidade;

                if (quantidade != produto.getSaldo()) {
                    item += " (Saldo insuficiente)";
                } else {
                    item += " (Saldo disponível)";
                }

                //Atualiza o item se possuir o código de barras igual
                listaConferencia.getItems().removeIf(i -> i.contains("Código: " + codBarrasConferencia));
                listaConferencia.getItems().add(item);

                Produto produtoConferido = new Produto(codBarrasConferencia, descricao, valor, quantidade);

                //Lista para conferir -> filtrar -> verifica os correspondentes -> verificando se o produto é o mesmo da conferencia
                if (primeiraContagem.stream().noneMatch(p -> p.getCodBarras().equals(codBarrasConferencia))) {
                    primeiraContagem.add(produtoConferido);
                    numeroDeContagens = 1;
                } else if (segundaContagem.stream().noneMatch(p -> p.getCodBarras().equals(codBarrasConferencia))) {
                    segundaContagem.add(produtoConferido);
                    numeroDeContagens = 2;
                } else if (terceiraContagem.stream().noneMatch(p -> p.getCodBarras().equals(codBarrasConferencia))) {
                    terceiraContagem.add(produtoConferido);
                    numeroDeContagens = 3;
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
            boolean produtoConferido = false;

            for (List<Produto> conferencias : contagens) {
                for (Produto conferido : conferencias) {
                    if (conferido.getCodBarras().equals(produto.getCodBarras())) {
                        if (conferido.getSaldo() != quantidadeRegistrada) {
                            primeiraContagem.add(conferido);
                            produtosDivergentes.add(conferido);
                            inventarioConcluidoComSucesso = false;
                        }
                        produtoConferido = true;
                        break;
                    }
                }
                if (produtoConferido) break;
            }

            if (!produtoConferido) {
                inventarioConcluidoComSucesso = false;
                produtosDivergentes.add(produto);
            }
        }

        if (inventarioConcluidoComSucesso) {
            mostrarAlerta("Sucesso", "Inventário Concluído", "O inventário foi concluído com sucesso!", true);
            System.exit(0);
        }

        //Limpa o ListView
        listaConferencia.getItems().clear();
        listaParaConferir = new ArrayList<>(listaDeProdutos);
        contagens.clear();
        listaConferencia.refresh();

        //Validações das mensagens
        if (!inventarioConcluidoComSucesso) {
            if (numeroDeContagens == 1) {
                verificarDivergencias(primeiraContagem);
                produtosDivergentes.clear();
            }
            if (numeroDeContagens == 2) {
                verificarDivergencias(segundaContagem);
                produtosDivergentes.clear();
            }
            if (numeroDeContagens == 3) {
                verificarDivergencias(terceiraContagem);
                produtosDivergentes.clear();
            }
        }

    }

    //Formulador de mensagem
    private void verificarDivergencias(List<Produto> contagem) {

        StringBuilder mensagemErro = new StringBuilder("Os seguintes produtos possuem divergências:\n");

        for (Produto produto : produtosDivergentes) {
                        //adicionar
            mensagemErro.append(produto.getCodBarras())
                        .append(" - ")
                        .append(produto.getDescricao())
                                //nova linha
                        .append("\n");
        }

        //Verificações para mensagens
        if (!primeiraContagem.equals(listaParaConferir) && numeroDeContagens == 1) {
            mostrarAlerta("Divergência", "Inventário Incompleto", "Alguns produtos possuem quantidade incorreta. Iniciando segunda conferência", false);
        } else if (segundaContagem.equals(listaParaConferir) && numeroDeContagens == 2) {
            mostrarAlerta("Sucesso", "Inventário Concluído", "O inventário foi concluído com sucesso na segunda conferência!", true);
            System.exit(0);
                                            //Lista -> filtragem -> verifica se é compativel -> segunda lista -> filtragem -> verifica se é compatível -> p1 == p2
        } else if (numeroDeContagens == 2 && primeiraContagem.stream().allMatch(p1 -> segundaContagem.stream().anyMatch(p2 ->
                                             p1.getCodBarras().equals(p2.getCodBarras()) && p1.getSaldo() == p2.getSaldo()))) {
            mostrarAlerta("Divergência", "Inventário Finalizado com Divergências (Segunda Contagem igual a Primeira)", mensagemErro.toString(), false);
            System.exit(0);
        } else if (!segundaContagem.equals(listaParaConferir) && numeroDeContagens == 2) {
            mostrarAlerta("Divergência", "Inventário Incompleto", "Alguns produtos possuem quantidade incorreta. Iniciando terceira conferência", false);
        } else if (terceiraContagem.equals(listaParaConferir) && numeroDeContagens == 3) {
            mostrarAlerta("Sucesso", "Inventário Concluído", "O inventário foi concluído com sucesso na terceira conferência!", true);
            System.exit(0);
        } else {
            mostrarAlerta("Divergência", "Inventário Finalizado com Divergências", mensagemErro.toString(), false);
            System.exit(0);
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
}
