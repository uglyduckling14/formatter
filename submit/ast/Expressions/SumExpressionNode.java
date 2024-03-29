package submit.ast.Expressions;

import submit.ast.BinaryOperator;
import submit.ast.Expression;
import submit.ast.Node;

import java.util.List;

public class SumExpressionNode implements Node, Expression {
    List<Expression> termExpressions;
    List<String> sumops;
    public SumExpressionNode(List<Expression> termExpressions, List<String> sumops){
        this.sumops = sumops;
        this.termExpressions = termExpressions;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if(sumops.size()==0&&termExpressions.get(0)!= null){
            termExpressions.get(0).toCminus(builder, prefix);
        } else {
            BinaryOperator bOp = new BinaryOperator(termExpressions, sumops);
            bOp.toCminus(builder, prefix);
        }
    }
}
