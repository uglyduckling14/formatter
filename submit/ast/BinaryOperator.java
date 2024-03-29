/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

import java.util.List;

/**
 *
 * @author edwajohn
 */
public class BinaryOperator implements Expression {

//  private final Expression lhs, rhs;
//  private final BinaryOperatorType type;

//  public BinaryOperator(Expression lhs, BinaryOperatorType type, Expression rhs) {
//    this.lhs = lhs;
//    this.type = type;
//    this.rhs = rhs;
//  }
  private final List<Expression> expressions;
  private final List<String> ops;
  public BinaryOperator(List<Expression> expressions, List<String> ops) {
    this.expressions = expressions;
    this.ops = ops;
  }

  @Override
  public void toCminus(StringBuilder builder, String prefix) {
    for (int i = 0; i < expressions.size(); i++) {
      if(expressions.get(i)!=null) {
        expressions.get(i).toCminus(builder, prefix);
        if(i != expressions.size()-1) {
          builder.append(" ").append(ops.get(i)).append(" ");
        }
      }
    }
  }

}
