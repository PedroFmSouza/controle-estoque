package com.pedro.estoque.service;

import com.pedro.estoque.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class Estoque {
    private List<Produto> produtos;

    // Construtor
    public Estoque() {
        this.produtos = new ArrayList<>();
    }

    // Adicionar produto
    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
        System.out.println("Produto adicionado com sucesso!");
    }

    // Remover produto por ID
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

    // Buscar produto por nome
    public Produto buscarProdutoPorNome(String nome) {
        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }

    // Listar todos os produtos
    public void listarProdutos() {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto no estoque.");
        } else {
            for (Produto p : produtos) {
                System.out.println(p);
            }
        }
    }

    // Atualizar quantidade de um produto
    public void atualizarQuantidade(int id, int quantidade) {
        for (Produto p : produtos) {
            if (p.getId() == id) {
                p.setQuantidade(quantidade);
                System.out.println("Quantidade atualizada com sucesso!");
                return;
            }
        }
        System.out.println("Produto não encontrado.");
    }

    // Verificar estoque baixo (ex: < 5 unidades)
    public void alertaEstoqueBaixo() {
        boolean alerta = false;
        for (Produto p : produtos) {
            if (p.getQuantidade() < 5) {
                System.out.println("Estoque baixo: " + p);
                alerta = true;
            }
        }
        if (!alerta) {
            System.out.println("Nenhum produto com estoque baixo.");
        }
    }

    // Getter para lista de produtos
    public List<Produto> getProdutos() {
        return produtos;
    }

    private boolean existeId(int id) {
        return produtos.stream().anyMatch(p -> p.getId() == id);
    }
}
