package com.pcdoc.backend.service;

import com.pcdoc.backend.dto.ProdutoRequestDTO;
import com.pcdoc.backend.dto.ProdutoResponseDTO;
import com.pcdoc.backend.dto.VendaRequestDTO;
import com.pcdoc.backend.entity.Produto;
import com.pcdoc.backend.exception.EstoqueInsuficienteException;
import com.pcdoc.backend.exception.ProdutoJaCadastradoException;
import com.pcdoc.backend.exception.ProdutoNaoEncontradoException;
import com.pcdoc.backend.repository.ProdutoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    // ========================================================================
    // TESTES DE CRIAÇÃO
    // ========================================================================

    @Test
    @DisplayName("Deve criar um produto com sucesso")
    void deveCriarProdutoComSucesso() {
        ProdutoRequestDTO request = new ProdutoRequestDTO("Notebook", "Notebook Dell", new BigDecimal("3500.00"), 10);
        Produto produtoSalvo = new Produto(1L, "Notebook", "Notebook Dell", new BigDecimal("3500.00"), 10, LocalDateTime.now());

        when(produtoRepository.existsByNomeIgnoreCase("Notebook")).thenReturn(false);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);

        ProdutoResponseDTO response = produtoService.criarProduto(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Notebook", response.nome());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar produto com nome já existente")
    void naoDeveCriarProdutoComNomeDuplicado() {
        ProdutoRequestDTO request = new ProdutoRequestDTO("Notebook", "Notebook Dell", new BigDecimal("3500.00"), 10);

        when(produtoRepository.existsByNomeIgnoreCase("Notebook")).thenReturn(true);

        ProdutoJaCadastradoException exception = assertThrows(ProdutoJaCadastradoException.class, () -> {
            produtoService.criarProduto(request);
        });

        assertEquals("Já existe um produto cadastrado com o nome: Notebook", exception.getMessage());
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    // ========================================================================
    // TESTES DE BUSCA E LISTAGEM
    // ========================================================================

    @Test
    @DisplayName("Deve buscar um produto por ID com sucesso")
    void deveBuscarProdutoPorIdComSucesso() {
        Produto produto = new Produto(1L, "Mouse", "Mouse Gamer", new BigDecimal("150.00"), 20, LocalDateTime.now());

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoResponseDTO response = produtoService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals("Mouse", response.nome());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar produto com ID inexistente")
    void deveLancarExcecaoAoBuscarProdutoInexistente() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        ProdutoNaoEncontradoException exception = assertThrows(ProdutoNaoEncontradoException.class, () -> {
            produtoService.buscarPorId(99L);
        });

        assertEquals("Produto não encontrado com o ID: 99", exception.getMessage());
    }

    // ========================================================================
    // TESTES DE EXCLUSÃO
    // ========================================================================

    @Test
    @DisplayName("Deve remover um produto com sucesso")
    void deveRemoverProdutoComSucesso() {
        Produto produto = new Produto(1L, "Teclado", "Mecânico", new BigDecimal("300.00"), 15, LocalDateTime.now());

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        doNothing().when(produtoRepository).delete(produto);

        produtoService.removerProduto(1L);

        verify(produtoRepository, times(1)).delete(produto);
    }

    // ========================================================================
    // TESTES DA REGRA DE NEGÓCIO: VENDA
    // ========================================================================

    @Test
    @DisplayName("Deve realizar a venda e abater o estoque corretamente")
    void deveVenderProdutoComSucesso() {
        // Estoque inicial: 10
        Produto produtoNoBanco = new Produto(1L, "Monitor", "Monitor 24p", new BigDecimal("1000.00"), 10, LocalDateTime.now());
        VendaRequestDTO vendaRequest = new VendaRequestDTO(3); // Quer comprar 3

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoNoBanco));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProdutoResponseDTO response = produtoService.venderProduto(1L, vendaRequest);

        // Verifica se o estoque caiu para 7
        assertEquals(7, response.quantidade());
        verify(produtoRepository, times(1)).save(produtoNoBanco);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar vender quantidade maior que o estoque")
    void naoDeveVenderComEstoqueInsuficiente() {
        // Estoque inicial: 2
        Produto produtoNoBanco = new Produto(1L, "Monitor", "Monitor 24p", new BigDecimal("1000.00"), 2, LocalDateTime.now());
        VendaRequestDTO vendaRequest = new VendaRequestDTO(5); // Quer comprar 5

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoNoBanco));

        EstoqueInsuficienteException exception = assertThrows(EstoqueInsuficienteException.class, () -> {
            produtoService.venderProduto(1L, vendaRequest);
        });

        assertEquals("Estoque insuficiente. Quantidade disponível: 2", exception.getMessage());

        // Garante que o método save NUNCA foi chamado, protegendo o banco
        verify(produtoRepository, never()).save(any(Produto.class));
    }
}