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
        builder.append(prefix).append("{ \n");
        for (Node varDec: varDeclarations) {
            varDec.toCminus(builder, prefix+"\t");
        }
        for (Node stat: statements) {
            if(stat == null){return;}
            stat.toCminus(builder, prefix+"\t");
        }
        builder.append(prefix).append("}\n");
    }
}
