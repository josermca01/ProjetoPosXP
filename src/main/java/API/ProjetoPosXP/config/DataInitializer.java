package API.ProjetoPosXP.config;

import API.ProjetoPosXP.model.*;
import API.ProjetoPosXP.repository.ClienteRepository;
import API.ProjetoPosXP.repository.PedidoRepository;
import API.ProjetoPosXP.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Profile("!test") // Não executa durante os testes unitários para não interferir
public class DataInitializer implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (clienteRepository.count() > 0) {
            return; // Banco já populado
        }

        // 1. Criar Clientes
        Cliente c1 = Cliente.builder().nome("Jose da Silva").email("jose@email.com").cpf("12345678901").build();
        Cliente c2 = Cliente.builder().nome("Maria Oliveira").email("maria@email.com").cpf("98765432100").build();
        clienteRepository.saveAll(List.of(c1, c2));

        // 2. Criar Produtos
        Produto p1 = Produto.builder().nome("Notebook Gamer").descricao("Ryzen 7, 16GB RAM, RTX 3060").preco(new BigDecimal("5500.00")).estoque(10).build();
        Produto p2 = Produto.builder().nome("Mouse Wireless").descricao("Logitech MX Master 3S").preco(new BigDecimal("450.00")).estoque(50).build();
        Produto p3 = Produto.builder().nome("Monitor 4K").descricao("Dell UltraSharp 27\"").preco(new BigDecimal("2800.00")).estoque(15).build();
        produtoRepository.saveAll(List.of(p1, p2, p3));

        // 3. Criar um Pedido Inicial para Jose
        Pedido ped1 = Pedido.builder()
                .cliente(c1)
                .dataPedido(LocalDateTime.now().minusDays(1))
                .status(StatusPedido.PAGO)
                .build();

        ItemPedido item1 = ItemPedido.builder()
                .pedido(ped1)
                .produto(p1)
                .quantidade(1)
                .precoUnitario(p1.getPreco())
                .build();

        ped1.adicionarItem(item1);
        pedidoRepository.save(ped1);

        System.out.println(">>> Banco de dados H2 populado com dados iniciais de demonstração.");
    }
}
