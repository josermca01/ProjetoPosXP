package API.ProjetoPosXP.service;

import API.ProjetoPosXP.dto.PedidoCreateDTO;
import API.ProjetoPosXP.dto.PedidoDTO;
import API.ProjetoPosXP.exception.BusinessException;
import API.ProjetoPosXP.exception.EstoqueInsuficienteException;
import API.ProjetoPosXP.exception.ResourceNotFoundException;
import API.ProjetoPosXP.mapper.PedidoMapper;
import API.ProjetoPosXP.model.*;
import API.ProjetoPosXP.event.PedidoStatusEvent;
import API.ProjetoPosXP.repository.ClienteRepository;
import API.ProjetoPosXP.repository.PedidoRepository;
import API.ProjetoPosXP.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PedidoMapper pedidoMapper;

    @Transactional(readOnly = true)
    public List<PedidoDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> buscarPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException("Cliente", clienteId);
        }
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(pedidoMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public PedidoDTO buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .map(pedidoMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
    }

    @Transactional
    public PedidoDTO criarPedido(PedidoCreateDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", dto.clienteId()));

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .dataPedido(LocalDateTime.now())
                .status(StatusPedido.AGUARDANDO_PAGAMENTO)
                .build();

        dto.itens().forEach(itemDto -> {
            Produto produto = produtoRepository.findById(itemDto.produtoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto", itemDto.produtoId()));

            if (produto.getEstoque() < itemDto.quantidade()) {
                throw new EstoqueInsuficienteException(
                        produto.getNome(),
                        produto.getEstoque(),
                        itemDto.quantidade()
                );
            }

            produto.setEstoque(produto.getEstoque() - itemDto.quantidade());
            produtoRepository.save(produto);

            ItemPedido item = ItemPedido.builder()
                    .pedido(pedido)
                    .produto(produto)
                    .quantidade(itemDto.quantidade())
                    .precoUnitario(produto.getPreco())
                    .build();

            pedido.adicionarItem(item);
        });

        return pedidoMapper.toDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoDTO atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));

        pedido.setStatus(novoStatus);
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        eventPublisher.publishEvent(new PedidoStatusEvent(this, pedidoSalvo, novoStatus));

        return pedidoMapper.toDTO(pedidoSalvo);
    }

    @Transactional
    public void cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new BusinessException("Pedido já está cancelado");
        }

        pedido.getItens().forEach(item -> {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
            produtoRepository.save(produto);
        });

        pedido.setStatus(StatusPedido.CANCELADO);
        Pedido pedidoCancelado = pedidoRepository.save(pedido);

        eventPublisher.publishEvent(new PedidoStatusEvent(this, pedidoCancelado, StatusPedido.CANCELADO));
    }
}
