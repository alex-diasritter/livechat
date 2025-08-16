const stompClient = new StompJs.Client({
    webSocketFactory: function() {
        return new SockJS('/livechat-websocket');
    }
});

let isConnected = false;

stompClient.onConnect = (frame) => {
    isConnected = true;
    setConnected(true);
    console.log('Conectado ao WebSocket: ' + frame);
    stompClient.subscribe('/topics/livechat', (message) => {
        updateLiveChat(JSON.parse(message.body).content);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Erro com WebSocket', error);
    alert('Não foi possível conectar ao chat. Sua sessão pode ter expirado. Por favor, faça login novamente.');
    window.location.href = '/index.html';
};

stompClient.onStompError = (frame) => {
    console.error('Broker reportou um erro: ' + frame.headers['message']);
    console.error('Detalhes adicionais: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
}

function connect() {
    if (!isConnected) {
        console.log('Tentando conectar ao WebSocket...');
        stompClient.activate();
    }
}

function disconnect() {
    stompClient.deactivate();
    isConnected = false;
    setConnected(false);
    console.log("Desconectado");
}

function sendMessage() {
    const userValue = $("#user").val();
    const messageValue = $("#message").val();

    if (userValue && messageValue) {
        stompClient.publish({
            destination: "/livechat/new-message",
            body: JSON.stringify({'user': userValue, 'message': messageValue})
        });
        $("#message").val("");
    }
}

function updateLiveChat(message) {
    $("#livechat").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendMessage());
});