package API.ProjetoPosXP.service;

import API.ProjetoPosXP.dto.ItemPedidoCreateDTO;
import API.ProjetoPosXP.dto.ItemPedidoDTO;
import API.ProjetoPosXP.dto.PedidoCreateDTO;
import API.ProjetoPosXP.dto.PedidoDTO;
import API.ProjetoPosXP.event.PedidoStatusEvent;
import API.ProjetoPosXP.exception.BusinessException;
import API.ProjetoPosXP.exception.EstoqueInsuficienteException;
import API.ProjetoPosXP.mapper.PedidoMapper;
import API.ProjetoPosXP.model.*;
import API.ProjetoPosXP.repository.ClienteRepository;
import API.ProjetoPosXP.repository.PedidoRepository;
import API.ProjetoPosXP.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private PedidoMapper pedidoMapper;

    @InjectMocks
    private PedidoService pedidoService;

    private Cliente cliente;
    private Produto produto;
    private Pedido pedido;
    private PedidoDTO pedidoDTO;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder().id(1L).nome("João").build();
        produto = Produto.builder().id(1L).nome("Teclado").preco(new BigDecimal("100.00")).estoque(10).build();

        ItemPedido item = ItemPedido.builder()
                .id(1L)
                .produto(produto)
                .quantidade(2)
                .precoUnitario(new BigDecimal("100.00"))
                .build();

        pedido = Pedido.builder()
                .id(1L)
                .cliente(cliente)
                .dataPedido(LocalDateTime.now())
                .status(StatusPedido.AGUARDANDO_PAGAMENTO)
                .itens(List.of(item))
                .build();

        item.setPedido(pedido);

        pedidoDTO = new PedidoDTO(
                1L, 1L, "João", pedido.getDataPedido(),
                StatusPedido.AGUARDANDO_PAGAMENTO,
                List.of(new ItemPedidoDTO(1L, 1L, "Teclado", 2, new BigDecimal("100.00"))));
    }

    @Test
    @DisplayName("Deve criar pedido com sucesso e baixar estoque")
    void deveCriarPedidoComSucesso() {
        PedidoCreateDTO dto = new PedidoCreateDTO(1L, List.of(new ItemPedidoCreateDTO(1L, 2)));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pedidoMapper.toDTO(any(Pedido.class))).thenReturn(pedidoDTO);

        PedidoDTO resultado = pedidoService.criarPedido(dto);

        assertThat(resultado.status()).isEqualTo(StatusPedido.AGUARDANDO_PAGAMENTO);
        assertThat(produto.getEstoque()).isEqualTo(8);
        verify(produtoRepository, times(1)).save(produto);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar EstoqueInsuficienteException quando estoque for insuficiente")
    void deveLancarExcecaoEstoqueInsuficiente() {
        PedidoCreateDTO dto = new PedidoCreateDTO(1L, List.of(new ItemPedidoCreateDTO(1L, 20)));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        assertThatThrownBy(() -> pedidoService.criarPedido(dto))
                .isInstanceOf(EstoqueInsuficienteException.class)
                .hasMessageContaining("Estoque insuficiente");

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve cancelar pedido e devolver estoque")
    void deveCancelarPedidoComSucesso() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        produto.setEstoque(8);
        pedidoService.cancelarPedido(1L);

        assertThat(pedido.getStatus()).isEqualTo(StatusPedido.CANCELADO);
        assertThat(produto.getEstoque()).isEqualTo(10);
        verify(produtoRepository, times(1)).save(produto);
        verify(eventPublisher, times(1)).publishEvent(any(PedidoStatusEvent.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao cancelar pedido já cancelado")
    void deveLancarExcecaoPedidoJaCancelado() {
        pedido.setStatus(StatusPedido.CANCELADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        assertThatThrownBy(() -> pedidoService.cancelarPedido(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Pedido já está cancelado");
    }

    @Test
    @DisplayName("Deve atualizar status do pedido")
    void deveAtualizarStatus() {
        PedidoDTO dtoPago = new PedidoDTO(
                1L, 1L, "João", pedido.getDataPedido(), StatusPedido.PAGO, pedidoDTO.itens());

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pedidoMapper.toDTO(any(Pedido.class))).thenReturn(dtoPago);

        PedidoDTO resultado = pedidoService.atualizarStatus(1L, StatusPedido.PAGO);

        assertThat(resultado.status()).isEqualTo(StatusPedido.PAGO);
        verify(pedidoRepository, times(1)).save(pedido);
        verify(eventPublisher, times(1)).publishEvent(any(PedidoStatusEvent.class));
    }
}
