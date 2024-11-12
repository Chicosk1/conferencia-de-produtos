package dva;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Mudanca (Stage stage) {

    Parent root1;

    {
        try {
            root1 = FXMLLoader.load(getClass().getResource("Estoque.fxml"));
            Scene scene = new Scene(root1);
            stage.setTitle("Estoque de Produtos");
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
