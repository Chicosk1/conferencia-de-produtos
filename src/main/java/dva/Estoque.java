package dva;
import java.util.ArrayList;
import java.util.List;

public class Estoque {
    static List<Produto> estoqueAtual() {
        List<Produto> produtos = new ArrayList<>();
        produtos.add(new Produto("123", "SAPATO", 2, 20.99));
        produtos.add(new Produto("234", "CHINELOS", 3, 19.99));
        produtos.add(new Produto("456", "BOTA", 4, 5.00));
        produtos.add(new Produto("567", "TENIS", 7, 7.00));
        produtos.add(new Produto("678", "MEIA", 1, 3.99));
        return produtos;
    }
}