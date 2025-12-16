<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Добавить тур</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/add-tour.css">
</head>
<body>

<div class="admin-form-container">
    <h2>Добавить новый тур</h2>

    <form action="admin-panel" method="post" class="add-tour-form">
        <input type="hidden" name="action" value="createTour"/>

        <label for="title">Название:</label>
        <input type="text" id="title" name="title" required>

        <label for="category">Категория:</label>
        <select id="category" name="category" required>
            <option value="1">Отдых</option>
            <option value="2">Шоппинг</option>
            <option value="3">Экскурсия</option>
        </select>

        <label for="description">Описание:</label>
        <textarea id="description" name="description" required></textarea>

        <label for="price">Цена (BYN):</label>
        <input type="number" id="price" name="price" step="0.01" required>

        <label for="discount">Скидка (%):</label>
        <input type="number" id="discount" name="discount" min="0" max="100" step="1" required>

        <button type="submit" class="btn-submit">Добавить тур</button>
    </form>

    <a href="admin-panel" class="back-link">⬅ Назад в панель администратора</a>
</div>

</body>
</html>
