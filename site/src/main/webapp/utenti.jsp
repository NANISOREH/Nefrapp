<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Debug Utenti</title>
</head>
<body>

<div class="card">
    <h5 class="card-title">Aggiunta utente</h5>
    <div class="card-body" style="width: 18rem;">
        <form class="user" method="post" th:action="@{/utenti}" th:object="${formitem}">
            <input type="hidden" id="hashedpass" name="password"
                   value="loginUtente" th:field="*{password}">
            <div class="form-group">
                <input type="text" class="form-control form-control-user"
                       name="codiceFiscale" id="codiceFiscale"
                       placeholder="Codice fiscale" required="required"
                       th:field="*{codiceFiscale}">
                <br>
                <input type="password"
                       class="form-control form-control-user"
                       id="password" placeholder="Password" required="required">

            <br>
                <input type="text"
                       class="form-control form-control-user" name="nome" th:field="*{nome}"
                       id="nome" placeholder="Nome" required="required">
                <br>
                <input type="text"
                       class="form-control form-control-user" name="cognome" th:field="*{cognome}"
                       id="cognome" placeholder="Cognome" required="required">
                <br>
                <br>
                <select title="Sesso" name="sesso">
                    <option value="M">Uomo</option>
                    <option value="F">Donna</option>
                </select>
                <select title="Ruolo" name="role">
                    <option value="MEDICO">Medico</option>
                    <option value="PAZIENTE">Paziente</option>
                    <option value="ADMIN">Amministratore</option>
                </select>
            </div>

            <button class="btn btn-primary btn-user btn-block mt-3"
                    id="accediAdminButton">Aggiungi
            </button>
        </form>
    </div>
</div>

<br><br>
<c:forEach var="item" items="${utenti}">
    ${item.codiceFiscale}
    ${item.nome}
    ${item.cognome}
    ${item.authorities}
    <c:if test = "${!item.authorities.equals('ADMIN')}">
        <button class="btn btn-primary btn-user btn-block mt-3"
                value="${item.codiceFiscale}" id="cancUtenteBtn"
                onclick="deleteUser(this)">Rimuovi
        </button>
        <button class="btn btn-primary btn-user btn-block mt-3"
                value="${item.codiceFiscale}" id="editUtenteBtn"
                onclick="window.location.href='/editpage/${item.codiceFiscale}';">Modifica
        </button>
    </c:if>
    <br><br>
</c:forEach>

</body>
<%@include file="/includes/scripts.jsp" %>
<script src="/js/utenti.js"></script>
<script src="/js/ParameterControl.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js"></script>
</html>