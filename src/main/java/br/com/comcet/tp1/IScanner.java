package br.com.comcet.tp1;

/*
 * Interface que define o comportamento de um Scanner.
 * Toda classe que implementar essa interface deve
 * fornecer o método nextToken().
 */

public interface IScanner {

    // Retorna o próximo token encontrado no código fonte.
    Token nextToken();
}