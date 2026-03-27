package API.ProjetoPosXP.dto;

import java.io.Serializable;

public record ClienteDTO(
    Long id,
    String nome,
    String email,
    String cpf
) implements Serializable {}
