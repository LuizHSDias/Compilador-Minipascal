package br.com.comcet.tp2;

import br.com.comcet.tp1.Token;
import br.com.comcet.tp1.TokenType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ScannerTest {

    /*
    * Testa se o Scanner reconhece corretamente
    * uma declaração simples de variável.
    */
    @Test
    void reconheceDeclaracaoSimples() {

        String codigo = "var x : integer;";

        // Cria o analisador léxico.
        Scanner scanner = new Scanner(codigo);

        // Verifica o reconhecimento da palavra reservada "var".
        Token t1 = scanner.nextToken();
        assertEquals(TokenType.KEYWORD, t1.type());
        assertEquals("var", t1.text());

        // Verifica o identificador da variável.
        Token t2 = scanner.nextToken();
        assertEquals(TokenType.IDENTIFIER, t2.type());
        assertEquals("x", t2.text());

        // Verifica o delimitador ':'.
        Token t3 = scanner.nextToken();
        assertEquals(TokenType.DELIMITER, t3.type());
        assertEquals(":", t3.text());

        // Verifica a palavra reservada "integer".
        Token t4 = scanner.nextToken();
        assertEquals(TokenType.KEYWORD, t4.type());
        assertEquals("integer", t4.text());

        // Verifica o delimitador ';'.
        Token t5 = scanner.nextToken();
        assertEquals(TokenType.DELIMITER, t5.type());
        assertEquals(";", t5.text());

        // Ao final da leitura deve ser retornado o token EOF.
        Token eof = scanner.nextToken();
        assertEquals(TokenType.EOF, eof.type());
    }
}