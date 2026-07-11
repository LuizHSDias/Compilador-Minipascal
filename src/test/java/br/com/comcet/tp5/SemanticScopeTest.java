package br.com.comcet.tp5;

import br.com.comcet.tp1.ast.AstNode;
import br.com.comcet.tp4.MyVisitor;
import br.com.comcet.tp4.parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SemanticScopeTest {

    /*
     * Converte um código MiniPascal em uma AST,
     * utilizando o ANTLR e o MyVisitor.
     */
    private AstNode parse(String codigo) {

        CharStream input =
                CharStreams.fromString(codigo);

        // Análise léxica.
        MiniPascalLexer lexer =
                new MiniPascalLexer(input);

        // Geração da sequência de tokens.
        CommonTokenStream tokens =
                new CommonTokenStream(lexer);

        // Análise sintática.
        MiniPascalParser parser =
                new MiniPascalParser(tokens);

        ParseTree tree = parser.program();

        // Converte a Parse Tree em AST.
        return new MyVisitor().visit(tree);
    }

    /*
     * Verifica se o analisador semântico detecta
     * o uso de uma variável que não foi declarada.
     */
    @Test
    void detectaVariavelNaoDeclarada() {

        String codigo =
                "var x : integer; begin y := 1; end";

        AstNode ast = parse(codigo);

        // Executa a análise semântica.
        SemanticAnalyzer sem =
                new SemanticAnalyzer();

        sem.analyze(ast);

        // Deve existir pelo menos um erro.
        assertTrue(sem.hasErrors());

        // Verifica se o erro é referente à variável não declarada.
        assertTrue(
                sem.getErrors()
                        .stream()
                        .anyMatch(e ->
                                e.contains(
                                        "Variável não declarada"
                                ))
        );
    }

     /*
     * Verifica se o analisador semântico detecta
     * duas declarações da mesma variável.
     */
    @Test
    void detectaDuplaDeclaracao() {

        String codigo =
                "var x : integer; var x : integer; begin x := 1; end";

        AstNode ast = parse(codigo);

        // Executa a análise semântica.
        SemanticAnalyzer sem =
                new SemanticAnalyzer();

        sem.analyze(ast);

        // Deve existir pelo menos um erro.
        assertTrue(sem.hasErrors());

        // Verifica se o erro indica dupla declaração.
        assertTrue(
                sem.getErrors()
                        .stream()
                        .anyMatch(e ->
                                e.contains(
                                        "já declarada"
                                ))
        );
    }
}