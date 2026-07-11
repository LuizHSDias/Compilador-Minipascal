package br.com.comcet.tp7;

import br.com.comcet.tp1.ast.Program;
import br.com.comcet.tp4.MyVisitor;
import br.com.comcet.tp4.parser.MiniPascalLexer;
import br.com.comcet.tp4.parser.MiniPascalParser;
import br.com.comcet.tp5.SemanticAnalyzer;
import br.com.comcet.tp6.TypeChecker;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import java.nio.file.Files;
import java.nio.file.Path;

// PASSO A PASSO PARA EXECUTAR UM ARQUIVO .PAS 

// 1. Criar ou receber um arquivo .pas
// 2. Entre na pasta: cd comcet
// 3. Compilar o arquivo com o seu compilador: mvn exec:java -Dexec.mainClass="br.com.comcet.tp7.MainCodeGenerator" -Dexec.args="caso_teste/teste.pas"
// 4. Se não houver erros, será gerado um arquivo .class
// 5. Executar o arquivo gerado: java teste


/*
 * Classe principal do compilador.
 * É responsável por executar todas as etapas da compilação,
 * desde a leitura do arquivo .pas até a geração do arquivo .class.
 */

public class MainCodeGenerator {

    public static void main(String[] args)
            throws Exception {

        // Verifica se o usuário informou o arquivo Pascal.
        if (args.length != 1) {

            System.out.println(
                    "Uso: java MainCodeGenerator arquivo.pas");

            return;
        }

        // Obtém o caminho do arquivo informado.
        Path arquivo = Path.of(args[0]);

        // O nome da classe gerada será o mesmo do arquivo Pascal.
        String nomeClasse = arquivo.getFileName()
                .toString()
                .replace(".pas", "");

        // Lê todo o conteúdo do arquivo.
        String codigo = Files.readString(arquivo);

        
        // Análise Léxica e Sintática

        // Converte o código fonte em um fluxo de caracteres.
        CharStream input = CharStreams.fromString(codigo);

        // O Lexer identifica os tokens da linguagem.
        MiniPascalLexer lexer =
                new MiniPascalLexer(input);

        // Organiza os tokens para serem utilizados pelo Parser.
        CommonTokenStream tokens =
                new CommonTokenStream(lexer);

        // Organiza os tokens para serem utilizados pelo Parser.
        MiniPascalParser parser =
                new MiniPascalParser(tokens);

        // Gera a árvore sintática.
        ParseTree tree = parser.program();

        // Converte a árvore sintática em uma AST.
        Program ast =
                (Program) new MyVisitor().visit(tree);

        // ANÁLISE SEMÂNTICA. 

        // Verifica regras de escopo e declaração de identificadores.
        SemanticAnalyzer semantic =
                new SemanticAnalyzer();

        semantic.analyze(ast);

        // Caso existam erros semânticos, interrompe a compilação.
        if (semantic.hasErrors()) {

            System.out.println("Erros semânticos:");

            semantic.getErrors()
                    .forEach(System.out::println);

            return;
        }

        // VERIFICAÇÃO DE TIPOS

        // Verifica a compatibilidade entre os tipos das expressões.
        TypeChecker typeChecker =
                new TypeChecker();

        typeChecker.check(ast);

        // Caso existam erros de tipos, interrompe a compilação.
        if (typeChecker.hasErrors()) {

            System.out.println("Erros de tipos:");

            typeChecker.getErrors()
                    .forEach(System.out::println);

            return;
        }

        // GERAÇÃO DE BYTECODE
       

        // Percorre a AST gerando o bytecode da JVM.
        CodeGenerator generator =
                new CodeGenerator();

        generator.generate(
                ast,
                nomeClasse);

        // Informa que o arquivo .class foi gerado com sucesso.
        System.out.println(
                nomeClasse + ".class gerado com sucesso!");
    }
}