package org.example.dao;

import org.example.model.Admin;
import org.example.util.MySQLConnection;
import java.sql.*;


public class AdminDAO {

    public Admin getAdminByLogin(String login) {
        String sql = "SELECT * FROM admins WHERE login = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Admin(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
