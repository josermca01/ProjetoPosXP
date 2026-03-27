package API.ProjetoPosXP.service;

import API.ProjetoPosXP.dto.ClienteDTO;
import API.ProjetoPosXP.exception.ResourceNotFoundException;
import API.ProjetoPosXP.mapper.ClienteMapper;
import API.ProjetoPosXP.model.Cliente;
import API.ProjetoPosXP.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Transactional(readOnly = true)
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long contar() {
        return clienteRepository.count();
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public ClienteDTO buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .map(clienteMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
    }

    @Transactional
    public ClienteDTO salvar(ClienteDTO dto) {
        Cliente cliente = clienteMapper.toEntity(dto);
        return clienteMapper.toDTO(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteDTO atualizar(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));

        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setCpf(dto.cpf());

        return clienteMapper.toDTO(clienteRepository.save(cliente));
    }

    @Transactional
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente", id);
        }
        clienteRepository.deleteById(id);
    }
}
