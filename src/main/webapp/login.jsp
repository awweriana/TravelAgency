<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Вход в аккаунт</title>
    <link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body>

<div class="login-container">
    <h1>Вход в аккаунт</h1>

    <% if (request.getParameter("error") != null) { %>
    <p class="error-message"><%= request.getParameter("error") %></p>
    <% } %>

    <form action="user-login" method="post">
        <input type="hidden" name="action" value="loginAction">

        <div class="form-group">
            <label for="username">Логин:</label><br>
            <input type="text" id="username" name="username" required>
        </div>

        <div class="form-group">
            <label for="password">Пароль:</label><br>
            <input type="password" id="password" name="password" required>
        </div>

        <button type="submit">Войти</button>
    </form>

    <a href="main">На главную</a>
</div>

</body>
</html>
