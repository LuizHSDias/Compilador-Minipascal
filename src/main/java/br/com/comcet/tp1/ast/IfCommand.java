package br.com.comcet.tp1.ast;

/*
 * Representa o comando if da linguagem.
 * Armazena a condição e o bloco de comandos
 * que será executado caso ela seja verdadeira.
 */

public class IfCommand extends Command {

    // Expressão utilizada como condição do if.
    private final Expression condition;

    // Bloco executado quando a condição for verdadeira.
    private final Program block;

    /*
    * Cria um novo comando if.
    */
    public IfCommand(
            Expression condition,
            Program block
    ) {
        this.condition = condition;
        this.block = block;
    }

    // Retorna a condição do if.
    public Expression condition() {
        return condition;
    }

    // Retorna o bloco de comandos do if.
    public Program block() {
        return block;
    }

    /*
    * Imprime a estrutura do comando if na AST.
    */
    @Override
    protected void printTree(
            StringBuilder sb,
            int level
    ) {

        sb.append(indent(level))
          .append("IfCommand\n");

        // Imprime a condição do if.
        condition.printTree(
                sb,
                level + 1
        );

        // Imprime o bloco executado pelo if.
        block.printTree(
                sb,
                level + 1
        );
    }
}
