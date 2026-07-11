package br.com.comcet.tp1;

import java.util.HashMap;
import java.util.Map;

/*
 * Primeira versão da Tabela de Símbolos.
 * Armazena os identificadores da linguagem utilizando
 * um único mapa, sem suporte a escopos.
 */

public class SymbolTable {

    // Mapa que associa o nome do símbolo ao seu objeto Symbol.
    private final Map<String, Symbol> symbols = new HashMap<>();

    /*
     * Adiciona um novo símbolo na tabela.
     * Não permite símbolos com o mesmo nome.
     */
    public void add(String name, Symbol symbol) {

        if (symbols.containsKey(name)) {

            throw new IllegalArgumentException(
                    "Símbolo já existe: " + name);
        }

        symbols.put(name, symbol);
    }

    /*
     * Procura um símbolo pelo nome.
     * Retorna null caso ele não exista.
     */
    public Symbol get(String name) {

        return symbols.get(name);
    }
}