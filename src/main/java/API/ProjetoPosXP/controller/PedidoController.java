package API.ProjetoPosXP.controller;

import API.ProjetoPosXP.dto.PedidoCreateDTO;
import API.ProjetoPosXP.dto.PedidoDTO;
import API.ProjetoPosXP.facade.AppFacade;
import API.ProjetoPosXP.model.StatusPedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Endpoints para checkout e monitoramento de pedidos")
public class PedidoController {

    private final AppFacade appFacade;

    @GetMapping
    @Operation(summary = "Listar todos os pedidos", description = "Retorna o histórico completo de pedidos realizados")
    public List<PedidoDTO> listar() {
        return appFacade.listarPedidos();
    }

    @GetMapping("/count")
    @Operation(summary = "Contar total de pedidos", description = "Retorna o número total de pedidos realizados")
    public long contar() {
        return appFacade.contarPedidos();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar pedidos por nome do cliente", description = "Retorna uma lista de pedidos realizados por clientes com o termo pesquisado")
    public List<PedidoDTO> buscarPorClienteNome(@RequestParam String nome) {
        return appFacade.buscarPedidosPorClienteNome(nome);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Consultar pedido por ID", description = "Retorna os itens e status de um pedido específico")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    public PedidoDTO buscar(@PathVariable Long id) {
        return appFacade.buscarPedidoPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Realizar Checkout", description = "Cria um novo pedido e processa a baixa de estoque")
    @ApiResponse(responseCode = "201", description = "Pedido realizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação ou estoque insuficiente")
    public PedidoDTO criar(@Valid @RequestBody PedidoCreateDTO dto) {
        return appFacade.criarPedido(dto);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Altera o estado do pedido (ex: PAGO, ENVIADO)")
    @ApiResponse(responseCode = "200", description = "Status atualizado e cliente notificado via Observer")
    public PedidoDTO atualizarStatus(
            @PathVariable Long id, 
            @Parameter(description = "Novo status do pedido") @RequestParam StatusPedido status) {
        return appFacade.atualizarStatusPedido(id, status);
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar pedido", description = "Cancela o pedido e estorna os itens para o estoque")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable Long id) {
        appFacade.cancelarPedido(id);
    }
}
