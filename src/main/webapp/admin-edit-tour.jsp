<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Редактировать тур</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/edit-tour.css">
</head>
<body>

<div class="admin-form-container">
    <h2>Редактировать тур</h2>

    <form action="admin-panel" method="post" class="edit-tour-form">
        <input type="hidden" name="action" value="saveTour">
        <input type="hidden" name="id" value="${tour.id}">

        <label for="title">Название:</label>
        <input type="text" id="title" name="title" value="${tour.title}" required>

        <label for="category">Категория:</label>
        <select id="category" name="category" required>
            <option value="1" ${tour.category == '1' ? 'selected' : ''}>Отдых</option>
            <option value="2" ${tour.category == '2' ? 'selected' : ''}>Шоппинг</option>
            <option value="3" ${tour.category == '3' ? 'selected' : ''}>Экскурсия</option>
        </select>

        <label for="description">Описание:</label>
        <textarea id="description" name="description" required>${tour.description}</textarea>

        <label for="price">Цена (до скидки, BYN):</label>
        <input type="number" id="price" name="price" step="0.01" value="${tour.price}" required>

        <label for="discount">Скидка (%):</label>
        <input type="number" id="discount" name="discount" min="0" max="100" step="1" value="${tour.discount}" required>

        <label for="is_hot">Горящий:</label>
        <input type="checkbox" id="is_hot" name="is_hot" <c:if test="${tour.hot}">checked</c:if>>

        <button type="submit" class="btn-submit">Сохранить</button>
    </form>

    <a href="admin-panel" class="back-link">⬅ Назад в панель администратора</a>
</div>

</body>
</html>
