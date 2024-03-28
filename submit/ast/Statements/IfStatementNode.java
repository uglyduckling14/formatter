package submit.ast.Statements;

import submit.ast.Node;
import submit.ast.Statement;

import java.util.List;

public class IfStatementNode implements Statement {
    private Node simpleExpression;
    private List<Statement> statements;
    public IfStatementNode(Node simpleExpression, List<Statement> statements){
        this.simpleExpression = simpleExpression;
        this.statements = statements;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);
        builder.append("if (");
        simpleExpression.toCminus(builder, "");
        builder.append(")\n");
        statements.get(0).toCminus(builder, " "+prefix);
        if(statements.size() > 1){
            builder.append(prefix).append("else\n");
            statements.get(1).toCminus(builder, " "+prefix);
        }
    }
}
