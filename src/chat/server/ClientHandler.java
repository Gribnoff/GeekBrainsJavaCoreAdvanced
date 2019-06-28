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

    String getNickname() {
        return nickname;
    }

    public static int getOnline() {
        return online;
    }

    Socket getSocket() {
        return socket;
    }

    ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        online++;
        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/auth")) {
                            String[] tokens = str.split(" ");
                            String nick = AuthService.getNickByLoginAndPass(tokens[1], tokens[2]);
                            if (nick == null)
                                sendMessage("/authFailed");
                            else if (server.getClients().contains(getClientByNick(nick)))
                                sendMessage("/authOverlap");
                            else {
                                sendMessage("/authPassed");
                                nickname = nick;
                                server.subscribe(ClientHandler.this);
                                break;
                            }
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
                            String[] whisper = str.split(" ", 3);
                            ClientHandler target = getClientByNick(whisper[1]);

                            if (target == null)
                                out.writeUTF("Пользователь не найден(либо он оффлайн)");
                            else {
                                str = whisper[2];
                                out.writeUTF(dateFormat.format(date) + " " + "Whisper to " + target.nickname + ": " + str);
                                target.sendMessage(dateFormat.format(date) + " " + "Whisper from " + nickname + ": " + str);
                            }

                            continue;

                        } else if (str.startsWith("/")) {
                            noSuchCommandMessage();
                            continue;
                        }

                        str = dateFormat.format(date) + " " + nickname + ": " + str;
                        server.broadcastMessage(str);

                        System.out.println(str);
                    }
                } catch (IOException e) {
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
                server.unsubscribe(ClientHandler.this);
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * отправка сообщения пользователю
     */
    void sendMessage(String text) {
        try {
            out.writeUTF(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * отключение от чата
     */
    private void disconnect() {
        sendMessage("/disconnectionAccepted");
        online--;
    }

    /**
     * выводит список доступных комманд
     */
    private void noSuchCommandMessage() {
        sendMessage("Такой комманды нет. Список доступных комманд:");
        for (String command : userCommands) {
            sendMessage(command);
        }
    }

    /**
     * поиск клиента по нику для авторизации
     *
     * @param nick ник ползователя
     * @return целевого клиента - если найден, null - если не найден
     */
    private ClientHandler getClientByNick(String nick) {
        ClientHandler target = null;
        for (ClientHandler client : server.getClients()) {
            if (client.nickname.equals(nick)) {
                target = client;
                break;
            }
        }
        return target;
    }
}
