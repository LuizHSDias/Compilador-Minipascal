package br.com.comcet.tp1.ast;

public class RepeatCommand extends Command {

    private final Program block;
    private final Expression condition; 

    public RepeatCommand (Program block, Expression condition)
    {
        this.block = block;
        this.condition = condition; 
    }

    public Program block(){
        return block;
    }

    public Expression condition(){
        return condition;
    }

         
    @Override
    protected void printTree(StringBuilder sb, int level) {
        for (int i = 0; i < level; i++){
            sb.append(" ");
        }
        sb.append("RepeatCommand:\n");
        if(block != null){
            block.printTree(sb, level + 1);
        }
        if (condition != null){
            condition.printTree(sb, level + 1);
        }
    }

}