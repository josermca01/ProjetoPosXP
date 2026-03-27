# ProjetoPosXP - API de Gestão de Pedidos

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-brightgreen)
![Java](https://img.shields.io/badge/Java-21-blue)
![Maven](https://img.shields.io/badge/Maven-3.9+-red)

Este projeto é uma API RESTful desenvolvida para o gerenciamento de um domínio de **E-commerce**, abrangendo as entidades de **Clientes**, **Produtos** e **Pedidos**. A aplicação segue padrões arquiteturais modernos e princípios de código limpo.

## 🚀 Tecnologias Utilizadas

- **Java 21**: Utilização de recursos modernos como Records para DTOs.
- **Spring Boot 4.0.5**: Framework base para a aplicação.
- **Spring Data JPA**: Abstração da camada de persistência.
- **H2 Database**: Banco de dados em memória para desenvolvimento e testes rápidos.
- **Lombok**: Redução de código boilerplate.
- **SpringDoc OpenAPI (Swagger)**: Documentação interativa da API.
- **Spring Events**: Implementação do padrão de projeto **Observer**.

## 🏗️ Arquitetura e Padrões

O projeto segue uma estrutura de camadas bem definida:
1.  **Controller**: Gerencia as rotas e a comunicação externa.
2.  **Service**: Contém as regras de negócio (ex: cálculo de estoque, validações).
3.  **Repository**: Interface de acesso aos dados.
4.  **DTO (Data Transfer Object)**: Separação total entre as entidades do banco e os dados da API.
5.  **Observer Pattern**: Implementado via `Spring Events` para notificar clientes sobre mudanças no status de seus pedidos de forma assíncrona.

## 🛠️ Como Executar

1.  Certifique-se de ter o **Java 21** e **Maven** instalados.
2.  Clone o repositório.
3.  Execute o comando:
    ```bash
    mvn spring-boot:run
    ```

## 📖 Documentação da API (Swagger)

A documentação interativa pode ser acessada enquanto a aplicação estiver rodando no endereço:
👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Principais Endpoints:

- `GET /api/clientes` - Lista todos os clientes.
- `POST /api/produtos` - Cadastra um novo produto.
- `POST /api/pedidos` - Realiza o checkout de um pedido (baixa no estoque).
- `PATCH /api/pedidos/{id}/status` - Atualiza o status e notifica o cliente.

## ✅ Testes

Para executar os testes unitários:
```bash
mvn test
```

---
Desenvolvido como projeto de demonstração para a **XPEducation**.
