package com.pedro.estoque.util;

import com.pedro.estoque.model.Produto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArquivoHelper {

    private static final String ARQUIVO = "estoque.txt";

    // Salvar lista de produtos no arquivo
    public static void salvarEstoque(List<Produto> produtos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Produto p : produtos) {
                bw.write(p.getId() + ";" + p.getNome() + ";" + p.getCategoria() + ";" + p.getPreco() + ";" + p.getQuantidade());
                bw.newLine();
            }
            System.out.println("💾 Estoque salvo com sucesso!");
        } catch (IOException e) {
            System.out.println("❌ Erro ao salvar o estoque: " + e.getMessage());
        }
    }

    // Carregar lista de produtos do arquivo
    public static List<Produto> carregarEstoque() {
        List<Produto> produtos = new ArrayList<>();
        File arquivo = new File(ARQUIVO);

        if (!arquivo.exists()) {
            return produtos; // retorna lista vazia
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                int id = Integer.parseInt(dados[0]);
                String nome = dados[1];
                String categoria = dados[2];
                double preco = Double.parseDouble(dados[3]);
                int quantidade = Integer.parseInt(dados[4]);

                produtos.add(new Produto(id, nome, categoria, preco, quantidade));
            }
        } catch (IOException e) {
            System.out.println("❌ Erro ao carregar o estoque: " + e.getMessage());
        }

        return produtos;
    }
}
