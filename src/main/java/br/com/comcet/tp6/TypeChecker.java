package br.com.comcet.tp6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import br.com.comcet.tp1.ast.Program;
import br.com.comcet.tp1.ast.RepeatCommand;
import br.com.comcet.tp1.ast.ReturnCommand;
import br.com.comcet.tp1.ast.VarDeclaration;
import br.com.comcet.tp1.ast.WhileCommand;

/*
 * Responsável por verificar se as operações realizadas no programa
 * utilizam tipos compatíveis. Também valida chamadas de funções,
 * atribuições, retornos e condições de estruturas de controle.
 */

public class TypeChecker {

    // Armazena todos os erros encontrados durante a verificação.
    private final List<String> errors = new ArrayList<>();

    // Guarda o tipo de cada variável declarada.
    private final Map<String, Type> variables = new HashMap<>();

    // Armazena o tipo de retorno da função que está sendo analisada.
    private Type currentFunctionReturnType = null;

    // Guarda todas as funções declaradas para validar chamadas.
    private final Map<String, FunctionDeclaration> functions = new HashMap<>();

    /*
     * Percorre toda a AST verificando os tipos de cada comando
     * e expressão do programa.
     */
    public void check(AstNode node) {

        if (node instanceof Program prog) {

            // Visita todos os nós do programa.
            for (AstNode child : prog.children()) {
                check(child);
            }

        } else if (node instanceof VarDeclaration decl) {

            // Registra a variável e seu tipo para consultas futuras.
            variables.put(
                    decl.name(),
                    decl.type());

        } else if (node instanceof AssignmentCommand cmd) {

            // Descobre o tipo da expressão que será atribuída.
            Type exprType = inferType(cmd.expr());

            // Obtém o tipo da variável de destino.
            Type varType = variables.get(
                    cmd.id().name());

            // Verifica se os tipos da variável e da expressão são compatíveis.
            if (varType != null &&
                    exprType != Type.ERROR &&
                    varType != exprType) {

                errors.add(
                        "Atribuição incompatível: "
                                + varType
                                + " <- "
                                + exprType);
            }

        } 
        
        else if (node instanceof RepeatCommand repeat){

            Type conditionType = inferType(repeat.condition());

            if(conditionType != Type.BOOLEAN){
                errors.add("Condição do repeat deve ser boolean");
            }

            check(repeat.block());

        }
        
        
        else if (node instanceof IfCommand ifCmd) {

            // A condição do if deve obrigatoriamente ser booleana.
            Type conditionType = inferType(
                    ifCmd.condition());

            if (conditionType != Type.BOOLEAN) {

                errors.add(
                        "Condição do if deve ser BOOLEAN");
            }

            // Continua a verificação dentro do bloco do if.
            check(ifCmd.block());

        } 
        
        
        
        else if (node instanceof WhileCommand whileCmd) {

            // A condição do while deve ser do tipo BOOLEAN.
            Type conditionType = inferType(
                    whileCmd.condition());

            if (conditionType != Type.BOOLEAN) {

                errors.add(
                        "Condição do while deve ser BOOLEAN");
            }

            check(whileCmd.block());

        } else if (node instanceof ReturnCommand ret) {

            // Verifica se o tipo retornado é compatível com o tipo
            // declarado pela função.
            Type returnType = inferType(
                    ret.expression());

            if (currentFunctionReturnType != null &&
                    returnType != currentFunctionReturnType) {

                errors.add(
                        "Tipo de retorno incompatível: esperado "
                                + currentFunctionReturnType
                                + ", encontrado "
                                + returnType);
            }
        } else if (node instanceof FunctionDeclaration func) {

            // Registra a função para permitir futuras chamadas.
            functions.put(
                    func.name(),
                    func);

            Type previous = currentFunctionReturnType;

            currentFunctionReturnType = func.returnType();

            // Salva as variáveis atuais
            Map<String, Type> oldVariables = new HashMap<>(variables);

            // Os parâmetros passam a fazer parte do escopo local da função.
            for (Parameter p : func.parameters()) {

                variables.put(
                        p.name(),
                        p.type());
            }

            check(func.block());

            // Ao finalizar a função, restaura o escopo anterior.
            variables.clear();

            variables.putAll(oldVariables);

            currentFunctionReturnType = previous;

        } else if (node instanceof FunctionCall call) {

            checkFunctionCall(call);
        }
    }

