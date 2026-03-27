package API.ProjetoPosXP.repository;

import API.ProjetoPosXP.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    java.util.List<Cliente> findByNomeContainingIgnoreCase(String nome);
}

