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
        if(varDeclarations.size() ==0 && statements.size()==0){
            builder.append(prefix);
            builder.append("{\n");
            builder.append(prefix).append("}\n");
        }else if(prefix.length()>2) {
            builder.append(prefix, 0, prefix.length()-2);
            builder.append("{\n");
            for (Node varDec: varDeclarations) {
                varDec.toCminus(builder, prefix);
            }
            for (Node stat: statements) {
                if(stat == null){return;}
                stat.toCminus(builder, prefix);
            }
            builder.append(prefix, 0, prefix.length()-2).append("}\n");
        }else{
            builder.append(prefix);
            builder.append("{\n");
            for (Node varDec: varDeclarations) {
                varDec.toCminus(builder, prefix+"  ");
            }
            for (Node stat: statements) {
                if(stat == null){return;}
                stat.toCminus(builder, prefix+"  ");
            }
            builder.append(prefix).append("}\n");
        }

    }
}
