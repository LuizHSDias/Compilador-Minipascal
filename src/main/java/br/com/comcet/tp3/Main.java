package br.com.comcet.tp3;

import br.com.comcet.tp2.Scanner;
import br.com.comcet.tp1.ast.Command;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * Classe utilizada para testar o Parser da linguagem.
 * Ela lê um arquivo de entrada, executa a análise sintática
 * e imprime a AST gerada.
 */

public class Main {
    public static void main(String[] args) {

        try {

             // Lê o código fonte que será analisado.
            String codigo = new String(
                Files.readAllBytes(Paths.get("teste.txt"))
            );

            // Executa a análise léxica.
            Scanner scanner = new Scanner(codigo);

            // Cria o parser responsável por analisar a sequência de tokens.
            Parser parser = new Parser(scanner);

            // Inicia a análise sintática e gera a AST.
            Command cmd = parser.parseCommand();

            // Exibe a árvore sintática construída.
            System.out.println(cmd.printTree());

        } catch (Exception e) {
            
            // Exibe possíveis erros encontrados durante a execução.
            e.printStackTrace();
        }
    }
}