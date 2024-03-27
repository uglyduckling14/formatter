package submit.ast;

public class Immutable implements Node{

    Node expression;
    public Immutable(Node expression){
        this.expression = expression;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append("(");
        expression.toCminus(builder, prefix);
        builder.append(")");
    }
}
