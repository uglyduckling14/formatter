package submit.ast.Expressions;

import org.antlr.v4.runtime.tree.TerminalNode;
import submit.ast.Expression;
import submit.ast.Node;

import java.util.List;

public class UnaryRelExpressionNode implements Expression, Node {
    List<TerminalNode> bangs;
    Node relExpression;
    public UnaryRelExpressionNode(List<TerminalNode> bangs, Node relExpression){
        this.relExpression = relExpression;
        this.bangs = bangs;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        for (TerminalNode bang: bangs) {
            builder.append(bang.getText());
        }
        if(relExpression == null){return;}
        relExpression.toCminus(builder, prefix);
    }
}
