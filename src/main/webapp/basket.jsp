<%@ page import="org.example.model.Tour" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.dao.UserDAO" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%

    Integer userId = (Integer) session.getAttribute("userId"); // ✅
    if (userId == null) {
        userId = 1;
        session.setAttribute("userId", userId);
        session.setAttribute("userName", "Тестовый Пользователь");
    }

    UserDAO userDAO = new UserDAO();
    double userDiscountPercent = 0;
    if (session.getAttribute("userDiscount") != null) {
        userDiscountPercent = (Double) session.getAttribute("userDiscount");
    } else {
        userDiscountPercent = userDAO.getUserDiscount(userId);
        session.setAttribute("userDiscount", userDiscountPercent);
    }

    List<Tour> basket = (List<Tour>) session.getAttribute("basket");
%>


<!DOCTYPE html>
<html>
<head>
    <title>Корзина покупок</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/basket.css">
</head>
<body>

<h1 class="page-title">🛒 Корзина покупок</h1>

<div class="buttons-row">
    <a href="main" class="btn btn-back">← Вернуться назад</a>
</div>

<%
    if (basket == null || basket.isEmpty()) {
%>
<p class="empty-msg">Корзина пуста</p>
<%
} else {
    double totalWithoutDiscount = 0;
    double totalWithDiscount = 0;
    double totalSaved = 0;
%>

<table class="basket-table">
    <tr>
        <th>Тур</th>
        <th>Описание</th>
        <th>Цена</th>
        <th>Скидка тура</th>
        <th>Ваша скидка</th>
        <th>Итоговая цена</th>
        <th>Действия</th>
    </tr>

    <%
        for (Tour t : basket) {
            double originalPrice = t.getPrice();
            double tourDiscountPercent = t.getDiscount();
            double priceAfterTourDiscount = originalPrice * (100 - tourDiscountPercent) / 100;
            double finalPrice = priceAfterTourDiscount * (100 - userDiscountPercent) / 100;
            finalPrice = Math.round(finalPrice * 100.0) / 100.0;
            totalWithoutDiscount += originalPrice;
            totalWithDiscount += finalPrice;
            totalSaved += (originalPrice - finalPrice);
    %>
    <tr>
        <td><b><%= t.getTitle() %></b></td>
        <td><%= t.getDescription() %></td>
        <td><%= String.format("%.2f", originalPrice) %> BYN</td>
        <td>
            <% if (tourDiscountPercent > 0) { %>
            <span class="discount">-<%= tourDiscountPercent %>%</span><br>
            <small>Экономия: <%= String.format("%.2f", originalPrice - priceAfterTourDiscount) %> BYN</small>
            <% } else { %>-<% } %>
        </td>
        <td>
            <% if (userDiscountPercent > 0) { %>
            <span class="user-discount">-<%= userDiscountPercent %>%</span><br>
            <small>Экономия: <%= String.format("%.2f", priceAfterTourDiscount - finalPrice) %> BYN</small>
            <% } else { %>Нет<% } %>
        </td>
        <td><b><%= String.format("%.2f", finalPrice) %> BYN</b></td>
        <td>
            <form action="basket" method="post" style="display:inline;">
                <input type="hidden" name="tourId" value="<%= t.getId() %>">
                <input type="hidden" name="action" value="remove">
                <button class="btn btn-remove" type="submit">Удалить</button>
            </form>
        </td>
    </tr>
    <% } %>

    <tr class="total-row">
        <td colspan="3"><b>ИТОГО:</b></td>
        <td colspan="2">
            <% if (totalSaved > 0) { %>
            <span class="discount">Вы экономите: <%= String.format("%.2f", totalSaved) %> BYN</span><br>
            <small>Общая скидка: <%= String.format("%.1f", (totalSaved / totalWithoutDiscount) * 100) %>%</small>
            <% } %>
        </td>
        <td colspan="2">
            <div>
                <s class="old-price"><%= String.format("%.2f", totalWithoutDiscount) %> BYN</s><br>
                <span class="new-price"><%= String.format("%.2f", totalWithDiscount) %> BYN</span>
            </div>
        </td>
    </tr>
</table>

<div class="buttons-row">
    <form action="basket" method="post" style="display:inline;">
        <input type="hidden" name="action" value="clear">
        <button class="btn btn-clear" type="submit">🗑️ Очистить корзину</button>
    </form>
    <a href="auth-check" class="btn btn-order">✅ Оформить заказ</a>
</div>

<p class="total-count">Всего товаров: <%= basket.size() %></p>

<% if (userDiscountPercent > 0) { %>
<div class="user-discount-info">
    🎁 Ваша личная скидка: <%= userDiscountPercent %>% — применяется ко всем товарам
</div>
<% } %>

<% } %>
</body>
</html>
