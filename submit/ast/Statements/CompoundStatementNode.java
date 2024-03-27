package submit.ast.Statements;

import submit.ast.Node;
import submit.ast.Statement;

import java.util.List;

public class CompoundStatementNode implements Node, Statement {
    List<Node> varDeclarations;
    List<Node> statements;
    public CompoundStatementNode(List<Node> varDeclarations, List<Node> statements) {
        this.varDeclarations = varDeclarations;
        this.statements = statements;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append("{ \n");
        for (Node varDec: varDeclarations) {
            varDec.toCminus(builder, prefix);
        }
        for (Node stat: statements) {
            stat.toCminus(builder, prefix);
        }
        builder.append("}\n");
    }
}
