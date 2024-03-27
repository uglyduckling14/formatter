package submit.ast.Expressions;

import submit.ast.Expression;
import submit.ast.Node;

import java.util.List;

public class OrExpressionNode implements Node, Expression {
    List<Node> ands;
    public OrExpressionNode(List<Node> ands){
        this.ands = ands;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if(ands.contains(null)){return;}
        if(ands.size()==1){
            ands.get(0).toCminus(builder, prefix);
        } else {
            for (int i = 0; i < ands.size(); i++) {
                ands.get(i).toCminus(builder, prefix);
                if (i < ands.size() - 1) {
                    builder.append(" || ");
                }
            }
        }
    }
}
