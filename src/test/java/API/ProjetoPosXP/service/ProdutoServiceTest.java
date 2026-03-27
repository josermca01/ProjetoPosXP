package API.ProjetoPosXP.service;

import API.ProjetoPosXP.dto.ProdutoDTO;
import API.ProjetoPosXP.exception.ResourceNotFoundException;
import API.ProjetoPosXP.mapper.ProdutoMapper;
import API.ProjetoPosXP.model.Produto;
import API.ProjetoPosXP.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;
    private ProdutoDTO produtoDTO;

    @BeforeEach
    void setUp() {
        produto = Produto.builder()
                .id(1L)
                .nome("Teclado")
                .descricao("Mecânico")
                .preco(new BigDecimal("250.00"))
                .estoque(10)
                .build();
        
        produtoDTO = new ProdutoDTO(1L, "Teclado", "Mecânico", new BigDecimal("250.00"), 10);
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void deveListarTodosOsProdutos() {
        when(produtoRepository.findAll()).thenReturn(List.of(produto));
        when(produtoMapper.toDTO(produto)).thenReturn(produtoDTO);
        
        List<ProdutoDTO> resultado = produtoService.listarTodos();
        
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).nome()).isEqualTo("Teclado");
        verify(produtoRepository, times(1)).findAll();
        verify(produtoMapper, times(1)).toDTO(produto);
    }

    @Test
    @DisplayName("Deve buscar produto por ID com sucesso")
    void deveBuscarPorIdComSucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoMapper.toDTO(produto)).thenReturn(produtoDTO);
        
        ProdutoDTO resultado = produtoService.buscarPorId(1L);
        
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("Teclado");
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar produto inexistente")
    void deveLancarExcecaoAoBuscarInexistente() {
        when(produtoRepository.findById(2L)).thenReturn(Optional.empty());
        
        assertThatThrownBy(() -> produtoService.buscarPorId(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Produto não encontrado");
    }

    @Test
    @DisplayName("Deve salvar novo produto")
    void deveSalvarNovoProduto() {
        when(produtoMapper.toEntity(produtoDTO)).thenReturn(produto);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
        when(produtoMapper.toDTO(produto)).thenReturn(produtoDTO);
        
        ProdutoDTO resultado = produtoService.salvar(produtoDTO);
        
        assertThat(resultado.nome()).isEqualTo("Teclado");
        verify(produtoMapper, times(1)).toEntity(produtoDTO);
        verify(produtoRepository, times(1)).save(any(Produto.class));
        verify(produtoMapper, times(1)).toDTO(produto);
    }

    @Test
    @DisplayName("Deve atualizar produto existente")
    void deveAtualizarProduto() {
        ProdutoDTO novoDto = new ProdutoDTO(1L, "Mouse", "Gamer", new BigDecimal("150.00"), 5);
        Produto produtoAtualizado = Produto.builder()
                .id(1L).nome("Mouse").descricao("Gamer")
                .preco(new BigDecimal("150.00")).estoque(5).build();
        ProdutoDTO dtoAtualizado = new ProdutoDTO(1L, "Mouse", "Gamer", new BigDecimal("150.00"), 5);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoAtualizado);
        when(produtoMapper.toDTO(produtoAtualizado)).thenReturn(dtoAtualizado);
        
        ProdutoDTO resultado = produtoService.atualizar(1L, novoDto);
        
        assertThat(resultado.nome()).isEqualTo("Mouse");
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve deletar produto com sucesso")
    void deveDeletarComSucesso() {
        when(produtoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(produtoRepository).deleteById(1L);
        
        produtoService.deletar(1L);
        
        verify(produtoRepository, times(1)).existsById(1L);
        verify(produtoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar produto inexistente")
    void deveLancarExcecaoAoDeletarInexistente() {
        when(produtoRepository.existsById(2L)).thenReturn(false);
        
        assertThatThrownBy(() -> produtoService.deletar(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Produto não encontrado");
    }
}
