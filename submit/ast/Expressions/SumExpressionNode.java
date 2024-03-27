package submit.ast.Expressions;

import submit.ast.Expression;
import submit.ast.Node;

import java.util.List;

public class SumExpressionNode implements Node, Expression {
    List<Node> termExpressions;
    List<Node> sumops;
    public SumExpressionNode(List<Node> termExpressions, List<Node> sumops){
        this.sumops = sumops;
        this.termExpressions = termExpressions;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if(sumops.size()==0&&termExpressions.get(0)!= null){
            termExpressions.get(0).toCminus(builder, prefix);
        } else {
            for (int i = 0; i < termExpressions.size(); i++) {
                if(termExpressions.get(i)!=null) {
                    termExpressions.get(i).toCminus(builder, prefix);
                    if (i < sumops.size() && sumops.get(i)!=null) {
                        sumops.get(i).toCminus(builder, prefix);
                    }
                }
            }
        }
    }
}
