package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.fabric.DAOFactory;
import org.example.dao.PurchasesDAO;
import org.example.dao.TourDAO;
import org.example.dao.UserDAO;
import org.example.model.Tour;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BasketServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(BasketServlet.class);

    private TourDAO tourDAO;
    private PurchasesDAO purchasesDAO;
    private UserDAO userDAO;

    @Override
    public void init() {
        // Получаем DAO через фабрику
        tourDAO = DAOFactory.getTourDAO();
        purchasesDAO = DAOFactory.getPurchasesDAO();
        userDAO = DAOFactory.getUserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            userId = 1;
            session.setAttribute("userId", userId);
            session.setAttribute("userName", "Тестовый Пользователь");
        }

        double userDiscountPercent = userDAO.getUserDiscount(userId);
        session.setAttribute("userDiscount", userDiscountPercent);

        logger.info("Пользователь {} открыл корзину, скидка: {}%", userId, userDiscountPercent);

        request.getRequestDispatcher("basket.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "";
        logger.info("BasketServlet POST action={}", action);

        switch (action) {
            case "add":
                addToBasket(request, response);
                break;
            case "remove":
                removeFromBasket(request, response);
                break;
            case "clear":
                clearBasket(request, response);
                break;
            case "checkout":
                checkout(request, response);
                break;
            case "processPayment":
                processPayment(request, response);
                break;
            default:
                logger.warn("Неизвестное действие: {}", action);
                response.sendRedirect("basket");
        }
    }

    private void addToBasket(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        List<Tour> basket = (List<Tour>) session.getAttribute("basket");
        if (basket == null) basket = new ArrayList<>();

        int tourId = Integer.parseInt(request.getParameter("tourId"));
        Tour tour = tourDAO.getTourById(tourId);
        if (tour != null) {
            basket.add(tour);
            session.setAttribute("basket", basket);
            session.setAttribute("basketCount", basket.size());
            logger.info("Добавлен тур {} в корзину пользователя {}", tourId, session.getAttribute("userId"));
        }
        response.sendRedirect(request.getHeader("Referer") != null ? request.getHeader("Referer") : "main.jsp");
    }

    private void removeFromBasket(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        List<Tour> basket = (List<Tour>) session.getAttribute("basket");
        if (basket != null) {
            int tourId = Integer.parseInt(request.getParameter("tourId"));
            basket.removeIf(t -> t.getId() == tourId);
            session.setAttribute("basket", basket);
            session.setAttribute("basketCount", basket.size());
            logger.info("Удалён тур {} из корзины пользователя {}", tourId, session.getAttribute("userId"));
        }
        response.sendRedirect("basket");
    }

    private void clearBasket(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("basket");
        session.setAttribute("basketCount", 0);
        logger.info("Корзина очищена пользователем {}", session.getAttribute("userId"));
        response.sendRedirect("basket");
    }

    private void checkout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("Пользователь {} инициировал оформление заказа", request.getSession().getAttribute("userId"));
        response.sendRedirect("payment.jsp");
    }

    private void processPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) userId = 1;

        Double userDiscountPercentObj = (Double) session.getAttribute("userDiscount");
        double userDiscountPercent = (userDiscountPercentObj != null) ?
                userDiscountPercentObj : userDAO.getUserDiscount(userId);

        if (userDiscountPercentObj == null) {
            session.setAttribute("userDiscount", userDiscountPercent);
        }

        List<Tour> basket = (List<Tour>) session.getAttribute("basket");
        if (basket == null || basket.isEmpty()) {
            response.sendRedirect("basket?error=" + URLEncoder.encode("Корзина пуста", "UTF-8"));
            return;
        }

        double totalAmount = 0;
        int itemsCount = 0;
        boolean allSaved = true;

        for (Tour t : basket) {
            double priceAfterTourDiscount = t.getPrice() * (100 - t.getDiscount()) / 100;
            double finalPrice = priceAfterTourDiscount * (100 - userDiscountPercent) / 100;
            finalPrice = Math.round(finalPrice * 100.0) / 100.0;
            totalAmount += finalPrice;
            itemsCount++;
            try {
                purchasesDAO.addPurchase(userId, t.getId(), finalPrice);
            } catch (Exception e) {
                allSaved = false;
                logger.error("Ошибка при добавлении покупки для пользователя {} и тура {}: {}", userId, t.getId(), e.getMessage());
                break;
            }
        }

        if (allSaved && itemsCount > 0) {
            basket.clear();
            session.setAttribute("basket", basket);
            session.setAttribute("basketCount", 0);
            logger.info("Заказ пользователя {} успешно сохранён, сумма: {} BYN, товаров: {}", userId, totalAmount, itemsCount);

            String redirectUrl = "success.jsp?total=" + totalAmount +
                    "&count=" + itemsCount +
                    "&userDiscount=" + userDiscountPercent;
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("basket?error=" + URLEncoder.encode("Ошибка при оформлении заказа", "UTF-8"));
        }
    }
}
