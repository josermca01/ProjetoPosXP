package API.ProjetoPosXP.dto;

import API.ProjetoPosXP.model.StatusPedido;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoDTO(
    Long id,
    Long clienteId,
    String clienteNome,
    LocalDateTime dataPedido,
    StatusPedido status,
    List<ItemPedidoDTO> itens
) implements Serializable {}
