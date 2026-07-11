package br.com.comcet.tp6;

import br.com.comcet.tp1.ast.AstNode;
import br.com.comcet.tp4.MyVisitor;
import br.com.comcet.tp4.parser.MiniPascalLexer;
import br.com.comcet.tp4.parser.MiniPascalParser;
import br.com.comcet.tp5.SemanticAnalyzer;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FrontendSmokeTest {

    /*
     * Converte um código MiniPascal em uma AST,
     * utilizando o ANTLR e o MyVisitor.
     */
    private AstNode parse(String codigo) {

        CharStream input =
                CharStreams.fromString(codigo);

        // Executa a análise léxica.
        MiniPascalLexer lexer =
                new MiniPascalLexer(input);

        // Gera a sequência de tokens.
        CommonTokenStream tokens =
                new CommonTokenStream(lexer);

        // Executa a análise sintática.
        MiniPascalParser parser =
                new MiniPascalParser(tokens);

        ParseTree tree =
                parser.program();

        // Converte a Parse Tree em AST.
        return new MyVisitor().visit(tree);
    }

     /*
     * Verifica se um programa válido passa
     * pela análise semântica e pela verificação
     * de tipos sem gerar erros.
     */
    @Test
    void testaProgramaValido() {

        String codigo =
                "var x : integer; begin x := 10; end";

        AstNode ast = parse(codigo);

        // Executa a análise semântica.
        SemanticAnalyzer sem =
                new SemanticAnalyzer();

        sem.analyze(ast);

        TypeChecker tc =
                new TypeChecker();

        tc.check(ast);

        // Não deve haver erros.
        assertFalse(sem.hasErrors());
        assertFalse(tc.hasErrors());
    }

     /*
     * Verifica se a análise semântica detecta
     * o uso de uma variável não declarada.
     */
    @Test
    void testaVariavelNaoDeclarada() {

        String codigo =
                "var x : integer; begin y := 10; end";

        AstNode ast = parse(codigo);

        SemanticAnalyzer sem =
                new SemanticAnalyzer();

        sem.analyze(ast);

        // Deve existir pelo menos um erro semântico.
        assertTrue(sem.hasErrors());
    }

    /*
     * Verifica se o TypeChecker detecta
     * uma operação entre tipos incompatíveis.
     */
    @Test
    void testaErroDeTipo() {

        String codigo =
                "var x : integer; begin x := 10 + true; end";

        AstNode ast = parse(codigo);

        TypeChecker tc =
                new TypeChecker();

        tc.check(ast);

        // Deve existir pelo menos um erro de tipo.
        assertTrue(tc.hasErrors());
    }
}