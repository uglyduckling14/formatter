package submit;

import parser.CminusBaseVisitor;
import parser.CminusParser;
import submit.ast.*;
import submit.ast.Expressions.*;
import submit.ast.Ops.MulopNode;
import submit.ast.Ops.RelopNode;
import submit.ast.Ops.SumopNode;
import submit.ast.Ops.UnaryopNode;
import submit.ast.Statements.CompoundStatementNode;
import submit.ast.Statements.ExpressionStatementNode;
import submit.ast.Statements.IfStatementNode;
import submit.ast.Statements.WhileStatementNode;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ASTVisitor extends CminusBaseVisitor<Node> {
    private final Logger LOGGER;
    private SymbolTable symbolTable;

    public ASTVisitor(Logger LOGGER) {
        this.LOGGER = LOGGER;
    }

    private VarType getVarType(CminusParser.TypeSpecifierContext ctx) {
        if(ctx == null){return null;}
        final String t = ctx.getText();
        return (t.equals("int")) ? VarType.INT : (t.equals("bool")) ? VarType.BOOL : VarType.CHAR;
    }

    @Override public Node visitProgram(CminusParser.ProgramContext ctx) {
        symbolTable = new SymbolTable();
        List<Declaration> decls = new ArrayList<>();
        for (CminusParser.DeclarationContext d : ctx.declaration()) {
            decls.add((Declaration) visitDeclaration(d));
        }
        return new Program(decls);
    }

    @Override public Node visitVarDeclaration(CminusParser.VarDeclarationContext ctx) {
        for (CminusParser.VarDeclIdContext v : ctx.varDeclId()) {
            String id = v.ID().getText();
            LOGGER.fine("Var ID: " + id);
        }

        VarType type = getVarType(ctx.typeSpecifier());
        List<String> ids = new ArrayList<>();
        List<Integer> arraySizes = new ArrayList<>();
        for (CminusParser.VarDeclIdContext v : ctx.varDeclId()) {
            String id = v.ID().getText();
            ids.add(id);
            symbolTable.addSymbol(id, new SymbolInfo(id, type, false));
            if (v.NUMCONST() != null) {
                arraySizes.add(Integer.parseInt(v.NUMCONST().getText()));
            } else {
                arraySizes.add(-1);
            }
        }
        final boolean isStatic = false;
        return new VarDeclaration(type, ids, arraySizes, isStatic);
    }

    @Override public Statement visitReturnStmt(CminusParser.ReturnStmtContext ctx) {
        if (ctx.expression() != null) {
            return new Return((Expression) visitExpression(ctx.expression()));
        }
        return new Return(null);
    }

    @Override public Node visitConstant(CminusParser.ConstantContext ctx) {
        final Node node;
        if (ctx.NUMCONST() != null) {
            node = new NumConstant(Integer.parseInt(ctx.NUMCONST().getText()));
        } else if (ctx.CHARCONST() != null) {
            node = new CharConstant(ctx.CHARCONST().getText().charAt(0));
        } else if (ctx.STRINGCONST() != null) {
            node = new StringConstant(ctx.STRINGCONST().getText());
        } else {
            node = new BoolConstant(ctx.getText().equals("true"));
        }
        return node;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitDeclaration(CminusParser.DeclarationContext ctx) {
        if(ctx.varDeclaration() != null){
            CminusParser.VarDeclarationContext varDec = ctx.varDeclaration();
            return visitVarDeclaration(varDec);
        } else if (ctx.funDeclaration() != null) {
            CminusParser.FunDeclarationContext funDec = ctx.funDeclaration();
            return visitFunDeclaration(funDec);
        }
        return visitChildren(ctx);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitVarDeclId(CminusParser.VarDeclIdContext ctx) {
        if(ctx.ID() != null){
            return (Node) ctx.ID();
        }
        return new NumConstant(Integer.parseInt(ctx.NUMCONST().getText()));
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitFunDeclaration(CminusParser.FunDeclarationContext ctx) {
        VarType type = getVarType(ctx.typeSpecifier());
        String functionName = ctx.ID().getText();
        symbolTable.addSymbol(functionName, new SymbolInfo(functionName, type, true));
        List<Node> params = new ArrayList<>();
        SymbolTable child = symbolTable.createChild();
        if(!ctx.param().isEmpty()){
            for (CminusParser.ParamContext p : ctx.param()) {
                if (p != null) {
                    child.addSymbol(p.paramId().ID().getText(), new SymbolInfo(p.paramId().ID().getText(), type, false));
                    params.add(visitParam(p));
                }
            }
        }
        symbolTable = child;
        Statement body = visitStatement(ctx.statement());
        symbolTable =  symbolTable.getParent();
        return new FunDeclaration(type, functionName, params, body);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public TypeSpecifierNode visitTypeSpecifier(CminusParser.TypeSpecifierContext ctx) {
        String typeSpecifierText = ctx.getText(); // Get the text of the type specifier

        // Create a VarType enum based on the type specifier text
        VarType varType = switch (typeSpecifierText) {
            case "int" -> VarType.INT;
            case "bool" -> VarType.BOOL;
            case "char" -> VarType.CHAR;
            default ->
                // Handle unknown type specifier
                    throw new IllegalArgumentException("Unknown type specifier: " + typeSpecifierText);
        };

        // Return the VarType enum representing the type specifier
        return new TypeSpecifierNode(varType);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public ParamNode visitParam(CminusParser.ParamContext ctx) {
        VarType type = getVarType(ctx.typeSpecifier());
        ParamIdNode paramId = visitParamId(ctx.paramId());
        return new ParamNode(type, paramId);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public ParamIdNode visitParamId(CminusParser.ParamIdContext ctx) {
        return new ParamIdNode(ctx.getText());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Statement visitStatement(CminusParser.StatementContext ctx) {
        if (ctx.expressionStmt() != null) {
            return visitExpressionStmt(ctx.expressionStmt());
        } else if (ctx.compoundStmt() != null) {
            return visitCompoundStmt(ctx.compoundStmt());
        } else if (ctx.ifStmt() != null) {
            return visitIfStmt(ctx.ifStmt());
        } else if (ctx.whileStmt() != null) {
            return visitWhileStmt(ctx.whileStmt());
        } else if (ctx.returnStmt() != null) {
            return visitReturnStmt(ctx.returnStmt());
        } else if (ctx.breakStmt() != null) {
            return visitBreakStmt(ctx.breakStmt());
        }
        return (Statement)visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Statement visitCompoundStmt(CminusParser.CompoundStmtContext ctx) {
        List<Node> varNodes = new ArrayList<>();
        List<Node> staNodes = new ArrayList<>();
        for(CminusParser.VarDeclarationContext varDeclarationContext : ctx.varDeclaration()){
            varNodes.add(visitVarDeclaration(varDeclarationContext));
        }
        for (CminusParser.StatementContext statementContext : ctx.statement()) {
            staNodes.add(visitStatement(statementContext));
        }
        return new CompoundStatementNode(
                varNodes,
                staNodes);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Statement visitExpressionStmt(CminusParser.ExpressionStmtContext ctx) {
        return new ExpressionStatementNode(visitExpression(ctx.expression()));
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Statement visitIfStmt(CminusParser.IfStmtContext ctx) {
        List<Statement> statements = new ArrayList<>();
        for (CminusParser.StatementContext c: ctx.statement()) {
            statements.add(visitStatement(c));
        }
        return new IfStatementNode(visitSimpleExpression(ctx.simpleExpression()),statements);
    }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
    @Override public Statement visitWhileStmt(CminusParser.WhileStmtContext ctx) {
        return new WhileStatementNode(visitSimpleExpression(ctx.simpleExpression()), visitStatement(ctx.statement()));
    }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
    @Override public Statement visitBreakStmt(CminusParser.BreakStmtContext ctx) {
        return new Break();
    }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
    @Override public Node visitExpression(CminusParser.ExpressionContext ctx) {
        if(ctx ==null){return null;}
        if(ctx.mutable() != null && ctx.getChildCount()>2){// mutable & expression & op
            return new ExpressionNode(
                    visitMutable(ctx.mutable()),
                    (Expression) visitExpression(ctx.expression()),
                    ctx.getChild(1).getText()
            );
        } else if(ctx.mutable() != null){
            return new ExpressionNode(visitMutable(ctx.mutable()), ctx.getChild(1).getText());
        }
        else{
            return visitSimpleExpression(ctx.simpleExpression());
        }
    }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
    @Override public Node visitSimpleExpression(CminusParser.SimpleExpressionContext ctx) {
        return visitOrExpression(ctx.orExpression());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Expression visitOrExpression(CminusParser.OrExpressionContext ctx) {
        List<Node> ands = new ArrayList<>();
        for (CminusParser.AndExpressionContext and:ctx.andExpression()) {
            ands.add(visitAndExpression(and));
        }
        return new OrExpressionNode(ands);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitAndExpression(CminusParser.AndExpressionContext ctx) {
        List<Node> unRels = new ArrayList<>();
        for (CminusParser.UnaryRelExpressionContext un:ctx.unaryRelExpression()) {
            unRels.add(visitUnaryRelExpression(un));
        }
        return new AndExpressionNode(unRels);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitUnaryRelExpression(CminusParser.UnaryRelExpressionContext ctx) {
        return new UnaryRelExpressionNode(ctx.BANG(), visitRelExpression(ctx.relExpression()));
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitRelExpression(CminusParser.RelExpressionContext ctx) {
        List<Node> sumExpressions = new ArrayList<>();
        List<Node> relops = new ArrayList<>();
        for (CminusParser.SumExpressionContext exp: ctx.sumExpression()) {
            sumExpressions.add(visitSumExpression(exp));
        }
        for (CminusParser.RelopContext relop: ctx.relop()) {
            relops.add(visitRelop(relop));
        }
        return new RelExpressionNode(sumExpressions, relops);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitRelop(CminusParser.RelopContext ctx) {
        return new RelopNode(ctx.getText());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitSumExpression(CminusParser.SumExpressionContext ctx) {
        List<Node> termExpressions = new ArrayList<>();
        List<Node> sumops = new ArrayList<>();
        for (CminusParser.TermExpressionContext exp: ctx.termExpression()) {
            termExpressions.add(visitTermExpression(exp));
        }
        for (CminusParser.SumopContext sumop: ctx.sumop()) {
            sumops.add(visitSumop(sumop));
        }
        return new SumExpressionNode(termExpressions, sumops);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitSumop(CminusParser.SumopContext ctx) {
        return new SumopNode(ctx.getText());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitTermExpression(CminusParser.TermExpressionContext ctx) {
        List<Node> unaryExpressions = new ArrayList<>();
        List<Node> mulops = new ArrayList<>();
        for (CminusParser.UnaryExpressionContext exp: ctx.unaryExpression()) {
            unaryExpressions.add(visitUnaryExpression(exp));
        }
        for (CminusParser.MulopContext mulop: ctx.mulop()) {
            mulops.add(visitMulop(mulop));
        }
        return new TermExpressionNode(unaryExpressions, mulops);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitMulop(CminusParser.MulopContext ctx) {
        return new MulopNode(ctx.getText());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitUnaryExpression(CminusParser.UnaryExpressionContext ctx) {
        List<Node> unop = new ArrayList<>();
        for (CminusParser.UnaryopContext un:ctx.unaryop()) {
            unop.add(visitUnaryop(un));
        }
        return new UnaryExpressionNode(unop, visitFactor(ctx.factor()));
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitUnaryop(CminusParser.UnaryopContext ctx) {
        return new UnaryopNode(ctx.getText());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitFactor(CminusParser.FactorContext ctx) {
        if(ctx.mutable() != null){
            return visitMutable(ctx.mutable());
        }
        return visitImmutable(ctx.immutable());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitMutable(CminusParser.MutableContext ctx) {
        if(symbolTable.find(ctx.ID().getText()) == null){
            LOGGER.warning("Undefined symbol on line "+ctx.getStart().getLine()+": "+ctx.ID().getText());
        }
        return new Mutable(ctx.ID().getText(), (Expression)visitExpression(ctx.expression()));
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitImmutable(CminusParser.ImmutableContext ctx) {
        if(ctx.expression() != null){
            return new Immutable(visitExpression(ctx.expression()));
        }
        if(ctx.call() != null){
            return visitCall(ctx.call());
        }
        if(ctx.constant() != null){
            return visitConstant(ctx.constant());
        }
        return visitChildren(ctx);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public Node visitCall(CminusParser.CallContext ctx) {
        List<Expression> exps = new ArrayList<>();
        for (CminusParser.ExpressionContext exp:ctx.expression()) {
            exps.add((Expression)visitExpression(exp));
        }
        if(symbolTable.find(ctx.ID().getText()) == null){
            LOGGER.warning("Undefined symbol on line "+ctx.getStart().getLine()+": "+ctx.ID().getText());
        }
        return new Call(exps, ctx.ID().getText());
    }

}
