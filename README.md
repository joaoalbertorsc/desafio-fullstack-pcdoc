# 📦 Sistema de Gerenciamento de Produtos

Solução Full-Stack para controle de estoque e registro de vendas. O projeto adota uma arquitetura em camadas, separando as responsabilidades entre uma API RESTful (Backend), uma Single Page Application reativa (Frontend) e um banco de dados relacional, todos orquestrados via containers.

## 🛠️ Stack Tecnológico e Arquitetura

* **Backend:** Desenvolvido em **Java (Spring Boot)**. Utiliza o padrão MVC, injeção de dependências e expõe endpoints RESTful consumidos pelo client.
* **Frontend:** Construído em **Angular 17+**. Utiliza *Standalone Components* para melhor coesão, dispensando a complexidade de `NgModules`. Focado em *Optimistic UI* (Atualizações Otimistas) para garantir alta responsividade na interface.
* **Banco de Dados:** **PostgreSQL**, com persistência mapeada via Hibernate/JPA.
* **DevOps & Infraestrutura:** Orquestração centralizada com **Docker Compose**. O ambiente configura uma rede interna isolada, permitindo a comunicação fluida entre o banco de dados, a API e a interface de forma nativa.

## 📂 Estrutura do Repositório

```text
/
├── backend-desafio/       # API Spring Boot (Contém seu próprio Dockerfile)
├── frontend-desafio/     # SPA Angular (Contém seu próprio Dockerfile de desenvolvimento)
└── docker-compose.yml     # Orquestrador central da infraestrutura

```

## 🚀 Como Executar o Projeto

O projeto foi empacotado para garantir um processo de avaliação sem atritos (Zero-Install). Não é necessário possuir Java, Node.js ou Angular CLI instalados na máquina hospedeira.

**Pré-requisito:** Ter o [Docker](https://www.docker.com/) e o Docker Compose instalados.

1. Abra o terminal na **raiz do projeto** (onde se encontra o arquivo `docker-compose.yml`).
2. Construa as imagens e inicie os serviços executando o comando:
```bash
  docker-compose up --build -d

```


3. Aguarde a inicialização dos containers. O orquestrador subirá o Banco de Dados, a API e o Servidor Angular.
4. **Acesse a aplicação:**
* **Interface (Frontend):** `http://localhost:4200`
* **Documentação da API (Swagger)**: `http://localhost:8080/swagger-ui/index.html` (ou `/swagger-ui.html` dependendo da configuração
* **API (Backend):** `http://localhost:8080`



> **Nota para Avaliação:** O frontend está configurado para rodar através do servidor de desenvolvimento nativo do Angular (`ng serve`) dentro do container, mantendo o comportamento exato de um ambiente local. A flag `-DskipTests` é utilizada no build da API estritamente para agilizar a inicialização do container.

## 👨‍💻 Autor

Projeto desenhado e desenvolvido por **João Alberto Rodrigues Soares Costa**.
O código reflete práticas sólidas de Engenharia de Software, priorizando manutenibilidade, Clean Code e integração de infraestrutura.