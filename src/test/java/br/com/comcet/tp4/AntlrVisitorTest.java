package br.com.comcet.tp4;

import br.com.comcet.tp1.ast.*;
import br.com.comcet.tp4.parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
* Verifica se o ANTLR e o MyVisitor constroem
* corretamente a AST, respeitando a precedência
* dos operadores aritméticos.
*/
public class AntlrVisitorTest {

    @Test
    void testaPrecedencia() {

        String codigo =
                "var x : integer; begin x := 10 + 5 * 2; end";

        // Cria o fluxo de entrada para o ANTLR.
        CharStream input = CharStreams.fromString(codigo);

        // Executa a análise léxica.
        MiniPascalLexer lexer =
                new MiniPascalLexer(input);

        // Gera a sequência de tokens.
        CommonTokenStream tokens =
                new CommonTokenStream(lexer);

        // Executa a análise sintática.
        MiniPascalParser parser =
                new MiniPascalParser(tokens);

        // Obtém a Parse Tree.
        ParseTree tree = parser.program();

        // Converte a Parse Tree em AST.
        MyVisitor visitor = new MyVisitor();

        AstNode ast = visitor.visit(tree);

        // Verifica se a AST foi criada.
        assertNotNull(ast);

        // O nó raiz deve ser um Program.
        assertTrue(ast instanceof Program);

        // Recupera o bloco principal do programa.
        Program prog = (Program) ast;

        Program block =
                (Program) prog.children().get(1);

        // Recupera o comando de atribuição.
        AssignmentCommand a =
                (AssignmentCommand)
                        block.children().get(0);

        // Recupera a expressão da atribuição.
        BinaryExpression plus =
                (BinaryExpression) a.expr();

        // O operador principal deve ser '+'.
        assertEquals("+", plus.operator());

        // O lado direito deve ser uma multiplicação,
        // comprovando que a precedência foi respeitada.
        assertTrue(
                plus.right()
                        instanceof BinaryExpression
        );
    }
}