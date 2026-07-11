package br.com.comcet.tp5;

import br.com.comcet.tp1.ast.AstNode;
import br.com.comcet.tp4.MyVisitor;
import br.com.comcet.tp4.parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

/*
 * Classe utilizada para testar a análise semântica.
 * Ela gera a AST do programa e executa o SemanticAnalyzer,
 * exibindo os erros encontrados, caso existam.
 */

public class MainSemantico {

        public static void main(String[] args) {

                // Código MiniPascal utilizado para os testes.
                String codigo = """
                                function soma()
                                begin
                                end

                                function soma()
                                begin
                                end

                                begin
                                end
                                """;

                // Converte o código em um fluxo de caracteres.
                CharStream input = CharStreams.fromString(codigo);

                // O Lexer identifica os tokens da linguagem.
                MiniPascalLexer lexer = new MiniPascalLexer(input);

                // Armazena os tokens gerados pelo Lexer.
                CommonTokenStream tokens = new CommonTokenStream(lexer);

                // O Parser verifica se o código segue a gramática.
                MiniPascalParser parser = new MiniPascalParser(tokens);

                // Gera a árvore sintática do programa.
                ParseTree tree = parser.program();

                // Converte a árvore sintática em uma AST.
                AstNode ast = new MyVisitor().visit(tree);

                // Executa a análise semântica.
                SemanticAnalyzer sem = new SemanticAnalyzer();

                sem.analyze(ast);

                // Exibe todos os erros encontrados.
                if (sem.hasErrors()) {

                        System.out.println("Erros semânticos:");

                        for (String erro : sem.getErrors()) {
                                System.out.println(erro);
                        }

                } else {

                        // Caso nenhum erro seja encontrado,
                        // o programa é considerado semanticamente válido.
                        System.out.println(
                                        "Programa semanticamente correto");
                }
        }
}