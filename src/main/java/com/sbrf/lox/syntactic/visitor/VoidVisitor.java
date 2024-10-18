package com.sbrf.lox.syntactic.visitor;

import com.sbrf.lox.syntactic.Expr;

public interface VoidVisitor<A> {
    void visit(Expr.Binary expr, A args);

    void visit(Expr.Grouping expr, A args);

    void visit(Expr.Literal expr, A args);

    void visit(Expr.Unary expr, A args);
}
