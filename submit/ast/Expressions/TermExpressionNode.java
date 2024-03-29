package submit.ast.Expressions;

import submit.ast.BinaryOperator;
import submit.ast.Expression;
import submit.ast.Node;

import java.util.List;

public class TermExpressionNode implements Node, Expression {
    List<Expression> termExpressions;
    List<String> mulops;
    public TermExpressionNode(List<Expression> termExpressions, List<String> mulops){
        this.mulops = mulops;
        this.termExpressions = termExpressions;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if(mulops.size()==0&&termExpressions.get(0)!= null){
            termExpressions.get(0).toCminus(builder, prefix);
        } else {
            BinaryOperator bOp = new BinaryOperator(termExpressions, mulops);
            bOp.toCminus(builder, prefix);
        }
    }
}
