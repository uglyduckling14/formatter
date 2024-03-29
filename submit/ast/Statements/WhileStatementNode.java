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
        if(prefix.length()>4){
            builder.append(prefix, 0, (prefix.length()-2)).append("while (");
            simpleExp.toCminus(builder, "");
            builder.append(")\n");
            statement.toCminus(builder, prefix);
        }else {
            builder.append(prefix).append("while (");
            simpleExp.toCminus(builder, "");
            builder.append(")\n");
            statement.toCminus(builder, prefix+"  ");
        }
    }
}
