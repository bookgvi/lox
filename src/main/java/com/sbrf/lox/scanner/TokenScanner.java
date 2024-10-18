package com.sbrf.lox.scanner;

import com.sbrf.lox.token.Token;
import com.sbrf.lox.token.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.sbrf.lox.token.TokenType.*;

public class TokenScanner {

    private static final Logger log = LoggerFactory.getLogger(TokenScanner.class);

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private volatile boolean isMultilineComment;

    TokenScanner(String source, int line) {
        this.source = source;
        this.line = line;
    }

    public Collection<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(Token.builder()
                .type(TokenType.EOF)
                .lexeme("")
                .literal(null)
                .line(line)
                .start(start)
                .end(current)
                .build()
        );
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*':
                if (match('/')) {
                    isMultilineComment = false;
                } else {
                    addToken(STAR);
                }
                break;
            case '@': addToken(AMPERSAND); break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd() && !isMultilineComment) {
                        advance();
                    }
                } else if (match('*') && !isAtEnd() && peekNext() != '*') {
                    isMultilineComment = true;
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                break; // Ignore whitespace.
            case '\n':
                line++;
                break;
            case '"': string(); break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    log.info("Unexpected character {}", line);
                }
                break;
        }
    }

    private boolean isAlpha(char c) {
        return Character.isJavaIdentifierStart(c);
    }

    private boolean isAlphaNumeric(char c) {
        return Character.isJavaIdentifierPart(c);
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }
        String text = source.substring(start, current);
        TokenType type = TokenType.getKeywords().getOrDefault(text, IDENTIFIER);
        addToken(type);
    }

    private boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }
        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            do {
                advance();
            } while (isDigit(peek()));
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd() && !isMultilineComment) {
            if (peek() == '\n') line++;
            advance();
        }
        if (isAtEnd()) {
            log.error("Unterminated string. {}", line);
            return;
        }
        // The closing ".
        advance();
        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }


    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }
        if (source.charAt(current) != expected) {
            return false;
        }
        current++;
        return true;
    }

    private char advance() {
        if (isAtEnd()) {
            return '\u001a';
        }
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        if (isMultilineComment) {
            return;
        }
        String text = source.substring(start, current);
        tokens.add(Token.builder()
                .type(type)
                .lexeme(text)
                .literal(literal)
                .line(line)
                .start(start)
                .end(current)
                .build()
        );
    }

    public boolean isAtEnd() {
        return current >= source.length();
    }
}