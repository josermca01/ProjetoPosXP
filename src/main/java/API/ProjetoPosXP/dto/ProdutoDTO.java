package API.ProjetoPosXP.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProdutoDTO(
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    Integer estoque
) implements Serializable {}
