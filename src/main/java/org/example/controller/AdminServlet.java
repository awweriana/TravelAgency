package org.example.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.example.service.AdminService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

public class AdminServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(AdminServlet.class);
    private AdminService adminService;

    @Override
    public void init() {
        adminService = new AdminService();
        logger.info("AdminServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("admin-login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("loginAction".equals(action)) {
            String login = request.getParameter("login");
            String password = request.getParameter("password");

            if (adminService.login(login, password)) {
                request.getSession().setAttribute("admin", login);
                response.sendRedirect("admin-panel.jsp");
            } else {
                request.setAttribute("errorLoginPassMessage", "Неверный логин или пароль!");
                request.getRequestDispatcher("admin-login.jsp").forward(request, response);
            }

        } else {
            request.setAttribute("wrongAction", "Неизвестное действие!");
            request.getRequestDispatcher("admin-login.jsp").forward(request, response);
        }
    }

}
