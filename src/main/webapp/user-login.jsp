<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Вход пользователя</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth-style.css">
</head>
<body>
<div class="auth-container">
    <div class="auth-card">
        <h1 class="auth-title">Вход пользователя</h1>

        <% if (request.getParameter("error") != null) { %>
        <div class="alert alert-error">
            <%= request.getParameter("error") %>
        </div>
        <% } %>

        <div class="auth-form"> <!-- ДОБАВИЛ div с классом auth-form -->
            <form action="user-login" method="post">
                <input type="hidden" name="action" value="loginAction">

                <div class="form-group">
                    <label for="username" class="form-label">Логин</label>
                    <input type="text" id="username" name="username"
                           class="form-input" placeholder="Введите ваш логин" required>
                </div>

                <div class="form-group">
                    <label for="password" class="form-label">Пароль</label>
                    <input type="password" id="password" name="password"
                           class="form-input" placeholder="Введите ваш пароль" required>
                </div>

                <button type="submit" class="btn btn-primary btn-block">
                    Войти
                </button>
            </form>
        </div> <!-- Закрывающий div для auth-form -->

        <div class="auth-divider">
            <span>или</span>
        </div>

        <div class="auth-form"> <!-- ДОБАВИЛ div с классом auth-form -->
            <form action="user-login" method="post">
                <input type="hidden" name="action" value="registerAction">
                <button type="submit" class="btn btn-secondary btn-block">
                    Создать аккаунт
                </button>
            </form>
        </div> <!-- Закрывающий div для auth-form -->

        <div class="auth-links">
            <a href="admin-login" class="auth-link">
                Войти как администратор
            </a>
        </div>
    </div>
</div>
</body>
</html>