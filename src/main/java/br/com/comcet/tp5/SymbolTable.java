package br.com.comcet.tp5;

import br.com.comcet.tp1.Symbol;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/*
 * Implementa a tabela de símbolos utilizando uma pilha de escopos.
 * Cada escopo possui um conjunto de variáveis e funções declaradas.
 */
public class SymbolTable {

    // Cada posição da pilha representa um escopo da linguagem.
    private Stack<Map<String, Symbol>> scopes = new Stack<>();

    // Cria um novo escopo quando entramos em um bloco ou função.
    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    // Remove o escopo atual ao sair do bloco ou função.
    public void exitScope() {
        scopes.pop();
    }

    // Adiciona um símbolo no escopo atual.
    public void add(String name, Symbol symbol) {
        scopes.peek().put(name, symbol);
    }

    // Procura um símbolo começando pelo escopo mais interno.
    // Caso não encontre, continua procurando nos escopos externos.
    public Symbol lookup(String name) {

        for (int i = scopes.size() - 1; i >= 0; i--) {

            Map<String, Symbol> scope = scopes.get(i);

            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }

        // Retorna null caso o símbolo não exista em nenhum escopo.
        return null;
    }

    // Verifica se um símbolo já foi declarado apenas no escopo atual.
    public boolean existsInCurrentScope(String name) {
        return scopes.peek().containsKey(name);
    }
}