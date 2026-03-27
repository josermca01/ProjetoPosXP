package API.ProjetoPosXP.mapper;

import API.ProjetoPosXP.dto.ClienteDTO;
import API.ProjetoPosXP.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getCpf()
        );
    }

    public Cliente toEntity(ClienteDTO dto) {
        return Cliente.builder()
                .nome(dto.nome())
                .email(dto.email())
                .cpf(dto.cpf())
                .build();
    }
}
