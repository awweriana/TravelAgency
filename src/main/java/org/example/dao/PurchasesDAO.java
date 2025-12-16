package org.example.dao;

import org.example.model.Purchases;
import org.example.strategy.DiscountResult;
import org.example.util.MySQLConnection;
import org.example.strategy.DiscountStrategy;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchasesDAO {

    public boolean addPurchase(int userId, int tourId, double finalPrice) {
        System.out.println("PurchasesDAO.addPurchase(): userId=" + userId +
                ", tourId=" + tourId + ", finalPrice=" + finalPrice);

        // Получаем оригинальную цену тура
        double originalPrice = getTourOriginalPrice(tourId);

        String sql = "INSERT INTO purchases (user_id, tour_id, original_price, final_price, purchase_date) " +
                "VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, tourId);
            ps.setDouble(3, originalPrice);
            ps.setDouble(4, finalPrice);

            int rowsAffected = ps.executeUpdate();
            System.out.println("PurchasesDAO: Добавлена покупка в БД. Записей: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("PurchasesDAO ОШИБКА при сохранении покупки:");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private double getTourOriginalPrice(int tourId) {
        String sql = "SELECT price FROM tours WHERE id = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tourId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("price");
            }

        } catch (Exception e) {
            System.err.println("Ошибка при получении цены тура ID " + tourId);
            e.printStackTrace();
        }

        return 0;
    }

    public List<Purchases> getAllPurchases() {
        List<Purchases> purchasesList = new ArrayList<>();

        String sql = "SELECT * FROM purchases ORDER BY purchase_date DESC";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Purchases purchase = new Purchases(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("tour_id"),
                        rs.getDouble("final_price"),
                        rs.getString("purchase_date"),
                        rs.getDouble("original_price"),
                        0
                );
                purchasesList.add(purchase);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchasesList;
    }

    public List<Map<String, Object>> getPurchaseHistoryWithCalculations(DiscountStrategy discountStrategy) {
        List<Map<String, Object>> purchasesList = new ArrayList<>();

        String sql = "SELECT p.id, u.username, u.discount AS user_discount, " +
                "t.title, t.discount AS admin_discount, p.original_price, p.purchase_date " +
                "FROM purchases p " +
                "JOIN users u ON p.user_id = u.id " +
                "JOIN tours t ON p.tour_id = t.id " +
                "ORDER BY p.purchase_date DESC";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> purchase = new HashMap<>();
                purchase.put("id", rs.getInt("id"));
                purchase.put("username", rs.getString("username"));
                purchase.put("tourTitle", rs.getString("title"));

                double originalPrice = rs.getDouble("original_price");
                double userDiscount = rs.getDouble("user_discount");
                double adminDiscount = rs.getDouble("admin_discount");

                // Рассчитываем все через стратегию
                DiscountResult result = discountStrategy.calculate(originalPrice, userDiscount, adminDiscount);

                purchase.put("originalPrice", originalPrice);
                purchase.put("finalPrice", result.getFinalPrice());
                purchase.put("discountAmount", result.getDiscountAmount());
                purchase.put("discountPercent", result.getDiscountPercent());
                purchase.put("userDiscount", userDiscount);
                purchase.put("adminDiscount", adminDiscount);
                purchase.put("purchaseDate", rs.getString("purchase_date"));

                purchasesList.add(purchase);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return purchasesList;
    }
}