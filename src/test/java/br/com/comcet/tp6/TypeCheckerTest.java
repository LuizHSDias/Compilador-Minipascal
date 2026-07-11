package br.com.comcet.tp6;

import br.com.comcet.tp1.ast.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TypeCheckerTest {

    /*
     * Verifica se o TypeChecker detecta uma operação
     * inválida entre um inteiro e um booleano.
     */
    @Test
    void detectaErroAoSomarInteiroComBoolean() {

        // Cria a expressão: 10 + true
        Expression expr =
                new BinaryExpression(
                        new Literal(10),
                        new Literal(true),
                        "+"
                );

        // Cria o comando: x := 10 + true
        AssignmentCommand cmd =
                new AssignmentCommand(
                        new Identifier("x"),
                        expr
                );

        // Cria um programa contendo apenas esse comando.
        Program prog =
                new Program(
                        java.util.List.of(cmd)
                );

        // Executa a verificação de tipos.
        TypeChecker tc =
                new TypeChecker();

        tc.check(prog);

        // Deve existir pelo menos um erro.
        assertTrue(tc.hasErrors());

        // Verifica se o erro corresponde
        // à operação inválida.
        assertTrue(
                tc.getErrors()
                  .stream()
                  .anyMatch(e ->
                          e.contains("Operação inválida"))
        );
    }
}