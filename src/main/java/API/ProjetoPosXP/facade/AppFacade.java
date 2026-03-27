package API.ProjetoPosXP.facade;

import API.ProjetoPosXP.dto.*;
import API.ProjetoPosXP.model.StatusPedido;
import API.ProjetoPosXP.service.ClienteService;
import API.ProjetoPosXP.service.PedidoService;
import API.ProjetoPosXP.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppFacade {

    private final ClienteService clienteService;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;

    // Cliente operations
    public List<ClienteDTO> listarClientes() {
        return clienteService.listarTodos();
    }

    public ClienteDTO buscarClientePorId(Long id) {
        return clienteService.buscarPorId(id);
    }

    public ClienteDTO salvarCliente(ClienteDTO dto) {
        return clienteService.salvar(dto);
    }

    public ClienteDTO atualizarCliente(Long id, ClienteDTO dto) {
        return clienteService.atualizar(id, dto);
    }

    public void deletarCliente(Long id) {
        clienteService.deletar(id);
    }

    // Produto operations
    public List<ProdutoDTO> listarProdutos() {
        return produtoService.listarTodos();
    }

    public ProdutoDTO buscarProdutoPorId(Long id) {
        return produtoService.buscarPorId(id);
    }

    public ProdutoDTO salvarProduto(ProdutoDTO dto) {
        return produtoService.salvar(dto);
    }

    public ProdutoDTO atualizarProduto(Long id, ProdutoDTO dto) {
        return produtoService.atualizar(id, dto);
    }

    public void deletarProduto(Long id) {
        produtoService.deletar(id);
    }

    // Pedido operations
    public List<PedidoDTO> listarPedidos() {
        return pedidoService.listarTodos();
    }

    public PedidoDTO buscarPedidoPorId(Long id) {
        return pedidoService.buscarPorId(id);
    }

    public PedidoDTO criarPedido(PedidoCreateDTO dto) {
        return pedidoService.criarPedido(dto);
    }

    public PedidoDTO atualizarStatusPedido(Long id, StatusPedido status) {
        return pedidoService.atualizarStatus(id, status);
    }

    public void cancelarPedido(Long id) {
        pedidoService.cancelarPedido(id);
    }
}
