package br.com.comcet.tp2;

import br.com.comcet.tp1.Token;
import br.com.comcet.tp1.TokenType;
import br.com.comcet.tp1.IScanner;

/*
 * Responsável pela análise léxica da linguagem.
 * Lê o código fonte caractere por caractere e
 * transforma o texto em uma sequência de tokens.
 */

public class Scanner implements IScanner {

    // Código fonte que será analisado.
    private String input;

    // Posição atual da leitura.
    private int pos = 0;

    // Linha atual do código.
    private int line = 1;

    // Coluna atual do código.
    private int column = 1;

    /*
     * Recebe o código fonte que será analisado.
     */
    public Scanner(String input) {
        this.input = input;
    }

    /*
     * Retorna o caractere atual sem avançar
     * a posição da leitura.
     */
    private char peek() {
        if (pos >= input.length())
            return '\0';
        return input.charAt(pos);
    }

    /*
     * Avança para o próximo caractere,
     * atualizando linha e coluna.
     */
    private char advance() {
        char c = peek();
        pos++;

        if (c == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }

        return c;
    }

    /*
     * Ignora espaços, tabulações e quebras
     * de linha antes da leitura do próximo token.
     */
    private void skipWhitespace() {
        while (Character.isWhitespace(peek())) {
            advance();
        }
    }

    /*
     * Reconhece identificadores e palavras-chave.
     */
    private Token identifier() {
        StringBuilder sb = new StringBuilder();

        while (Character.isLetterOrDigit(peek())) {
            sb.append(advance());
        }

        String text = sb.toString();

        // Verifica se o texto corresponde a uma palavra-chave.
        if (text.equals("var") || text.equals("integer")) {
            return new Token(TokenType.KEYWORD, text);
        }

        return new Token(TokenType.IDENTIFIER, text);
    }

    /*
     * Reconhece números inteiros da linguagem.
     */
    private Token number() {
        StringBuilder sb = new StringBuilder();

        while (Character.isDigit(peek())) {
            sb.append(advance());
        }

        return new Token(TokenType.NUMBER, sb.toString());
    }

    /*
     * Método principal do Scanner.
     * Analisa o próximo token do código fonte
     * e o retorna para o Parser.
     */
    @Override
    public Token nextToken() {

        // Ignora espaços antes de iniciar a leitura.
        skipWhitespace();

        char current = peek();

        // Verifica se chegou ao final do arquivo.
        if (current == '\0') {
            return new Token(TokenType.EOF, "");
        }

        // Reconhece identificadores e palavras-chave.
        if (Character.isLetter(current)) {
            return identifier();
        }

        // Reconhece números inteiros.
        if (Character.isDigit(current)) {
            return number();
        }

        // Reconhece operadores e delimitadores da linguagem.
        switch (current) {

            case ':':
                advance();
                if (peek() == '=') {
                    advance();
                    return new Token(TokenType.ASSIGN, ":=");
                }
                return new Token(TokenType.DELIMITER, ":");

            case ';':
                advance();
                return new Token(TokenType.DELIMITER, ";");

            case '(':
                advance();
                return new Token(TokenType.DELIMITER, "(");

            case ')':
                advance();
                return new Token(TokenType.DELIMITER, ")");

            case '+':
            case '-':
            case '*':
            case '/':
                advance();
                return new Token(TokenType.OPERATOR, String.valueOf(current));
        }

        // Caso encontre um caractere inválido,
        // gera um erro léxico.
        throw new LexicalException(
                "Caractere inválido '" + current +
                        "' na linha " + line + ", coluna " + column);
    }
}