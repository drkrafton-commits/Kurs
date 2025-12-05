package sayana.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private int orderId;
    private int userId;
    private int addressId;
    private String orderStatus; // оформляется, в_доставке, выдан
    private double totalAmount;
    private LocalDateTime orderDate;
    private String formattedDate;

    public Order(int orderId, int userId, int addressId,
                 String orderStatus, double totalAmount,
                 LocalDateTime orderDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.addressId = addressId;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.formattedDate = formatDate(orderDate);
    }

    private String formatDate(LocalDateTime date) {
        if (date == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return date.format(formatter);
    }

    // Геттеры
    public int getOrderId() { return orderId; }
    public int getUserId() { return userId; }
    public int getAddressId() { return addressId; }
    public String getOrderStatus() { return orderStatus; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public String getFormattedDate() { return formattedDate; }

    // Сеттеры
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getStatusText() {
        switch (orderStatus) {
            case "pending": return "Оформляется";
            case "in_progress": return "В доставке";
            case "delivered": return "Выдан";
            case "cancelled": return "Отменен";
            case "confirmed": return "Подтвержден";
            default: return orderStatus;
        }
    }
}