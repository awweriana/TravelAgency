package org.example.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.PurchasesDAO;
import org.example.fabric.DAOFactory;
import org.example.model.Purchases;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class PurchasesServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(PurchasesServlet.class);

    private PurchasesDAO purchasesDAO;

    @Override
    public void init() {
        purchasesDAO = DAOFactory.getPurchasesDAO();
        logger.info("PurchasesServlet инициализирован");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Загружаем все покупки через фабрику DAO
            List<Purchases> purchases = purchasesDAO.getAllPurchases();
            request.setAttribute("purchases", purchases);

            logger.info("Загружен список всех покупок, всего: {}", purchases.size());

            // Перенаправляем на JSP админ-панели
            request.getRequestDispatcher("admin-panel.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Ошибка при загрузке списка покупок: {}", e.getMessage(), e);
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<h1>Ошибка при загрузке покупок</h1><p>" + e.getMessage() + "</p>");
        }
    }
}
