<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Вход для оформления заказа</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body>

<div class="auth-container">
    <h2>Вход для оформления заказа</h2>

    <% if (request.getParameter("error") != null) { %>
    <p class="error-msg"><%= request.getParameter("error") %></p>
    <% } %>

    <form action="auth-check" method="post" class="auth-form">
        <div class="form-group">
            <label>Логин:</label>
            <input type="text" name="username" required>
        </div>

        <div class="form-group">
            <label>Пароль:</label>
            <input type="password" name="password" required>
        </div>

        <button type="submit" class="btn btn-login">Войти и оформить заказ</button>
    </form>

    <a href="basket" class="back-link">← Назад в корзину</a>
</div>

</body>
</html>
