package com.pcdoc.backend.controller;

import com.pcdoc.backend.dto.ProdutoRequestDTO;
import com.pcdoc.backend.dto.ProdutoResponseDTO;
import com.pcdoc.backend.dto.VendaRequestDTO;
import com.pcdoc.backend.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de estoque e venda de produtos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Operation(summary = "Cadastra um novo produto", description = "Cria um novo produto na base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erros de validação nos dados enviados"),
            @ApiResponse(responseCode = "409", description = "Já existe um produto com o mesmo nome")
    })
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criarProduto(@Valid @RequestBody ProdutoRequestDTO produtoRequestDTO) {
        ProdutoResponseDTO produtoResponseDTO = produtoService.criarProduto(produtoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoResponseDTO);
    }

    @Operation(summary = "Lista todos os produtos", description = "Retorna uma lista contendo todos os produtos cadastrados no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        List<ProdutoResponseDTO> produtoResponseDTOList = produtoService.listarTodos();
        return ResponseEntity.ok(produtoResponseDTOList);
    }

    @Operation(summary = "Busca um produto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado com o ID informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        ProdutoResponseDTO produtoResponseDTO = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produtoResponseDTO);
    }

    @Operation(summary = "Atualiza um produto", description = "Substitui os dados de um produto existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erros de validação nos dados enviados"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado com o ID informado"),
            @ApiResponse(responseCode = "409", description = "O novo nome já está em uso por outro produto")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequestDTO produtoRequestDTO) {

        ProdutoResponseDTO produtoResponseDTO = produtoService.atualizarProduto(id, produtoRequestDTO);
        return ResponseEntity.ok(produtoResponseDTO);
    }

    @Operation(summary = "Remove um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso (Sem conteúdo no retorno)"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado com o ID informado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerProduto(@PathVariable Long id) {
        produtoService.removerProduto(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Realiza a venda de um produto", description = "Verifica se há estoque disponível e abate a quantidade vendida.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda realizada com sucesso e estoque atualizado"),
            @ApiResponse(responseCode = "400", description = "Quantidade de venda inválida (ex: menor que 1)"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado com o ID informado"),
            @ApiResponse(responseCode = "409", description = "Estoque insuficiente para realizar a venda")
    })
    @PostMapping("/{id}/vender")
    public ResponseEntity<ProdutoResponseDTO> venderProduto(
            @PathVariable Long id,
            @Valid @RequestBody VendaRequestDTO vendaRequestDTO) {

        ProdutoResponseDTO produtoResponseDTO = produtoService.venderProduto(id, vendaRequestDTO);
        return ResponseEntity.ok(produtoResponseDTO);
    }
}
