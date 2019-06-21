package chat.client.sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Initializable {

    @FXML
    VBox window;
    @FXML
    TextArea textArea;
    @FXML
    TextField textField;
    @FXML
    Button buttonSend, buttonClear, buttonSearch;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private final String IP_ADDRESS = "localhost";
    private final int PORT = 8189;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                            textArea.appendText(str + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                out.writeUTF(textField.getText());
                textField.clear();
                textField.requestFocus();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public void clearText() {
        textField.clear();
    }

    public void clearChat() {
        textArea.clear();
    }

    public void exit() {
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
}
