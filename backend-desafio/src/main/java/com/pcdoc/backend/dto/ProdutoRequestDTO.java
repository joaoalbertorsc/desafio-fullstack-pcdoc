package com.pcdoc.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        String nome,

        String descricao,

        @NotNull(message = "O preço é obrigatório.")
        @Min(value = 0, message = "O preço não pode ser negativo.")
        BigDecimal preco,

        @NotNull(message = "A quantidade é obrigatória.")
        @Min(value = 0, message = "A quantidade não pode ser negativa.")
        Integer quantidade
) {
}