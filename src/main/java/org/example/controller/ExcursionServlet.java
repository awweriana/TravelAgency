package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.TourDAO;
import org.example.fabric.DAOFactory;
import org.example.model.Tour;

import java.io.IOException;
import java.util.List;

public class ExcursionServlet extends HttpServlet {

    private TourDAO tourDAO;

    @Override
    public void init() {
        // Используем фабрику для создания DAO
        tourDAO = DAOFactory.getTourDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Tour> tours = tourDAO.getToursByCategory(2);

            request.setAttribute("tours", tours);
            request.setAttribute("categoryName", "Экскурсии");

            request.getRequestDispatcher("excursion-panel.jsp").forward(request, response);

        } catch (Exception e) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<h1>Ошибка</h1><p>" + e.getMessage() + "</p>");
        }
    }
}
