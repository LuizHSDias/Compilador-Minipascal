package br.com.comcet.tp5;

import java.util.ArrayList;
import java.util.List;

import br.com.comcet.tp1.Symbol;
import br.com.comcet.tp1.ast.AssignmentCommand;
import br.com.comcet.tp1.ast.AstNode;
import br.com.comcet.tp1.ast.BinaryExpression;
import br.com.comcet.tp1.ast.FunctionDeclaration;
import br.com.comcet.tp1.ast.Identifier;
import br.com.comcet.tp1.ast.Program;
import br.com.comcet.tp1.ast.RepeatCommand;
import br.com.comcet.tp1.ast.VarDeclaration;

/*
 * Responsável pela análise semântica do programa.
 * Verifica regras de escopo, declarações duplicadas
 * e uso de variáveis e funções declaradas.
 */

public class SemanticAnalyzer {

    // Tabela de símbolos utilizada para controlar os escopos.
    private SymbolTable table = new SymbolTable();

    // Armazena todos os erros semânticos encontrados.
    private List<String> errors = new ArrayList<>();

    /*
     * Inicia a análise semântica criando o escopo global
     * e percorrendo toda a AST.
     */

    public void analyze(AstNode node) {

        // Cria o escopo global do programa.
        table.enterScope();

        visit(node);

        // Remove o escopo global ao finalizar a análise.
        table.exitScope();
    }

    /*
     * Percorre a AST identificando o tipo de cada nó
     * para executar a verificação semântica correspondente.
     */
    private void visit(AstNode node) {

        if (node instanceof Program prog) {

            // Cada bloco do programa possui seu próprio escopo.
            table.enterScope();

            // Visita todos os nós do programa.
            for (AstNode child : prog.children()) {
                visit(child);
            }

            table.exitScope();

        } else if (node instanceof VarDeclaration decl) {

            visitVarDeclaration(decl);

        } else if (node instanceof FunctionDeclaration func) {

            visitFunctionDeclaration(func);

        } else if (node instanceof AssignmentCommand cmd) {

            visitAssignment(cmd);

        } else if (node instanceof BinaryExpression bin) {

            visit(bin.left());
            visit(bin.right());

        } else if (node instanceof Identifier id) {

            visitIdentifier(id);
        } else if (node instanceof RepeatCommand repeat){
            visit(repeat.block());
            visit(repeat.condition());
        }
    }

    /*
     * Verifica se a variável já foi declarada no escopo atual.
     * Caso contrário, adiciona a variável na tabela de símbolos.
     */
    private void visitVarDeclaration(
            VarDeclaration decl) {

        String name = decl.name();

        // Evita declarações duplicadas no mesmo escopo.
        if (table.existsInCurrentScope(name)) {

            errors.add(
                    "Variável já declarada: " + name);

        } else {

            // Registra a variável na tabela de símbolos.
            table.add(
                    name,
                    new Symbol(
                            name,
                            "variable",
                            decl.type(),
                            null));
        }
    }

    /*
     * Registra a função na tabela de símbolos
     * e cria um novo escopo para seu corpo.
     */
    private void visitFunctionDeclaration(
            FunctionDeclaration func) {

        String name = func.name();

        // Verifica se já existe uma função com o mesmo nome.
        if (table.existsInCurrentScope(name)) {

            errors.add(
                    "Função já declarada: " + name);

            return;
        }

        // Adiciona a função ao escopo atual.
        table.add(
                name,
                new Symbol(
                        name,
                        "function",
                        null,
                        null));

        // Cria o escopo local da função.
        table.enterScope();

        // Analisa semanticamente o corpo da função.
        visit(func.block());

        // Encerra o escopo da função.
        table.exitScope();
    }

    /*
     * Verifica se a variável utilizada na atribuição
     * foi declarada e pertence à categoria correta.
     */
    private void visitAssignment(
            AssignmentCommand cmd) {

        String name = cmd.id().name();

        // Procura a variável na tabela de símbolos.
        Symbol symbol = table.lookup(name);

        if (symbol == null) {

            errors.add(
                    "Variável não declarada: " + name);

        } else if (!symbol.category().equals("variable")) {

            errors.add(
                    name + " não é variável");
        }

        // Continua a análise da expressão atribuída.
        visit(cmd.expr());
    }

    /*
     * Verifica se o identificador utilizado
     * existe em algum escopo válido.
     */
    private void visitIdentifier(
            Identifier id) {

        // Procura o identificador na tabela de símbolos.
        Symbol symbol = table.lookup(id.name());

        if (symbol == null) {

            errors.add(
                    "Identificador não declarado: "
                            + id.name());
        }
    }

    // Indica se foram encontrados erros semânticos.
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    // Retorna todos os erros encontrados durante a análise.
    public List<String> getErrors() {
        return errors;
    }
}