package submit.ast.Statements;

import submit.ast.Node;
import submit.ast.Statement;

public class ExpressionStatementNode implements Statement {
    Node exp;
    public ExpressionStatementNode(Node exp){
        this.exp = exp;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);
        if(exp != null) {
            exp.toCminus(builder, "");
        }
        builder.append(";\n");
    }
}
