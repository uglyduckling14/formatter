package submit.ast.Ops;

import submit.ast.Node;

public class RelopNode implements Node {
    String lit;
    public RelopNode(String lit){
        this.lit = lit;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(lit).append(" ");
    }
}
