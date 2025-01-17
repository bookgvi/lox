package com.sbrf.lox.syntactic.visitor;

import com.sbrf.lox.syntactic.Expr;

public interface GenericVisitor<R, A> {
    R visit(Expr.Binary expr, A args);

    R visit(Expr.Grouping expr, A args);

    R visit(Expr.Literal expr, A args);

    R visit(Expr.Unary expr, A args);
}
