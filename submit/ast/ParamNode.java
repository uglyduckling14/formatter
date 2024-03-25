package submit.ast;

public class ParamNode implements Node{
    VarType type;
    ParamIdNode paramName;

    public ParamNode(VarType type, ParamIdNode paramName){
        this.type = type;
        this.paramName = paramName;
    }

    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);
        builder.append(type).append(" ");
        paramName.toCminus(builder, prefix+ " ");
    }
}
