package API.ProjetoPosXP.repository;

import API.ProjetoPosXP.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    java.util.List<Produto> findByNomeContainingIgnoreCase(String nome);
}

