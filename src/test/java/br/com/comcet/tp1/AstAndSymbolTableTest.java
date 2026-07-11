package br.com.comcet.tp1;

import br.com.comcet.tp1.ast.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AstAndSymbolTableTest {

    /*
    * Testa a criação de uma AST para a expressão:
    * x := 10 + 5
    */
    @Test
    void criaAstAtribuicaoSoma() {

        // Cria os literais da expressão.
        Expression dez = new Literal(10);
        Expression cinco = new Literal(5);

        // Cria a operação 10 + 5.
        Expression soma = new BinaryExpression(dez, cinco, "+");

        // Cria o comando de atribuição.
        Command atrib = new AssignmentCommand(new Identifier("x"), soma);

        // Verifica se o comando criado é uma atribuição.
        assertTrue(atrib instanceof AssignmentCommand);

        AssignmentCommand a = (AssignmentCommand) atrib;

        // Verifica o nome da variável.
        assertEquals("x", a.id().name());

        // Verifica se a expressão é uma operação binária.
        assertTrue(a.expr() instanceof BinaryExpression);

        BinaryExpression b = (BinaryExpression) a.expr();

        // Verifica se o operador é "+".
        assertEquals("+", b.operator());

        // Verifica se os dois operandos são literais.
        assertTrue(b.left() instanceof Literal);
        assertTrue(b.right() instanceof Literal);
    }

    /*
    * Testa a inserção e a busca de símbolos
    * na tabela de símbolos.
    */
    @Test
    void symbolTableAddGet() {
        SymbolTable st = new SymbolTable();

        Symbol sx = new Symbol("x",  "variable", null, null);

        // Adiciona o símbolo na tabela.
        st.add("x", sx);

        // Verifica se o símbolo pode ser recuperado.
        assertSame(sx, st.get("x"));

        // Verifica o comportamento para um símbolo inexistente.
        assertNull(st.get("y"));
    }

     /*
     * Verifica se a tabela de símbolos
     * impede declarações duplicadas.
     */
    @Test
    void symbolTableNaoPermiteDuplicado() {
        SymbolTable st = new SymbolTable();
        st.add("x", new Symbol("x",  "variable", null, null));

        // Deve lançar exceção ao tentar inserir
        // um símbolo com o mesmo nome.
        assertThrows(IllegalArgumentException.class, () -> {
            st.add("x", new Symbol("x",  "variable", null, null));
        });
    }
}