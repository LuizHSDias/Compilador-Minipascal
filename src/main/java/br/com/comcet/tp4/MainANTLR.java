package br.com.comcet.tp4;

import br.com.comcet.tp1.ast.AstNode;
import br.com.comcet.tp4.parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

/*
 * Classe utilizada para testar o ANTLR e a construção da AST.
 * Ela lê um código MiniPascal, gera a árvore sintática e,
 * em seguida, utiliza o MyVisitor para construir a AST.
 */

public class MainANTLR {

    public static void main(String[] args) {

        // Código MiniPascal utilizado para os testes.
        String codigo = """
                var x : integer;
                var y : boolean;
                var nome : string;

                begin
                end
                """;

        // Converte o código em um fluxo de caracteres.
        CharStream input = CharStreams.fromString(codigo);

        // O Lexer identifica os tokens da linguagem.
        MiniPascalLexer lexer = new MiniPascalLexer(input);

        // Armazena os tokens produzidos pelo Lexer.
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // O Parser verifica se o código segue a gramática.
        MiniPascalParser parser = new MiniPascalParser(tokens);

        // Gera a árvore sintática (ParseTree).
        ParseTree tree = parser.program();

        // Percorre a ParseTree e constrói a AST.
        MyVisitor visitor = new MyVisitor();
        AstNode ast = visitor.visit(tree);

        // Exibe a AST gerada.
        System.out.println(ast.printTree());
    }
}