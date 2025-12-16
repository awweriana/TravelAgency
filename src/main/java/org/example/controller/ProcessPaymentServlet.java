package org.example.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.PurchasesDAO;
import org.example.dao.UserDAO;
import org.example.fabric.DAOFactory;
import org.example.model.Tour;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class ProcessPaymentServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ProcessPaymentServlet.class);

    private PurchasesDAO purchaseDAO;
    private UserDAO userDAO;

    @Override
    public void init() {
        purchaseDAO = DAOFactory.getPurchasesDAO();
        userDAO = DAOFactory.getUserDAO();
        logger.info("Сервлет ProcessPaymentServlet инициализирован");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("GET-запрос на /process-payment, перенаправление на basket.jsp");
        request.getRequestDispatcher("basket.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("POST-запрос на /process-payment начат");

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            logger.warn("Пользователь не авторизован, перенаправление на страницу входа");
            String errorMsg = URLEncoder.encode("Вы не авторизованы", "UTF-8");
            response.sendRedirect("user-login?error=" + errorMsg);
            return;
        }

        List<Tour> basket = (List<Tour>) session.getAttribute("basket");
        if (basket == null || basket.isEmpty()) {
            logger.warn("Корзина пуста для пользователя userId={}", userId);
            String errorMsg = URLEncoder.encode("Корзина пуста", "UTF-8");
            response.sendRedirect("basket?error=" + errorMsg);
            return;
        }

        double userDiscountPercent = 0;
        if (session.getAttribute("userDiscount") != null) {
            userDiscountPercent = (Double) session.getAttribute("userDiscount");
        } else {
            userDiscountPercent = userDAO.getUserDiscount(userId);
            session.setAttribute("userDiscount", userDiscountPercent);
        }

        double totalAmount = 0;
        int itemsCount = 0;
        boolean allSaved = true;

        for (Tour tour : basket) {
            double priceAfterTourDiscount = tour.getPrice() * (100 - tour.getDiscount()) / 100;
            double finalPrice = priceAfterTourDiscount * (100 - userDiscountPercent) / 100;
            finalPrice = Math.round(finalPrice * 100.0) / 100.0;
            totalAmount += finalPrice;
            itemsCount++;

            boolean saved = purchaseDAO.addPurchase(userId, tour.getId(), finalPrice);
            if (!saved) {
                allSaved = false;
                logger.error("Ошибка сохранения покупки: userId={}, tourId={}", userId, tour.getId());
                break;
            } else {
                logger.info("Покупка сохранена: userId={}, tourId={}, цена={}", userId, tour.getId(), finalPrice);
            }
        }

        if (allSaved && itemsCount > 0) {
            session.removeAttribute("basket");
            session.setAttribute("basketCount", 0);

            logger.info("Все покупки успешно сохранены для пользователя userId={}, сумма={}, количество={}",
                    userId, totalAmount, itemsCount);

            String redirectUrl = "success.jsp?total=" + String.format("%.2f", totalAmount) +
                    "&count=" + itemsCount;
            response.sendRedirect(redirectUrl);
        } else {
            logger.warn("Ошибка сохранения покупок для пользователя userId={}", userId);
            String errorMsg = URLEncoder.encode("Ошибка при сохранении заказа", "UTF-8");
            response.sendRedirect("basket?error=" + errorMsg);
        }
    }
}
