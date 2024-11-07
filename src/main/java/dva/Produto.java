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

    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    public String getDescricao() {
        return nomeProduto;
    }

    public void setDescricao(String descricao) {
        this.nomeProduto = descricao;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
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