package dva;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

public class EstoqueController extends ConferenciaController implements Initializable{

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


    // Construtor padrão
    public EstoqueController() {
        // Sem parâmetros, necessário para o FXMLLoader
    }

    // Método para inicializar os dados na tabela
    ObservableList<Produto> initialData() {
        Produto p1 = new Produto("123", "SAPATO", 20.99, 2);
        Produto p2 = new Produto("234", "CHINELOS", 19.99, 3);
        Produto p3 = new Produto("456", "BOTA", 5.00, 4);
        Produto p4 = new Produto("567", "TENIS", 7.00, 7);
        Produto p5 = new Produto("678", "MEIA", 3.99, 1);

        return FXCollections.observableArrayList(p1, p2, p3, p4, p5);
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

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void irParaConferencia(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Conferencia.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
