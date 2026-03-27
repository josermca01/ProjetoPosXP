package API.ProjetoPosXP.controller;

import API.ProjetoPosXP.dto.PedidoCreateDTO;
import API.ProjetoPosXP.dto.PedidoDTO;
import API.ProjetoPosXP.model.StatusPedido;
import API.ProjetoPosXP.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Endpoints para checkout e monitoramento de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    @Operation(summary = "Listar todos os pedidos", description = "Retorna o histórico completo de pedidos realizados")
    public List<PedidoDTO> listar() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar pedido por ID", description = "Retorna os itens e status de um pedido específico")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    public ResponseEntity<PedidoDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Realizar Checkout", description = "Cria um novo pedido e processa a baixa de estoque")
    @ApiResponse(responseCode = "201", description = "Pedido realizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação ou estoque insuficiente")
    public ResponseEntity<PedidoDTO> criar(@Valid @RequestBody PedidoCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criarPedido(dto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Altera o estado do pedido (ex: PAGO, ENVIADO)")
    @ApiResponse(responseCode = "200", description = "Status atualizado e cliente notificado via Observer")
    public ResponseEntity<PedidoDTO> atualizarStatus(
            @PathVariable Long id, 
            @Parameter(description = "Novo status do pedido") @RequestParam StatusPedido status) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, status));
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar pedido", description = "Cancela o pedido e estorna os itens para o estoque")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
    }
}
