package br.com.comcet.tp1.ast;

import br.com.comcet.tp6.Type;

/*
 * Representa um parâmetro de uma função na AST.
 * Armazena o nome e o tipo do parâmetro.
 */

public class Parameter extends AstNode {

    // Nome do parâmetro.
    private final String name;

    // Tipo do parâmetro.
    private final Type type;

    /*
    * Cria um novo parâmetro.
    */
    public Parameter(
            String name,
            Type type
    ) {
        this.name = name;
        this.type = type;
    }

    // Retorna o nome do parâmetro.
    public String name() {
        return name;
    }

    // Retorna o tipo do parâmetro.
    public Type type() {
        return type;
    }

    /*
    * Imprime o parâmetro na AST.
    */
    @Override
    protected void printTree(
            StringBuilder sb,
            int level
    ) {

        sb.append(indent(level))
          .append("Parameter(")
          .append(name)
          .append(", ")
          .append(type)
          .append(")\n");
    }
}