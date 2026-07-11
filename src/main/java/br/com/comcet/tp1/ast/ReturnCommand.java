package br.com.comcet.tp1.ast;

/*
 * Representa o comando return na AST.
 * Armazena a expressão que será retornada pela função.
 */

public class ReturnCommand extends Command {

    // Expressão que será retornada.
    private final Expression expression;

    /*
    * Cria um novo comando de retorno.
    */
    public ReturnCommand(
            Expression expression
    ) {
        this.expression = expression;
    }

    // Retorna a expressão associada ao return.
    public Expression expression() {
        return expression;
    }

    /*
    * Imprime a estrutura do comando return na AST.
    */
    @Override
    protected void printTree(
            StringBuilder sb,
            int level
    ) {

        sb.append(indent(level))
          .append("ReturnCommand\n");

        // Imprime a expressão retornada.
        expression.printTree(
                sb,
                level + 1
        );
    }
}