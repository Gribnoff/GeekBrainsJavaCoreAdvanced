package chat.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class LoginWindowController {
    @FXML
    VBox window;
    @FXML
    TextField loginField, passField;
    @FXML
    Pane loginPane;
    @FXML
    Label loginError, loginOverlap;

    private boolean authorized;

    private static ChatWindow chatWindow;
    private static LoginWindow loginWindow;

    /**
     * синглтон для окна авторизации
     */
    static LoginWindow getLoginWindowInstance() {
        if (loginWindow == null) {
            try {
                loginWindow = new LoginWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return loginWindow;
    }

    /**
     * установка статуса авторизации пользователя в положение true и открытие основного окна
     */
    private void setAuthorized() {
        setLoginWindowVisible(false);
        this.authorized = true;
        chatWindow.getController().show(in, out, socket);
        chatWindow.getController().setChatWindowVisible(true);
    }

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private final String IP_ADDRESS = "localhost";
    private final int PORT = 8189;

    /**
     * взаимодействие пользователя с чатом и другими пользователями
     */
    private void connect() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    auth:
                    while (true) {
                        String str = in.readUTF();
                        switch (str) {
                            case "/authPassed":
                                chatWindow = ChatWindowController.getChatWindowInstance();
                                setAuthorized();
                                break auth;
                            case "/authFailed":
                                loginOverlap.setVisible(false);
                                loginError.setVisible(true);
                                break;
                            case "/authOverlap":
                                loginError.setVisible(false);
                                loginOverlap.setVisible(true);
                                break;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * отправка сообщения на сервер
     *
     * @param text сообщение для отправки
     */
    private void sendMessage(String text) {
        try {
            if (!socket.isClosed()) {
                out.writeUTF(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * окно подтвержения выхода из программы
     *
     * @return кнопку которую нажал пользователь(OK или Cancel)
     */
    private Optional<ButtonType> askForExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Выход");
        alert.setHeaderText(null);
        alert.setContentText("Действительно выйти из программы?");

        return alert.showAndWait();
    }

    /**
     * выход из программы
     */
    public void exit() {
        if (askForExit().get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * закрытие окна авторизации
     */
    void setLoginWindowVisible(boolean visible) {
        Platform.runLater(() -> {
            Stage stage = (Stage) window.getScene().getWindow();
            if (visible)
                stage.show();
            else
                stage.hide();
        });
    }

    /**
     * попытка авторизации
     */
    public void login() {
        if (socket == null || socket.isClosed())
            connect();

        sendMessage("/auth " + loginField.getText() + " " + passField.getText());
        passField.clear();
    }

    public void setDefaultTheme() {
        window.getStylesheets().clear();
    }

    public void setGrassTheme() {
        window.getStylesheets().clear();
        window.getStylesheets().add("chat/client/css/grass.css");
    }

    public void setSeaTheme() {
        window.getStylesheets().clear();
        window.getStylesheets().add("chat/client/css/sea.css");
    }

    public void setDarculaTheme() {
        window.getStylesheets().clear();
        window.getStylesheets().add("chat/client/css/darcula.css");
    }
}
