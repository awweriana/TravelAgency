package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.UserDAO;
import org.example.model.User;

import java.io.IOException;
import java.net.URLEncoder;

public class AuthCheckServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(AuthCheckServlet.class);

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("GET-запрос на страницу auth-check.jsp");
        request.getRequestDispatcher("auth-check.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            logger.warn("Попытка входа с пустым логином или паролем");
            String msg = URLEncoder.encode("Введите логин и пароль", "UTF-8");
            response.sendRedirect("auth-check?error=" + msg);
            return;
        }

        try {
            User user = userDAO.getUserByUsername(username);

            if (user != null) {
                // логиним для теста
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());

                logger.info("Успешный вход пользователя: {} (ID={})", username, user.getId());

                response.sendRedirect("payment.jsp");
            } else {
                logger.warn("Попытка входа несуществующего пользователя: {}", username);
                String msg = URLEncoder.encode("Пользователь не найден", "UTF-8");
                response.sendRedirect("auth-check?error=" + msg);
            }
        } catch (Exception e) {
            logger.error("Ошибка при попытке входа пользователя: {}", username, e);
            String msg = URLEncoder.encode("Внутренняя ошибка сервера", "UTF-8");
            response.sendRedirect("auth-check?error=" + msg);
        }
    }
}
