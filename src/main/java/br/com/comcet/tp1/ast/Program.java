package br.com.comcet.tp1.ast;

import java.util.List;

/*
 * Representa o nó raiz da AST.
 * Armazena todos os elementos que compõem o programa,
 * como declarações, funções e blocos.
 */

public class Program extends AstNode {

    // Lista contendo os nós filhos do programa.
    private final List<AstNode> children;

     /*
     * Cria um novo programa contendo seus nós filhos.
     */
    public Program(List<AstNode> children) {
        this.children = children;
    }

    // Retorna todos os elementos do programa.
    public List<AstNode> children() {
        return children;
    }

    /*
    * Imprime a estrutura da AST do programa.
    */
    @Override
    protected void printTree(StringBuilder sb, int level) {

        sb.append(indent(level))
          .append("Program\n");

        // Imprime todos os nós filhos do programa.
        for (AstNode child : children) {
            child.printTree(sb, level + 1);
        }
    }
}