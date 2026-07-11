package br.com.comcet.tp4;

import java.util.ArrayList;
import java.util.List;

import br.com.comcet.tp1.ast.AssignmentCommand;
import br.com.comcet.tp1.ast.AstNode;
import br.com.comcet.tp1.ast.BinaryExpression;
import br.com.comcet.tp1.ast.Expression;
import br.com.comcet.tp1.ast.FunctionCall;
import br.com.comcet.tp1.ast.FunctionCallExpression;
import br.com.comcet.tp1.ast.FunctionDeclaration;
import br.com.comcet.tp1.ast.Identifier;
import br.com.comcet.tp1.ast.IfCommand;
import br.com.comcet.tp1.ast.Literal;
import br.com.comcet.tp1.ast.Parameter;
import br.com.comcet.tp1.ast.PrintCommand;
import br.com.comcet.tp1.ast.Program;
import br.com.comcet.tp1.ast.RepeatCommand;
import br.com.comcet.tp1.ast.ReturnCommand;
import br.com.comcet.tp1.ast.VarDeclaration;
import br.com.comcet.tp1.ast.WhileCommand;
import br.com.comcet.tp4.parser.MiniPascalBaseVisitor;
import br.com.comcet.tp4.parser.MiniPascalParser;
import br.com.comcet.tp6.Type;

/*
 * Visitor responsável por percorrer a ParseTree gerada pelo ANTLR
 * e construir a AST (Árvore Sintática Abstrata) utilizada pelas
 * próximas etapas do compilador.
 */

public class MyVisitor extends MiniPascalBaseVisitor<AstNode> {

    /*
     * Cria o nó principal da AST contendo todas as declarações,
     * funções e o bloco principal do programa.
     */
    @Override
    public AstNode visitProgram(
            MiniPascalParser.ProgramContext ctx) {

        List<AstNode> nodes = new ArrayList<>();

        // Adiciona todas as declarações globais.
        for (var decl : ctx.declaration()) {
            nodes.add(visit(decl));
        }

        // Adiciona todas as funções declaradas.
        for (var func : ctx.functionDecl()) {
            nodes.add(visit(func));
        }

        // Adiciona o bloco principal do programa.
        nodes.add(visit(ctx.block()));

        return new Program(nodes);
    }

    /*
     * Converte uma declaração da gramática em um nó
     * VarDeclaration da AST.
     */
    @Override
    public AstNode visitDeclaration(
            MiniPascalParser.DeclarationContext ctx) {

        String typeName = ctx.type().getText();

        Type type;

        // Converte o tipo informado na gramática para o enum Type.
        switch (typeName) {

            case "integer":
                type = Type.INTEGER;
                break;

            case "boolean":
                type = Type.BOOLEAN;
                break;

            case "string":
                type = Type.STRING;
                break;

            default:
                type = Type.ERROR;
        }

        return new VarDeclaration(
                ctx.ID().getText(),
                type);
    }

    /*
     * Constrói um bloco da AST contendo declarações locais
     * e comandos.
     */
    @Override
    public AstNode visitBlock(
            MiniPascalParser.BlockContext ctx) {

        List<AstNode> nodes = new ArrayList<>();

        for (var decl : ctx.declaration()) {
            nodes.add(visit(decl));
        }

        for (var cmd : ctx.command()) {
            nodes.add(visit(cmd));
        }

        return new Program(nodes);
    }

    /*
     * Cria um comando de atribuição contendo
     * o identificador e a expressão.
     */
    @Override
    public AstNode visitAssignment(
            MiniPascalParser.AssignmentContext ctx) {

        Identifier id = new Identifier(
                ctx.ID().getText());

        Expression expr = (Expression) visit(ctx.expression());

        return new AssignmentCommand(id, expr);
    }

    /*
     * Constrói uma chamada de função e adiciona
     * todos os argumentos informados.
     */
    @Override
    public AstNode visitFunctionCall(
            MiniPascalParser.FunctionCallContext ctx) {

        List<Expression> arguments = new ArrayList<>();

        if (ctx.argumentList() != null) {

            // Visita cada argumento da chamada da função.
            for (var expr : ctx.argumentList().expression()) {

                arguments.add(
                        (Expression) visit(expr));
            }
        }

        return new FunctionCall(
                ctx.ID().getText(),
                arguments);
    }

    /*
     * Cria um nó de expressão binária para
     * operações de soma e subtração.
     */
    @Override
    public AstNode visitBinaryExpr(
            MiniPascalParser.BinaryExprContext ctx) {

        Expression left = (Expression) visit(ctx.expression());

        Expression right = (Expression) visit(ctx.term());

        String op = ctx.op.getText();

        return new BinaryExpression(
                left,
                right,
                op);
    }

    @Override
    public AstNode visitRepeatCommand(MiniPascalParser.RepeatCommandContext ctx){

        Program block = (Program) visit(ctx.block());

        Expression condition = (Expression) visit(ctx.expression());

        return new RepeatCommand(block, condition);
    }

    /*
     * Cria um nó de expressão binária para
     * multiplicação e divisão.
     */
    @Override
    public AstNode visitBinaryTerm(
            MiniPascalParser.BinaryTermContext ctx) {

        Expression left = (Expression) visit(ctx.term());

        Expression right = (Expression) visit(ctx.factor());

        String op = ctx.op.getText();

        return new BinaryExpression(
                left,
                right,
                op);
    }

    

