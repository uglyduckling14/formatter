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
        builder.append(prefix);
        mutable.toCminus(builder, prefix);
        builder.append(op);
        if(expression== null){return;}
        expression.toCminus(builder, prefix);
    }
}
