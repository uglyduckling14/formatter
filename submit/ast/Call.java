package submit.ast;

import java.util.*;

public class Call implements Node{
    List<Expression> expressions;
    String id;

    public Call(List<Expression> expressions, String id){
        this.id = id;
        this.expressions = expressions;
    }
    @Override
    public void toCminus(StringBuilder builder, String prefix) {
        builder.append(id).append("(");
        for(int i = 0; i< expressions.size()-1; i++){
            expressions.get(i).toCminus(builder, prefix);
            builder.append(",");
        }
        expressions.get(expressions.size()-1).toCminus(builder, prefix);
        builder.append(")");
    }
}
