# 💻 Frontend - Gerenciamento de Produtos

Single Page Application (SPA) para o controle de produtos. Desenvolvida com foco em performance, componentização e reatividade.

## 🚀 Tecnologias e Funcionalidades

* **Angular 17+:** Uso exclusivo de *Standalone Components* (sem `NgModules`) para uma estrutura coesa.
* **TypeScript:** Tipagem estática via interfaces (`Produto`, `ProdutoRequest`, `VendaRequest`) para garantir os contratos da API.
* **UI/UX Nativa:** Estilização responsiva em CSS puro e HTML5, sem dependência de bibliotecas externas.
* **Atualização Otimista:** A interface reflete ações (vendas, exclusões) instantaneamente, utilizando `ChangeDetectorRef` para sincronia da árvore de renderização.

## ⚙️ Como Executar

A interface pode ser executada nativamente via Docker com o restante do sistema ou isolada para desenvolvimento. A API (Backend) deve estar ativa na porta `8080` em ambos os cenários.

### Opção 1: Via Docker (Recomendado)

Na **raiz do projeto principal**, execute:

```bash
  docker-compose up --build -d

```

Acesse `http://localhost:4200/`. O container expõe o `ng serve` para simular o ambiente de desenvolvimento local.

### Opção 2: Execução Manual (Desenvolvimento)

Requer [Node.js](https://nodejs.org/) e [Angular CLI](https://angular.io/cli). Na pasta `frontend-produtos`:

1. Instale as dependências: `npm install`
2. Inicie o servidor: `ng serve`
3. Acesse `http://localhost:4200/`

## 🏗️ Arquitetura

O projeto adota a *Feature-Based Architecture*. O escopo de Produtos (`src/app/features/produtos`) é composto por:

* **ProdutoListComponent:** Camada de visualização (tabela, modais) e orquestração de eventos da UI.
* **ProdutoService:** Lógica de comunicação HTTP com o backend via `RxJS` (Observables).
* **Modelos:** Interfaces de domínio que padronizam o payload entre cliente e servidor.
