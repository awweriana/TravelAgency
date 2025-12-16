<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Админ панель</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-panel.css">
</head>
<body class="admin-page">

<h2>Привет, Админ!</h2>
<a href="user-login" class="admin-logout">Выход</a>

<hr>
<h2>Управление турами</h2>

<table class="admin-table">
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Категория</th>
        <th>Описание</th>
        <th>Цена</th>
        <th>Горящий</th>
        <th>Скидка</th>
        <th>Действия</th>
    </tr>

    <c:forEach var="t" items="${tours}">
        <tr>
            <td>${t.id}</td>
            <td>${t.title}</td>
            <td>${t.category}</td>
            <td>${t.description}</td>
            <td>${t.price}</td>
            <td>${t.hot ? "🔥Да" : "Нет"}</td>
            <td>${t.discount}%</td>
            <td>
                <a href="admin-panel?action=editTour&id=${t.id}" class="admin-btn admin-btn-edit">Редактировать</a>
                <a href="admin-panel?action=deleteTour&id=${t.id}" class="admin-btn admin-btn-delete">Удалить</a>
                <form action="admin-panel" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="toggleHot"/>
                    <input type="hidden" name="id" value="${t.id}"/>
                    <input type="hidden" name="isHot" value="${!t.hot}"/>
                    <button type="submit" class="admin-btn admin-btn-hot">
                            ${t.hot ? "Сделать обычным" : "Сделать горячим🔥"}
                    </button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<a href="admin-panel?action=addTour" class="admin-btn admin-btn-add">Добавить тур</a>

<hr>
<h2>Пользователи</h2>

<table class="admin-table">
    <tr>
        <th>ID</th>
        <th>Имя</th>
        <th>Роль</th>
        <th>Скидка</th>
        <th>Действия</th>
    </tr>

    <c:forEach var="u" items="${users}">
        <tr>
            <td>${u.id}</td>
            <td>${u.username}</td>
            <td>${u.role}</td>
            <td>${u.discount}%</td>
            <td>
                <a href="admin-panel?action=discountUser&id=${u.id}" class="admin-btn admin-btn-discount">Изменить скидку</a>
            </td>
        </tr>
    </c:forEach>
</table>

<hr>
<h2>История покупок</h2>

<table class="admin-table">
    <tr>
        <th>ID</th>
        <th>Пользователь</th>
        <th>Тур</th>
        <th>Итоговая цена</th>
        <th>Дата покупки</th>
        <th>Скидка</th>
    </tr>

    <c:forEach var="p" items="${purchases}">
        <tr>
            <td>${p.id}</td>
            <td>${p.username}</td>
            <td>${p.tourTitle}</td>
            <td>${p.finalPrice} BYN</td>
            <td>${p.purchaseDate}</td>
            <td>${p.userDiscount}%</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
