package br.com.comcet.tp1.ast;

import br.com.comcet.tp6.Type;

/*
 * Representa a declaração de uma variável na AST.
 * Armazena o nome da variável e o seu tipo.
 */

public class VarDeclaration extends AstNode {

    // Nome da variável declarada.
    private final String name;

    // Tipo da variável (INTEGER, BOOLEAN ou STRING).
    private final Type type;

    /*
     * Cria uma nova declaração de variável.
     */
    public VarDeclaration(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    // Retorna o nome da variável.
    public String name() {
        return name;
    }

    // Retorna o tipo da variável.
    public Type type() {
        return type;
    }

    /*
    * Imprime a declaração da variável na AST.
    */
    @Override
    protected void printTree(StringBuilder sb, int level) {

        sb.append(indent(level))
                .append("VarDeclaration(")
                .append(name)
                .append(", ")
                .append(type)
                .append(")\n");
    }
}