const stompClient = new StompJs.Client({
    webSocketFactory: function() {
        return new SockJS('/livechat-websocket');
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000
});

let isConnected = false;
let currentUsername = '';

async function checkAuth() {
    try {
        const response = await fetch('/current-user', {
            credentials: 'include' // Crucial para enviar cookies de sessão
        });

        if (!response.ok) {
            throw new Error('Erro na resposta do servidor');
        }

        const data = await response.json();

        if (data.authenticated && data.username) {
            currentUsername = data.username;
            return true;
        }
        return false;

    } catch (error) {
        console.error('Falha na verificação:', error);
        return false;
    }
}


async function initializeChat() {
    const isAuthenticated = await checkAuth();

    if (!isAuthenticated) {
        alert('Sessão expirada. Redirecionando para login...');
        window.location.href = '/index.html?error=session_expired';
        return;
    }

    $("#user").val(currentUsername).prop('disabled', true);
    connect();
}

$(function() {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendMessage());

    // Inicia com pequeno delay para garantir cookies
    setTimeout(initializeChat, 100);
});

stompClient.onConnect = (frame) => {
    isConnected = true;
    setConnected(true);
    console.log('Conectado:', frame);

    stompClient.subscribe('/topics/livechat', (message) => {
        try {
            const msg = JSON.parse(message.body);
            showMessage(msg.user, msg.message, msg.timestamp, msg.type);
        } catch (e) {
            console.error('Erro ao processar mensagem:', e);
        }
    });

    sendSystemMessage(`${currentUsername} entrou no chat`);
};


stompClient.onStompError = (frame) => {
    console.error('Erro STOMP:', frame.headers.message);
    console.error('Detalhes:', frame.body);
};

stompClient.onWebSocketError = (error) => {
    console.error('Erro WebSocket:', error);
    alert('Erro de conexão. Tentando reconectar...');
};

function setConnected(connected) {
    isConnected = connected;
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#message-input").toggle(connected);
}

function connect() {
    if (!isConnected) {
        stompClient.activate();
    }
}

function disconnect() {
    if (isConnected) {
        sendSystemMessage(`${currentUsername} saiu do chat`);
        stompClient.deactivate();
        setConnected(false);
    }
}

function sendMessage() {
    const messageText = $("#message").val().trim();
    if (!messageText) return;

    stompClient.publish({
        destination: "/livechat/new-message",
        body: JSON.stringify({
            user: currentUsername,
            message: messageText,
            type: "user"
        })
    });

    $("#message").val('').focus();
}

function sendSystemMessage(text) {
    stompClient.publish({
        destination: "/livechat/new-message",
        body: JSON.stringify({
            user: "System",
            message: text,
            type: "system"
        })
    });
}

function showMessage(user, message, time, type) {
    const isSystem = type === 'system';
    const messageClass = isSystem ? 'system-message' : 'user-message';

    const messageHtml = `
        <tr class="${messageClass}">
            <td>
                <span class="message-time">[${time}]</span>
                ${!isSystem ? `<strong class="message-user">${user}:</strong> ` : ''}
                <span class="message-content">${message}</span>
            </td>
        </tr>
    `;

    $("#livechat").append(messageHtml);
    scrollToBottom();
}

function scrollToBottom() {
    const chatContainer = $("#conversation");
    chatContainer.scrollTop(chatContainer[0].scrollHeight);
}

// Inicialização quando o DOM estiver pronto
$(document).ready(function() {
    // Previne submit de formulários
    $("form").on('submit', function(e) {
        e.preventDefault();
    });

    // Configura botões
    $("#connect").click(connect);
    $("#disconnect").click(disconnect);
    $("#send").click(sendMessage);

    // Tecla Enter para enviar mensagem
    $("#message").keypress(function(e) {
        if (e.which === 13) { // Enter key
            sendMessage();
        }
    });

    // Inicia o chat automaticamente
    initializeChat();
});