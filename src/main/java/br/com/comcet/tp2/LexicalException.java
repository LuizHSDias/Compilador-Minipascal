package br.com.comcet.tp2;

/*
 * Exceção utilizada para indicar erros encontrados
 * durante a análise léxica do código fonte.
 */

public class LexicalException extends RuntimeException {

    // Recebe a mensagem de erro e a repassa para a classe RuntimeException.
    public LexicalException(String message) {
        super(message);
    }
}