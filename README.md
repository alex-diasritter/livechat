# Chat em Tempo Real com Autenticação

Aplicação de chat em tempo real com cadastro e autenticação de usuários usando Spring Security. Todas as conversas são salvas de forma segura em um banco de dados H2 embarcado e criptografado.

## 🚀 Funcionalidades

- 🔒 **Autenticação Segura**: Sistema completo de cadastro e login com Spring Security.
- 💬 **Mensagens em Tempo Real**: Comunicação instantânea via WebSockets após a autenticação.
- 👤 **Perfis de Usuário**: As mensagens são vinculadas ao perfil do usuário autenticado.
- 📝 **Histórico de Conversas**: Todas as mensagens são persistidas de forma segura.
- 🔔 **Notificações de Usuários**: Alertas em tempo real quando um usuário entra ou sai do chat.
- 🔐 **Banco de Dados Criptografado**: Os dados em repouso são protegidos com criptografia.

## 🛠️ Tecnologias

### Backend
- **Java** com **Spring Boot**
- **Spring Security** para autenticação e autorização.
- **Spring WebSocket** para comunicação em tempo real com **STOMP**.
- **Spring Data JPA** para persistência de dados.
- **H2 Database** (Banco de dados embarcado com criptografia).
- **Maven** para gerenciamento de dependências.

### Frontend
- **HTML5, CSS3 e JavaScript puro**.
- **Fetch API** para as requisições de cadastro e login.
- **STOMP.js** e **SockJS** para a comunicação WebSocket.

## 🚀 Como executar

### Pré-requisitos
- Java 17 ou superior
- Maven

### Executando a aplicação localmente (Recomendado)

Este projeto utiliza um banco de dados H2 embarcado, que é criado e configurado automaticamente ao iniciar a aplicação.

1.  **Clone o repositório**
    ```bash
    git clone <url-do-seu-repositorio>
    cd <nome-do-repositorio>
    ```

2.  **Execute com Maven**
    ```bash
    # Na raiz do projeto backend, execute:
    mvn spring-boot:run
    ```

    A aplicação estará disponível em:
    - **API e Frontend**: `http://localhost:8080`
    - **Console do H2**: `http://localhost:8080/h2-console`
        - **JDBC URL**: `jdbc:h2:file:./data/livechatdb;CIPHER=AES` (ou conforme `application.properties`)

## 📝 Uso

### 1. Cadastro
- Acesse a página de registro em `http://localhost:8080/register.html`.
- Preencha os dados necessários para criar uma conta.

### 2. Login
- Acesse a página principal `http://localhost:8080`.
- Faça login com suas credenciais.

### 3. Conversar
- Após o login, você será direcionado para a página do chat.
- Envie e receba mensagens em tempo real.

## 🔒 Segurança

- Autenticação e Autorização completas com Spring Security.
- Senhas armazenadas com hash seguro (**BCryptPasswordEncoder**).
- Proteção contra ataques comuns (CSRF, XSS, etc.).
- Banco de dados H2 criptografado para proteção dos dados em repouso.

## 📦 Estrutura do Projeto
Com certeza. Aqui está o código-fonte (raw Markdown) do arquivo README.md que elaboramos.

Você pode copiar e colar este bloco de código diretamente em um arquivo README.md no seu projeto.
Markdown

# Chat em Tempo Real com Autenticação

Aplicação de chat em tempo real com cadastro e autenticação de usuários usando Spring Security. Todas as conversas são salvas de forma segura em um banco de dados H2 embarcado e criptografado.

## 🚀 Funcionalidades

- 🔒 **Autenticação Segura**: Sistema completo de cadastro e login com Spring Security.
- 💬 **Mensagens em Tempo Real**: Comunicação instantânea via WebSockets após a autenticação.
- 👤 **Perfis de Usuário**: As mensagens são vinculadas ao perfil do usuário autenticado.
- 📝 **Histórico de Conversas**: Todas as mensagens são persistidas de forma segura.
- 🔔 **Notificações de Usuários**: Alertas em tempo real quando um usuário entra ou sai do chat.
- 🔐 **Banco de Dados Criptografado**: Os dados em repouso são protegidos com criptografia.

