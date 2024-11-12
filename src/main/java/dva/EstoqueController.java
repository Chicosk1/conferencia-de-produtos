package dva;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.naming.spi.ResolveResult;
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

    ObservableList<Produto> initialData(){
        Produto p1 = new Produto("123", "carro", 5, 100 );
        Produto p2 = new Produto("234", "carro2", 5, 1000 );

        return FXCollections.<Produto> observableArrayList(p1,p2);
    }
    @FXML
    private Button btnEstoque;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<Produto, String>("123"));
        Product.setCellValueFactory(new PropertyValueFactory<Produto, String>("234"));
        Saldo.setCellValueFactory(new PropertyValueFactory<Produto, Integer>("345"));
        Cost.setCellValueFactory(new PropertyValueFactory<Produto, Integer>("456"));


        tableView.setItems(initialData());
    }

    private Stage stage;

    public EstoqueController(Stage stage) {

        this.stage = stage;

    }

    @FXML
    private void irParaConferencia() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Conferencia.fxml"));
            Scene conferenciaScene = new Scene(loader.load());

            stage.setScene(conferenciaScene);
            stage.setTitle("Conferência de Estoque");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
