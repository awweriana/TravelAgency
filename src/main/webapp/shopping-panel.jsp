<%@ page import="org.example.model.Tour" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/tours-style.css">
    <title>Туры</title>
</head>
<body>

<h2 class="page-title">Доступные туры</h2>
<a class="back-link" href="main">Назад</a>
<br><br>

<%
    List<Tour> tours = (List<Tour>) request.getAttribute("tours");

    if (tours == null || tours.isEmpty()) {
%>
<p>Нет доступных туров</p>
<%
} else {
%>
<table class="tour-table">
    <tr>
        <th>Название</th>
        <th>Описание</th>
        <th>Цена</th>
        <th>Скидка</th>
        <th>Горящий</th>
        <th>Действие</th>
    </tr>

    <%
        for (Tour t : tours) {
    %>
    <tr>
        <td class="tour-title"><b><%= t.getTitle() %></b></td>
        <td><%= t.getDescription() %></td>

        <%
            if (t.getDiscount() > 0) {
                double newPrice = t.getPrice() * (1 - t.getDiscount() / 100);
        %>
        <td>
            <span class="old-price"><%= t.getPrice() %> BYN</span><br>
            <b class="new-price"><%= String.format("%.2f", newPrice) %> BYN</b>
        </td>
        <td class="discount">-<%= t.getDiscount() %>%</td>
        <%
        } else {
        %>
        <td><b class="new-price"><%= t.getPrice() %> BYN</b></td>
        <td>-</td>
        <%
            }
        %>

        <%
            if (t.isHot()) {
        %>
        <td class="hot">🔥 Да</td>
        <%
        } else {
        %>
        <td>Нет</td>
        <%
            }
        %>

        <td>
            <form action="basket" method="post">
                <input type="hidden" name="tourId" value="<%= t.getId() %>">
                <input type="hidden" name="action" value="add">
                <button type="submit" class="buy-btn">Купить</button>
            </form>
        </td>
    </tr>
    <%
        }
    %>
</table>

<br>
<p class="total-count">Всего туров: <%= tours.size() %></p>
<%
    }
%>

</body>
</html>
