package br.com.comcet.tp1.ast;

/*
 * Representa o comando de impressão da linguagem.
 * Armazena a expressão que será exibida na saída.
 */

public class PrintCommand extends Command {

    // Expressão que será impressa.
    private final Expression expression;

    /*
    * Cria um novo comando de impressão.
    */
    public PrintCommand(Expression expression) {
        this.expression = expression;
    }

    // Retorna a expressão que será impressa.
    public Expression expression() {
        return expression;
    }

    /*
    * Imprime a estrutura do comando na AST.
    */
    @Override
    protected void printTree(
            StringBuilder sb,
            int level) {

        sb.append(indent(level))
          .append("PrintCommand\n");

        // Imprime a expressão associada ao comando.
        expression.printTree(
                sb,
                level + 1);
    }
}