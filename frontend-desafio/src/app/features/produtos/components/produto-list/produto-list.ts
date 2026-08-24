import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProdutoService } from '../../produto.service';
import { Produto, ProdutoRequest, VendaRequest } from '../../produto.model';

@Component({
  selector: 'app-produto-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './produto-list.html',
  styleUrl: './produto-list.css'
})
export class ProdutoListComponent implements OnInit {

  produtos: Produto[] = [];
  errorMessage: string = '';
  successMessage: string = '';

  showModal: boolean = false;
  isEditing: boolean = false;
  selectedProdutoId: number | null = null;

  formProduto: ProdutoRequest = {
    nome: '',
    descricao: '',
    preco: 0,
    quantidade: 0
  };

  constructor(
    private produtoService: ProdutoService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.carregarProdutos();
  }

  carregarProdutos(): void {
    this.produtoService.listarTodos().subscribe({
      next: (data) => {
        this.produtos = [...data];
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.tratarErro('Erro ao carregar produtos.', err);
        this.cdr.detectChanges();
      }
    });
  }

  abrirModalCriacao(): void {
    this.isEditing = false;
    this.selectedProdutoId = null;
    this.formProduto = { nome: '', descricao: '', preco: 0, quantidade: 0 };
    this.limparMensagens();
    this.showModal = true;
  }

  abrirModalEdicao(produto: Produto): void {
    this.isEditing = true;
    this.selectedProdutoId = produto.id;
    this.formProduto = {
      nome: produto.nome,
      descricao: produto.descricao || '',
      preco: produto.preco,
      quantidade: produto.quantidade
    };
    this.limparMensagens();
    this.showModal = true;
  }

  fecharModal(): void {
    this.showModal = false;
  }

  salvarProduto(): void {
    this.limparMensagens();

    if (this.isEditing && this.selectedProdutoId) {
      this.produtoService.atualizarProduto(this.selectedProdutoId, this.formProduto).subscribe({
        next: () => {
          this.successMessage = 'Produto atualizado com sucesso!';
          this.fecharModal();
          this.carregarProdutos();
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.tratarErro('Erro ao atualizar produto.', err);
          this.cdr.detectChanges();
        }
      });
    } else {
      this.produtoService.criarProduto(this.formProduto).subscribe({
        next: () => {
          this.successMessage = 'Produto cadastrado com sucesso!';
          this.fecharModal();
          this.carregarProdutos();
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.tratarErro('Erro ao cadastrar produto.', err);
          this.cdr.detectChanges();
        }
      });
    }
  }

  vender(produto: Produto): void {
    this.limparMensagens();
    const quantidadeStr = prompt(`Informe a quantidade para vender de "${produto.nome}":`);

    if (!quantidadeStr) return;

    const quantidade = parseInt(quantidadeStr, 10);
    if (isNaN(quantidade) || quantidade <= 0) {
      this.errorMessage = 'Quantidade inválida para venda.';
      return;
    }

    const vendaReq: VendaRequest = { quantidade };

    this.produtoService.venderProduto(produto.id, vendaReq).subscribe({
      next: () => {
        this.successMessage = `Venda de ${quantidade} unidade(s) realizada com sucesso!`;
        this.carregarProdutos();
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.tratarErro('Erro ao realizar venda.', err);
        this.cdr.detectChanges();
      }
    });
  }

  excluir(produto: Produto): void {
    this.limparMensagens();
    const confirmacao = confirm(`Deseja realmente excluir o produto "${produto.nome}"?`);

    if (confirmacao) {
      this.produtoService.excluirProduto(produto.id).subscribe({
        next: () => {
          this.successMessage = 'Produto removido com sucesso!';
          this.produtos = this.produtos.filter(p => p.id !== produto.id);
          this.carregarProdutos();
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.tratarErro('Erro ao excluir produto.', err);
          this.cdr.detectChanges();
        }
      });
    }
  }

  private tratarErro(mensagemPadrao: string, err: any): void {
    if (err.error && err.error.detail) {
      this.errorMessage = err.error.detail;
    } else if (err.error && typeof err.error === 'string') {
      this.errorMessage = err.error;
    } else {
      this.errorMessage = mensagemPadrao;
    }
  }

  private limparMensagens(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }
}
