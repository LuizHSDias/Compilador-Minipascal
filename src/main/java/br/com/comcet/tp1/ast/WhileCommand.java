package br.com.comcet.tp1.ast;

/*
 * Representa o comando while da linguagem.
 * Armazena a condição de repetição e o bloco
 * de comandos que será executado.
 */

public class WhileCommand extends Command {

    // Expressão utilizada como condição do laço.
    private final Expression condition;

    // Bloco executado enquanto a condição for verdadeira.
    private final Program block;

      /*
     * Cria um novo comando while.
     */
    public WhileCommand(
            Expression condition,
            Program block
    ) {
        this.condition = condition;
        this.block = block;
    }

    // Retorna a condição do while.
    public Expression condition() {
        return condition;
    }

    // Retorna o bloco de comandos do while.
    public Program block() {
        return block;
    }

    /*
     * Imprime a estrutura do comando while na AST.
     */
    @Override
    protected void printTree(
            StringBuilder sb,
            int level
    ) {

        sb.append(indent(level))
          .append("WhileCommand\n");

        // Imprime a condição do laço.
        condition.printTree(
                sb,
                level + 1
        );

        // Imprime o bloco executado pelo while.
        block.printTree(
                sb,
                level + 1
        );
    }
}