package sayana.DAO;
import java.util.ArrayList;
import java.util.List;

import sayana.models.CartItem;
import sayana.models.Product;
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

    // Метод аутентификации пользователя
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
                    rs.getString("user_info"),
                    rs.getString("inn"),
                    rs.getString("pasport"),
                    rs.getInt("birth")
            );
        }
        return null;
    }

    // Метод добавления пользователя
    public boolean addUser(String fullName, String username, String password, String email, String phone, String inn, String pasport, Integer birth) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO users (full_name, username, password_hash, email, phone, inn, pasport, birth) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setString(1, fullName);
        prst.setString(2, username);
        prst.setString(3, password);
        prst.setString(4, email);
        prst.setString(5, phone);
        prst.setString(6, inn);
        prst.setString(7, pasport);
        prst.setInt(8, birth);
        int rowsAffected = prst.executeUpdate();
        return rowsAffected > 0;
    }

    // Метод проверки существования username
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

    // Метод проверки существования email
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

    // Метод получения пользователя по ID
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
                    rs.getString("user_info"),
                    rs.getString("inn"),
                    rs.getString("pasport"),
                    rs.getInt("birth")
            );
        }
        return null;
    }

    // Метод обновления пользователя
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

    // Остальные методы для работы с продуктами, категориями и адресами...
    public List<Product> getProductsByCategory(int categoryId) throws SQLException, ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, pi.image_url " +
                "FROM products p " +
                "LEFT JOIN product_images pi ON p.product_id = pi.product_id AND pi.is_main = true " +
                "WHERE p.category_id = ? AND p.availability_status = true";
        PreparedStatement st = getDbConnection().prepareStatement(sql);
        st.setInt(1, categoryId);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getInt("category_id"),
                    rs.getString("product_name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getString("composition"),
                    rs.getBoolean("availability_status")
            );

            // Устанавливаем URL изображения
            String imageUrl = rs.getString("image_url");
            if (imageUrl != null && !rs.wasNull()) {
                product.setImageUrl(imageUrl);
            }

            products.add(product);
        }
        return products;
    }


    public java.util.List<sayana.models.Category> getAllCategories() throws SQLException, ClassNotFoundException {
        java.util.List<sayana.models.Category> categories = new java.util.ArrayList<>();
        String sql = "SELECT * FROM categories";
        PreparedStatement st = getDbConnection().prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            sayana.models.Category category = new sayana.models.Category(
                    rs.getInt("category_id"),
                    rs.getString("category_name"),
                    rs.getString("description")
            );
            categories.add(category);
        }
        return categories;
    }

    public java.util.List<sayana.models.Address> getUserAddresses(int userId) throws SQLException, ClassNotFoundException {
        java.util.List<sayana.models.Address> addresses = new java.util.ArrayList<>();
        String sql = "SELECT * FROM addresses WHERE user_id = ?";
        PreparedStatement st = getDbConnection().prepareStatement(sql);
        st.setInt(1, userId);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            sayana.models.Address address = new sayana.models.Address(
                    rs.getInt("address_id"),
                    rs.getInt("user_id"),
                    rs.getString("city"),
                    rs.getString("street_address"),
                    rs.getString("house_number"),
                    rs.getString("apartment"),
                    rs.getInt("floor")
            );
            addresses.add(address);
        }
        return addresses;
    }

    public boolean addAddress(int userId, String city, String streetAddress,
                              String houseNumber, String apartment, Integer floor) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO addresses (user_id, city, street_address, house_number, apartment, floor) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setInt(1, userId);
        prst.setString(2, city);
        prst.setString(3, streetAddress);
        prst.setString(4, houseNumber);
        prst.setString(5, apartment);
        if (floor != null) {
            prst.setInt(6, floor);
        } else {
            prst.setNull(6, Types.INTEGER);
        }
        int rowsAffected = prst.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean createOrder(int userId, int addressId, double totalAmount) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO orders (user_id, address_id, total_amount) VALUES (?, ?, ?)";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setInt(1, userId);
        prst.setInt(2, addressId);
        prst.setDouble(3, totalAmount);
        int rowsAffected = prst.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean updateUserProfile(int userId, String fullName, String email, String phone) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE users SET full_name = ?, email = ?, phone = ? WHERE user_id = ?";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setString(1, fullName);
        prst.setString(2, email);
        prst.setString(3, phone);
        prst.setInt(4, userId);
        int rowsAffected = prst.executeUpdate();
        return rowsAffected > 0;
    }
    public boolean addToFavorites(int userId, int productId) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO favorites (user_id, product_id) VALUES (?, ?) " +
                "ON CONFLICT (user_id, product_id) DO NOTHING";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setInt(1, userId);
        prst.setInt(2, productId);
        int rowsAffected = prst.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean removeFromFavorites(int userId, int productId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND product_id = ?";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setInt(1, userId);
        prst.setInt(2, productId);
        int rowsAffected = prst.executeUpdate();
        return rowsAffected > 0;
    }

    public List<Product> getUserFavorites(int userId) throws SQLException, ClassNotFoundException {
        List<Product> favorites = new ArrayList<>();
        String sql = "SELECT p.*, pi.image_url FROM products p " +
                "INNER JOIN favorites f ON p.product_id = f.product_id " +
                "LEFT JOIN product_images pi ON p.product_id = pi.product_id AND pi.is_main = true " +
                "WHERE f.user_id = ? AND p.availability_status = true";
        PreparedStatement st = getDbConnection().prepareStatement(sql);
        st.setInt(1, userId);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getInt("category_id"),
                    rs.getString("product_name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getString("composition"),
                    rs.getBoolean("availability_status")
            );

            // Устанавливаем URL изображения
            String imageUrl = rs.getString("image_url");
            if (imageUrl != null && !rs.wasNull()) {
                product.setImageUrl(imageUrl);
            }

            favorites.add(product);
        }
        return favorites;
    }

    public boolean isFavorite(int userId, int productId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM favorites WHERE user_id = ? AND product_id = ?";
        PreparedStatement st = getDbConnection().prepareStatement(sql);
        st.setInt(1, userId);
        st.setInt(2, productId);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    // === МЕТОДЫ ДЛЯ КОРЗИНЫ ===

    public boolean addToCart(int userId, int productId, int quantity) throws SQLException, ClassNotFoundException {
        // Проверяем, есть ли уже товар в корзине
        if (isInCart(userId, productId)) {
            return updateCartQuantity(userId, productId, quantity);
        }

        String sql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setInt(1, userId);
        prst.setInt(2, productId);
        prst.setInt(3, quantity);
        int rowsAffected = prst.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean removeFromCart(int userId, int productId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setInt(1, userId);
        prst.setInt(2, productId);
        int rowsAffected = prst.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean updateCartQuantity(int userId, int productId, int quantity) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE cart SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setInt(1, quantity);
        prst.setInt(2, userId);
        prst.setInt(3, productId);
        int rowsAffected = prst.executeUpdate();
        return rowsAffected > 0;
    }

    public List<CartItem> getUserCart(int userId) throws SQLException, ClassNotFoundException {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT c.*, p.*, pi.image_url FROM cart c " +
                "INNER JOIN products p ON c.product_id = p.product_id " +
                "LEFT JOIN product_images pi ON p.product_id = pi.product_id AND pi.is_main = true " +
                "WHERE c.user_id = ? AND p.availability_status = true";
        PreparedStatement st = getDbConnection().prepareStatement(sql);
        st.setInt(1, userId);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getInt("category_id"),
                    rs.getString("product_name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getString("composition"),
                    rs.getBoolean("availability_status")
            );

            // Устанавливаем URL изображения
            String imageUrl = rs.getString("image_url");
            if (imageUrl != null && !rs.wasNull()) {
                product.setImageUrl(imageUrl);
            }

            CartItem cartItem = new CartItem(
                    rs.getInt("cart_id"),
                    rs.getInt("user_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity")
            );
            cartItem.setProduct(product);
            cartItems.add(cartItem);
        }
        return cartItems;
    }

    public boolean isInCart(int userId, int productId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM cart WHERE user_id = ? AND product_id = ?";
        PreparedStatement st = getDbConnection().prepareStatement(sql);
        st.setInt(1, userId);
        st.setInt(2, productId);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    public double getCartTotal(int userId) throws SQLException, ClassNotFoundException {
        double total = 0;
        String sql = "SELECT SUM(p.price * c.quantity) as total FROM cart c " +
                "INNER JOIN products p ON c.product_id = p.product_id " +
                "WHERE c.user_id = ?";
        PreparedStatement st = getDbConnection().prepareStatement(sql);
        st.setInt(1, userId);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            total = rs.getDouble("total");
        }
        return total;
    }

    public void clearCart(int userId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setInt(1, userId);
        prst.executeUpdate();
    }
    public boolean addProduct(int categoryId, String productName, String description,
                              double price, String composition) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO products (category_id, product_name, description, price, composition) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setInt(1, categoryId);
        prst.setString(2, productName);
        prst.setString(3, description);
        prst.setDouble(4, price);
        prst.setString(5, composition);
        int rowsAffected = prst.executeUpdate();
        return rowsAffected > 0;
    }

    // Метод получения ID последнего добавленного продукта
    public int getLastProductId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT MAX(product_id) FROM products";
        PreparedStatement st = getDbConnection().prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    // Метод сохранения изображения продукта
    public boolean saveProductImage(int productId, String imageUrl) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO product_images (product_id, image_url) VALUES (?, ?)";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setInt(1, productId);
        prst.setString(2, imageUrl);
        int rowsAffected = prst.executeUpdate();
        return rowsAffected > 0;
    }

    // Метод получения URL изображения продукта
    public String getProductImageUrl(int productId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT image_url FROM product_images WHERE product_id = ? AND is_main = true LIMIT 1";
        PreparedStatement st = getDbConnection().prepareStatement(sql);
        st.setInt(1, productId);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            return rs.getString("image_url");
        }
        return null;
    }

    // Метод удаления продукта
    public boolean deleteProduct(int productId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM products WHERE product_id = ?";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setInt(1, productId);
        int rowsAffected = prst.executeUpdate();
        return rowsAffected > 0;
    }
}
