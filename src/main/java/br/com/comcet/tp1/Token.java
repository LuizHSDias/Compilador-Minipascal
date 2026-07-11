package br.com.comcet.tp1;

/*
 * Representa um token identificado durante a análise léxica.
 * Cada token possui um tipo e o texto correspondente
 * encontrado no código fonte.
 */

public class Token {

    // Categoria do token (IDENTIFIER, NUMBER, KEYWORD, etc.).
    private final TokenType type;

    // Texto original do token.
    private final String text;

    /*
     * Cria um novo token com seu tipo e conteúdo.
     */
    public Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    // Retorna o tipo do token.
    public TokenType type() {
        return type;
    }

    // Retorna o texto do token.
    public String text() {
        return text;
    }

    /*
     * Retorna uma representação em texto do token,
     * utilizada principalmente para testes e depuração.
     */
    @Override
    public String toString() {
        return "[" + type + ", \"" + text + "\"]";
    }
}