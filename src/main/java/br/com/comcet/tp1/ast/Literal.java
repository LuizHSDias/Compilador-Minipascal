package br.com.comcet.tp1.ast;

/*
 * Representa um valor literal na AST.
 * Pode armazenar números, textos ou valores booleanos.
 */

public class Literal extends Expression {

    // Valor armazenado pelo literal.
    private final Object value;

    /*
    * Cria um novo literal.
    */
    public Literal(Object value) {
        this.value = value;
    }

    // Retorna o valor do literal.
    public Object value() {
        return value;
    }

    /*
    * Imprime o literal na AST.
    */
    @Override
    protected void printTree(StringBuilder sb, int level) {

        sb.append(indent(level))
          .append("Literal(")
          .append(value)
          .append(")\n");
    }
}