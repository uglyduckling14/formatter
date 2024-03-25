package submit.ast;

public class TypeSpecifierNode implements Node{

    VarType varType;
    public TypeSpecifierNode(VarType varType){
       this.varType = varType;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(prefix);
        builder.append(varType);
    }
}
