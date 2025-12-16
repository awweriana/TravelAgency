package org.example.fabric;

import org.example.dao.*;

public class DAOFactory {

    private DAOFactory() {}

    public static AdminDAO getAdminDAO() {
        return new AdminDAO();
    }

    public static UserDAO getUserDAO() {
        return new UserDAO();
    }

    public static TourDAO getTourDAO() {
        return new TourDAO();
    }

    public static PurchasesDAO getPurchasesDAO() {
        return new PurchasesDAO();
    }
}
