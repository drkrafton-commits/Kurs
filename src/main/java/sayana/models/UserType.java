package sayana.models;

public class UserType {
    private int userId;
    private String fullName;
    private String username;
    private String passwordHash;
    private String email;
    private String phone;
    private String userInfo;
    private String inn;
    private String pasport;
    private Integer birth;

    // Конструктор для аутентификации
    public UserType(int userId, String fullName, String username, String passwordHash) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // Полный конструктор
    public UserType(int userId, String fullName, String username, String passwordHash,
                    String email, String phone, String userInfo, String inn, String pasport, Integer birth) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phone = phone;
        this.userInfo = userInfo;
        this.inn = inn;
        this.pasport = pasport;
        this.birth = birth;

    }

    // Геттеры
    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getUserInfo() { return userInfo; }
    public String getInn() { return inn; }
    public String getPasport() { return pasport; }
    public Integer getBirth() { return birth; }

    // Сеттеры
    public void setUserId(int userId) { this.userId = userId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setUserInfo(String userInfo) { this.userInfo = userInfo; }
    public void setInn(String inn) { this.inn = inn; }
    public void setPasport(String pasport) { this.pasport = pasport; }
    public void setBirth(Integer birth) { this.birth = birth; }

    @Override
    public String toString() {
        return "UserType{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}