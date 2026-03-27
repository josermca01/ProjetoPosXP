package API.ProjetoPosXP.exception;

public class EstoqueInsuficienteException extends BusinessException {

    private final String produtoNome;
    private final Integer estoqueDisponivel;
    private final Integer quantidadeSolicitada;

    public EstoqueInsuficienteException(String produtoNome, Integer estoqueDisponivel, Integer quantidadeSolicitada) {
        super("Estoque insuficiente para o produto: " + produtoNome
                + ". Disponível: " + estoqueDisponivel
                + ", Solicitado: " + quantidadeSolicitada);
        this.produtoNome = produtoNome;
        this.estoqueDisponivel = estoqueDisponivel;
        this.quantidadeSolicitada = quantidadeSolicitada;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public Integer getEstoqueDisponivel() {
        return estoqueDisponivel;
    }

    public Integer getQuantidadeSolicitada() {
        return quantidadeSolicitada;
    }
}
