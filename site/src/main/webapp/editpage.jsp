<%--
  Created by IntelliJ IDEA.
  User: nico
  Date: 05/08/20
  Time: 16.20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
${utente.codiceFiscale} - ${utente.authorities}
<br><br><br>


<form class="user" method="post" action="/edit-user">
    <div class="form-group">
        <input type="hidden" name="role", value="${utente.authorities}", th:field="*{role}">
        <input type="text" class="form-control form-control-user"
               name="codiceFiscale" id="codiceFiscale" value="${utente.codiceFiscale}"
               placeholder="Codice fiscale" required="required"
               th:field="*{codiceFiscale}">
        <br>
        <input type="text" value="${utente.nome}"
               class="form-control form-control-user" name="nome" th:field="*{nome}"
               id="nome" placeholder="Nome" required="required">
        <br>
        <input type="text" value="${utente.cognome}"
               class="form-control form-control-user" name="cognome" th:field="*{cognome}"
               id="cognome" placeholder="Cognome" required="required">
        <br>
        <select title="Sesso" name="sesso" h:field="*{sesso}">
            <option value="M">Uomo</option>
            <option value="F">Donna</option>
        </select>

    </div>

    <button class="btn btn-primary btn-user btn-block mt-3"
            id="accediAdminButton">Salva
    </button>
</form>


</body>
</html>
