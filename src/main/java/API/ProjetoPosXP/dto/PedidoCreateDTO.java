package API.ProjetoPosXP.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoCreateDTO(
    @NotNull(message = "ID do cliente é obrigatório")
    Long clienteId,

    @NotEmpty(message = "O pedido deve ter pelo menos um item")
    List<ItemPedidoCreateDTO> itens
) {}
