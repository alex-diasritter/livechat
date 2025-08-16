$(function () {
    const urlParams = new URLSearchParams(window.location.search);

    if (urlParams.has('error')) {
        alert("Falha no login: usuário ou senha inválidos. Por favor, tente novamente.");
    }

    if (urlParams.has('logout')) {
        console.log("Logout realizado com sucesso.");
    }
});
