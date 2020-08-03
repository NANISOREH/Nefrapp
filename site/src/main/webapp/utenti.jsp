<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="user" method="post" th:action="@{/utenti}" th:object="${formitem}">
    <input type="hidden" id="hashedpass" name="password"
           value="loginUtente" th:field="*{password}">
    <div class="form-group">
        <input type="text" class="form-control form-control-user"
               name="codiceFiscale" id="codiceFiscale"
               placeholder="Codice fiscale" required="required"
               th:field="*{codiceFiscale}">
    </div>
    <div class="form-group">
        <input type="password"
               class="form-control form-control-user"
               id="password" placeholder="Password" required="required">
    </div>
    <div class="form-group">
        <select title="Ruolo" name="role">
            <option value="ROLE_MEDICO">Medico</option>
            <option value="ROLE_PAZIENTE">Paziente</option>
            <option value="ROLE_ADMIN">Amministratore</option>
        </select>
    </div>

    <button class="btn btn-primary btn-user btn-block mt-3"
            id="accediAdminButton">Aggiungi
    </button>
</form>

<br><br>
<c:forEach var="item" items="${utenti}">
    ${item.codiceFiscale}
    ${item.password}
    ${item.authorities}
    <button class="btn btn-primary btn-user btn-block mt-3"
            value="${item.codiceFiscale}" id="cancUtenteBtn"
            onclick="deleteUser(this)">Rimuovi
    </button>
    <br><br>
</c:forEach>

</body>
<%@include file="/includes/scripts.jsp" %>
<script src="/js/utenti.js"></script>
<script src="/js/ParameterControl.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js"></script>
</html>