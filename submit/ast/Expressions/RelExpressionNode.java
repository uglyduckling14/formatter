package submit.ast.Expressions;

import submit.ast.BinaryOperator;
import submit.ast.Expression;
import submit.ast.Node;

import java.util.List;

public class RelExpressionNode implements Node, Expression {
    List<Expression> sumExpressions;
    List<String> relops;
    public RelExpressionNode(List<Expression> sumExpressions, List<String> relops){
        this.relops = relops;
        this.sumExpressions = sumExpressions;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if(relops.size()==0&&sumExpressions.get(0)!= null){
            sumExpressions.get(0).toCminus(builder, prefix);
        } else {
            BinaryOperator bOp = new BinaryOperator(sumExpressions, relops);
            bOp.toCminus(builder, prefix);
        }
    }
}
