package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Перенаправляем на главную JSP страницу
        request.getRequestDispatcher("/main.jsp").forward(request, response);
        //интерпретируется как путь относительно корня веб-приложения (webapp root).
    }
}