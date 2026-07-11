package br.com.comcet.tp1.ast;

import br.com.comcet.tp6.Type;

/*
 * Classe base para todas as expressões da linguagem.
 * Além de representar uma expressão da AST, também
 * armazena o tipo inferido durante a análise semântica.
 */

public abstract class Expression extends AstNode {

    // Tipo da expressão (INTEGER, BOOLEAN, STRING, etc.).
    // Esse campo é preenchido pelo TypeChecker no TP6.
    private Type type;

    // Retorna o tipo da expressão.
    public Type getType() {
        return type;
    }

    // Define o tipo da expressão.
    public void setType(Type type) {
        this.type = type;
    }
}