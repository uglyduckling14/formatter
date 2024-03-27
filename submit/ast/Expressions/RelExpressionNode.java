package submit.ast.Expressions;

import submit.ast.Expression;
import submit.ast.Node;

import java.util.List;

public class RelExpressionNode implements Node, Expression {
    List<Node> sumExpressions;
    List<Node> relops;
    public RelExpressionNode(List<Node> sumExpressions, List<Node> relops){
        this.relops = relops;
        this.sumExpressions = sumExpressions;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        //TODO fix null things
        if(relops.size()==0&&sumExpressions.get(0)!= null){
            sumExpressions.get(0).toCminus(builder, prefix);
        } else {
            for (int i = 0; i < sumExpressions.size(); i++) {
                if(sumExpressions.get(i)!=null) {
                    sumExpressions.get(i).toCminus(builder, prefix);
                    if (i < relops.size()) {
                        relops.get(i).toCminus(builder, prefix);
                    }
                }
            }
        }
    }
}
