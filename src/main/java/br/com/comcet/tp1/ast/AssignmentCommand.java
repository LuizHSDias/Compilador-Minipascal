package br.com.comcet.tp1.ast;

/*
 * Representa um comando de atribuição na AST.
 * Armazena a variável que receberá o valor e
 * a expressão que será atribuída.
 */

public class AssignmentCommand extends Command {

    // Variável que receberá o resultado da expressão.
    private final Identifier id;

    // Expressão que será atribuída à variável.
    private final Expression expr;

    /*
    * Cria um novo comando de atribuição.
    */
    public AssignmentCommand(Identifier id, Expression expr) {
        this.id = id;
        this.expr = expr;
    }

    // Retorna o identificador da variável.
    public Identifier id() {
        return id;
    }

    // Retorna a expressão da atribuição.
    public Expression expr() {
        return expr;
    }

    /*
    * Imprime a estrutura do comando de atribuição na AST.
    */
    @Override
    protected void printTree(StringBuilder sb, int level) {
        sb.append(indent(level))
          .append("AssignmentCommand\n");

        // Imprime a variável que receberá o valor.
        id.printTree(sb, level + 1);

        // Imprime a expressão atribuída.
        expr.printTree(sb, level + 1);
    }
}