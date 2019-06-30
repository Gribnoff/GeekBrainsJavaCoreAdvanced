package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

class ClientHandler {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Server server;
    private String nickname;
    private List<String> blackList;
    private static DateFormat dateFormat;

    private static SortedSet<String> userCommands = new TreeSet<>();
    static {
        userCommands.add("/disconnect");
        userCommands.add("/w");
        userCommands.add("/whisper");
        userCommands.add("/bl");
        userCommands.add("/blacklist");
    }

    String getNickname() {
        return nickname;
    }

    Socket getSocket() {
        return socket;
    }

    boolean checkBlackList(String nick) {
        return blackList.contains(nick);
    }

    ClientHandler(Socket socket, Server server) {
        blackList = new ArrayList<>();
        this.socket = socket;
        this.server = server;
        dateFormat = new SimpleDateFormat("HH:mm:ss");
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

                        if ("/disconnect".equals(str)) {
                            disconnect();
                            break;
                        }

                        if (str.startsWith("/w ") || str.startsWith("/whisper ")) {
                            String[] whisper = str.split(" ", 3);
                            ClientHandler target = getClientByNick(whisper[1]);

                            if (target == null)
                                out.writeUTF("Пользователь не найден(либо он оффлайн)");
                            else {
                                str = whisper[2];
                                sendWhisper(target, str);
                            }

                        } else if ("/bl".equals(str) || "/blacklist".equals(str)) {
                            printBlackList();

                        } else if (str.startsWith("/blacklist ") || str.startsWith("/bl")) {
                            String[] blacklist = str.split(" ");
                            blackList.add(blacklist[1]);
                            sendMessage("Вы добавили пользователя " + blacklist[1] + " в чёрный список");

                        } else if (str.startsWith("/")) {
                            noSuchCommandMessage();

                        } else {

                            str = dateFormat.format(new Date()) + " " + nickname + ": " + str;
                            server.broadcastMessage(ClientHandler.this, str);

                            System.out.println(str);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                        out.close();
                        socket.close();
                        server.unsubscribe(ClientHandler.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * отправка сообщения пользователю
     */
    void sendMessage(String text) throws IOException {
        out.writeUTF(text);

    }

    /**
     * отправка личного сообщения
     */
    private void sendWhisper(ClientHandler target, String msg) throws IOException {
        if (target.checkBlackList(this.nickname))
            out.writeUTF("Вы в чёрном списке у пользователя " + target.nickname);
        else {
            out.writeUTF(dateFormat.format(new Date()) + " " + "Whisper to " + target.nickname + ": " + msg);
            target.sendMessage(dateFormat.format(new Date()) + " " + "Whisper from " + nickname + ": " + msg);
        }
    }

    /**
     * отключение от чата
     */
    private void disconnect() throws IOException {
        sendMessage("/disconnectionAccepted");
    }

    /**
     * выводит чёрный список
     */
    private void printBlackList() throws IOException {
        sendMessage("В вашем чёрном списке:");
        for (String s : blackList) {
            sendMessage(s);
        }
    }

    /**
     * выводит список доступных комманд
     */
    private void noSuchCommandMessage() throws IOException {
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
