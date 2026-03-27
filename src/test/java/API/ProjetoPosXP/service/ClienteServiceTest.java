package API.ProjetoPosXP.service;

import API.ProjetoPosXP.dto.ClienteDTO;
import API.ProjetoPosXP.exception.ResourceNotFoundException;
import API.ProjetoPosXP.mapper.ClienteMapper;
import API.ProjetoPosXP.model.Cliente;
import API.ProjetoPosXP.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .id(1L)
                .nome("João Silva")
                .email("joao@example.com")
                .cpf("12345678900")
                .build();
        
        clienteDTO = new ClienteDTO(1L, "João Silva", "joao@example.com", "12345678900");
    }

    @Test
    @DisplayName("Deve listar todos os clientes")
    void deveListarTodosOsClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        when(clienteMapper.toDTO(cliente)).thenReturn(clienteDTO);
        
        List<ClienteDTO> resultado = clienteService.listarTodos();
        
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).nome()).isEqualTo("João Silva");
        verify(clienteRepository, times(1)).findAll();
        verify(clienteMapper, times(1)).toDTO(cliente);
    }

    @Test
    @DisplayName("Deve buscar cliente por ID com sucesso")
    void deveBuscarPorIdComSucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDTO(cliente)).thenReturn(clienteDTO);
        
        ClienteDTO resultado = clienteService.buscarPorId(1L);
        
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("João Silva");
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar cliente inexistente")
    void deveLancarExcecaoAoBuscarInexistente() {
        when(clienteRepository.findById(2L)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> clienteService.buscarPorId(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente não encontrado");
    }

    @Test
    @DisplayName("Deve salvar novo cliente")
    void deveSalvarNovoCliente() {
        when(clienteMapper.toEntity(clienteDTO)).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toDTO(cliente)).thenReturn(clienteDTO);
        
        ClienteDTO resultado = clienteService.salvar(clienteDTO);
        
        assertThat(resultado.nome()).isEqualTo("João Silva");
        verify(clienteMapper, times(1)).toEntity(clienteDTO);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(clienteMapper, times(1)).toDTO(cliente);
    }

    @Test
    @DisplayName("Deve atualizar cliente existente")
    void deveAtualizarCliente() {
        ClienteDTO novoDto = new ClienteDTO(1L, "João Modificado", "joao@mod.com", "111");
        Cliente clienteAtualizado = Cliente.builder()
                .id(1L).nome("João Modificado").email("joao@mod.com").cpf("111").build();
        ClienteDTO dtoAtualizado = new ClienteDTO(1L, "João Modificado", "joao@mod.com", "111");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteAtualizado);
        when(clienteMapper.toDTO(clienteAtualizado)).thenReturn(dtoAtualizado);
        
        ClienteDTO resultado = clienteService.atualizar(1L, novoDto);
        
        assertThat(resultado.nome()).isEqualTo("João Modificado");
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve deletar cliente com sucesso")
    void deveDeletarComSucesso() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);
        
        clienteService.deletar(1L);
        
        verify(clienteRepository, times(1)).existsById(1L);
        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar cliente inexistente")
    void deveLancarExcecaoAoDeletarInexistente() {
        when(clienteRepository.existsById(2L)).thenReturn(false);
        
        assertThatThrownBy(() -> clienteService.deletar(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente não encontrado");
    }
}
