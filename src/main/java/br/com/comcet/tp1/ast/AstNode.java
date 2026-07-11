package br.com.comcet.tp1.ast;

/*
 * Classe base de todos os nós da AST.
 * Define os métodos utilizados para imprimir
 * a estrutura da árvore de sintaxe.
 */
public abstract  class AstNode {

    /*
    * Inicia a impressão da árvore a partir deste nó.
    */
     public final String printTree() {
        StringBuilder sb = new StringBuilder();
        printTree(sb, 0);
        return sb.toString();
    }

    /*
    * Método que deve ser implementado pelas subclasses
    * para imprimir sua própria estrutura.
    */
    protected abstract void printTree(StringBuilder sb, int level);

    /*
    * Gera a indentação de acordo com o nível do nó
    * na árvore, deixando a impressão organizada.
    */
    protected String indent(int level) {
        return "  ".repeat(level);
    }
}