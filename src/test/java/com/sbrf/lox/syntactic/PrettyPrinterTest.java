package com.sbrf.lox.syntactic;

import com.sbrf.lox.token.Token;
import com.sbrf.lox.token.TokenType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrettyPrinterTest {

    @Test
    void print() {
        Expr expression = new Expr.Binary(
                new Expr.Unary(Token.builder().type(TokenType.MINUS).lexeme("-").literal(null).line(1).build(), new Expr.Literal(123)),
                Token.builder().type(TokenType.STAR).lexeme("*").literal(null).line(1).build(),
                new Expr.Grouping(new Expr.Literal(45.67))
        );
        String res = new PrettyPrinter().print(expression, null);
        assertEquals("(* (- 123) (group 45.67))", res);
    }
}