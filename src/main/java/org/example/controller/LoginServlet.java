package org.example.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.dao.UserDAO;
import org.example.fabric.DAOFactory;
import org.example.model.User;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        // Создание через фабрику
        userDAO = DAOFactory.getUserDAO();
    }

    // Показ формы логина пользователя
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("user-login.jsp").forward(req, resp);
    }

    // Обработка логина
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("loginAction".equals(action)) {

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            User user = userDAO.userLogin(username, password);

            if (user != null) {
                HttpSession session = request.getSession();

                // сохраняем ВСЮ информацию о пользователе
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());

                // получаем и сохраняем скидку пользователя
                double userDiscount = userDAO.getUserDiscount(user.getId());
                session.setAttribute("userDiscount", userDiscount);

                response.sendRedirect("main");
            } else {
                request.setAttribute("error", "Неверный логин или пароль!");
                request.getRequestDispatcher("user-login.jsp").forward(request, response);
            }

        } else if ("registerAction".equals(action)) {
            response.sendRedirect("register.jsp");
        } else {
            request.setAttribute("error", "Неизвестное действие!");
            request.getRequestDispatcher("user-login.jsp").forward(request, response);
        }
    }
}
