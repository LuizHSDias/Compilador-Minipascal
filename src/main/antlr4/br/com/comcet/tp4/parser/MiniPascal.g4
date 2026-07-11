// Gramática da linguagem MiniPascal.
// Define todas as regras sintáticas utilizadas pelo ANTLR
// para reconhecer programas válidos.
grammar MiniPascal;

// Estrutura principal do programa.
// Um programa pode possuir declarações globais,
// funções e um bloco principal.
program
    : declaration*
      functionDecl*
      block
      EOF
    ;

// Declaração de variável.
declaration
    : 'var' ID ':' type ';'
    ;

// Tipos suportados pela linguagem.
type
    : 'integer'
    | 'boolean'
    | 'string'
    ;

// Declaração de uma função.
functionDecl
    : 'function'
      ID
      '(' parameterList? ')'
      ':' type
      block
    ;

// Lista de parâmetros da função.
parameterList
    : parameter (',' parameter)*
    ;

// Declaração de um parâmetro.
parameter
    : ID ':' type
    ;

// Bloco de comandos delimitado por begin/end.
block
    : 'begin'
        declaration*
        command*
      'end'
    ;

// Todos os comandos aceitos pela linguagem.
command
    : assignment
    | functionCall
    | printCommand
    | ifCommand
    | repeatCommand
    | whileCommand
    | returnCommand
    ;

// Comando de atribuição.
assignment
    : ID ':=' expression ';'
    ;

// Chamada de função como comando.
functionCall
    : ID '(' argumentList? ')' ';'
    ;

// Impressão de uma expressão na saída.
printCommand
    : 'writeln' '(' expression ')' ';'
    ;

// Retorno de uma função.
returnCommand
    : 'return' expression ';'
    ;

// Lista de argumentos passados para uma função.
argumentList
    : expression (',' expression)*
    ;

// Estrutura condicional.
ifCommand
    : 'if' expression 'then' block
    ;

// repeatCommand
repeatCommand
    : 'repeat' block 'until' expression
    ;

// Estrutura de repetição.
whileCommand
    : 'while' expression 'do' block
    ;

// Expressões relacionais e aritméticas.
expression
    : expression op=('>'|'<'|'=') expression # relationalExpr
    | expression op=('+'|'-') term           # binaryExpr
    | term                                   # termOnly
    ;

// Operações de multiplicação e divisão.
term
    : term op=('*'|'/') factor               # binaryTerm
    | factor                                 # factorOnly
    ;

// Elementos básicos de uma expressão.
factor
    : NUMBER                                # number
    | STRING                                # stringLiteral
    | BOOLEAN                               # booleanLiteral
    | functionExpr                          # functionExpression
    | ID                                    # identifier
    | '(' expression ')'                    # parens
    ;

// Chamada de função utilizada dentro de expressões.
functionExpr
    : ID '(' argumentList? ')'
    ;

// TOKENS

// Valor booleano.
BOOLEAN
    : 'true'
    | 'false'
    ;

// Identificador da linguagem.
ID
    : [a-zA-Z]+
    ;

// Número inteiro.
NUMBER
    : [0-9]+
    ;

// Texto delimitado por aspas.
STRING
    : '"' .*? '"'
    ;

// Ignora espaços em branco.
WS
    : [ \t\r\n]+ -> skip
    ;