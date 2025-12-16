package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.TourDAO;
import org.example.fabric.DAOFactory;
import org.example.model.Tour;

import java.io.IOException;
import java.util.List;

public class ShoppingServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ShoppingServlet.class);

    private TourDAO tourDAO;

    @Override
    public void init() {
        tourDAO = DAOFactory.getTourDAO();
        logger.info("Сервлет ShoppingServlet инициализирован");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Tour> tours = tourDAO.getToursByCategory(3);
            request.setAttribute("tours", tours);
            request.setAttribute("categoryName", "Шоппинг");

            logger.info("Загружены туры категории 'Шоппинг', количество: {}", tours.size());

            request.getRequestDispatcher("shopping-panel.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Ошибка при загрузке туров категории 'Шоппинг'", e);
            response.setContentType("text/html");
            response.getWriter().println("<h1>Ошибка</h1><p>" + e.getMessage() + "</p>");
        }
    }
}
