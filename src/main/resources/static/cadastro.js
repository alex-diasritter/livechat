$(function () {
    $("#registerForm").on('submit', function (e) {
        e.preventDefault(); // Impede o envio padrão do formulário

        const username = $("#regUsername").val();
        const password = $("#regPassword").val();
        const confirmPassword = $("#confirmPassword").val();

        if (password !== confirmPassword) {
            // Em uma aplicação real, você usaria uma caixa de diálogo personalizada
            // ou exibiria uma mensagem de erro na página.
            alert("As senhas não coincidem!");
            return;
        }

        // Em uma aplicação real, você enviaria essas informações para o seu backend
        // via AJAX (fetch ou jQuery.ajax) para processar o registro.
        console.log("Tentando cadastrar novo usuário:", { username, password });

        // Simula o sucesso da chamada da API e redireciona para a página de login.
        setTimeout(() => {
            alert("Cadastro realizado com sucesso! Faça login para continuar.");
            window.location.href = "/index.html"; // Redireciona para a página de login
        }, 500); // Simula um pequeno atraso de rede
    });
});
