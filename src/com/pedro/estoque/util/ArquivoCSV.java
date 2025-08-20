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

    // ---------- SALVAR ----------
    public void salvar(List<Produto> produtos) {
        try (Writer out = new OutputStreamWriter(new FileOutputStream(caminhoArquivo), UTF8);
             PrintWriter writer = new PrintWriter(out)) {

            // Escreve BOM para o Excel reconhecer UTF-8
            writer.write('\uFEFF');

            writer.println("ID;Nome;Categoria;Preco;Quantidade");
            NumberFormat nf = NumberFormat.getNumberInstance(LOCALE_BR);
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);

            for (Produto p : produtos) {
                String id = String.valueOf(p.getId());
                String nome = csvEscape(p.getNome());
                String cat = csvEscape(p.getCategoria());
                String preco = nf.format(p.getPreco());     // ex: 1.234,56
                String qtd = String.valueOf(p.getQuantidade());

                writer.print(id); writer.print(SEP);
                writer.print(nome); writer.print(SEP);
                writer.print(cat); writer.print(SEP);
                writer.print(preco); writer.print(SEP);
                writer.print(qtd); writer.print("\r\n");     // CRLF (Windows-friendly)
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

    // ---------- CARREGAR ----------
    public List<Produto> carregar() {
        List<Produto> produtos = new ArrayList<>();
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) return produtos;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), UTF8))) {
            String linha = reader.readLine();
            if (linha != null && linha.startsWith("\uFEFF")) {
                // remove BOM do cabeçalho, se presente
                linha = linha.substring(1);
            }
            // pula cabeçalho
            if (linha == null || isHeader(linha)) {
                // ok, segue
            }

            while ((linha = reader.readLine()) != null) {
                List<String> cols = splitCsvSemicolonAwareQuotes(linha);
                if (cols.size() != 5) {
                    System.out.println("Linha ignorada (colunas != 5): " + linha);
                    continue;
                }
                try {
                    int id = parseIntSafe(cols.get(0));
                    String nome = unquote(cols.get(1));
                    String categoria = unquote(cols.get(2));
                    double preco = parsePrecoBRCompat(cols.get(3)); // aceita 1.234,56 e 1234.56
                    int quantidade = parseIntSafe(cols.get(4));

                    produtos.add(new Produto(id, nome, categoria, preco, quantidade));
                } catch (IllegalArgumentException | ParseException ex) {
                    System.out.println("Linha inválida ignorada: " + linha);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar CSV: " + e.getMessage());
        }
        return produtos;
    }

    private boolean isHeader(String linha) {
        String lower = linha.toLowerCase();
        return lower.contains("id") && lower.contains("nome") && lower.contains("preco");
    }

    private int parseIntSafe(String s) {
        return Integer.parseInt(unquote(s).trim());
    }

    private String unquote(String s) {
        String t = s.trim();
        if (t.length() >= 2 && t.charAt(0) == QUOTE && t.charAt(t.length() - 1) == QUOTE) {
            t = t.substring(1, t.length() - 1).replace("\"\"", "\"");
        }
        return t;
    }

    // Aceita preço tanto em pt-BR (1.234,56) quanto US (1234.56)
    private double parsePrecoBRCompat(String s) throws ParseException {
        String t = unquote(s).trim();
        // Tenta pt-BR
        try {
            NumberFormat nfBR = NumberFormat.getNumberInstance(LOCALE_BR);
            return nfBR.parse(t).doubleValue();
        } catch (ParseException e) {
            // fallback: formato US
            t = t.replace(",", "."); // caso venha "1234,56"
            return Double.parseDouble(t);
        }
    }

    // Split de linha CSV com ; respeitando aspas (;) dentro de "..."
    private List<String> splitCsvSemicolonAwareQuotes(String line) {
        List<String> cols = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == QUOTE) {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == QUOTE) {
                    // aspas escapadas ("")
                    cur.append(QUOTE);
                    i++; // consume a próxima aspas
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == SEP && !inQuotes) {
                cols.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        cols.add(cur.toString()); // último campo
        return cols;
    }
}
