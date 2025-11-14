package com.example.kurs.models;

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

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFio() { return fio; }
    public void setFio(String fio) { this.fio = fio; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}