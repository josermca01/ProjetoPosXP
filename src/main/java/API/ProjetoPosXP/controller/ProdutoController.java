package API.ProjetoPosXP.controller;

import API.ProjetoPosXP.dto.ProdutoDTO;
import API.ProjetoPosXP.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos e estoque")
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    @Operation(summary = "Listar todos os produtos", description = "Retorna o catálogo completo de produtos")
    public List<ProdutoDTO> listar() {
        return produtoService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Pesquisa os detalhes de um produto específico")
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    public ResponseEntity<ProdutoDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar produto", description = "Adiciona um novo produto ao estoque")
    @ApiResponse(responseCode = "201", description = "Produto cadastrado")
    public ResponseEntity<ProdutoDTO> salvar(@Valid @RequestBody ProdutoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.salvar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Modifica dados de um produto existente")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover produto", description = "Remove um produto permanentemente")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        produtoService.deletar(id);
    }
}
