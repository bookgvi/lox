package com.sbrf.lox.scanner;

import com.sbrf.lox.token.Token;
import com.sbrf.lox.token.TokenType;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TestComments {

    @Test
    public void test1() {
        Path path = Path.of("/Users/21594124/IdeaProjects/Lox/src/test/java/com/sbrf/lox/common/TokenCommentsForTest.java");
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path.toFile()))) {
            BufferedReader buffer = new BufferedReader(inputStreamReader);
            AtomicInteger indexAtomic = new AtomicInteger(0);
            String text = buffer.lines().collect(Collectors.joining("\n"));
            Collection<Token> tokens = new TokenScanner(text, indexAtomic.incrementAndGet()).scanTokens();
            assertFalse(tokens.isEmpty());
            assertEquals(1, tokens.stream().filter(token -> token.getType() == TokenType.END_OF_LINE_COMMENT).count());
            assertEquals(2, tokens.stream().filter(token -> token.getType() == TokenType.TRADITIONAL_COMMENT).count());
            assertEquals(1, tokens.stream().filter(token -> token.getType() == TokenType.STRING).count());
            assertEquals("QQQ", tokens.stream()
                    .filter(token -> token.getType() == TokenType.STRING)
                    .map(Token::getLiteral).
                    findFirst()
                    .orElse(""));
            assertTrue(tokens.stream().anyMatch(token -> "toString".equals(token.getLexeme())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
