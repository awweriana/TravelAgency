<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Изменить скидку</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/discount-user.css">
</head>
<body>

<div class="discount-container">
    <h2>Изменить скидку пользователю</h2>

    <form action="admin-panel" method="post" class="discount-form">
        <input type="hidden" name="action" value="saveDiscount"/>
        <input type="hidden" name="id" value="${user.id}"/>

        <p>Пользователь: <b>${user.username}</b></p>
        <p>Текущая скидка: <b>${user.discount}%</b></p>

        <label for="discount">Новая скидка:</label>
        <input type="number" id="discount" name="discount" min="0" max="100" required> %

        <button type="submit" class="btn-save">Сохранить</button>
    </form>

    <a href="admin-panel" class="btn-back">⬅ Назад</a>
</div>

</body>
</html>
