$(function () {
    // Listener para o envio do formulário de login
    $("#loginForm").on('submit', function (e) {
        e.preventDefault(); // Impede o envio padrão do formulário

        const username = $("#username").val();
        const password = $("#password").val();

        // Em uma aplicação real, você enviaria essas credenciais para o seu backend
        // via AJAX (fetch ou jQuery.ajax) para autenticação.
        console.log("Tentando fazer login com:", { username, password });

        // Simula o sucesso da chamada da API e redireciona para a página de chat.
        // O backend real faria o redirecionamento após a autenticação.
        setTimeout(() => {
            window.location.href = "/chat.html"; // Redireciona para a página de chat
        }, 500); // Simula um pequeno atraso de rede
    });

    // O link de Login com GitHub (href="/oauth2/authorization/github") será
    // automaticamente gerenciado pelo Spring Security para iniciar o fluxo OAuth2.
    // Não é necessário JavaScript adicional aqui para o link do GitHub.
});
