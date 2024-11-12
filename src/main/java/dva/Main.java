package dva;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Conferencia.fxml"));
        Scene conferenciaScene = new Scene(loader.load());

        ConferenciaController conferenciaController = loader.getController();
        conferenciaController.inicializar(primaryStage);

        primaryStage.setScene(conferenciaScene);
        primaryStage.setTitle("Tela de Conferência");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}