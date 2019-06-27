package chat.client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    @FXML
    VBox window;
    @FXML
    TextArea textArea;
    @FXML
    TextField textField, loginField, passField;
    @FXML
    Button buttonSend, buttonClear, buttonSearch;
    @FXML
    Pane loginPane, chatPane;
    @FXML
    MenuItem clearChat;
    @FXML
    Label loginError;

    private boolean authorized;

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
        if (authorized) {
            loginPane.setVisible(false);
            loginPane.setManaged(false);
            loginError.setVisible(false);
            chatPane.setManaged(true);
            chatPane.setVisible(true);
            clearChat.setDisable(false);
        } else {
            loginPane.setVisible(true);
            loginPane.setManaged(true);
            chatPane.setManaged(false);
            chatPane.setVisible(false);
            clearChat.setDisable(true);
        }
    }

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private final String IP_ADDRESS = "localhost";
    private final int PORT = 8189;

    public void connect() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = in.readUTF();
                            if ("/authPassed".equals(str)) {
                                setAuthorized(true);
                                break;
                            } else
                                loginError.setVisible(true);
                        }

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
                            out.close();
                            in.close();
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        setAuthorized(false);
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private void sendMessage(String text) {
        try {
            if (!socket.isClosed()) {
                out.writeUTF(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private Optional<ButtonType> askForExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Выход");
        alert.setHeaderText(null);
        alert.setContentText("Действительно выйти из программы?");

        return alert.showAndWait();
    }

    public void clearText() {
        textField.clear();
    }

    public void clearChat() {
        textArea.clear();
    }

    public void exit() {
        if (askForExit().get() == ButtonType.OK) {
            sendMessage("/disconnect");
            System.exit(0);
        }
    }

    public void close() {
        System.exit(0);
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

    public void login() {
        if (socket == null || socket.isClosed())
            connect();

        try {
            out.writeUTF("/auth " + loginField.getText() + " " + passField.getText());
            loginField.clear();
            passField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}