    /*
     * Verifica se a chamada da função é válida,
     * conferindo quantidade e tipos dos argumentos.
     */
    private void checkFunctionCall(
            FunctionCall call) {

        FunctionDeclaration function = functions.get(
                call.name());

        // Confere se a função foi declarada.
        if (function == null) {

            errors.add(
                    "Função não declarada: "
                            + call.name());

            return;
        }

        // Verifica se a quantidade de argumentos está correta.
        if (call.arguments().size() != function.parameters().size()) {

            errors.add(
                    "Quantidade incorreta de argumentos em "
                            + call.name());

            return;
        }

        // Compara o tipo de cada argumento com o tipo
        // do parâmetro correspondente.
        for (int i = 0; i < call.arguments().size(); i++) {

            Type argumentType = inferType(
                    call.arguments().get(i));

            Type parameterType = function.parameters()
                    .get(i)
                    .type();

            if (argumentType != parameterType) {

                errors.add(
                        "Tipo incompatível no argumento "
                                + (i + 1)
                                + " da função "
                                + call.name());
            }
        }
    }

    /*
     * Descobre o tipo de uma expressão e o propaga
     * para os nós superiores da AST.
     */
    private Type inferType(Expression expr) {

        if (expr instanceof Literal lit) {

            // O tipo do literal é determinado pelo valor armazenado.
            Object value = lit.value();

            if (value instanceof Integer) {

                expr.setType(Type.INTEGER);

            } else if (value instanceof Boolean) {

                expr.setType(Type.BOOLEAN);

            } else if (value instanceof String) {

                expr.setType(Type.STRING);

            } else {

                expr.setType(Type.ERROR);
            }

            return expr.getType();
        }

        if (expr instanceof BinaryExpression bin) {

            // Primeiro descobre o tipo dos dois operandos.
            Type left = inferType(bin.left());

            Type right = inferType(bin.right());

            String op = bin.operator();

            // Operações aritméticas aceitam apenas inteiros.
            if (op.equals("+") ||
                    op.equals("-") ||
                    op.equals("*") ||
                    op.equals("/")) {

                // Comparações retornam BOOLEAN quando os tipos são compatíveis.
                if (left == Type.INTEGER &&
                        right == Type.INTEGER) {

                    expr.setType(Type.INTEGER);

                } else {

                    expr.setType(Type.ERROR);

                    errors.add(
                            "Operação inválida: "
                                    + left
                                    + " "
                                    + op
                                    + " "
                                    + right);
                }

                return expr.getType();
            }

            if (op.equals(">") ||
                    op.equals("<") ||
                    op.equals("=")) {

                if (left == right) {

                    expr.setType(Type.BOOLEAN);

                } else {

                    expr.setType(Type.ERROR);

                    errors.add(
                            "Comparação inválida: "
                                    + left
                                    + " "
                                    + op
                                    + " "
                                    + right);
                }

                return expr.getType();
            }
        }

        // Verifica se a função utilizada na expressão existe.
        if (expr instanceof FunctionCallExpression call) {

            FunctionDeclaration function = functions.get(call.name());

            if (function == null) {

                errors.add(
                        "Função não declarada: "
                                + call.name());

                expr.setType(Type.ERROR);

                return Type.ERROR;
            }

            if (call.arguments().size() != function.parameters().size()) {

                errors.add(
                        "Quantidade incorreta de argumentos em "
                                + call.name());

                expr.setType(Type.ERROR);

                return Type.ERROR;
            }

            // Confere se todos os argumentos possuem tipos compatíveis.
            for (int i = 0; i < call.arguments().size(); i++) {

                Type argumentType = inferType(
                        call.arguments().get(i));

                Type parameterType = function.parameters()
                        .get(i)
                        .type();

                if (argumentType != parameterType) {

                    errors.add(
                            "Tipo incompatível no argumento "
                                    + (i + 1)
                                    + " da função "
                                    + call.name());

                    // O tipo da expressão passa a ser o tipo de retorno da função.
                    expr.setType(Type.ERROR);

                    return Type.ERROR;
                }
            }

            expr.setType(
                    function.returnType());

            return function.returnType();
        }

        if (expr instanceof Identifier id) {

            // Consulta o tipo da variável na tabela de variáveis.
            Type type = variables.get(
                    id.name());

            if (type == null) {

                // Associa o tipo encontrado ao identificador.
                expr.setType(Type.ERROR);

                errors.add(
                        "Variável não declarada: "
                                + id.name());

                return Type.ERROR;
            }

            expr.setType(type);

            return type;
        }

        expr.setType(Type.ERROR);

        return Type.ERROR;
    }

    // Indica se algum erro foi encontrado durante a verificação.
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    // Retorna a lista completa de erros encontrados.
    public List<String> getErrors() {
        return errors;
    }
}