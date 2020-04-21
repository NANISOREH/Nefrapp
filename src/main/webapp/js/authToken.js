//costruisce i dati di autenticazione da inserire nella richiesta prelevando i cookie dedicati
// const myHeaders = new Headers({
//     'Content-Type': 'text/plain',
// });
// myHeaders.append("Authorization", getCookie("nefrapp_auth"))
// myHeaders.append("Role", getCookie("role"))
// setCookie("nefrapp_auth", getCookie("nefrapp_auth").replace("/", " "));
// myHeaders.append("Credentials", getCookie("credentials"));
// console.log(myHeaders.get("Authorization").toString() + "\n" + myHeaders.get("Role").toString() + "\n" + myHeaders.get("Credentials").toString())

//include il token di autenticazione negli headers della richiesta
//si attiva su tutte le richieste di utenti autenticati
//  if (getCookie("nefrapp_auth") != "" && getCookie("nefrapp_auth").includes("Bearer ")) {
//      fetch(document.URL, {
//          credentials: 'include',
//          headers: myHeaders,
//          mode: "cors"
//      })
//  }


//cancella il cookie contenente il token di autenticazione nel caso in cui si effettui il logout
if (document.URL.includes("logout")) {
    setCookie("nefrapp_auth", '', -1);
}




//funzioni di set-get dei cookie
function setCookie(name, value, days) {
    var d = new Date;
    d.setTime(d.getTime() + 24*60*60*1000*days);
    document.cookie = name + "=" + value + ";path=/;expires=" + d.toGMTString();
}
// function getCookie(cname) {
//     var name = cname + "=";
//     var decodedCookie = decodeURIComponent(document.cookie);
//     var ca = decodedCookie.split(';');
//     for(var i = 0; i <ca.length; i++) {
//         var c = ca[i];
//         while (c.charAt(0) == ' ') {
//             c = c.substring(1);
//         }
//         if (c.indexOf(name) == 0) {
//             return c.substring(name.length, c.length);
//         }
//     }
//     return "";
// }