package org.example.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.example.dao.TourDAO;
import org.example.dao.UserDAO;
import org.example.dao.PurchasesDAO;
import org.example.fabric.DAOFactory;
import org.example.model.Tour;
import org.example.model.User;
import org.example.strategy.CombinedDiscountStrategy;
import org.example.strategy.DiscountStrategy;

import java.io.IOException;

public class AdminMainServlet extends HttpServlet {

    private TourDAO tourDAO;
    private UserDAO userDAO;
    private PurchasesDAO purchasesDAO;

    @Override
    public void init() {
        // Используем фабрику для создания DAO
        tourDAO = DAOFactory.getTourDAO();
        userDAO = DAOFactory.getUserDAO();
        purchasesDAO = DAOFactory.getPurchasesDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            loadAdminPage(request, response);
        }else {

        switch (action) {

            case "deleteTour":
                tourDAO.deleteTour(Integer.parseInt(request.getParameter("id")));
                response.sendRedirect("admin-panel");
                return;

            case "makeHot":
                tourDAO.setHot(Integer.parseInt(request.getParameter("id")), true);
                response.sendRedirect("admin-panel");
                return;

            case "discountUser":
                User user = userDAO.getUserById(Integer.parseInt(request.getParameter("id")));
                request.setAttribute("user", user);
                request.getRequestDispatcher("admin-edit-discount.jsp").forward(request, response);
                return;

            case "addTour":
                request.getRequestDispatcher("admin-add-tour.jsp").forward(request, response);
                return;

            case "editTour":
                Tour tour = tourDAO.getTourById(Integer.parseInt(request.getParameter("id")));
                request.setAttribute("tour", tour);
                request.getRequestDispatcher("admin-edit-tour.jsp").forward(request, response);
                return;

            default:
                loadAdminPage(request, response);
                }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        switch (action) {

            case "saveDiscount":
                int id = Integer.parseInt(request.getParameter("id"));
                double discount = Double.parseDouble(request.getParameter("discount"));
                userDAO.updateDiscount(id, discount);
                response.sendRedirect("admin-panel");
                break;

            case "toggleHot": {
                try {
                    int Togid = Integer.parseInt(request.getParameter("id"));
                    boolean isHot = Boolean.parseBoolean(request.getParameter("isHot"));
                    tourDAO.setHot(Togid, isHot);
                    response.sendRedirect("admin-panel");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Некорректный id тура");
                    request.getRequestDispatcher("admin-panel").forward(request, response);
                }
                break;
            }

            case "saveTour":
                Tour tour = new Tour();
                tour.setId(Integer.parseInt(request.getParameter("id")));
                tour.setTitle(request.getParameter("title"));
                tour.setCategory(request.getParameter("category"));
                tour.setDescription(request.getParameter("description"));
                tour.setPrice(Double.parseDouble(request.getParameter("price")));
                tour.setDiscount(Double.parseDouble(request.getParameter("discount")));
                tour.setHot(request.getParameter("is_hot") != null);

                tourDAO.updateTour(tour);
                response.sendRedirect("admin-panel");
                break;

            case "createTour":
                Tour newTour = new Tour();
                newTour.setTitle(request.getParameter("title"));
                newTour.setCategory(request.getParameter("category"));
                newTour.setDescription(request.getParameter("description"));
                newTour.setPrice(Double.parseDouble(request.getParameter("price")));
                newTour.setDiscount(Double.parseDouble(request.getParameter("discount")));
                newTour.setHot(request.getParameter("is_hot") != null);

                tourDAO.addTour(newTour);
                response.sendRedirect("admin-panel");
                break;

            default:
                response.sendRedirect("admin-panel");
        }
    }

    private void loadAdminPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("tours", tourDAO.getAllTour());
        request.setAttribute("users", userDAO.getAllUsers());

        DiscountStrategy strategy = new CombinedDiscountStrategy();
        request.setAttribute("purchases", purchasesDAO.getPurchaseHistoryWithCalculations(strategy));

        // Форвардим на JSP, а не на сам сервлет
        request.getRequestDispatcher("/admin-panel.jsp").forward(request, response);
    }
}
