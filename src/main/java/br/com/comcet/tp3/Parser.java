package br.com.comcet.tp3;

import br.com.comcet.tp2.Scanner;
import br.com.comcet.tp1.Token;
import br.com.comcet.tp1.TokenType;
import br.com.comcet.tp1.ast.*;

/*
 * Parser responsável pela análise sintática da linguagem.
 * Ele recebe os tokens produzidos pelo Scanner e constrói
 * a AST do programa.
 */

public class Parser {

    // Scanner utilizado para obter os próximos tokens.
    private Scanner scanner;

    // Token que está sendo analisado no momento.
    private Token currentToken;

    /*
     * Inicializa o parser e lê o primeiro token do programa.
     */
    public Parser(Scanner scanner) {
        this.scanner = scanner;
        this.currentToken = scanner.nextToken();
    }

    /*
     * Avança para o próximo token produzido pelo Scanner.
     */
    private void advance() {
        currentToken = scanner.nextToken();
    }

    /*
     * Verifica se o token atual é o esperado.
     * Caso seja, avança para o próximo token.
     * Caso contrário, lança um erro sintático.
     */
    private void match(TokenType expected) {
        if (currentToken.type() == expected) {
            advance();
        } else {
            throw new RuntimeException(
                    "Erro sintático: esperado " + expected +
                            " mas encontrou " + currentToken.type());
        }
    }

    /*
     * Analisa expressões de soma e subtração.
     * Também respeita a precedência das operações.
     */
    public Expression parseExpression() {
        Expression left = parseTerm();

        // Continua enquanto encontrar operadores + ou -.
        while (currentToken.type() == TokenType.OPERATOR &&
                (currentToken.text().equals("+") || currentToken.text().equals("-"))) {

            Token op = currentToken;
            match(TokenType.OPERATOR);

            Expression right = parseTerm();
            left = new BinaryExpression(left, right, op.text());
        }

        return left;
    }

    /*
     * Analisa multiplicações e divisões.
     * Possui maior precedência que soma e subtração.
     */
    public Expression parseTerm() {
        Expression left = parseFactor();

        // Continua enquanto encontrar operadores * ou /.
        while (currentToken.type() == TokenType.OPERATOR &&
                (currentToken.text().equals("*") || currentToken.text().equals("/"))) {

            Token op = currentToken;
            match(TokenType.OPERATOR);

            Expression right = parseFactor();
            left = new BinaryExpression(left, right, op.text());
        }

        return left;
    }

    /*
     * Analisa os elementos básicos de uma expressão:
     * números, identificadores e expressões entre parênteses.
     */
    public Expression parseFactor() {

        // Reconhece um número inteiro.
        if (currentToken.type() == TokenType.NUMBER) {
            Token num = currentToken;
            match(TokenType.NUMBER);
            return new Literal(Integer.parseInt(num.text()));

            // Reconhece um identificador.
        } else if (currentToken.type() == TokenType.IDENTIFIER) {
            Token id = currentToken;
            match(TokenType.IDENTIFIER);
            return new Identifier(id.text());

            // Reconhece uma expressão entre parênteses.
        } else if (currentToken.type() == TokenType.DELIMITER &&
                currentToken.text().equals("(")) {

            match(TokenType.DELIMITER);

            Expression expr = parseExpression();

            if (currentToken.type() == TokenType.DELIMITER &&
                    currentToken.text().equals(")")) {
                match(TokenType.DELIMITER);
            } else {
                throw new RuntimeException("Esperado )");
            }

            return expr;
        }

        // Caso nenhum fator válido seja encontrado, gera erro.
        throw new RuntimeException("Token inesperado: " + currentToken);
    }

    /*
     * Analisa um comando de atribuição.
     * Exemplo: x := 10 + y;
     */
    public Command parseAssignment() {

        Token id = currentToken;
        match(TokenType.IDENTIFIER);

        match(TokenType.ASSIGN);

        Expression expr = parseExpression();

        match(TokenType.DELIMITER);

        // Cria o comando de atribuição na AST.
        return new AssignmentCommand(new Identifier(id.text()), expr);
    }

    /*
     * Identifica qual comando deve ser analisado.
     * Nesta versão do parser apenas atribuições são suportadas.
     */
    public Command parseCommand() {

        if (currentToken.type() == TokenType.IDENTIFIER) {
            return parseAssignment();
        }

        // Gera erro caso o comando não seja reconhecido.
        throw new RuntimeException("Comando inválido");
    }
}