package submit.ast.Expressions;

import submit.ast.Expression;
import submit.ast.Node;

import java.util.List;

public class AndExpressionNode implements Node, Expression {
    List<Node> unRels;
    public AndExpressionNode(List<Node> unRels){
        this.unRels = unRels;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        if(unRels.contains(null)){return;}
        if(unRels.size()==1){
            unRels.get(0).toCminus(builder, prefix);
        } else {
            for (int i = 0; i < unRels.size(); i++) {
                unRels.get(i).toCminus(builder, prefix);
                if (i < unRels.size() - 1) {
                    builder.append(" && ");
                }
            }
        }
    }
}
