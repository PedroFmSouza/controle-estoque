package com.pedro.estoque.util;

import com.pedro.estoque.model.Produto;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArquivoCSV {
    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private static final char SEP = ';';
    private static final char QUOTE = '"';

    private final String caminhoArquivo;

    public ArquivoCSV(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public void salvar(List<Produto> produtos) {
        try (Writer out = new OutputStreamWriter(new FileOutputStream(caminhoArquivo), StandardCharsets.UTF_8);
             PrintWriter writer = new PrintWriter(out)) {

            // Força BOM UTF-8
            writer.write('\uFEFF');

            // Cabeçalho com ; separando
            writer.println("ID;Nome;Categoria;Preco;Quantidade");

            for (Produto p : produtos) {
                writer.printf("%d;%s;%s;%.2f;%d%n",
                        p.getId(),
                        csvEscape(p.getNome()),
                        csvEscape(p.getCategoria()),
                        p.getPreco(),
                        p.getQuantidade());
            }

            System.out.println("Estoque salvo em " + caminhoArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar CSV: " + e.getMessage());
        }
    }


    // Coloca entre aspas se necessário e escapa aspas internas
    private String csvEscape(String s) {
        if (s == null) return "";
        boolean precisaQuote = s.indexOf(SEP) >= 0 || s.indexOf('\n') >= 0 || s.indexOf('\r') >= 0 || s.indexOf(QUOTE) >= 0;
        if (!precisaQuote) return s;
        return QUOTE + s.replace("\"", "\"\"") + QUOTE;
    }

    public List<Produto> carregar() {
        List<Produto> produtos = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(caminhoArquivo), StandardCharsets.UTF_8))) {

            String linha = reader.readLine(); // pula cabeçalho
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                // quebra usando ; (nosso separador)
                String[] campos = linha.split(";", -1);

                if (campos.length == 5) {
                    try {
                        int id = Integer.parseInt(campos[0].trim());
                        String nome = campos[1].replace("\"", "").trim();
                        String categoria = campos[2].replace("\"", "").trim();

                        // preço pode vir com , ou .
                        String precoStr = campos[3].trim().replace(",", ".");
                        double preco = Double.parseDouble(precoStr);

                        int quantidade = Integer.parseInt(campos[4].trim());

                        produtos.add(new Produto(id, nome, categoria, preco, quantidade));
                    } catch (NumberFormatException e) {
                        System.out.println("⚠ Linha ignorada (erro de formato): " + linha);
                    }
                } else {
                    System.out.println("⚠ Linha inválida (colunas != 5): " + linha);
                }
            }
            System.out.println("📂 Estoque carregado de " + caminhoArquivo);
        } catch (IOException e) {
            System.out.println("⚠ Nenhum estoque carregado (novo arquivo será criado).");
        }

        return produtos;
    }

}
