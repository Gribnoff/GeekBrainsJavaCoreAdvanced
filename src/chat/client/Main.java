package chat.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("appearance.fxml"));
        root.setId("root");
        primaryStage.setTitle("GeekChat");
        Scene scene = new Scene(root);
        /*

        FXMLLoader loader = new FXMLLoader();
        Controller controller = loader.getController();
        primaryStage.setOnCloseRequest(event -> {
            controller.exit();
        });

        */
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}