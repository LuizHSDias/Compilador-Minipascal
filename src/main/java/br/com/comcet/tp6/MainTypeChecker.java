package br.com.comcet.tp6;

import br.com.comcet.tp1.ast.AstNode;
import br.com.comcet.tp4.MyVisitor;
import br.com.comcet.tp4.parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import java.nio.file.Files;
import java.nio.file.Path;

/*
 * Classe utilizada para testar a verificação de tipos.
 * Ela lê um arquivo MiniPascal, gera a AST e executa o TypeChecker.
 */

public class MainTypeChecker {

    public static void main(String[] args)
            throws Exception {

        // Lê o código fonte que será analisado.
        String codigo =
                Files.readString(
                        Path.of(
                            "comcet/caso_teste/retorno_atribuicao_invalida.pas"
                        )
                );

        // Converte o código em um fluxo de caracteres.
        CharStream input =
                CharStreams.fromString(codigo);

        // O Lexer identifica os tokens da linguagem.
        MiniPascalLexer lexer =
                new MiniPascalLexer(input);

        // Armazena os tokens produzidos pelo Lexer.
        CommonTokenStream tokens =
                new CommonTokenStream(lexer);

        // O Parser verifica se o código segue a gramática.
        MiniPascalParser parser =
                new MiniPascalParser(tokens);

        // Gera a árvore sintática do programa.
        ParseTree tree =
                parser.program();

        // Caso exista algum erro de sintaxe, interrompe a execução.
        if (parser.getNumberOfSyntaxErrors() > 0) {

            System.out.println(
                    "Erros sintáticos encontrados.");

            return;
        }

        // Converte a árvore sintática em uma AST.
        AstNode ast =
                new MyVisitor().visit(tree);

        // Executa a verificação de tipos.
        TypeChecker tc =
                new TypeChecker();

        tc.check(ast);

        // Exibe todos os erros encontrados durante a análise.
        if (tc.hasErrors()) {

            System.out.println("Erros:");

            tc.getErrors()
              .forEach(System.out::println);

        // Caso nenhum erro seja encontrado, o programa é válido.
        } else {

            System.out.println(
                    "Programa válido"
            );
        }
    }
}