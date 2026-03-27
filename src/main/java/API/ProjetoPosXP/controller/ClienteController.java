package API.ProjetoPosXP.controller;

import API.ProjetoPosXP.dto.ClienteDTO;
import API.ProjetoPosXP.facade.AppFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
public class ClienteController {

    private final AppFacade appFacade;

    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista de todos os clientes cadastrados")
    public List<ClienteDTO> listar() {
        return appFacade.listarClientes();
    }

    @GetMapping("/{id}/pedidos")
    @Operation(summary = "Consultar histórico de pedidos", description = "Retorna todos os pedidos realizados por um cliente")
    @ApiResponse(responseCode = "200", description = "Histórico recuperado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public List<API.ProjetoPosXP.dto.PedidoDTO> consultarHistorico(@PathVariable Long id) {
        return appFacade.consultarHistoricoPedidos(id);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um único cliente com base no ID fornecido")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ClienteDTO buscar(@PathVariable Long id) {
        return appFacade.buscarClientePorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar novo cliente", description = "Cria um novo cliente no sistema")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
    public ClienteDTO salvar(@Valid @RequestBody ClienteDTO dto) {
        return appFacade.salvarCliente(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    public ClienteDTO atualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
        return appFacade.atualizarCliente(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover cliente", description = "Exclui um cliente permanentemente do sistema")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        appFacade.deletarCliente(id);
    }
}
