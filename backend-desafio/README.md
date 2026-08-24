# ⚙️ API de Gerenciamento de Produtos

O objetivo deste microsserviço é fornecer uma interface robusta, segura e escalável para o gerenciamento de estoque e registro de vendas de produtos.

## 🛠️ Stack

O projeto foi desenvolvido utilizando o que há de mais moderno no ecossistema Java:

*   **Linguagem:** Java 17
*   **Framework:** Spring Boot 4.1.1
*   **Banco de Dados:** PostgreSQL 15
*   **Infraestrutura:** Docker & Docker Compose
*   **Mapeamento Objeto-Relacional (ORM):** Spring Data JPA / Hibernate
*   **Validação:** Jakarta Bean Validation
*   **Documentação:** Springdoc OpenAPI 3.0.0 (Swagger)
*   **Testes Automatizados:** JUnit 5 & Mockito

---

## 🏗️ Arquitetura e Decisões Técnicas

A aplicação foi desenhada focando em **Clean Code**, **Manutenibilidade** e **Performance**. Abaixo estão os principais padrões adotados:

### 1. Padrão de Camadas (Layered Architecture)
*   **Controllers:** Responsáveis unicamente por receber requisições HTTP, delegar para o serviço e retornar a resposta adequada.
*   **Services:** Isolam 100% das regras de negócio. É a camada mais crítica e totalmente coberta por testes.
*   **Repositories:** Abstração de acesso a dados usando interfaces do Spring Data.

### 2. DTOs com Java Records (Imutabilidade)
Ao invés de expor as entidades de banco de dados (`@Entity`) diretamente nos Controllers, implementamos o padrão **Data Transfer Object (DTO)** utilizando **Java Records**.
*   **Por quê?** Os Records garantem imutabilidade no transporte dos dados, eliminam a necessidade de bibliotecas como Lombok para a criação de construtores/getters, e previnem o vazamento de informações sensíveis do banco de dados (Over-posting/Mass Assignment).

### 3. Tratamento Global de Exceções (RFC 7807)
A API não retorna *Stack Traces* genéricos. Utilizamos um `@RestControllerAdvice` combinado com o padrão **ProblemDetail** (implementação Spring para a RFC 7807) para padronizar todos os erros da API.
*   **400 Bad Request:** Para falhas de validação sintática (ex: preço negativo).
*   **404 Not Found:** Para recursos não encontrados no banco.
*   **409 Conflict:** Para violações de regras de negócio (ex: estoque insuficiente, nome duplicado).
*   **500 Internal Server Error:** Interceptador genérico para falhas inesperadas de infraestrutura.

### 4. Transações Otimizadas (Read-Only by Default)
A classe `ProdutoService` é anotada com `@Transactional(readOnly = true)` a nível de classe.
*   **Por quê?** Isso desativa o *Dirty Checking* do Hibernate para todos os métodos de leitura (GET), economizando memória e tempo de processamento. A escrita é habilitada pontualmente nos métodos de criação, atualização e deleção utilizando o `@Transactional` simples.

---

## 🛡️ Regras de Negócio e Validações

A API garante a integridade do estoque através de regras estritas:

1.  **Venda Segura:** O endpoint de venda valida matematicamente o estoque atual. Se a quantidade solicitada for maior que a disponível, a transação é revertida e um erro `409 Conflict` é retornado. **O estoque nunca fica negativo.**
2.  **Integridade de Nomenclatura:** É impossível cadastrar dois produtos com o mesmo nome (`409 Conflict`). A validação ignora *Case Sensitivity* (maiusculas/minusculas).
3.  **Dados Semânticos:** Através do Jakarta Validation, atributos como Preço e Quantidade não aceitam valores menores que zero (`400 Bad Request`).

---

## 📡 Endpoints da API

| Verbo  | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/produtos` | Cadastra um novo produto. |
| `GET` | `/produtos` | Lista todos os produtos cadastrados. |
| `GET` | `/produtos/{id}` | Busca um produto específico pelo ID. |
| `PUT` | `/produtos/{id}` | Atualiza os dados de um produto existente. |
| `DELETE` | `/produtos/{id}` | Remove um produto do sistema. |
| `POST` | `/produtos/{id}/vender` | Abate uma quantidade específica do estoque do produto. |

> **💡 Nota de Design de API (Disclaimer):**
> O endpoint de vendas foi implementado como `POST /produtos/{id}/vender` para atender estritamente ao contrato solicitado no documento do desafio. No entanto, em um cenário real e escalável de produção, a abordagem ideal envolveria a separação de domínios (Domain-Driven Design): a criação de um Domínio de Vendas independente (VendaController ou um microsserviço dedicado), respondendo na rota raiz `POST /vendas`. Isso garantiria o uso correto de substantivos nas URIs (Richardson Maturity Model), reduziria o acoplamento e permitiria a evolução natural para vendas contendo múltiplos itens.
---

## 🚀 Como Executar o Projeto

A API pode ser executada de forma automatizada via Docker (junto com o banco de dados) ou isolada para fins de desenvolvimento.

### Opção 1: Via Docker (Recomendado para Avaliação)

Não é necessário ter Java ou Maven instalados. A partir da **raiz do projeto principal** (onde está o orquestrador geral), execute:

```bash
  docker-compose up --build -d
```

* O PostgreSQL e a API iniciarão automaticamente.
* A API estará disponível na porta `8080`.
* *Nota:* O build no Docker utiliza a flag `-DskipTests` estritamente para agilizar a inicialização durante a avaliação.

### Opção 2: Execução Manual (Modo Desenvolvimento)

Requer [Java 17] e [Maven]. Na pasta deste microsserviço (`backend-desafio`):

1. **Suba apenas o Banco de Dados:** Certifique-se de que o container do PostgreSQL está rodando na porta `5432`.
2. **Inicie a aplicação:**
```bash
  mvn spring-boot:run
```



---

## 📚 Documentação (Swagger)

Com a API em execução (via Docker ou manual), acesse a documentação interativa OpenAPI 3.0:

* **Interface Gráfica (Swagger UI):** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* **JSON Docs:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🧪 Testes Automatizados

O projeto conta com uma suíte de **Testes Unitários** focada na camada de Serviço (`ProdutoService`), utilizando **JUnit 5** e **Mockito** para garantir as regras de negócio de forma isolada (sem dependência de banco de dados real).

Para executar os testes localmente, rode:
```bash
  mvn test
```