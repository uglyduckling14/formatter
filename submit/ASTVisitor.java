package submit;

import parser.CminusBaseVisitor;
import parser.CminusParser;
import submit.ast.*;

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
//        for (CminusParser.VarDeclIdContext v : ctx.varDeclId()) {
//            String id = v.ID().getText();
//            LOGGER.fine("Var ID: " + id);
//        }

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

    @Override public Node visitReturnStmt(CminusParser.ReturnStmtContext ctx) {
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

    // TODO Uncomment and implement whatever methods make sense
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
    @Override public Node visitDeclaration(CminusParser.DeclarationContext ctx) {
        if(ctx.varDeclaration() != null){
            CminusParser.VarDeclarationContext varDec = ctx.varDeclaration();
            LOGGER.fine(""+varDec);
            return visitVarDeclaration(varDec);
        } else if (ctx.funDeclaration() != null) {
            CminusParser.FunDeclarationContext funDec = ctx.funDeclaration();
            LOGGER.fine(""+funDec.getText());
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
//    @Override public T visitVarDeclId(CminusParser.VarDeclIdContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
    @Override public FunDeclaration visitFunDeclaration(CminusParser.FunDeclarationContext ctx) {
        VarType type = getVarType(ctx.typeSpecifier());
        String functionName = ctx.ID().getText();
        symbolTable.addSymbol(functionName, new SymbolInfo(functionName, type, true));
        List<Node> params = new ArrayList<>();
        if(!ctx.param().isEmpty()){
            for (CminusParser.ParamContext p : ctx.param()) {
                if (p != null) {
                    params.add(visitParam(p));
                }
            }
        }
        Node body = visitStatement(ctx.statement());
        return new FunDeclaration(type, functionName, params, body);
    }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
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
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
    @Override public ParamNode visitParam(CminusParser.ParamContext ctx) {
        VarType type = getVarType(ctx.typeSpecifier());
        ParamIdNode paramId = visitParamId(ctx.paramId());
        return new ParamNode(type, paramId);
    }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
    @Override public ParamIdNode visitParamId(CminusParser.ParamIdContext ctx) {
        return new ParamIdNode(ctx.getText());
    }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
    @Override public Node visitStatement(CminusParser.StatementContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitCompoundStmt(CminusParser.CompoundStmtContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitExpressionStmt(CminusParser.ExpressionStmtContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitIfStmt(CminusParser.IfStmtContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitWhileStmt(CminusParser.WhileStmtContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitBreakStmt(CminusParser.BreakStmtContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitExpression(CminusParser.ExpressionContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitSimpleExpression(CminusParser.SimpleExpressionContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitOrExpression(CminusParser.OrExpressionContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitAndExpression(CminusParser.AndExpressionContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitUnaryRelExpression(CminusParser.UnaryRelExpressionContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitRelExpression(CminusParser.RelExpressionContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitRelop(CminusParser.RelopContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitSumExpression(CminusParser.SumExpressionContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitSumop(CminusParser.SumopContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitTermExpression(CminusParser.TermExpressionContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitMulop(CminusParser.MulopContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitUnaryExpression(CminusParser.UnaryExpressionContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitUnaryop(CminusParser.UnaryopContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitFactor(CminusParser.FactorContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitMutable(CminusParser.MutableContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitImmutable(CminusParser.ImmutableContext ctx) { return visitChildren(ctx); }
//    /**
//     * {@inheritDoc}
//     *
//     * <p>The default implementation returns the result of calling
//     * {@link #visitChildren} on {@code ctx}.</p>
//     */
//    @Override public T visitCall(CminusParser.CallContext ctx) { return visitChildren(ctx); }

}
