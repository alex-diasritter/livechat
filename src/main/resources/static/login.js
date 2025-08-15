$(function () {
    // Listener para o envio do formulário de login
    $("#loginForm").on('submit', function (e) {
        e.preventDefault(); // Impede o envio padrão do formulário

        const username = $("#username").val();
        const password = $("#password").val();

        // Faz a chamada POST para o endpoint /login do seu backend
        $.ajax({
            type: "POST",
            url: "/login", // Endpoint de login no seu backend Spring Boot
            contentType: "application/json", // Indica que estamos enviando JSON
            data: JSON.stringify({ username: username, password: password }), // Converte os dados para JSON
            success: function(response) {
                console.log("Login bem-sucedido!", response);

                // *** NOVA LÓGICA: Extrair e armazenar o JWT ***
                if (response && response.token) { // Assumindo que o token vem no campo 'token'
                    localStorage.setItem('jwt_token', response.token); // Armazena o token no localStorage
                    console.log("JWT Token armazenado:", response.token);
                    window.location.href = "/chat.html"; // Redireciona para a página de chat
                } else {
                    console.error("Login bem-sucedido, mas nenhum token JWT foi recebido na resposta.");
                    alert("Login bem-sucedido, mas não foi possível iniciar a sessão. Tente novamente.");
                }
            },
            error: function(xhr, status, error) {
                // Lida com erros de login (ex: credenciais inválidas)
                console.error("Erro no login:", xhr.responseText);
                // Exibe uma mensagem de erro para o usuário.
                // Em uma aplicação real, você usaria uma UI mais sofisticada (modal, div de erro).
                alert("Falha no login! Verifique seu usuário e senha. Detalhes: " + xhr.responseText);
            }
        });
    });
});
