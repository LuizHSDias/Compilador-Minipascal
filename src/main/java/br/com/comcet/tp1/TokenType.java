package br.com.comcet.tp1;

/*
 * Enum que representa todos os tipos de tokens
 * reconhecidos pelo Scanner da linguagem.
 */

public enum TokenType {

    // Palavra reservada da linguagem (ex.: var, integer).
    KEYWORD,

    // Nome de variável ou função.
    IDENTIFIER,

    // Valor numérico inteiro.
    NUMBER,

    // Operadores aritméticos (+, -, *, /).
    OPERATOR,

    // Delimitadores como ; : ( ).
    DELIMITER,

    // Operador de atribuição (:=).
    ASSIGN,

    // Indica o fim do código fonte.
    EOF
}