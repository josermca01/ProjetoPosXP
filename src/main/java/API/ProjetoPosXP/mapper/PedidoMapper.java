package API.ProjetoPosXP.mapper;

import API.ProjetoPosXP.dto.ItemPedidoDTO;
import API.ProjetoPosXP.dto.PedidoDTO;
import API.ProjetoPosXP.model.ItemPedido;
import API.ProjetoPosXP.model.Pedido;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    public PedidoDTO toDTO(Pedido pedido) {
        List<ItemPedidoDTO> itensDTO = pedido.getItens().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());

        return new PedidoDTO(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                pedido.getDataPedido(),
                pedido.getStatus(),
                itensDTO
        );
    }

    private ItemPedidoDTO toItemDTO(ItemPedido item) {
        return new ItemPedidoDTO(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario()
        );
    }
}
