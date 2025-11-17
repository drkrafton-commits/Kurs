package sayana.models;

public class UserType {
    private int id;
    private String fio;
    private String login;
    private String password;
    private String role;

    public UserType(int id, String fio, String login, String password, String role) {
        this.id = id;
        this.fio = fio;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public String getFio() { return fio; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}