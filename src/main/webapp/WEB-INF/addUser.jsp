
<%@ page language="java" %>
<html>
  <head>
    <title>Hash</title>
  </head>
  <body>
    <form class="user" method="post" th:action="@{/addUser}" th:object="${formitem}">
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
        <button class="btn btn-primary btn-user btn-block mt-3"
            id="accediAdminButton">Aggiungi</button>
    </form>
  </body>
  <%@include file="/includes/scripts.jsp"%>
  <script src="/js/ParameterControl.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js"></script>
</html>
