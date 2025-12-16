package org.example.model;

public class Admin {
    private int id;
    private String login;
    private String password;
    private String role;

    public Admin(int id, String login, String password, String role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}
