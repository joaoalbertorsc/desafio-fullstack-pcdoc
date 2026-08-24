export interface Produto {
  id: number;
  nome: string;
  descricao?: string;
  preco: number;
  quantidade: number;
  dataCriacao: string;
}

export interface ProdutoRequest {
  nome: string;
  descricao?: string;
  preco: number;
  quantidade: number;
}

export interface VendaRequest {
  quantidade: number;
}
