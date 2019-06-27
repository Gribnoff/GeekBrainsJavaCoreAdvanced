package chat.server;

import java.sql.*;

class AuthService {
    private static Connection connection;
    private static Statement state;

    /**
     * включение сервиса авторизации
     */
    static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mainDB.db");
            state = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * выключение сервиса авторизации
     */
    static void disconect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * получение ника пользователя по логину и паролю
     *
     * @param login логин пользователя
     * @param pass  пароль от логина
     * @return ник - если пользователь существует, null - если нет
     */
    static String getNickByLoginAndPass(String login, String pass) {
        String result = null;
        String sql = String.format("SELECT nickname FROM main WHERE login = '%s' AND password = '%s'", login, pass);
        try {
            ResultSet rs = state.executeQuery(sql);
            if (rs.next())
                result = rs.getString("nickname");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
