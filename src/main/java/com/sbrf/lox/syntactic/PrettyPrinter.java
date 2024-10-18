package com.sbrf.lox.syntactic;

import com.sbrf.lox.syntactic.visitor.GenericVisitor;

import java.util.Optional;

public class PrettyPrinter implements GenericVisitor<String, String> {

    public String print(Expr expr, Object args) {
        return expr.accept(this, null);
    }

    @Override
    public String visit(Expr.Binary expr, String args) {
        return parentheses(expr.getOperator().getLexeme(), expr.getLeft(), expr.getRight());
    }

    @Override
    public String visit(Expr.Grouping expr, String args) {
        return parentheses("group", expr.getExpr());
    }

    @Override
    public String visit(Expr.Literal expr, String args) {
        return Optional.ofNullable(expr).map(ex -> ex.getValue().toString()).orElse("null");
    }

    @Override
    public String visit(Expr.Unary expr, String args) {
        return parentheses(expr.getOperator().getLexeme(), expr.getRight());
    }

    private String parentheses(String name, Expr ...exprArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(name);
        for (Expr expr : exprArr) {
            sb.append(" ").append(expr.accept(this, null));
        }
        sb.append(")");
        return sb.toString();
    }
}
