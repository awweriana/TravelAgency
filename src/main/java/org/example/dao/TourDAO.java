package org.example.dao;

import org.example.model.Tour;
import org.example.util.MySQLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TourDAO {
    public List<Tour> getAllTour() {
        List<Tour> tourList = new ArrayList<>();

        String sql = "select * from tours";

        try(Connection conn = MySQLConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                Tour tour = new Tour(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getDouble("discount"),
                        rs.getBoolean("is_hot")
                );
                tourList.add(tour);
                System.out.println("TOTAL TOURS = " + tourList.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return tourList;
    }

    public Tour getTourById(int id) {
        String sql = "SELECT * FROM tours WHERE id = ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Tour(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("category"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getDouble("discount"),
                            rs.getBoolean("is_hot")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addTour(Tour tour) {
        String sql = "INSERT INTO tours (title, category, description, price, discount, is_hot) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection connection = MySQLConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, tour.getTitle());
            ps.setString(2, tour.getCategory());
            ps.setString(3, tour.getDescription());
            ps.setDouble(4, tour.getPrice());
            ps.setDouble(5, tour.getDiscount());
            ps.setBoolean(6, tour.isHot());

            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateTour(Tour tour) {
        String sql = "UPDATE tours SET title=?, category=?, description=?, price=?, discount=?, is_hot=? " +
                "WHERE id=?";

        try(Connection connection = MySQLConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, tour.getTitle());
            ps.setString(2, tour.getCategory());
            ps.setString(3, tour.getDescription());
            ps.setDouble(4, tour.getPrice());
            ps.setDouble(5, tour.getDiscount());
            ps.setBoolean(6, tour.isHot());
            ps.setInt(7, tour.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteTour(int id) {
        String sql = "DELETE FROM tours WHERE id=?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setHot(int id, boolean isHot) {
        String sql = "UPDATE tours SET is_hot=? WHERE id=?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, isHot);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Tour> getToursByCategory(int categoryId) {
        System.out.println("=== getToursByCategory: categoryId=" + categoryId + " ===");

        List<Tour> list = new ArrayList<>();
        String sql = "SELECT * FROM tours WHERE category = ?";

        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // В базе category - это число (1, 2, 3)
                int categoryFromDB = rs.getInt("category");

                // Преобразуем число в строку с названием
                String categoryName;
                switch (categoryFromDB) {
                    case 1: categoryName = "Отдых"; break;
                    case 2: categoryName = "Экскурсии"; break;
                    case 3: categoryName = "Шоппинг"; break;
                    default: categoryName = "Категория " + categoryFromDB;
                }

                // Создаем тур с названием категории
                Tour t = new Tour(
                        rs.getInt("id"),
                        rs.getString("title"),
                        categoryName,      // отдых
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getDouble("discount"),
                        rs.getBoolean("is_hot")
                );

                list.add(t);

                System.out.println("Добавлен тур: " + t.getTitle() +
                        " | Категория: " + categoryName +
                        " | Цена: " + t.getPrice());
            }

            System.out.println("Всего найдено туров: " + list.size());

        } catch (SQLException e) {
            System.err.println("Ошибка в getToursByCategory:");
            e.printStackTrace();
        }

        return list;
    }
}
