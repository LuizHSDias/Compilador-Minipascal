package br.com.comcet.tp1.ast;

/*
 * Representa uma expressão binária na AST.
 * Possui uma expressão à esquerda, uma à direita
 * e o operador que será aplicado entre elas.
 */

public class BinaryExpression extends Expression {

    // Operando da esquerda.
    private final Expression left;

    // Operando da direita.
    private final Expression right;

    // Operador da expressão (+, -, *, /, >, <, =).
    private final String operator;

    /*
    * Cria uma nova expressão binária.
    */
    public BinaryExpression(Expression left, Expression right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    // Retorna a expressão da esquerda.
    public Expression left() {
        return left;
    }

    // Retorna a expressão da direita. 
    public Expression right() {
        return right;
    }

    // Retorna o operador da expressão.
    public String operator() {
        return operator;
    }

    /*
    * Imprime a estrutura da expressão binária na AST.
    */
    @Override
    protected void printTree(StringBuilder sb, int level) {
        sb.append(indent(level))
          .append("BinaryExpression ")
          .append(operator)
          .append("\n");

        // Imprime o operando esquerdo.
        left.printTree(sb, level + 1);

        // Imprime o operando direito. 
        right.printTree(sb, level + 1);
    }
}