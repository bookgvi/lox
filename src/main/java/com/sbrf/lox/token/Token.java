package com.sbrf.lox.token;

public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;
    final int start;
    final int end;

    private Token(
            TokenType type,
            String lexeme,
            Object literal,
            int line,
            int start,
            int end
    ) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.start = start;
        this.end = end;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString () {
        return type + " " + lexeme + " " + literal;
    }


    public static class Builder {
        private TokenType type;
        private String lexeme;
        private Object literal;
        private int line;
        private int start;
        private int end;

        public Builder end(int end) {
            this.end = end;
            return this;
        }

        public Builder lexeme(String lexeme) {
            this.lexeme = lexeme;
            return this;
        }

        public Builder line(int line) {
            this.line = line;
            return this;
        }

        public Builder literal(Object literal) {
            this.literal = literal;
            return this;
        }

        public Builder start(int start) {
            this.start = start;
            return this;
        }

        public Builder type(TokenType type) {
            this.type = type;
            return this;
        }

        public Token build() {
            return new Token(
                    type,
                    lexeme,
                    literal,
                    line,
                    start,
                    end
            );
        }
    }
}