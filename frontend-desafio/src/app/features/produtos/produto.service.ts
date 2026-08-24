import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Produto, ProdutoRequest, VendaRequest } from './produto.model';

@Injectable({
  providedIn: 'root'
})
export class ProdutoService {

  private readonly API_URL = 'http://localhost:8080/produtos';

  constructor(private http: HttpClient) { }

  listarTodos(): Observable<Produto[]> {
    return this.http.get<Produto[]>(this.API_URL);
  }

  buscarPorId(id: number): Observable<Produto> {
    return this.http.get<Produto>(`${this.API_URL}/${id}`);
  }

  criarProduto(request: ProdutoRequest): Observable<Produto> {
    return this.http.post<Produto>(this.API_URL, request);
  }

  atualizarProduto(id: number, request: ProdutoRequest): Observable<Produto> {
    return this.http.put<Produto>(`${this.API_URL}/${id}`, request);
  }

  excluirProduto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  venderProduto(id: number, request: VendaRequest): Observable<Produto> {
    return this.http.post<Produto>(`${this.API_URL}/${id}/vender`, request);
  }
}
