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

    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int traditionalCommentStart = -1;
    private int current = 0;
    private int line = 1;
    private volatile boolean isMultilineComment;
    private final char[] buff;

    TokenScanner(String source, int line) {
        this.line = line;
        buff = source.toCharArray();
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
                    String lexeme = new String(buff, traditionalCommentStart, current - traditionalCommentStart);
                    String literal = new String(buff, traditionalCommentStart + 2, current - 2 - (traditionalCommentStart + 2));
                    traditionalCommentStart = -1;
                    addToken(TRADITIONAL_COMMENT, lexeme, literal);
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
                    String lexeme = new String(buff, start, current - start);
                    String value = new String(buff, start + 2, current - (start + 2));
                    addToken(END_OF_LINE_COMMENT, lexeme, value);
                } else if (match('*') && !isAtEnd() && peekNext() != '*') {
                    isMultilineComment = true;
                    traditionalCommentStart = traditionalCommentStart == -1 ? start : traditionalCommentStart;
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
        String text = new String(buff, start, current - start);
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
        String value = new String(buff, start, current - start);
        addToken(NUMBER, Double.parseDouble(value));
    }

    private char peekNext() {
        if (current + 1 >= buff.length) return '\0';
        return buff[current + 1];
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
        String lexeme = new String(buff, start, current - start);
        String value = new String(buff, start + 1, current - 1 - (start + 1));

        addToken(STRING, lexeme, value);
    }


    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return buff[current];
    }

    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }
        if (buff[current] != expected) {
            return false;
        }
        current++;
        return true;
    }

    private char advance() {
        if (isAtEnd()) {
            return '\u001a';
        }
        return buff[current++];
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = new String(buff, start, current - start);
        addToken(type, text, null);
    }

    private void addToken(TokenType type, String text, Object literal) {
        if (isMultilineComment) {
            return;
        }
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
        return current >= buff.length;
    }
}