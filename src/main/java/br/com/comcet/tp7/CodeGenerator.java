package br.com.comcet.tp7;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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

// Responsável por percorrer a AST e gerar o bytecode da JVM utilizando a biblioteca ASM.
// Cada nó da árvore é traduzido para uma ou mais instruções da máquina virtual.
public class CodeGenerator implements Opcodes {

        // Cria a estrutura da classe .class que será gerada.
        private ClassWriter classWriter;

        // Escreve as instruções de cada método (main e funções).
        private MethodVisitor methodVisitor;

        // Associa cada variável ao índice utilizado pela JVM.
        private final Map<String, Integer> variables = new HashMap<>();

        // No método main o índice 0 pertence ao parâmetro String[] args.
        // Por isso as variáveis locais começam no índice 1.
        private int nextLocal = 1;

        // Cria toda a estrutura da classe JVM e inicia a geração
        // do método main. Depois percorre a AST para gerar
        // todas as instruções correspondentes ao programa Pascal.
        public void generate(
                        Program program,
                        String className) throws IOException {

                classWriter = new ClassWriter(
                                ClassWriter.COMPUTE_FRAMES);

                // Define as informações básicas da classe que será criada.
                classWriter.visit(
                                V17,
                                ACC_PUBLIC,
                                className,
                                null,
                                "java/lang/Object",
                                null);

                methodVisitor = classWriter.visitMethod(
                                ACC_PUBLIC | ACC_STATIC,
                                "main",
                                "([Ljava/lang/String;)V",
                                null,
                                null);

                methodVisitor.visitCode();

                // Percorre toda a AST gerando o bytecode correspondente.
                visit(program);

                methodVisitor.visitInsn(RETURN);

                methodVisitor.visitMaxs(0, 0);

                methodVisitor.visitEnd();

                classWriter.visitEnd();

                byte[] bytes = classWriter.toByteArray();

                // Escreve o bytecode gerado no arquivo .class.
                try (FileOutputStream fos = new FileOutputStream(
                                className + ".class")) {

                        fos.write(bytes);
                }
        }

        // Percorre a AST identificando o tipo de cada nó.
        // Para cada tipo existe um método responsável por gerar
        // o bytecode correspondente.
        private void visit(AstNode node) {

                if (node instanceof Program program) {

                        for (AstNode child : program.children()) {

                                visit(child);
                        }

                } else if (node instanceof VarDeclaration decl) {

                        visitVarDeclaration(decl);

                } else if (node instanceof AssignmentCommand cmd) {

                        visitAssignment(cmd);

                } else if (node instanceof BinaryExpression expr) {

                        visitBinaryExpression(expr);

                } else if (node instanceof Literal lit) {

                        visitLiteral(lit);

                } else if (node instanceof Identifier id) {

                        visitIdentifier(id);

                } else if (node instanceof FunctionDeclaration func) {

                        visitFunction(func);

                } else if (node instanceof FunctionCallExpression callExpr) {

                        visitFunctionCallExpression(callExpr);

                } else if (node instanceof FunctionCall call) {

                        visitFunctionCall(call);

                }

                else if (node instanceof PrintCommand print) {

                        visitPrint(print);

                }

                else if (node instanceof IfCommand ifCmd) {

                        visitIf(ifCmd);

                } 
                
                else if(node instanceof RepeatCommand repeat){
                        visitRepeat(repeat);
                }
                
                else if (node instanceof WhileCommand whileCmd) {

                        visitWhile(whileCmd);

                } else if (node instanceof ReturnCommand ret) {

                        visitReturn(ret);
                }
        }

        // Reserva um índice da JVM para a variável declarada.
        // Esse índice será utilizado posteriormente por ILOAD e ISTORE.
        private void visitVarDeclaration(
                        VarDeclaration decl) {

                variables.put(
                                decl.name(),
                                nextLocal++);

        }

        private void visitAssignment(
                        AssignmentCommand cmd) {

                // Primeiro gera o código da expressão.
                // O resultado ficará no topo da pilha da JVM.
                visit(cmd.expr());

                Integer index = variables.get(cmd.id().name());

                if (index != null) {

                        methodVisitor.visitVarInsn(
                                        // Remove o valor da pilha e armazena na variável.
                                        ISTORE,
                                        index);
                }
        }

        // Gera as operações aritméticas e relacionais.
        // Como a JVM é baseada em pilha, primeiro os operandos
        // são empilhados e depois a operação é executada.
        private void visitBinaryExpression(
                        BinaryExpression expr) {

                visit(expr.left());

                visit(expr.right());

                // Operações relacionais são implementadas utilizando
                // Labels e saltos condicionais da JVM.
                switch (expr.operator()) {

                        case "+":
                                methodVisitor.visitInsn(IADD);
                                break;

                        case "-":
                                methodVisitor.visitInsn(ISUB);
                                break;

                        case "*":
                                methodVisitor.visitInsn(IMUL);
                                break;

                        case "/":
                                methodVisitor.visitInsn(IDIV);
                                break;

                        case ">":
                        case "<":
                        case "=":

                                Label trueLabel = new Label();
                                Label endLabel = new Label();

                                switch (expr.operator()) {

                                        case ">":
                                                methodVisitor.visitJumpInsn(
                                                                IF_ICMPGT,
                                                                trueLabel);
                                                break;

                                        case "<":
                                                methodVisitor.visitJumpInsn(
                                                                IF_ICMPLT,
                                                                trueLabel);
                                                break;

                                        case "=":
                                                methodVisitor.visitJumpInsn(
                                                                IF_ICMPEQ,
                                                                trueLabel);
                                                break;
                                }

                                methodVisitor.visitInsn(ICONST_0);

                                methodVisitor.visitJumpInsn(
                                                GOTO,
                                                endLabel);

                                methodVisitor.visitLabel(trueLabel);

                                methodVisitor.visitInsn(ICONST_1);

                                methodVisitor.visitLabel(endLabel);

                                break;

                        default:
                                throw new IllegalArgumentException(
                                                "Operador não suportado: "
                                                                + expr.operator());
                }
        }

