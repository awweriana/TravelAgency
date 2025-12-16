<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.model.Tour" %>
<%@ page import="org.example.dao.UserDAO" %>
<!DOCTYPE html>
<html>
<head>
    <title>Успешно</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/success.css">
</head>
<body>

<div class="success-container">
    <h1>✅ Заказ успешно оформлен!</h1>

    <%
        String total = request.getParameter("total");
        String count = request.getParameter("count");

        List<Tour> basket = (List<Tour>) session.getAttribute("basket");

        Double userDiscountPercent = (Double) session.getAttribute("userDiscount");
        if (userDiscountPercent == null) {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId != null) {
                UserDAO userDAO = new UserDAO();
                userDiscountPercent = userDAO.getUserDiscount(userId);
                session.setAttribute("userDiscount", userDiscountPercent);
            } else {
                userDiscountPercent = 0.0;
            }
        }
    %>

    <p class="message">Ваш заказ принят в обработку.</p>

    <% if (total != null) { %>
    <p class="order-info"><strong>Сумма заказа: <%= total %> BYN</strong></p>
    <% } %>

    <% if (count != null) { %>
    <p class="order-info">Количество товаров: <%= count %></p>
    <% } %>

    <% if (userDiscountPercent > 0) { %>
    <p class="order-info">Ваша личная скидка: <%= userDiscountPercent %>%</p>
    <% } %>

    <div class="buttons-row">
        <a href="main" class="btn btn-back">Вернуться на главную</a>
        <a href="basket" class="btn btn-basket">Перейти в корзину</a>
    </div>
</div>

</body>
</html>
