package br.com.comcet.tp1;

import br.com.comcet.tp1.ast.*;

/*
 * Classe utilizada para testar a construção manual da AST.
 * Cria alguns nós da árvore e imprime sua estrutura.
 */

public class MainAST {

    public static void main(String[] args) {

        // Cria os literais utilizados na expressão.
        Expression dez = new Literal(10);
        Expression cinco = new Literal(5);

        // Cria a expressão 10 + 5.
        Expression soma = new BinaryExpression(dez, cinco, "+");

        // Cria o comando de atribuição: x := 10 + 5.
        Command atrib = new AssignmentCommand(
                new Identifier("x"),
                soma);

        // Exibe a árvore construída.
        System.out.println(atrib.printTree());
    }
}