package com.pcdoc.backend.service;

import com.pcdoc.backend.dto.ProdutoRequestDTO;
import com.pcdoc.backend.dto.ProdutoResponseDTO;
import com.pcdoc.backend.dto.VendaRequestDTO;
import com.pcdoc.backend.entity.Produto;
import com.pcdoc.backend.exception.EstoqueInsuficienteException;
import com.pcdoc.backend.exception.ProdutoJaCadastradoException;
import com.pcdoc.backend.exception.ProdutoNaoEncontradoException;
import com.pcdoc.backend.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Transactional
    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO produtoRequestDTO) {
        if (produtoRepository.existsByNomeIgnoreCase(produtoRequestDTO.nome())) {
            throw new ProdutoJaCadastradoException("Já existe um novoProduto cadastrado com o nome: " + produtoRequestDTO.nome());
        }

        Produto novoProduto = new Produto();
        novoProduto.setNome(produtoRequestDTO.nome());
        novoProduto.setDescricao(produtoRequestDTO.descricao());
        novoProduto.setPreco(produtoRequestDTO.preco());
        novoProduto.setQuantidade(produtoRequestDTO.quantidade());

        novoProduto = produtoRepository.save(novoProduto);
        return mapToResponse(novoProduto);
    }

    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO produtoRequestDTO) {
        Produto produto = buscarEntidadePorId(id);

        if (!produto.getNome().equalsIgnoreCase(produtoRequestDTO.nome()) &&
                produtoRepository.existsByNomeIgnoreCase(produtoRequestDTO.nome())) {
            throw new ProdutoJaCadastradoException("Já existe um produto cadastrado com o nome: " + produtoRequestDTO.nome());
        }

        produto.setNome(produtoRequestDTO.nome());
        produto.setDescricao(produtoRequestDTO.descricao());
        produto.setPreco(produtoRequestDTO.preco());
        produto.setQuantidade(produtoRequestDTO.quantidade());

        produto = produtoRepository.save(produto);
        return mapToResponse(produto);
    }

    @Transactional
    public void removerProduto(Long id) {
        Produto produto = buscarEntidadePorId(id);
        produtoRepository.delete(produto);
    }

    @Transactional
    public ProdutoResponseDTO venderProduto(Long id, VendaRequestDTO vendaRequestDTO) {
        Produto produto = buscarEntidadePorId(id);

        if (produto.getQuantidade() < vendaRequestDTO.quantidade()) {
            throw new EstoqueInsuficienteException("Estoque insuficiente. Quantidade disponível: " + produto.getQuantidade());
        }

        produto.setQuantidade(produto.getQuantidade() - vendaRequestDTO.quantidade());
        produto = produtoRepository.save(produto);

        return mapToResponse(produto);
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = buscarEntidadePorId(id);
        return mapToResponse(produto);
    }

    private Produto buscarEntidadePorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado com o ID: " + id));
    }

    private ProdutoResponseDTO mapToResponse(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getDataCriacao()
        );
    }
}
