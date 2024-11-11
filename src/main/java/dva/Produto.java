package dva;

public class Produto {
    private String codBarras;
    private String nomeProduto;
    private int saldo;
    private double valor;

    public Produto(String codBarras, String descricao, int saldo, double valor) {
        this.codBarras = codBarras;
        this.nomeProduto = descricao;
        this.saldo = saldo;
        this.valor = valor;
    }

    public String getCodBarras() {
        return codBarras;
    }

    public String getDescricao() {
        return nomeProduto;
    }

    public double getSaldo() {
        return saldo;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "codBarras='" + codBarras + '\'' +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", saldo=" + saldo +
                ", valor=" + valor +
                '}';
    }
}