package sayana.DAO;

import sayana.models.UserType;
import java.sql.*;

public class DbConnection {
    private final String HOST = "localhost";
    private final String PORT = "5432";
    private final String DB_NAME = "Sayana";
    private final String LOGIN = "postgres";
    private final String PASS = "26D11A8SH";

    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(connStr, LOGIN, PASS);
    }

    public UserType authenticate(String username, String password) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
        PreparedStatement st = getDbConnection().prepareStatement(sql);
        st.setString(1, username);
        st.setString(2, password);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return new UserType(
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("user_info")
            );
        }
        return null;
    }

    public boolean addUser(String fullName, String username, String password, String email, String phone) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO users (full_name, username, password_hash, email, phone) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setString(1, fullName);
        prst.setString(2, username);
        prst.setString(3, password);
        prst.setString(4, email);
        prst.setString(5, phone);
        int rowsAffected = prst.executeUpdate();
        return rowsAffected > 0;
    }

    // Дополнительные методы
    public UserType getUserById(int userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        PreparedStatement st = getDbConnection().prepareStatement(sql);
        st.setInt(1, userId);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return new UserType(
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("user_info")
            );
        }
        return null;
    }

    public boolean updateUser(UserType user) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE users SET full_name = ?, email = ?, phone = ?, user_info = ? WHERE user_id = ?";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setString(1, user.getFullName());
        prst.setString(2, user.getEmail());
        prst.setString(3, user.getPhone());
        prst.setString(4, user.getUserInfo());
        prst.setInt(5, user.getUserId());
        int rowsAffected = prst.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean checkUsernameExists(String username) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        PreparedStatement st = getDbConnection().prepareStatement(sql);
        st.setString(1, username);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    public boolean checkEmailExists(String email) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        PreparedStatement st = getDbConnection().prepareStatement(sql);
        st.setString(1, email);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }
}