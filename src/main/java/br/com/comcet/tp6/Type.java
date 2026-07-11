package br.com.comcet.tp6;

/*
 * Representa os tipos suportados pela linguagem MiniPascal.
 * Esses tipos são utilizados durante a verificação semântica
 * para validar atribuições, expressões e chamadas de funções.
 */

public enum Type {

    // Tipo inteiro (ex.: 10, 20, x + y)
    INTEGER,

    // Tipo lógico (true ou false)
    BOOLEAN,

    // Tipo texto (ex.: "Olá")
    STRING,

    // Utilizado quando ocorre algum erro de tipos
    // durante a análise semântica.
    ERROR
}