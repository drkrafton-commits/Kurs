package com.example.kurs.DAO;

import com.example.kurs.models.UserType;
import java.sql.*;

public class DbConnection {
    private final String HOST = "2.nntc.nnov.ru";
    private final String PORT = "6543";
    private final String DB_NAME = "postgres_user_5";
    private final String LOGIN = "postgres_user_5";
    private final String PASS = "r5a2w227";

    private Connection dbConn = null;

    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?currentSchema=lab1";
        Class.forName("org.postgresql.Driver");

        dbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
        return dbConn;
    }

    // Проверка существования пользователя
    public int getUser(String login, String password) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) as count FROM users WHERE login = ? AND password = ?";

        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, login);
        statement.setString(2, password);

        ResultSet res = statement.executeQuery();

        int count = 0;
        if (res.next()) {
            count = res.getInt("count");
        }

        statement.close();
        return count;
    }

    // Поиск пользователя по логину
    public UserType findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE login = ?";

        try (PreparedStatement st = getDbConnection().prepareStatement(sql)) {
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new UserType(
                        rs.getInt("id"),
                        rs.getString("fio"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Аутентификация пользователя
    public UserType authenticate(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return null;
        }

        UserType user = findByUsername(username);
        if (user == null) return null;

        // Проверка пароля
        if (password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    // Регистрация нового пользователя
    public boolean addUser(String fio, String login, String password) {
        String sql = "INSERT INTO users (fio, login, password) VALUES (?, ?, ?)";

        try (PreparedStatement prst = getDbConnection().prepareStatement(sql)) {
            prst.setString(1, fio);
            prst.setString(2, login);
            prst.setString(3, password);
            prst.executeUpdate();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Закрытие соединения
    public void closeConnection() {
        try {
            if (dbConn != null && !dbConn.isClosed()) {
                dbConn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}