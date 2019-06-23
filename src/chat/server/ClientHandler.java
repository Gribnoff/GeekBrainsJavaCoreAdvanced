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
    String name;
    static int online;

    private static SortedSet<String> commands = new TreeSet<>();
    static {
        commands.add("/disconnect");
    }

    public Socket getSocket() {
        return socket;
    }

    ClientHandler(Socket socket, Server server, String name) {
        this.socket = socket;
        this.server = server;
        this.name = name;
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
                            if ("/disconnect".equals(str)) {
                                disconnect();
                                break;
                            } else if (str.startsWith("/")) {
                                noSuchCommandMessage();
                                continue;
                            }

                            Date date = new Date();
                            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

                            str = dateFormat.format(date) + " " + name + ": " + str;
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
        server.broadcastMessage(name + " покинул здание!");
        System.out.println(name + " покинул здание!");
    }

    private void noSuchCommandMessage() {
        sendMessage("Такой комманды нет. Список доступных комманд:");
        for (String command : commands) {
            sendMessage(command);
        }
    }
}
