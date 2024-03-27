package submit.ast.Expressions;

import submit.ast.Node;

import java.util.List;

public class UnaryExpressionNode implements Node {
    List<Node> unaryOps;
    Node factor;
    public UnaryExpressionNode(List<Node> unaryOps, Node factor){
        this.factor = factor;
        this.unaryOps = unaryOps;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        //TODO remove null check
        for (Node op: unaryOps) {
            op.toCminus(builder, prefix);
        }
        if(factor==null){return;}
        factor.toCminus(builder, prefix);
    }
}
