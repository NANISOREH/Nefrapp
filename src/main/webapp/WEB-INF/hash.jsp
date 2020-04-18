
<%@ page language="java" %>
<html>
  <head>
    <title>Hash</title>
  </head>
  <body>
    <form class="user">
        <input type="hidden" id="hashedpass" name="password"
        value="loginUtente">
        <div class="form-group">
            <input type="text" class="form-control form-control-user"
                name="codiceFiscale" id="codiceFiscale"
                placeholder="Codice fiscale" required="required">
        </div>
        <div class="form-group">
            <input type="password"
                class="form-control form-control-user"
                id="password" placeholder="Password" required="required"
                >
        </div>
        <button type="button" onclick="doHashing()" class="btn btn-primary btn-user btn-block mt-3"
            id="accediAdminButton">Calcola</button>

    </form>
  </body>
  <script>
    function doHashing() {
        var pass = $("#password").val();
        $("#hashedpass").val(hashPassword(pass, pass + $("#codiceFiscale").val()));
        alert($("#hashedpass").val());
    }

    function hashPassword(plain, salt) {
        var key = CryptoJS.PBKDF2(plain, salt, {
            keySize: 512 / 32,
            iterations: 1000
        });

        var hashedPassword = CryptoJS.SHA3(key, { outputLength: 512 });
        return hashedPassword;
    }
  </script>
  <%@include file="/includes/scripts.jsp"%>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js"></script>
</html>
