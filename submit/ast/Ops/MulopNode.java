package submit.ast.Ops;

import submit.ast.Node;

public class MulopNode implements Node {
    String op;
    public MulopNode(String op){
        this.op = op;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(op).append(" ");
    }
}
