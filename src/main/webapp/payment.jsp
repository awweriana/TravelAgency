<%@ page import="org.example.model.Tour" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.dao.UserDAO" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    // Проверка авторизации
    Integer userId = (Integer) session.getAttribute("userId");
    if (userId == null) {
        response.sendRedirect("auth-check");
        return;
    }

    // Получаем скидку пользователя
    double userDiscountPercent = 0;
    UserDAO userDAO = new UserDAO();
    userDiscountPercent = userDAO.getUserDiscount(userId);

    // Проверка корзины
    List<Tour> basket = (List<Tour>) session.getAttribute("basket");
    if (basket == null || basket.isEmpty()) {
        response.sendRedirect("basket?error=Корзина пуста");
        return;
    }

    // Расчет итогов
    double totalWithoutDiscount = 0;
    double totalWithDiscount = 0;
    double totalSaved = 0;

    for (Tour tour : basket) {
        double originalPrice = tour.getPrice();
        double tourDiscountPercent = tour.getDiscount();

        double priceAfterTourDiscount = originalPrice * (100 - tourDiscountPercent) / 100;
        double finalPrice = priceAfterTourDiscount * (100 - userDiscountPercent) / 100;
        finalPrice = Math.round(finalPrice * 100.0) / 100.0;

        totalWithoutDiscount += originalPrice;
        totalWithDiscount += finalPrice;
        totalSaved += (originalPrice - finalPrice);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Оплата заказа</title>
    <link rel="stylesheet" href="css/payment.css">
</head>
<body>
<div class="payment-container">
    <header class="payment-header">
        <h1>💳 Оплата заказа</h1>
        <a href="basket" class="back-link">← Назад в корзину</a>
    </header>

    <% if (request.getParameter("error") != null) { %>
    <div class="alert alert-error">
        ❌ <%= request.getParameter("error") %>
    </div>
    <% } %>

    <% if (request.getParameter("success") != null) { %>
    <div class="alert alert-success">
        ✅ <%= request.getParameter("success") %>
    </div>
    <% } %>

    <section class="order-summary">
        <h2>Ваш заказ</h2>

        <% if (userDiscountPercent > 0) { %>
        <div class="user-discount-banner">
            🎁 <b>Ваша личная скидка: <%= userDiscountPercent %>%</b>
            <small>Применяется ко всем товарам</small>
        </div>
        <% } %>

        <div class="tour-items">
            <%
                for (Tour tour : basket) {
                    double originalPrice = tour.getPrice();
                    double tourDiscountPercent = tour.getDiscount();

                    double priceAfterTourDiscount = originalPrice * (100 - tourDiscountPercent) / 100;
                    double finalPrice = priceAfterTourDiscount * (100 - userDiscountPercent) / 100;
                    finalPrice = Math.round(finalPrice * 100.0) / 100.0;
            %>
            <div class="tour-item">
                <h3><%= tour.getTitle() %></h3>
                <div class="price-info">
                    <span class="price-old"><%= String.format("%.2f", originalPrice) %> BYN</span>
                    <span class="price-arrow">→</span>
                    <span class="price-new"><%= String.format("%.2f", finalPrice) %> BYN</span>
                </div>
                <div class="discount-tags">
                    <% if (tourDiscountPercent > 0) { %>
                    <span class="discount-tag tour-discount">Скидка тура: -<%= tourDiscountPercent %>%</span>
                    <% } %>
                    <% if (userDiscountPercent > 0) { %>
                    <span class="discount-tag user-discount">Ваша скидка: -<%= userDiscountPercent %>%</span>
                    <% } %>
                </div>
            </div>
            <% } %>
        </div>
    </section>

    <section class="total-summary">
        <h2>Итоговая сумма</h2>
        <div class="total-amount">
            <%= String.format("%.2f", totalWithDiscount) %> BYN
        </div>
        <% if (totalSaved > 0) { %>
        <div class="savings-info">
            <div class="savings-amount">
                Экономия: <span class="savings-value"><%= String.format("%.2f", totalSaved) %> BYN</span>
            </div>
            <div class="savings-percent">
                (<%= String.format("%.1f", (totalSaved / totalWithoutDiscount) * 100) %>%)
            </div>
        </div>
        <% } %>
    </section>

    <section class="payment-form-section">
        <h2>Выберите способ оплаты</h2>
        <form action="basket" method="post" class="payment-form">
            <input type="hidden" name="action" value="processPayment">
            <input type="hidden" name="total" value="<%= String.format("%.2f", totalWithDiscount) %>">

            <div class="form-group">
                <select id="paymentMethod" name="paymentMethod" required onchange="toggleCardFields()">
                    <option value="">-- Выберите способ оплаты --</option>
                    <option value="card">💳 Банковская карта</option>
                    <option value="cash">💵 Наличные при получении</option>
                </select>
            </div>

            <div id="cardFields" class="card-fields">
                <h3>Данные карты</h3>
                <div class="form-group">
                    <label for="cardNumber">Номер карты</label>
                    <input type="text" id="cardNumber" name="cardNumber"
                           placeholder="0000 0000 0000 0000" maxlength="19">
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="expiryDate">Срок действия (ММ/ГГ)</label>
                        <input type="text" id="expiryDate" name="expiryDate"
                               placeholder="MM/YY" maxlength="5">
                    </div>
                    <div class="form-group">
                        <label for="cvv">CVV код</label>
                        <input type="password" id="cvv" name="cvv"
                               placeholder="123" maxlength="3">
                    </div>
                </div>
            </div>

            <button type="submit" class="submit-btn">
                ✅ Подтвердить и оплатить
            </button>
        </form>
    </section>
</div>

<script>
    function toggleCardFields() {
        var paymentMethod = document.getElementById('paymentMethod').value;
        var cardFields = document.getElementById('cardFields');
        var cardInputs = document.querySelectorAll('#cardFields input');

        if (paymentMethod === 'card') {
            cardFields.style.display = 'block';
            cardInputs.forEach(function(input) {
                input.required = true;
            });
        } else {
            cardFields.style.display = 'none';
            cardInputs.forEach(function(input) {
                input.required = false;
                input.value = '';
            });
        }
    }
</script>
</body>
</html>