## 🛠️ Tecnologias

### Backend
- **Java** com **Spring Boot**
- **Spring Security** para autenticação e autorização.
- **Spring WebSocket** para comunicação em tempo real com **STOMP**.
- **Spring Data JPA** para persistência de dados.
- **H2 Database** (Banco de dados embarcado com criptografia).
- **Maven** para gerenciamento de dependências.

### Frontend
- **HTML5, CSS3 e JavaScript puro**.
- **Fetch API** para as requisições de cadastro e login.
- **STOMP.js** e **SockJS** para a comunicação WebSocket.

## 🚀 Como executar

### Pré-requisitos
- Java 17 ou superior
- Maven

### Executando a aplicação localmente (Recomendado)

Este projeto utiliza um banco de dados H2 embarcado, que é criado e configurado automaticamente ao iniciar a aplicação.

1.  **Clone o repositório**
    ```bash
    git clone <url-do-seu-repositorio>
    cd <nome-do-repositorio>
    ```

2.  **Execute com Maven**
    ```bash
    # Na raiz do projeto backend, execute:
    mvn spring-boot:run
    ```

    A aplicação estará disponível em:
    - **API e Frontend**: `http://localhost:8080`
    - **Console do H2**: `http://localhost:8080/h2-console`
        - **JDBC URL**: `jdbc:h2:file:./data/livechatdb;CIPHER=AES` (ou conforme `application.properties`)

## 📝 Uso

### 1. Cadastro
- Acesse a página de registro em `http://localhost:8080/register.html`.
- Preencha os dados necessários para criar uma conta.

### 2. Login
- Acesse a página principal `http://localhost:8080`.
- Faça login com suas credenciais.

### 3. Conversar
- Após o login, você será direcionado para a página do chat.
- Envie e receba mensagens em tempo real.

## 🔒 Segurança

- Autenticação e Autorização completas com Spring Security.
- Senhas armazenadas com hash seguro (**BCryptPasswordEncoder**).
- Proteção contra ataques comuns (CSRF, XSS, etc.).
- Banco de dados H2 criptografado para proteção dos dados em repouso.

## 📦 Estrutura do Projeto

livechat/
├── backend/                                # Código-fonte do backend (Spring Boot)
│   ├── src/main/java/com/alex/livechat/
│   │   ├── config/                       # Configurações da aplicação
│   │   │   ├── SecurityConfig.java       # Configuração do Spring Security
│   │   │   └── WebSocketConfig.java      # Configuração do WebSocket
│   │   ├── controller/                   # Controladores
│   │   │   ├── AuthController.java       # Endpoints REST para Login e Cadastro
│   │   │   └── ChatController.java       # Controlador WebSocket para mensagens
│   │   ├── domain/
│   │   │   ├── dto/                      # Objetos de transferência de dados
│   │   │   ├── entity/                   # Entidades JPA (Banco de Dados)
│   │   │   │   ├── Message.java
│   │   │   │   └── User.java             # Entidade de usuário com login e senha
│   │   ├── repository/                   # Repositórios JPA
│   │   │   ├── MessageRepository.java
│   │   │   └── UserRepository.java
│   │   └── service/                      # Lógica de negócio
│   │       ├── ChatService.java
│   │       └── UserDetailsServiceImpl.java # Serviço para integração com Spring Security
│   └── src/main/resources/               # Arquivos de recursos
│       ├── static/                       # Arquivos do Frontend (HTML, CSS, JS)
│       └── application.properties        # Configurações, incluindo banco de dados H2
└── pom.xml                               # Arquivo de configuração do Maven


## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.
