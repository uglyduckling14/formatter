package submit.ast.Expressions;

import submit.ast.Expression;
import submit.ast.Node;

import java.util.List;

public class TermExpressionNode implements Node, Expression {
    List<Node> termExpressions;
    List<Node> mulops;
    public TermExpressionNode(List<Node> termExpressions, List<Node> mulops){
        this.mulops = mulops;
        this.termExpressions = termExpressions;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        //TODO fix null things
        if(mulops.size()==0&&termExpressions.get(0)!= null){
            termExpressions.get(0).toCminus(builder, prefix);
        } else {
            for (int i = 0; i < termExpressions.size(); i++) {
                if(termExpressions.get(i)!=null) {
                    termExpressions.get(i).toCminus(builder, prefix);
                    if (i < mulops.size()&& mulops.get(i)!=null) {
                        mulops.get(i).toCminus(builder, prefix);
                    }
                }
            }
        }
    }
}
