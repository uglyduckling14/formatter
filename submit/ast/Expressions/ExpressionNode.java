package submit.ast.Expressions;

import submit.ast.Expression;
import submit.ast.Node;


public class ExpressionNode implements Node, Expression {
    Expression expression;
    Node mutable;
    String op;
    public ExpressionNode(Node mutable, Expression expression, String op){
        this.expression = expression;
        this.mutable = mutable;
        this.op = op;
    }
    public ExpressionNode(Node mutable, String op){
        this.mutable = mutable;
        this.op = op;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        mutable.toCminus(builder, prefix);
        if(expression== null){builder.append(op); return;}
        builder.append(" ").append(op);
        builder.append(" ");
        expression.toCminus(builder, prefix);
    }
}
