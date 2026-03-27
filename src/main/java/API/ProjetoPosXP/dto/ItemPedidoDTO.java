package API.ProjetoPosXP.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record ItemPedidoDTO(
    Long id,
    Long produtoId,
    String produtoNome,
    Integer quantidade,
    BigDecimal precoUnitario
) implements Serializable {}
