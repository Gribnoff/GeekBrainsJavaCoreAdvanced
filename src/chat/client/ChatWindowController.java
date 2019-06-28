package chat.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatWindowController {

    @FXML
    VBox window;
    @FXML
    TextArea textArea;
    @FXML
    TextField textField;
    @FXML
    Button buttonSend, buttonClear, buttonSearch;
    @FXML
    Pane chatPane;
    @FXML
    MenuItem clearChat;

    private boolean authorized;
    private static ChatWindow chatWindow;
    private static LoginWindow loginWindow;

    /**
     * синглтон окна чата
     */
    static ChatWindow getChatWindowInstance() {
        if (chatWindow == null) {
            try {
                chatWindow = new ChatWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return chatWindow;
    }

    /**
     * разлогин и изменение интерфейса
     */
    private void setUnauthorized() {
        this.authorized = false;
        setChatWindowVisible(false);
        loginWindow.getController().setLoginWindowVisible(true);
    }

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    /**
     * взаимодействие пользователя с чатом и другими пользователями
     */
    void show(DataInputStream in, DataOutputStream out, Socket socket) {
        this.socket = socket;

        this.in = in;
        this.out = out;

        new Thread(() -> {
            try {
                while (true) {
                    String str = in.readUTF();
                    if ("/disconnectionAccepted".equals(str))
                        break;
                    textArea.appendText(str + "\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    loginWindow = LoginWindowController.getLoginWindowInstance();
                    out.close();
                    in.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                setUnauthorized();
            }
        }).start();

    }

    /**
     * отправка сообщения из textField на сервер
     */
    public void sendMessage() {
        if (!textField.getText().isEmpty() && textField.getText() != null) {
            try {
                if (!socket.isClosed()) {
                    out.writeUTF(textField.getText());
                    textField.clear();
                    textField.requestFocus();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
     * поиск в чате фразу из textField
     */
    public void searchPhrase() {
        if (!textField.getText().isEmpty() && textField.getText() != null) {
            int index = textArea.getText().indexOf(textField.getText());
            if (index == -1) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Нет совпадений");
                alert.setHeaderText(null);
                alert.setContentText("По заданной фразе совпадений не обнаружено!");
                alert.showAndWait();
            } else {
                Pattern pattern = Pattern.compile(".*" + textField.getText() + ".*");
                Matcher matcher = pattern.matcher(textArea.getText());
                if(matcher.find(0)){
                    textArea.selectRange(index, index + textField.getLength());
                }
            }
        }
    }

    /**
     * окно подтвержения выхода из программы
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
     * очистка textField
     */
    public void clearText() {
        textField.clear();
    }

    /**
     * очистка чата
     */
    public void clearChat() {
        textArea.clear();
    }

    /**
     * выход из программы
     */
    public void exit() {
        if (askForExit().get() == ButtonType.OK) {
            sendMessage("/disconnect");

            System.exit(0);
        }
    }

    /**
     * скрывает или показывает окно с чатом
     * @param visible true - показывает, false - скрывает
     */
    void setChatWindowVisible(boolean visible) {
        Platform.runLater(() -> {
            Stage stage = (Stage) window.getScene().getWindow();
            if (visible)
                stage.show();
            else
                stage.hide();
        });
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
