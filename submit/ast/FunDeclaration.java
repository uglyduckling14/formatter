package submit.ast;

import java.util.List;

public class FunDeclaration implements Declaration, Node{
    private VarType returnType;
    private String functionName;
    private List<Node> parameters;
    private Node body;

    public FunDeclaration(VarType returnType, String functionName, List<Node> parameters, Node body) {
        this.returnType = returnType;
        this.functionName = functionName;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public void toCminus(StringBuilder builder, final String prefix) {
        builder.append(prefix);
        builder.append(returnType).append(" ");
        builder.append(functionName).append("(");
        if(!parameters.isEmpty()) {
            for (int i = 0; i < parameters.size(); i++) {
                if (i > 0) {
                    builder.append(", ");
                }
                parameters.get(i).toCminus(builder, "");
            }
        }
        builder.append(") {\n");
        if (body != null) {
            body.toCminus(builder, prefix + "  ");
        }
        builder.append(prefix).append("}\n");
    }
}
