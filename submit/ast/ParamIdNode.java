package submit.ast;

public class ParamIdNode implements Node{
    String id;

    public ParamIdNode(String id){
        this.id = id;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);
        builder.append(id);
    }
}
