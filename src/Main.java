import com.pedro.estoque.model.Produto;
import com.pedro.estoque.service.Estoque;
import com.pedro.estoque.util.ArquivoHelper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Estoque estoque = new Estoque();
        Scanner sc = new Scanner(System.in);

        // carregar produtos do arquivo
        estoque.getProdutos().addAll(ArquivoHelper.carregarEstoque());

        boolean executando = true;
        while (executando) {
            System.out.println("\n=== CONTROLE DE ESTOQUE ===");
            System.out.println("1. Adicionar produto");
            System.out.println("2. Remover produto");
            System.out.println("3. Buscar produto por nome");
            System.out.println("4. Listar produtos");
            System.out.println("5. Atualizar quantidade");
            System.out.println("6. Alertar estoque baixo");
            System.out.println("7. Salvar estoque");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            int opcao = sc.nextInt();
            sc.nextLine(); // consumir quebra de linha

            switch (opcao) {
                case 1 -> {
                    System.out.print("ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Nome: ");
                    String nome = sc.nextLine();

                    System.out.print("Categoria: ");
                    String categoria = sc.nextLine();

                    System.out.print("Preço: ");
                    double preco = sc.nextDouble();

                    System.out.print("Quantidade: ");
                    int quantidade = sc.nextInt();

                    Produto produto = new Produto(id, nome, categoria, preco, quantidade);
                    estoque.adicionarProduto(produto);
                }
                case 2 -> {
                    System.out.print("ID do produto a remover: ");
                    int id = sc.nextInt();
                    estoque.removerProduto(id);
                }
                case 3 -> {
                    System.out.print("Nome do produto: ");
                    String nomeBusca = sc.nextLine();
                    Produto encontrado = estoque.buscarProdutoPorNome(nomeBusca);
                    System.out.println(encontrado != null ? encontrado : "⚠ Produto não encontrado.");
                }
                case 4 -> estoque.listarProdutos();
                case 5 -> {
                    System.out.print("ID do produto: ");
                    int id = sc.nextInt();
                    System.out.print("Nova quantidade: ");
                    int qtd = sc.nextInt();
                    estoque.atualizarQuantidade(id, qtd);
                }
                case 6 -> estoque.alertaEstoqueBaixo();
                case 7 -> ArquivoHelper.salvarEstoque(estoque.getProdutos());
                case 0 -> {
                    ArquivoHelper.salvarEstoque(estoque.getProdutos()); // salvar antes de sair
                    executando = false;
                    System.out.println("👋 Saindo do sistema...");
                }
                default -> System.out.println("❌ Opção inválida.");
            }
        }

        sc.close();
    }
}
