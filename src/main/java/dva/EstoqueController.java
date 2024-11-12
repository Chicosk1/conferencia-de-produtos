package dva;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EstoqueController implements Initializable {

    @FXML
    private TableColumn<Produto, Integer> Saldo;

    @FXML
    private TableView<Produto> tableView;

    @FXML
    private TableColumn<Produto, String> Product;

    @FXML
    private Button btnConferencia;

    @FXML
    private TableColumn<Produto, String> colId;

    @FXML
    private TableColumn<Produto, Integer> Cost;

    @FXML
    private Button btnEstoque;

    private Stage stage;

    // Construtor padrão
    public EstoqueController() {
        // Sem parâmetros, necessário para o FXMLLoader
    }

    // Método para inicializar os dados na tabela
    ObservableList<Produto> initialData() {
        Produto p1 = new Produto("123", "carro", 5, 100);
        Produto p2 = new Produto("234", "carro2", 5, 1000);

        return FXCollections.observableArrayList(p1, p2);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configura as colunas da tabela
        colId.setCellValueFactory(new PropertyValueFactory<Produto, String>("codBarras"));
        Product.setCellValueFactory(new PropertyValueFactory<Produto, String>("nomeProduto"));
        Saldo.setCellValueFactory(new PropertyValueFactory<Produto, Integer>("saldo"));
        Cost.setCellValueFactory(new PropertyValueFactory<Produto, Integer>("valor"));

        // Preenche a tabela com dados
        tableView.setItems(initialData());
    }

    // Método para configurar o Stage no controlador
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
