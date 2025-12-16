package org.example.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private double discount;

    public User() {}

    public User(Integer id,String username, String password, String role, double discount) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.id = id;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getRole() {return role;}
    public double getDiscount() {return discount;}

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {this.role = role;}
    public void setDiscount(double discount) {}
}
