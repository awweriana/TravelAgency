package org.example.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.UserDAO;
import org.example.fabric.DAOFactory;
import org.example.model.User;

import java.io.IOException;

public class RegisterServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(RegisterServlet.class);

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = DAOFactory.getUserDAO();
        logger.info("Сервлет RegisterServlet инициализирован");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("Открыта страница регистрации");
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (!"registerAction".equals(action)) {
            logger.warn("Некорректное действие: {}", action);
            request.setAttribute("wrongAction", "Некорректное действие!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = "user";

        User user = new User(0, username, password, role, 0);

        if (userDAO.registerUser(user)) {
            logger.info("Пользователь успешно зарегистрирован: {}", username);
            request.setAttribute("successMessage", "Регистрация успешна! Теперь можете войти.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            logger.warn("Ошибка регистрации: логин уже существует - {}", username);
            request.setAttribute("errorMessage", "Ошибка регистрации: логин уже существует.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
