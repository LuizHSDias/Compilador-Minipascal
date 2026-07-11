package br.com.comcet.tp1.ast;

import java.util.List;

/*
* Representa uma chamada de função na AST.
* Armazena o nome da função e a lista de
* argumentos passados na chamada.
*
*/

public class FunctionCall extends Command {

    // Nome da função chamada.
    private final String name;

    // Lista de argumentos passados para a função.
    private final List<Expression> arguments;

    /*
    * Cria uma nova chamada de função.
    */
    public FunctionCall(
            String name,
            List<Expression> arguments
    ) {
        this.name = name;
        this.arguments = arguments;
    }

    // Retorna o nome da função.
    public String name() {
        return name;
    }

    // Retorna os argumentos da chamada.
    public List<Expression> arguments() {
        return arguments;
    }

    /*
    * Imprime a estrutura da chamada de função na AST.
    */
    @Override
    protected void printTree(
            StringBuilder sb,
            int level
    ) {

        sb.append(indent(level))
          .append("FunctionCall(")
          .append(name)
          .append(")\n");

        // Imprime cada argumento da função.
        for (Expression arg : arguments) {

            arg.printTree(
                    sb,
                    level + 1
            );
        }
    }
}