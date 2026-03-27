package API.ProjetoPosXP.controller;

import API.ProjetoPosXP.dto.ProdutoDTO;
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
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos e estoque")
public class ProdutoController {

    private final AppFacade appFacade;

    @GetMapping
    @Operation(summary = "Listar todos os produtos", description = "Retorna o catálogo completo de produtos")
    public List<ProdutoDTO> listar() {
        return appFacade.listarProdutos();
    }

    @GetMapping("/count")
    @Operation(summary = "Contar total de produtos", description = "Retorna o número total de produtos no estoque")
    public long contar() {
        return appFacade.contarProdutos();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar produtos por nome", description = "Retorna uma lista de produtos que contêm o termo pesquisado")
    public List<ProdutoDTO> buscarPorNome(@RequestParam String nome) {
        return appFacade.buscarProdutosPorNome(nome);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Pesquisa os detalhes de um produto específico")
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    public ProdutoDTO buscar(@PathVariable Long id) {
        return appFacade.buscarProdutoPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar produto", description = "Adiciona um novo produto ao estoque")
    @ApiResponse(responseCode = "201", description = "Produto cadastrado")
    public ProdutoDTO salvar(@Valid @RequestBody ProdutoDTO dto) {
        return appFacade.salvarProduto(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Modifica dados de um produto existente")
    public ProdutoDTO atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        return appFacade.atualizarProduto(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover produto", description = "Remove um produto permanentemente")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        appFacade.deletarProduto(id);
    }
}
