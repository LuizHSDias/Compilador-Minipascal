package br.com.comcet.tp1.ast;

/*
 * Representa um identificador na AST.
 * Um identificador corresponde ao nome de uma variável
 * ou função utilizada no programa.
 */

public class Identifier extends Expression {

    // Nome do identificador.
    private final String name;

    /*
    * Cria um novo identificador.
    */
    public Identifier(String name) {
        this.name = name;
    }

    // Retorna o nome do identificador.
    public String name() {
        return name;
    }


    /*
    * Imprime o identificador na AST.
    */
    @Override
    protected void printTree(StringBuilder sb, int level) {
        sb.append(indent(level))
          .append("Identifier(\"")
          .append(name)
          .append("\")\n");
    }
}