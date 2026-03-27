package API.ProjetoPosXP.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoCreateDTO(
    @NotNull(message = "ID do produto é obrigatório")
    Long produtoId,
    
    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade mínima é 1")
    Integer quantidade
) {}
