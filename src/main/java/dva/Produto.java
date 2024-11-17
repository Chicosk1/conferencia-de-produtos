package dva;

public class Produto {

    private String codBarras, nomeProduto;
    private double valor;
    private int saldo;

    public Produto(String codBarras, String descricao, double valor, int saldo) {
        this.codBarras = codBarras;
        this.nomeProduto = descricao;
        this.valor = valor;
        this.saldo = saldo;
    }

    public String getCodBarras() {
        return codBarras;
    }

    public String getDescricao() {
        return nomeProduto;
    }

    public double getValor() {
        return valor;
    }

    public double getSaldo() {
        return saldo;
    }

}