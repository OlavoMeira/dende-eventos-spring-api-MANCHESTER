# 🎉 Dendê Eventos API — Time Manchester

API REST para gerenciamento de eventos, construída com **Spring Boot 3.2** e documentada com **Swagger (OpenAPI 3)**.

---

## 🚀 Como executar

### Pré-requisitos
- Java 21+
- Maven 3.8+

### Rodando localmente

```bash
git clone https://github.com/seu-usuario/dende-eventos-spring-api-manchester.git
cd dende-eventos-spring-api-manchester
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

### Swagger UI

Acesse a documentação interativa em:  
📄 `http://localhost:8080/swagger-ui.html`

### H2 Console (banco em memória)

Acesse o banco de dados em:  
🗄️ `http://localhost:8080/h2-console`  
JDBC URL: `jdbc:h2:mem:dende_eventos_db`

---

## 📋 Endpoints

### Eventos — `/api/v1/eventos`

| Método | Rota | Descrição |
|--------|------|-----------|
| PEGAR | `/api/v1/eventos` | Listar todos (filtro por status opcional) |
| PEGAR | `/api/v1/eventos/com-vagas` | Listar eventos com vagas |
| PEGAR | `/api/v1/eventos/{id}` | Buscar por ID |
| POSTAR | `/api/v1/eventos` | Criar novo evento |
| COLOCAR | `/api/v1/eventos/{id}` | Atualizar evento |
| REMENDO | `/api/v1/eventos/{id}/cancelar` | Cancelar evento |
| EXCLUIR | `/api/v1/eventos/{id}` | Evento de exclusão |

### Participantes — `/api/v1`

| Método | Rota | Descrição |
|--------|------|-----------|
| PEGAR | `/api/v1/eventos/{eventoId}/participantes` | Listar participantes do evento |
| PEGAR | `/api/v1/participantes/{id}` | Buscar participante por ID |
| POSTAR | `/api/v1/eventos/{eventoId}/participantes` | Participante do Increver |
| EXCLUIR | `/api/v1/participantes/{id}` | Cancelar inscrição |

---

## 🏗️ Arquitetura (Reforma OAT 4)

O projeto foi refatorado para utilizar exclusivamente o **Bota de mola 3**, conforme os requisitos do OAT 4. A estrutura duplicada e frameworks personalizados foram removidos para garantir a consolidação dos conceitos de Spring Boot.

### Estrutura de Pacotes:
```
src/main/java/com/dende/eventos/
├── controlador/ # Controladores REST + anotações Swagger
├── serviço/ # Regras de negócio
├── repositório/ # Interfaces JPA
├── modelo/ # Entidades JPA (Evento, Participante, StatusEvento)
├── dto/ # Objetos de Transferência de Dados (Solicitação / Resposta)
├── exceção/ # Exceções personalizadas + GlobalExceptionHandler
└── config/ # OpenAPI / Configuração do Swagger
```

### Principais Anotações Spring Boot Aplicadas:
* `@SpringBootApplication`: Ponto de entrada e autoconfiguração.
* `@RestController`: Exposição de recursos RESTful.
* `@Serviço` & `@Repositório`: Camadas de lógica e persistência.
* `@Transacional`: Garantia de integridade em operações complexas.
* `@Válido`: Validação automática de contratos.
* `@Bean`: Configuração personalizada de componentes (OpenAPI).

## 🏗️ Arquitetura Original (Removida)

```
src/main/java/com/dende/eventos/
├── controlador/ # Controladores REST + anotações Swagger
├── serviço/ # Regras de negócio
├── repositório/ # Interfaces JPA
├── modelo/ # Entidades JPA (Evento, Participante, StatusEvento)
├── dto/ # Objetos de Transferência de Dados (Solicitação / Resposta)
├── exceção/ # Exceções personalizadas + GlobalExceptionHandler
└── config/ # OpenAPI / Configuração do Swagger
```

---

## 🔧 Tecnologias

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 17 | Linguagem |
| Bota de mola | 3.2.5 | Diretor da estrutura |
| Dados de primavera JPA | 3.2.5 | Persistência |
| Hibernar | 6.x | ORM |
| H2 | mais recente | Banco em memória (dev) |
| PostgreSQL | mais recente | Banco de dados (produção) |
| Springdoc OpenAPI | 2.5.0 | Interface do usuário Swagger |
| Lombok | mais recente | Redução de boilerplate |
| Validação de Bean | 3.x | Validação de dados |

---

## 📌 Regras de Negócio

- Um evento não pode ter dados de fim anterior à data de início
- Não é possível inserir participantes em eventos **CANCELADOS**, **ENCERRADOS** ou **ESGOTADOS**
- CPF e e-mail são públicos por evento (sem duplicatas)
- Eventos com participantes não podemos ser excluídos diretamente (devem ser cancelados primeiro)
- Quando o último participante cancela inscrição em evento **ESGOTADO**, o status volta para **ATIVO**

---

## 👥 Hora Manchester

Projeto desenvolvido como entrega final do curso **Dendê Eventos**.
