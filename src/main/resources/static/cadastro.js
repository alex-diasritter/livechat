$(function () {
    $("#registerForm").on('submit', function (e) {
        e.preventDefault();

        const username = $("#regUsername").val();
        const password = $("#regPassword").val();
        const confirmPassword = $("#confirmPassword").val();

        if (password !== confirmPassword) {
            alert("As senhas não coincidem! Por favor, digite senhas iguais.");
            return;
        }

        const registrationData = {
            username: username,
            password: password
        };

        $.ajax({
            type: "POST",
            url: "/users",
            contentType: "application/json",
            data: JSON.stringify(registrationData),
            success: function(response) {
                console.log("Cadastro bem-sucedido!", response);
                alert("Cadastro realizado com sucesso! Agora você pode fazer login.");
                window.location.href = "/index.html";
            },
            error: function(xhr, status, error) {
                console.error("Erro no cadastro:", xhr.responseText);
                let errorMessage = "Falha no cadastro! Tente novamente.";
                try {
                    const errorJson = JSON.parse(xhr.responseText);
                    if (errorJson.message) {
                        errorMessage = "Falha no cadastro: " + errorJson.message;
                    }
                } catch (e) {
                    // Mantém a mensagem padrão
                }
                alert(errorMessage);
            }
        });
    });
});
