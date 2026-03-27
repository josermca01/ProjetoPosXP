package API.ProjetoPosXP.service;

import API.ProjetoPosXP.dto.ProdutoDTO;
import API.ProjetoPosXP.exception.ResourceNotFoundException;
import API.ProjetoPosXP.mapper.ProdutoMapper;
import API.ProjetoPosXP.model.Produto;
import API.ProjetoPosXP.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(produtoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .map(produtoMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", id));
    }

    @Transactional
    public ProdutoDTO salvar(ProdutoDTO dto) {
        Produto produto = produtoMapper.toEntity(dto);
        return produtoMapper.toDTO(produtoRepository.save(produto));
    }

    @Transactional
    public ProdutoDTO atualizar(Long id, ProdutoDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", id));

        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setEstoque(dto.estoque());

        return produtoMapper.toDTO(produtoRepository.save(produto));
    }

    @Transactional
    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto", id);
        }
        produtoRepository.deleteById(id);
    }
}
