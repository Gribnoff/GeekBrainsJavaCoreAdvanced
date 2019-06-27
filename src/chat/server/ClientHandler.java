package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

class ClientHandler {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Server server;
    private String nickname;
    private static int online;

    private static SortedSet<String> userCommands = new TreeSet<>();
    static {
        userCommands.add("/disconnect");
    }

    public String getNickname() {
        return nickname;
    }

    public static int getOnline() {
        return online;
    }

    public Socket getSocket() {
        return socket;
    }

    ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        online++;
        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = in.readUTF();
                            if (str.startsWith("/auth")) {
                                String[] tokens = str.split(" ");
                                String nick = AuthService.getNickByLoginAndPass(tokens[1], tokens[2]);
                                if (nick != null) {
                                    sendMessage("/authPassed");
                                    nickname = nick;
                                    server.subsсribe(ClientHandler.this);
                                    break;
                                } else
                                    sendMessage("/authFailed");
                            }
                        }

                        while (true) {
                            String str = in.readUTF();

                            Date date = new Date();
                            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

                            if ("/disconnect".equals(str)) {
                                disconnect();
                                break;
                            } else if (str.startsWith("/w ") || str.startsWith("/whisper ")) {
                                boolean clientFound = false;
                                String[] whisper = str.split(" ");
                                for (ClientHandler client : server.getClients()) {
                                    if (client.nickname.equals(whisper[1])) {
                                        clientFound = true;
                                        str = str.substring(whisper[0].length() + whisper[1].length());
                                        out.writeUTF(dateFormat.format(date) + " " + "Whisper to " + nickname + ": " + str);
                                        client.sendMessage(dateFormat.format(date) + " " + "Whisper from " + nickname + ": " + str);
                                    }
                                }
                                if (!clientFound)
                                    out.writeUTF("Пользователя с ником " + whisper[1] + " не найдено(либо он оффлайн)");
                                continue;
                            } else if (str.startsWith("/")) {
                                noSuchCommandMessage();
                                continue;
                            }

                            str = dateFormat.format(date) + " " + nickname + ": " + str;
                            server.broadcastMessage(str);

                            System.out.println(str);
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                            out.close();
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    server.unsubsсribe(ClientHandler.this);
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String text) {
        try {
            out.writeUTF(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        sendMessage("/disconnectionAccepted");
        online--;
    }

    private void noSuchCommandMessage() {
        sendMessage("Такой комманды нет. Список доступных комманд:");
        for (String command : userCommands) {
            sendMessage(command);
        }
    }
}
