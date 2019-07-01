package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

class Server {
    private Vector<ClientHandler> clients;

    Vector<ClientHandler> getClients() {
        return clients;
    }

    Server() {
        clients = new Vector<>();
        ServerSocket server = null;
        Socket socket = null;

        try {
            AuthService.connect();

            server = new ServerSocket(8189);
            System.out.println("Server started");

            while (true) {
                socket = server.accept();
                new ClientHandler(socket, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert socket != null;
                socket.close();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AuthService.disconnect();
        }
    }

    /**
     * рассылка сообщения всем онлайн пользователям
     *
     * @param text пересылаемое сообщение
     */
    void broadcastMessage(ClientHandler from, String text) throws IOException {
        for (ClientHandler client : clients) {
            if (!client.checkBlackList(from.getNickname()))
                client.sendMessage(text);
        }
    }

    /**
     * рассылка серверных сообщений
     *
     * @param text сообщение
     */
    private void broadcastMessage(String text) throws IOException {
        for (ClientHandler client : clients) {
            client.sendMessage(text);
        }
    }
    /**
     * внесение пользователя в онлайн список при авторизации
     *
     * @param client пользователь
     */
    void subscribe(ClientHandler client) throws IOException {
        clients.add(client);
        distributeOnlineList();
        broadcastMessage(client.getNickname() + " ворвался в чат!");
        System.out.println(client.getNickname() + " joined the channel");
    }

    /**
     * удаление пользователя из онлайн список при отключении от чата
     *
     * @param client пользователь
     */
    void unsubscribe(ClientHandler client) throws IOException {
        clients.remove(client);
        distributeOnlineList();
        broadcastMessage(client.getNickname() + " заскучал и ушёл...");
        System.out.println(client.getNickname() + " has left the channel");
    }

    /**
     * рассылка списка онлайн пользователей
     */
    private void distributeOnlineList() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("/online ");
        for (ClientHandler client : clients) {
            sb.append(client.getNickname() + " ");
        }

        String out = sb.toString();

        broadcastMessage(out);
    }
}