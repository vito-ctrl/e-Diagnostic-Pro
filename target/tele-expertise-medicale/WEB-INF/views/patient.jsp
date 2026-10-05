<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Tele-Expertise Médicale</title>
</head>
<body>

     <c:forEach var="p" items="${patients}" >
        <p>${p.getNom() } | ${ p.getPrenom() } | ${ p.getTelephone() } | ${ p.getMutuelle()}</p>
     </c:forEach>
</body>
</html>