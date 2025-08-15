const stompClient = new StompJs.Client({
    brokerURL: 'ws://' + window.location.host + '/livechat-websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topics/livechat', (message) => {
        updateLiveChat(JSON.parse(message.body).content);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
}

function connect() {
    // *** LÓGICA ATUALIZADA: Recuperar o JWT e adicionar aos cabeçalhos ***
    const jwtToken = localStorage.getItem('jwt_token'); // Pega o token do localStorage

    if (!jwtToken) {
        // Se não houver token, alerta o usuário e o redireciona para a página de login
        alert("Você não está logado ou sua sessão expirou. Por favor, faça login novamente.");
        window.location.href = "/index.html";
        return; // Impede a conexão se não houver token
    }

    // Adiciona o cabeçalho Authorization com o token JWT
    // O formato padrão é "Bearer <TOKEN>"
    const headers = {
        'Authorization': 'Bearer ' + jwtToken
    };

    stompClient.connectHeaders = headers; // Define os cabeçalhos para a conexão WebSocket
    stompClient.activate(); // Ativa a conexão WebSocket
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
    // Opcional: Você pode querer remover o token do localStorage ao desconectar do chat
    localStorage.removeItem('jwt_token');
}

function sendMessage() {
    // Para enviar mensagens, o token já foi enviado na conexão inicial do WebSocket
    // se o seu Spring Security estiver configurado para interceptar mensagens STOMP.
    stompClient.publish({
        destination: "/livechat/new-message",
        body: JSON.stringify({'user': $("#user").val(), 'message': $("#message").val()})
    });
    $("#message").val("");
}

function updateLiveChat(message) {
    $("#livechat").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendMessage());
});
