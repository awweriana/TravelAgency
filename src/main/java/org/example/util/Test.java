package org.example.util;

import java.sql.Connection;

public class Test {
    public static void main(String[] args) {
        try (Connection conn = MySQLConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Подключение к MySQL успешно!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
