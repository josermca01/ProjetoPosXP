package API.ProjetoPosXP.listener;

import API.ProjetoPosXP.event.PedidoStatusEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailNotificationListener {

    @Async
    @EventListener
    public void onPedidoStatusChanged(PedidoStatusEvent event) {
        String clienteNome = event.getPedido().getCliente().getNome();
        String clienteEmail = event.getPedido().getCliente().getEmail();
        Long pedidoId = event.getPedido().getId();
        String status = event.getNovoStatus().name();

        log.info("📧 [SIMULAÇÃO DE EMAIL] Enviando notificação para {}: ", clienteEmail);
        log.info("Olá {}, o status do seu pedido #{} foi atualizado para: {}", 
                 clienteNome, pedidoId, status);
        
        // Simula processamento
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        log.info("✅ Notificação enviada com sucesso para {}", clienteEmail);
    }
}
