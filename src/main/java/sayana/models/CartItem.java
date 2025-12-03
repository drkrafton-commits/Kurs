package sayana.models;

public class CartItem {
    private int cartId;
    private int userId;
    private int productId;
    private int quantity;
    private Product product;

    public CartItem(int cartId, int userId, int productId, int quantity) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Геттеры и сеттеры
    public int getCartId() { return cartId; }
    public int getUserId() { return userId; }
    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public Product getProduct() { return product; }

    public void setCartId(int cartId) { this.cartId = cartId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setProductId(int productId) { this.productId = productId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setProduct(Product product) { this.product = product; }

    public double getTotalPrice() {
        return product != null ? product.getPrice() * quantity : 0;
    }
}