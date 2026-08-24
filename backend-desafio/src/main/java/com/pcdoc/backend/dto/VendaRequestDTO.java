package com.pcdoc.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record VendaRequestDTO(
        @NotNull(message = "A quantidade é obrigatória.")
        @Min(value = 1, message = "A quantidade de venda deve ser pelo menos 1.")
        Integer quantidade
) {
}
