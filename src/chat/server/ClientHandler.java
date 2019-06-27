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
                                String[] whisper = str.split(" ");
                                ClientHandler target = getClientForWhisper(whisper);

                                if (target == null)
                                    out.writeUTF("Пользователь не найден(либо он оффлайн)");
                                else {
                                    str = str.substring(whisper[0].length() + whisper[1].length() + 2);
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

    /**
     * поиск клиента по нику (для отправки личного сообщения)
     *
     * @param msg полная строка сообщения из textField
     * @return целевого клиента - если найден, null - если не найден
     */
    private ClientHandler getClientForWhisper(String[] msg) {
        ClientHandler result = null;

        for (ClientHandler client : server.getClients()) {
            boolean clientFound = true;
            String[] clientNickSplit = client.nickname.split(" ");

            for (int i = 0; i < clientNickSplit.length; i++) {
                if (!msg[i + 1].equals(clientNickSplit[i])) {
                    clientFound = false;
                    break;
                }
            }
            if (clientFound)
                result = client;
        }

        return result;
    }
}
