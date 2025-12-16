<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация пользователя</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reg.css">
</head>
<body class="auth-page">

<div class="auth-box">
    <h2 class="auth-box-title">Регистрация</h2>

    <form action="register" method="post" class="auth-box-form">
        <input type="hidden" name="action" value="registerAction">

        <label for="username">Логин:</label>
        <input type="text" id="username" name="username" required>

        <label for="password">Пароль:</label>
        <input type="password" id="password" name="password" required>

        <button type="submit" class="auth-btn auth-btn-register">Зарегистрироваться</button>
    </form>

    <%-- вывод ошибок/успеха --%>
    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        String successMessage = (String) request.getAttribute("successMessage");
    %>

    <% if (errorMessage != null) { %>
    <p class="auth-msg-error"><%= errorMessage %></p>
    <% } %>

    <% if (successMessage != null) { %>
    <p class="auth-msg-success"><%= successMessage %></p>
    <% } %>

    <a href="user-login" class="auth-btn auth-btn-back">← Выйти</a>
</div>

</body>
</html>
