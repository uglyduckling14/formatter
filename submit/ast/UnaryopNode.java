package submit.ast;

public class UnaryopNode implements Node {
    String op;
    public UnaryopNode(String op){
        this.op = op;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(op);
    }
}
