# 🖥️ Compilador MiniPascal

Projeto desenvolvido para a disciplina de **Compiladores**, com o objetivo de implementar, de forma incremental, um compilador para uma linguagem baseada em **MiniPascal**, utilizando **Java**, **ANTLR4** e **ASM**.

O projeto foi desenvolvido ao longo de oito etapas (TP0 até TP7), evoluindo desde um simples processamento de texto até a geração de bytecode executável pela Máquina Virtual Java (JVM).

---

# 📚 Objetivo

Desenvolver um compilador completo capaz de:

- Realizar análise léxica;
- Realizar análise sintática;
- Construir uma Árvore de Sintaxe Abstrata (AST);
- Executar análise semântica;
- Verificar tipos das expressões;
- Gerar bytecode para a JVM;
- Executar programas escritos em MiniPascal.

---

# 🚀 Tecnologias Utilizadas

- Java 17
- Maven
- ANTLR 4
- ASM (Java Bytecode)
- JUnit 5
- Git

---

# 📂 Estrutura do Projeto

```text
comcet
│
├── caso_teste/
├── src/
├── .gitignore
├── .gitlab-ci.yml
├── README.md
└── pom.xml
```

---

# 🛠️ Etapas do Projeto

## TP0 — Processamento de Texto

Na primeira etapa foi desenvolvido um programa responsável por realizar estatísticas sobre um texto informado pelo usuário.

Foram implementadas funcionalidades como:

- leitura do texto pela linha de comando ou arquivo;
- contagem de caracteres;
- contagem de palavras;
- identificação da palavra mais frequente;
- identificação da letra mais frequente;
- cálculo da maior palavra;
- exibição do Top 5 palavras.

Essa etapa teve como objetivo praticar manipulação de arquivos, estruturas de dados e organização do projeto utilizando Maven.

---

## TP1 — Estrutura da AST

Foi implementada toda a infraestrutura da Árvore de Sintaxe Abstrata (AST), utilizada para representar internamente programas MiniPascal.

Foram desenvolvidas classes como:

- AstNode
- Program
- Expression
- Command
- Literal
- Identifier
- BinaryExpression
- AssignmentCommand
- FunctionDeclaration
- FunctionCall
- FunctionCallExpression
- IfCommand
- WhileCommand
- PrintCommand
- ReturnCommand
- Parameter
- VarDeclaration

Também foram implementadas:

- Token
- TokenType
- Symbol
- SymbolTable

---

## TP2 — Scanner (Análise Léxica)

Foi desenvolvido um Scanner manual responsável por converter o código-fonte em uma sequência de tokens.

O Scanner reconhece:

- palavras reservadas;
- identificadores;
- números;
- operadores;
- delimitadores;
- operador de atribuição;
- fim do arquivo (EOF).

Também foi criada uma exceção léxica para identificar caracteres inválidos.

---

## TP3 — Parser (Análise Sintática)

Nesta etapa foi implementado um Parser Recursivo Descendente.

O Parser é responsável por:

- validar a estrutura sintática;
- respeitar precedência entre operadores;
- construir a AST.

Exemplo:

```pascal
x := 10 + 5 * 2;
```

A multiplicação é processada antes da soma, respeitando a precedência dos operadores.

---

## TP4 — Parser com ANTLR

O Parser manual foi substituído pelo **ANTLR4**.

Foi criada uma gramática (`MiniPascal.g4`) responsável por definir toda a linguagem.

A partir dessa gramática o ANTLR gera automaticamente:

- Lexer;
- Parser;
- Visitor.

Também foi implementada a classe **MyVisitor**, responsável por converter a Parse Tree gerada pelo ANTLR em uma Árvore de Sintaxe Abstrata (AST).

Nesta etapa também foram adicionados novos comandos da linguagem, como:

- if
- while
- funções
- chamadas de função
- print
- return

---

## TP5 — Análise Semântica

Foi desenvolvido um analisador semântico responsável por verificar regras da linguagem que não podem ser detectadas apenas pela sintaxe.

Entre as verificações implementadas estão:

- variável não declarada;
- variável declarada mais de uma vez;
- função declarada mais de uma vez;
- controle de escopos;
- utilização correta da tabela de símbolos.

Para isso foi implementada uma pilha de tabelas de símbolos para representar os diferentes escopos do programa.

---

## TP6 — Verificação de Tipos

Foi implementado um verificador de tipos responsável por garantir a compatibilidade entre expressões.

As principais verificações realizadas são:

- operações aritméticas entre inteiros;
- operações booleanas;
- operações relacionais;
- atribuições compatíveis;
- validação do tipo das condições dos comandos `if`, `while` e `repeat`;
- validação dos tipos de retorno das funções.

Sempre que uma inconsistência é encontrada, uma mensagem de erro é adicionada ao relatório do compilador.

---

## TP7 — Geração de Código

Na última etapa foi implementado o gerador de código utilizando a biblioteca **ASM**.

A AST é percorrida para produzir bytecode executável pela JVM.

Entre as funcionalidades implementadas estão:

- geração de classes Java (.class);
- geração de métodos;
- geração de variáveis locais;
- operações aritméticas;
- operadores relacionais;
- comandos de atribuição;
- impressão de valores;
- comandos condicionais (`if`);
- laços de repetição (`while` e `repeat`);
- funções;
- chamadas de função;
- retorno de funções.

Ao final da compilação é produzido um arquivo `.class`, que pode ser executado diretamente pela Máquina Virtual Java.

---

# ✔ Funcionalidades

- Processamento de texto
- Construção da AST
- Scanner manual
- Parser Recursivo Descendente
- Parser utilizando ANTLR4
- Visitor
- Análise Semântica
- Controle de Escopos
- Tabela de Símbolos
- Verificação de Tipos
- Geração de Bytecode JVM
- Execução de programas MiniPascal

---

# 🧪 Testes

O projeto possui testes automatizados utilizando **JUnit 5**.

Os testes abrangem:

- TP0
- TP1
- TP2
- TP3
- TP4
- TP5
- TP6

Executar todos os testes:

```bash
mvn test
```

---

# ▶ Como Executar

### Compilar o projeto

```bash
mvn clean compile
```

### Executar os testes

```bash
mvn test
```

### Gerar o bytecode

```bash
mvn exec:java -Dexec.mainClass="br.com.comcet.tp7.MainCodeGenerator" -Dexec.args="caso_teste/fatorial.pas"
```

Após a geração do bytecode será criado um arquivo `.class`.

Para executá-lo:

```bash
java fatorial
```

---

# 📄 Exemplo

Arquivo MiniPascal:

```pascal
var n : integer;
var fat : integer;

begin
    n := 5;
    fat := 1;

    while n > 1 do
    begin
        fat := fat * n;
        n := n - 1;
    end

    writeln(fat);
end
```

---

# 📈 Evolução do Projeto

| Etapa | Descrição |
|--------|-----------|
| TP0 | Processamento de Texto |
| TP1 | AST e Tabela de Símbolos |
| TP2 | Scanner |
| TP3 | Parser |
| TP4 | ANTLR + Visitor |
| TP5 | Análise Semântica |
| TP6 | Verificação de Tipos |
| TP7 | Geração de Bytecode JVM |

---

# 👨‍💻 Autor

**Luiz Henrique Santos Dias**

Projeto desenvolvido para a disciplina de **Compiladores**, implementando todas as etapas de construção de um compilador para MiniPascal, desde a análise léxica até a geração de código para a Máquina Virtual Java.
