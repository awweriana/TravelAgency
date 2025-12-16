package org.example.service;

import org.example.dao.AdminDAO;
import org.example.fabric.DAOFactory;
import org.example.model.Admin;
import org.mindrot.jbcrypt.BCrypt;

public class AdminService {

    private final AdminDAO adminDAO;

    public AdminService() {
        this.adminDAO = DAOFactory.getAdminDAO();
    }

    public boolean login(String login, String password) {
        Admin admin = adminDAO.getAdminByLogin(login);

        if (admin == null) {
            System.out.println("Admin not found");
            return false;
        }

        return BCrypt.checkpw(password, admin.getPassword());
    }
}
