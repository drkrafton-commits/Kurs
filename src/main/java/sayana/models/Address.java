package sayana.models;

public class Address {
    private int addressId;
    private int userId;
    private String city;
    private String streetAddress;
    private String houseNumber;
    private String apartment;
    private Integer floor;

    public Address(int addressId, int userId, String city, String streetAddress,
                   String houseNumber, String apartment, Integer floor) {
        this.addressId = addressId;
        this.userId = userId;
        this.city = city;
        this.streetAddress = streetAddress;
        this.houseNumber = houseNumber;
        this.apartment = apartment;
        this.floor = floor;
    }

    // Геттеры и сеттеры
    public int getAddressId() { return addressId; }
    public int getUserId() { return userId; }
    public String getCity() { return city; }
    public String getStreetAddress() { return streetAddress; }
    public String getHouseNumber() { return houseNumber; }
    public String getApartment() { return apartment; }
    public Integer getFloor() { return floor; }
}