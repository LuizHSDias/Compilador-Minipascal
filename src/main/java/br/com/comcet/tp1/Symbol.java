package br.com.comcet.tp1;

import br.com.comcet.tp6.Type;

/*
 * Representa um símbolo da Tabela de Símbolos.
 * Armazena as informações de um identificador,
 * como nome, categoria, tipo e valor.
 */

public class Symbol {

    // Nome do identificador (ex.: x, soma, contador).
    private final String name;

    // Categoria do símbolo (variable, function, etc.).
    private final String category;

    // Tipo de dado do símbolo.
    private final Type dataType;

    // Valor associado ao símbolo (quando necessário).
    private final Object value;

    /*
     * Cria um novo símbolo com todas as suas informações.
     */
    public Symbol(
            String name,
            String category,
            Type dataType,
            Object value
    ) {
        this.name = name;
        this.category = category;
        this.dataType = dataType;
        this.value = value;
    }

    // Retorna o nome do símbolo.
    public String name() {
        return name;
    }

    // Retorna a categoria do símbolo.
    public String category() {
        return category;
    }

    // Retorna o tipo do símbolo.
    public Type dataType() {
        return dataType;
    }

    // Retorna o valor armazenado no símbolo.
    public Object value() {
        return value;
    }
}