package org.example.dao;

import org.example.model.User;
import org.example.util.MySQLConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User userLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPasswordFromDB = rs.getString("password");

                // Проверяем пароль с хешем из БД
                if (BCrypt.checkpw(password, hashedPasswordFromDB)) {

                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            hashedPasswordFromDB,
                            rs.getString("role"),
                            rs.getDouble("discount")
                    );
                } else {
                    System.out.println("UserDAO.userLogin: Password incorrect for user: " + username);
                }
            } else {
                System.out.println("UserDAO.userLogin: User not found: " + username);
            }
            return null;

        } catch (SQLException e) {
            System.out.println("UserDAO.userLogin SQL error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getRole());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getDouble("discount")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateDiscount(int userId, double discount) {
        String sql = "UPDATE users SET discount = ? WHERE id = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, discount);
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();

        String sql = "SELECT * FROM users";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        null,
                        rs.getString("role"),
                        rs.getDouble("discount")
                );
                user.setDiscount(rs.getDouble("discount"));
                list.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT id, username, password, role, discount FROM users WHERE username = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setDiscount(rs.getInt("discount"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getUserDiscount(int userId) {
        String sql = "SELECT discount FROM users WHERE id = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double discount = rs.getDouble("discount");
                System.out.println("UserDAO: Найдена скидка = " + discount + "% для пользователя ID = " + userId);
                return discount;
            } else {

                //  есть ли вообще таблица users и какие в ней записи?
                String checkSql = "SELECT id, username, discount FROM users LIMIT 5";
                try (Statement checkStmt = conn.createStatement();
                     ResultSet checkRs = checkStmt.executeQuery(checkSql)) {

                    while (checkRs.next()) {
                        int id = checkRs.getInt("id");
                        String username = checkRs.getString("username");
                        double userDiscount = checkRs.getDouble("discount");
                        System.out.println("  ID: " + id + ", Имя: " + username + ", Скидка: " + userDiscount + "%");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("UserDAO: Ошибка SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("UserDAO: Общая ошибка: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("UserDAO: Возвращаем скидку по умолчанию 0%");
        return 0.0;
    }
}