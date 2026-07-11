package br.com.comcet.tp3;

import br.com.comcet.tp1.ast.*;
import br.com.comcet.tp2.Scanner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    /*
    * Verifica se o Parser respeita a precedência
    * dos operadores aritméticos.
    * Neste caso, a multiplicação deve ser realizada
    * antes da soma.
    */
    @Test
    void respeitaPrecedenciaMultiplicacao() {

        String codigo = "x := 10 + 5 * 2;";

        // Cria o Scanner e o Parser.
        Scanner scanner = new Scanner(codigo);
        Parser parser = new Parser(scanner);

        // Faz a análise sintática do comando.
        Command cmd = parser.parseCommand();

        // Verifica se o comando reconhecido é uma atribuição.
        assertTrue(cmd instanceof AssignmentCommand);

        AssignmentCommand a = (AssignmentCommand) cmd;

        // A expressão principal deve ser uma operação binária.
        assertTrue(a.expr() instanceof BinaryExpression);

        BinaryExpression plus = (BinaryExpression) a.expr();

        // operador principal deve ser +
        assertEquals("+", plus.operator());

        // O lado esquerdo da soma deve ser um literal.
        assertTrue(plus.left() instanceof Literal);

        // O lado direito da soma deve ser outra operação binária (*).
        assertTrue(plus.right() instanceof BinaryExpression);

        BinaryExpression mult = (BinaryExpression) plus.right();

        // Verifica se a multiplicação foi criada corretamente.
        assertEquals("*", mult.operator());
    }

    /*
    * Verifica se os parênteses alteram corretamente
    * a ordem de avaliação das expressões.
    */
    @Test
    void parentesesAlteramPrecedencia() {

        String codigo = "x := (10 + 5) * 2;";

        // Cria o Scanner e o Parser.
        Scanner scanner = new Scanner(codigo);
        Parser parser = new Parser(scanner);

        // Faz a análise sintática do comando.
        Command cmd = parser.parseCommand();

        // Verifica se foi criada uma atribuição.
        assertTrue(cmd instanceof AssignmentCommand);

        AssignmentCommand a = (AssignmentCommand) cmd;

        // A expressão principal deve ser uma operação binária.
        assertTrue(a.expr() instanceof BinaryExpression);

        BinaryExpression mult = (BinaryExpression) a.expr();

        // O operador principal agora deve ser '*'.
        assertEquals("*", mult.operator());

        // O lado esquerdo da multiplicação deve ser uma soma.
        assertTrue(mult.left() instanceof BinaryExpression);

        BinaryExpression plus = (BinaryExpression) mult.left();

        // Verifica se a soma foi construída corretamente.
        assertEquals("+", plus.operator());
    }
}