    /*
     * Converte um número da gramática
     * para um Literal inteiro.
     */
    @Override
    public AstNode visitNumber(
            MiniPascalParser.NumberContext ctx) {

        return new Literal(
                Integer.parseInt(
                        ctx.NUMBER().getText()));
    }

    /*
     * Cria um nó Identifier na AST.
     */
    @Override
    public AstNode visitIdentifier(
            MiniPascalParser.IdentifierContext ctx) {

        return new Identifier(
                ctx.ID().getText());
    }

    /*
     * Parênteses não criam um novo nó.
     * Apenas retorna a expressão interna.
     */
    @Override
    public AstNode visitParens(
            MiniPascalParser.ParensContext ctx) {

        return visit(ctx.expression());
    }

    /*
     * Constrói a declaração completa de uma função,
     * incluindo parâmetros, tipo de retorno e corpo.
     */
    @Override
    public AstNode visitFunctionDecl(
            MiniPascalParser.FunctionDeclContext ctx) {

        List<Parameter> parameters = new ArrayList<>();

        if (ctx.parameterList() != null) {

            // Converte todos os parâmetros da função.
            for (var p : ctx.parameterList().parameter()) {

                String name = p.ID().getText();

                String typeName = p.type().getText();

                Type type;

                // Determina o tipo de retorno da função.
                switch (typeName) {

                    case "integer":
                        type = Type.INTEGER;
                        break;

                    case "boolean":
                        type = Type.BOOLEAN;
                        break;

                    case "string":
                        type = Type.STRING;
                        break;

                    default:
                        type = Type.ERROR;
                }

                parameters.add(
                        new Parameter(
                                name,
                                type));
            }
        }

        String returnTypeName = ctx.type().getText();

        Type returnType;

        switch (returnTypeName) {

            case "integer":
                returnType = Type.INTEGER;
                break;

            case "boolean":
                returnType = Type.BOOLEAN;
                break;

            case "string":
                returnType = Type.STRING;
                break;

            default:
                returnType = Type.ERROR;
        }

        Program block = (Program) visit(ctx.block());

        return new FunctionDeclaration(
                ctx.ID().getText(),
                parameters,
                returnType,
                block);
    }

    /*
     * Converte um literal booleano
     * para um Literal da AST.
     */
    @Override
    public AstNode visitBooleanLiteral(
            MiniPascalParser.BooleanLiteralContext ctx) {

        return new Literal(
                Boolean.parseBoolean(
                        ctx.BOOLEAN().getText()));
    }

    /*
     * Remove as aspas do texto e cria
     * um Literal do tipo String.
     */
    @Override
    public AstNode visitStringLiteral(
            MiniPascalParser.StringLiteralContext ctx) {

        String text = ctx.STRING().getText();

        text = text.substring(
                1,
                text.length() - 1);

        return new Literal(text);
    }

    /*
     * Constrói uma expressão relacional,
     * como >, < ou =.
     */
    @Override
    public AstNode visitRelationalExpr(
            MiniPascalParser.RelationalExprContext ctx) {

        Expression left = (Expression) visit(ctx.expression(0));

        Expression right = (Expression) visit(ctx.expression(1));

        String op = ctx.op.getText();

        return new BinaryExpression(
                left,
                right,
                op);
    }

    /*
     * Cria um comando if contendo
     * a condição e o bloco executado.
     */
    @Override
    public AstNode visitIfCommand(
            MiniPascalParser.IfCommandContext ctx) {

        Expression condition = (Expression) visit(
                ctx.expression());

        Program block = (Program) visit(
                ctx.block());

        return new IfCommand(
                condition,
                block);
    }

    /*
     * Cria um comando while contendo
     * a condição e o bloco de repetição.
     */
    @Override
    public AstNode visitWhileCommand(
            MiniPascalParser.WhileCommandContext ctx) {

        Expression condition = (Expression) visit(
                ctx.expression());

        Program block = (Program) visit(
                ctx.block());

        return new WhileCommand(
                condition,
                block);
    }

    /*
     * Cria um comando de retorno
     * utilizado dentro das funções.
     */
    @Override
    public AstNode visitReturnCommand(
            MiniPascalParser.ReturnCommandContext ctx) {

        Expression expression = (Expression) visit(
                ctx.expression());

        return new ReturnCommand(
                expression);
    }

    /*
     * Constrói uma chamada de função
     * utilizada dentro de uma expressão.
     */
    @Override
    public AstNode visitFunctionExpression(
            MiniPascalParser.FunctionExpressionContext ctx) {

        List<Expression> arguments = new ArrayList<>();

        if (ctx.functionExpr().argumentList() != null) {

            // Converte todos os argumentos da função.
            for (var expr : ctx.functionExpr()
                    .argumentList()
                    .expression()) {

                arguments.add(
                        (Expression) visit(expr));
            }
        }

        return new FunctionCallExpression(
                ctx.functionExpr()
                        .ID()
                        .getText(),
                arguments);
    }

    /*
     * Cria um comando de impressão (writeln).
     */
    @Override
    public AstNode visitPrintCommand(
            MiniPascalParser.PrintCommandContext ctx) {

        Expression expression = (Expression) visit(ctx.expression());

        return new PrintCommand(expression);
    }

}