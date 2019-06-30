package chat.client;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

class ChatWindow extends Window {
    private ChatWindowController controller;

    ChatWindowController getController() {
        return controller;
    }

    ChatWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream("chatWindow.fxml"));
        controller = loader.getController();
        root.setId("root");
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("GeekChat");
            Scene scene = new Scene(root, 600, 420);
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(event -> controller.exit());
        });
    }
}
