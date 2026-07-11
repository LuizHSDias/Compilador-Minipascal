package br.com.comcet.tp0;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/*
 * Programa utilizado para realizar estatísticas sobre um texto.
 * O texto pode ser informado diretamente pelo usuário ou lido
 * a partir de um arquivo .txt.
 */
public class App {

    public static void main(String[] args) {

        String input = "";
        boolean isFilePath = false;

        // Leitura da entrada

        // Primeiro verifica se a entrada foi passada pela linha de comando.
        if (args.length > 0) {

            if (args[0].equals("-f") && args.length > 1) {

                input = args[1];
                isFilePath = true;

            } else {

                input = args[0];

                // Detecta automaticamente se foi informado um arquivo.
                if (input.toLowerCase().endsWith(".txt")
                        && new File(input).exists()) {

                    isFilePath = true;
                }
            }

        } else {

            // Caso não exista argumento, solicita a entrada pelo teclado.
            Scanner keyboard = new Scanner(System.in);

            System.out.print(
                    "Digite o texto ou o caminho de um arquivo .txt: ");

            input = keyboard.nextLine();

            if (input.toLowerCase().endsWith(".txt")
                    && new File(input).exists()) {

                isFilePath = true;
            }

            keyboard.close();
        }

        // Leitura do conteúdo
        String content = "";

        if (isFilePath) {

            try (Scanner scanner = new Scanner(new File(input))) {

                // Lê todo o conteúdo do arquivo.
                content = scanner
                        .useDelimiter("\\Z")
                        .next();

            } catch (FileNotFoundException e) {

                System.err.println(
                        "Erro: Arquivo não encontrado: " + input);

                return;
            }

        } else {

            // Caso seja apenas um texto, utiliza diretamente.
            content = input;
        }

        // Exibe as estatísticas do texto.
        exibirEstatisticas(content);
    }

    /*
     * Calcula diversas estatísticas do texto informado,
     * como quantidade de caracteres, palavras,
     * frequência e maior palavra.
     */
    
    public static void exibirEstatisticas(String text) {

        // Verifica se o texto está vazio.
        if (text == null || text.trim().isEmpty()) {

            System.out.println("Texto vazio.");

            return;
        }

        // Remove pontuação e converte tudo para minúsculas.
        String textoLimpo = text.toLowerCase()
                .replaceAll(
                        "[^a-zA-Záàâãéèêíïóôõöúçñ ]",
                        "");

        if (textoLimpo.trim().isEmpty()) {

            System.out.println("Texto vazio.");

            return;
        }

        // Divide o texto em palavras.
        String[] palavras = textoLimpo.trim().split("\\s+");

        // Conta apenas os caracteres alfabéticos.
        int caracteres = text.replaceAll(
                "[^a-zA-Záàâãéèêíïóôõöúçñ]",
                "")
                .length();

        // Frequência das palavras

        Map<String, Integer> freqPalavras = new TreeMap<>();

        for (String palavra : palavras) {

            freqPalavras.put(
                    palavra,
                    freqPalavras.getOrDefault(
                            palavra,
                            0)
                            + 1);
        }

        String palavraMaisFrequente = "";
        int maxPalavra = 0;

        // Procura a palavra mais frequente.
        for (Map.Entry<String, Integer> entry : freqPalavras.entrySet()) {

            if (entry.getValue() > maxPalavra) {

                maxPalavra = entry.getValue();

                palavraMaisFrequente = entry.getKey();
            }
        }

        // Frequência das letras

        Map<Character, Integer> freqLetras = new TreeMap<>();

        for (String palavra : palavras) {

            // Evita contar a mesma letra duas vezes
            // dentro da mesma palavra.
            Set<Character> letrasUnicas = new HashSet<>();

            for (char c : palavra.toCharArray()) {

                letrasUnicas.add(c);
            }

            for (char c : letrasUnicas) {

                freqLetras.put(
                        c,
                        freqLetras.getOrDefault(c, 0)
                                + 1);
            }
        }

        char letraMaisFrequente = ' ';
        int maxLetra = 0;

        // Procura a letra presente no maior número de palavras.
        for (Map.Entry<Character, Integer> entry : freqLetras.entrySet()) {

            if (entry.getValue() > maxLetra) {

                maxLetra = entry.getValue();

                letraMaisFrequente = entry.getKey();
            }
        }

        // Maior palavra

        String maiorPalavra = "";

        for (String palavra : palavras) {

            if (palavra.length() > maiorPalavra.length()) {

                maiorPalavra = palavra;
            }
        }

        // Exibição dos resultados

        System.out.println(
                "Caracteres: " + caracteres);

        System.out.println(
                "Frequente: " + letraMaisFrequente);

        System.out.println(
                "Palavras: " + palavras.length);

        System.out.println(
                "Frequente: " + palavraMaisFrequente);

        System.out.println(
                "Maior palavra: " + maiorPalavra);

        System.out.println("Top 5 palavras:");

        freqPalavras.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue()
                        .compareTo(a.getValue()))
                .limit(5)
                .forEach(e -> System.out.println(
                        e.getKey() + ": " + e.getValue()));

        System.out.println(
                "Letra em contexto: "
                        + letraMaisFrequente
                        + " aparece em "
                        + maxLetra
                        + " palavras diferentes.");
    }
}