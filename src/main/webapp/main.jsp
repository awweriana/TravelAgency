<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Главная страница</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="header">Тур-Агенство "Pink Voyage"</div>
<div class="subheader">Добро пожаловать, <%= session.getAttribute("username") %>!</div>

<div class="links">
    <a class="basket-link" href="basket">
        🛒 Корзина
        <%
            Integer basketCount = (Integer) session.getAttribute("basketCount");
            if (basketCount != null && basketCount > 0) {
        %>
        (<%= basketCount %>)
        <% } %>
    </a>
</div>

<div class="cards">
    <a class="card" href="rest-panel">Отдых</a>
    <a class="card" href="excursion-panel">Экскурсии</a>
    <a class="card" href="shopping-panel">Шоппинг</a>
</div>

<a class="logout-link" href="user-login">Выход</a>

</body>
</html>
