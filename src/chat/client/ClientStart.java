package chat.client;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClientStart extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginWindowController.getLoginWindowInstance();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
