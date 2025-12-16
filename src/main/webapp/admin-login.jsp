<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход администратора</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminlog-style.css">
</head>
<body class="admin-page">

<div class="admin-box">
    <h2 class="admin-box-title">Вход для администратора</h2>

    <form action="admin-login" method="post" class="admin-box-form">
        <input type="hidden" name="action" value="loginAction">

        <label>Логин:</label>
        <input type="text" name="login" required>

        <label>Пароль:</label>
        <input type="password" name="password" required>

        <button type="submit" class="admin-btn admin-btn-login">Войти</button>
    </form>

    <%-- сообщение об ошибке --%>
    <%
        String message = (String) request.getAttribute("errorLoginPassMessage");
        if (message != null) {
    %>
    <p class="admin-msg-error"><%= message %></p>
    <% } %>

    <a href="user-login" class="admin-btn admin-btn-back">← Назад</a>
</div>

</body>
</html>