        // Empilha todos os argumentos da função e gera
        // uma chamada INVOKESTATIC para o método correspondente.
        private void visitFunctionCallExpression(
                        FunctionCallExpression call) {

                StringBuilder descriptor = new StringBuilder("(");

                for (Expression arg : call.arguments()) {

                        visit(arg);

                        descriptor.append("I");
                }

                descriptor.append(")I");

                methodVisitor.visitMethodInsn(
                                INVOKESTATIC,
                                "Program",
                                call.name(),
                                descriptor.toString(),
                                false);
        }

        // Coloca um valor constante diretamente na pilha da JVM.
        private void visitLiteral(
                        Literal lit) {

                Object value = lit.value();

                if (value instanceof Integer i) {

                        methodVisitor.visitLdcInsn(i);

                } else if (value instanceof Boolean b) {

                        methodVisitor.visitLdcInsn(b ? 1 : 0);

                } else if (value instanceof String s) {

                        methodVisitor.visitLdcInsn(s);
                }
        }

        // Carrega o valor da variável para o topo da pilha.
        private void visitIdentifier(
                        Identifier id) {

                Integer index = variables.get(id.name());

                if (index != null) {

                        methodVisitor.visitVarInsn(
                                        ILOAD,
                                        index);
                }
        }

        // Gera um novo método da JVM para representar
        // uma função declarada em MiniPascal.
        private void visitFunction(
                        FunctionDeclaration func) {

                MethodVisitor oldVisitor = methodVisitor;

                Map<String, Integer> oldVariables = new HashMap<>(variables);

                int oldNextLocal = nextLocal;

                StringBuilder descriptor = new StringBuilder("(");

                for (int i = 0; i < func.parameters().size(); i++) {

                        descriptor.append("I");
                }

                descriptor.append(")I");

                MethodVisitor mv = classWriter.visitMethod(
                                ACC_PUBLIC | ACC_STATIC,
                                func.name(),
                                descriptor.toString(),
                                null,
                                null);

                methodVisitor = mv;

                // Cada função possui sua própria tabela de variáveis locais.
                variables.clear();

                nextLocal = 0;

                // Os parâmetros ocupam os primeiros índices da JVM.
                for (Parameter p : func.parameters()) {

                        variables.put(
                                        p.name(),
                                        nextLocal++);
                }

                mv.visitCode();

                visit(func.block());

                mv.visitMaxs(0, 0);

                mv.visitEnd();

                variables.clear();

                variables.putAll(oldVariables);

                nextLocal = oldNextLocal;

                methodVisitor = oldVisitor;
        }

        private void visitFunctionCall(
                        FunctionCall call) {

                // Bloco 8
        }

        // Utiliza um Label para marcar o final do bloco.
        // Caso a condição seja falsa, a execução salta
        // diretamente para esse ponto.
        private void visitIf(
                        IfCommand cmd) {

                Label endIf = new Label();

                visit(cmd.condition());

                methodVisitor.visitJumpInsn(
                                IFEQ,
                                endIf);

                visit(cmd.block());

                methodVisitor.visitLabel(endIf);
        }

        private void visitRepeat(RepeatCommand cmd){

                Label inicio = new Label();

                methodVisitor.visitLabel(inicio);
                
                visit(cmd.block());

                visit(cmd.condition());

                methodVisitor.visitJumpInsn(IFEQ, inicio);
        }

        // O laço é implementado utilizando dois Labels:
        // um marca o início da repetição e o outro o final.
        // Enquanto a condição for verdadeira, a execução
        // retorna para o início do laço.
        private void visitWhile(
                        WhileCommand cmd) {

                Label startWhile = new Label();

                Label endWhile = new Label();

                methodVisitor.visitLabel(startWhile);

                visit(cmd.condition());

                methodVisitor.visitJumpInsn(
                                IFEQ,
                                endWhile);

                visit(cmd.block());

                methodVisitor.visitJumpInsn(
                                GOTO,
                                startWhile);

                methodVisitor.visitLabel(endWhile);
        }

        // Implementa o comando writeln utilizando
        // System.out.println da biblioteca padrão do Java.
        private void visitPrint(
                        PrintCommand cmd) {

                methodVisitor.visitFieldInsn(
                                // Obtém o objeto System.out.
                                GETSTATIC,
                                "java/lang/System",
                                "out",
                                "Ljava/io/PrintStream;");

                // Coloca o valor que será impresso na pilha.
                visit(cmd.expression());

                methodVisitor.visitMethodInsn(
                                INVOKEVIRTUAL,
                                "java/io/PrintStream",
                                // Chama o método println(int).
                                "println",
                                "(I)V",
                                false);
        }

        // Gera o retorno da função.
        // O valor da expressão é colocado na pilha
        // e devolvido utilizando IRETURN.
        private void visitReturn(
                        ReturnCommand cmd) {

                visit(cmd.expression());

                methodVisitor.visitInsn(
                                IRETURN);
        }
}