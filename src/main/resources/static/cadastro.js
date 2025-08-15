$(function () {
    $("#registerForm").on('submit', function (e) {
        e.preventDefault(); // Impede o envio padrão do formulário

        const username = $("#regUsername").val();
        const password = $("#regPassword").val();
        const confirmPassword = $("#confirmPassword").val();

        if (password !== confirmPassword) {
            alert("As senhas não coincidem! Por favor, digite senhas iguais.");
            return;
        }

        // Prepara os dados para enviar ao backend.
        // Geralmente, o backend de registro não precisa de 'confirmPassword'.
        const registrationData = {
            username: username,
            password: password
        };

        // Faz a chamada POST para o endpoint /users do seu backend
        $.ajax({
            type: "POST",
            url: "/users", // Endpoint de registro no seu backend Spring Boot
            contentType: "application/json", // Indica que estamos enviando JSON
            data: JSON.stringify(registrationData), // Converte os dados para JSON
            success: function(response) {
                console.log("Cadastro bem-sucedido!", response);
                // Exibe uma mensagem de sucesso e redireciona para a página de login.
                alert("Cadastro realizado com sucesso! Agora você pode fazer login.");
                window.location.href = "/index.html"; // Redireciona para a página de login
            },
            error: function(xhr, status, error) {
                // Lida com erros de cadastro (ex: usuário já existe, validação falhou)
                console.error("Usuario ja cadastrado:", xhr.responseText);
                let errorMessage = "Falha no cadastro! Tente novamente.";
                try {
                    const errorJson = JSON.parse(xhr.responseText);
                    if (errorJson.message) {
                        errorMessage = "Falha no cadastro: " + errorJson.message;
                    }
                } catch (e) {
                    // Se a resposta de erro não for JSON, usa a mensagem padrão
                }
                alert(errorMessage);
            }
        });
    });
});
