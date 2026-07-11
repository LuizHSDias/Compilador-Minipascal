package br.com.comcet.tp1.ast;

import br.com.comcet.tp6.Type;
import java.util.List;

/*
 * Representa a declaração de uma função na AST.
 * Armazena o nome da função, seus parâmetros,
 * o tipo de retorno e o bloco de comandos.
 */

public class FunctionDeclaration extends AstNode {

    // Nome da função.
    private final String name;

    // Lista de parâmetros da função.
    private final List<Parameter> parameters;

    // Tipo de retorno da função.
    private final Type returnType;

    // Bloco que representa o corpo da função.
    private final Program block;

    /*
    * Cria uma nova declaração de função.
    */
    public FunctionDeclaration(
            String name,
            List<Parameter> parameters,
            Type returnType,
            Program block
    ) {
        this.name = name;
        this.parameters = parameters;
        this.returnType = returnType;
        this.block = block;
    }

    // Retorna o nome da função.
    public String name() {
        return name;
    }

    // Retorna os parâmetros da função.
    public List<Parameter> parameters() {
        return parameters;
    }

    // Retorna o tipo de retorno da função.
    public Type returnType() {
        return returnType;
    }

    // Retorna o corpo da função.
    public Program block() {
        return block;
    }

    /*
    * Imprime a estrutura da função na AST.
    */
    @Override
    protected void printTree(
            StringBuilder sb,
            int level
    ) {

        sb.append(indent(level))
          .append("FunctionDeclaration(")
          .append(name)
          .append(", ")
          .append(returnType)
          .append(")\n");

        // Imprime todos os parâmetros da função.
        for (Parameter p : parameters) {

            p.printTree(
                    sb,
                    level + 1
            );
        }

        // Imprime o corpo da função.
        block.printTree(
                sb,
                level + 1
        );
    }
}