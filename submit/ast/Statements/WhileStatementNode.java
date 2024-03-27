package submit.ast.Statements;

import submit.ast.Node;
import submit.ast.Statement;

public class WhileStatementNode implements Statement, Node {
    Node simpleExp;
    Statement statement;
    public WhileStatementNode(Node simpleExp, Statement statement){
        this.simpleExp = simpleExp;
        this.statement = statement;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append("while (");
        simpleExp.toCminus(builder, "");
        builder.append(")");
        statement.toCminus(builder, "");
    }
}
