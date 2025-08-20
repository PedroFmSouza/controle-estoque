package com.pedro.estoque.service;

import com.pedro.estoque.model.Produto;
import com.pedro.estoque.util.ArquivoCSV;

import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class Estoque {
    private List<Produto> produtos;
    private ArquivoCSV arquivo;

    public Estoque() {
        this.produtos = new ArrayList<>();
    }

    public Estoque(String caminhoArquivo) {
        this.arquivo = new ArquivoCSV(caminhoArquivo);
        this.produtos = arquivo.carregar(); // carrega ao iniciar
    }


    // Impede ID duplicado
    public boolean adicionarProduto(Produto produto) {
        if (produto == null) {
            System.out.println("Produto nulo não pode ser adicionado.");
            return false;
        }
        if (existeId(produto.getId())) {
            System.out.println("Já existe produto com ID " + produto.getId() + ".");
            return false;
        }
        produtos.add(produto);
        System.out.println("Produto adicionado com sucesso!");
        return true;
    }

    public boolean removerProduto(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) {
                produtos.remove(p);
                System.out.println("Produto removido com sucesso!");
                return true;
            }
        }
        System.out.println("Produto não encontrado.");
        return false;
    }

    public Produto buscarProdutoPorNome(String nome) {
        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }

    public Produto buscarProdutoPorId(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public void listarProdutos() {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto no estoque.");
        } else {
            for (Produto p : produtos) System.out.println(p);
        }
    }

    // Atualiza quantidade absoluta (define novo valor)
    public void atualizarQuantidade(int id, int novaQuantidade) {
        if (novaQuantidade < 0) {
            System.out.println("Quantidade não pode ser negativa.");
            return;
        }
        Produto p = buscarProdutoPorId(id);
        if (p == null) {
            System.out.println("Produto não encontrado.");
            return;
        }
        p.setQuantidade(novaQuantidade);
        System.out.println("Quantidade atualizada com sucesso!");
    }

    public void alertaEstoqueBaixo() {
        int limite = 5;
        boolean encontrou = false;
        for (Produto p : produtos) {
            if (p.getQuantidade() <= limite) {
                System.out.println("Estoque baixo (" + p.getQuantidade()+" unid):");
                encontrou = true;
            }
        }
        if (!encontrou) System.out.println("Nenhum produto com estoque baixo.");
    }

    public void valorTotalEstoque() {
        double total = 0;
        for (Produto p : produtos) {
            total += p.getPreco() * p.getQuantidade();
        }
        System.out.printf("Valor total do estoque: R$ %.2f%n", total);
    }

    public void exportarCSV(String caminhoArquivo) {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            writer.write("ID,Nome,Categoria,Preço,Quantidade\n");
            for (Produto p : produtos) {
                writer.write(p.getId() + "," + p.getNome() + "," +
                        p.getCategoria() + "," + p.getPreco() + "," +
                        p.getQuantidade() + "\n");
            }
            System.out.println("Relatório exportado para " + caminhoArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao exportar CSV: " + e.getMessage());
        }
    }

    public void salvar() {
        arquivo.salvar(produtos);
    }



    public List<Produto> getProdutos() { return produtos; }

    private boolean existeId(int id) {
        return produtos.stream().anyMatch(p -> p.getId() == id);
    }
}
