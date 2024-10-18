package com.sbrf.lox.syntactic;

import com.sbrf.lox.syntactic.visitor.GenericVisitor;
import com.sbrf.lox.syntactic.visitor.Visitable;
import com.sbrf.lox.syntactic.visitor.VoidVisitor;
import com.sbrf.lox.token.Token;

public abstract class Expr {

    abstract <A> void accept(VoidVisitor<A> v, A arg);

    abstract <R, A> R accept(GenericVisitor<R, A> v, A arg);

    public static class Binary extends Expr implements Visitable {
        private final Expr left;
        private final Expr right;
        private final Token operator;

        public Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        public Expr getLeft() {
            return left;
        }

        public Token getOperator() {
            return operator;
        }

        public Expr getRight() {
            return right;
        }

        @Override
        public <A> void accept(VoidVisitor<A> v, A arg) {
            v.visit(this, arg);
        }

        @Override
        public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
            return v.visit(this, arg);
        }
    }

    public static class Grouping extends Expr implements Visitable {
        private final Expr expr;

        public Grouping(Expr expr) {
            this.expr = expr;
        }

        public Expr getExpr() {
            return expr;
        }

        @Override
        public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
            return v.visit(this, arg);
        }

        @Override
        public <A> void accept(VoidVisitor<A> v, A arg) {
            v.visit(this, arg);
        }
    }

    public static class Literal extends Expr implements Visitable {

        private final Object value;

        public Literal(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }

        @Override
        public <A> void accept(VoidVisitor<A> v, A arg) {
            v.visit(this, arg);
        }

        @Override
        public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
            return v.visit(this, arg);
        }
    }

    public static class Unary extends Expr implements Visitable {
        private final Token operator;
        private final Expr right;

        public Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        public Token getOperator() {
            return operator;
        }

        public Expr getRight() {
            return right;
        }

        @Override
        public <A> void accept(VoidVisitor<A> v, A arg) {
            v.visit(this, arg);
        }

        @Override
        public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
            return v.visit(this, arg);
        }
    }
}