package br.com.comcet.tp2;

import br.com.comcet.tp1.Token;
import br.com.comcet.tp1.TokenType;

/*
 * Classe utilizada para testar o Scanner.
 * Ela executa a análise léxica e imprime todos os
 * tokens encontrados no código fonte.
 */

public class MainScanner {

    public static void main(String[] args) {

       // Código MiniPascal utilizado para os testes.
       String codigo = "var x : integer;\n" +
                "x := 10;";

        // Cria o Scanner responsável pela análise léxica.
        Scanner scanner = new Scanner(codigo);

        Token token;

        // Lê e imprime todos os tokens até encontrar o EOF.
        do {
            token = scanner.nextToken();
            System.out.println(token);
        } while (token.type() != TokenType.EOF);
    }
}