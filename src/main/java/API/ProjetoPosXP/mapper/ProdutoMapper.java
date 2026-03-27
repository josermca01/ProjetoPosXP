package API.ProjetoPosXP.mapper;

import API.ProjetoPosXP.dto.ProdutoDTO;
import API.ProjetoPosXP.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public ProdutoDTO toDTO(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getEstoque()
        );
    }

    public Produto toEntity(ProdutoDTO dto) {
        return Produto.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .preco(dto.preco())
                .estoque(dto.estoque())
                .build();
    }
}
