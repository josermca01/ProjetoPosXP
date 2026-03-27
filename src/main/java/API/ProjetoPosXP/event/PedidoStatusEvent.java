package API.ProjetoPosXP.event;

import API.ProjetoPosXP.model.Pedido;
import API.ProjetoPosXP.model.StatusPedido;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PedidoStatusEvent extends ApplicationEvent {
    
    private final Pedido pedido;
    private final StatusPedido novoStatus;

    public PedidoStatusEvent(Object source, Pedido pedido, StatusPedido novoStatus) {
        super(source);
        this.pedido = pedido;
        this.novoStatus = novoStatus;
    }
}
