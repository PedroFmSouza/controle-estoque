package com.pedro.estoque.model;

public class Produto {
    private int id;
    private String nome;
    private String categoria;
    private double preco;
    private int quantidade;

    // Construtor
    public Produto(int id, String nome, String categoria, double preco, int quantidade) {
        validarId(id);
        this.id = id;

        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    // Getters e Setters
    public int getId() {return id;}

    public void setId(int id) {
        validarId(id);
        this.id = id;
    }

    public String getNome() {return nome;}

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty())
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        this.nome = nome.trim();
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty())
            throw new IllegalArgumentException("Categoria não pode ser vazia.");
        this.categoria = categoria.trim();
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco < 0) throw new IllegalArgumentException("O preço não pode ser negativo");
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade<0) throw new IllegalArgumentException("Quantidade não pode ser negativa");
    }

    private void validarId(int id){
        if (id < 0) throw new IllegalArgumentException("ID não pode ser negativo");
    }

    // Representação em texto
    @Override
    public String toString() {
        return "Produto { " +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoria='" + categoria + '\'' +
                ", preco=" + preco +
                ", quantidade=" + quantidade +
                " }";
    }
}
