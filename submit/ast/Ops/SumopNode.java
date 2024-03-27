package submit.ast.Ops;

import submit.ast.Node;

public class SumopNode implements Node {
    String op;
    public SumopNode(String op){
        this.op = op;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(op).append(" ");
    }
}
