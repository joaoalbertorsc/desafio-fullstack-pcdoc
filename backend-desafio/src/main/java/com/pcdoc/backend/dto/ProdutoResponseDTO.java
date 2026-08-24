package com.pcdoc.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer quantidade,
        LocalDateTime dataCriacao
) {
}
