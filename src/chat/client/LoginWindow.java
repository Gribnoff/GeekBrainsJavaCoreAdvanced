package chat.client;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

class LoginWindow extends Window {
    private LoginWindowController controller;

    LoginWindowController getController() {
        return controller;
    }

    LoginWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream("loginWindow.fxml"));
        controller = loader.getController();
//        root.setId("root");
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("GeekChat");
            Scene scene = new Scene(root, 400, 420);
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(event -> controller.exit());
        });
    }
}
