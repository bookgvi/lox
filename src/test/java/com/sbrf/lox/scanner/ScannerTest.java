package com.sbrf.lox.scanner;

import com.sbrf.lox.token.Token;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;

class ScannerTest {

    @Test
    public void test1() {
        Path path = Path.of("/Users/21594124/IdeaProjects/Lox/src/test/java/com/sbrf/lox/common/ClassForTest.java");
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path.toFile()))) {
            BufferedReader buffer = new BufferedReader(inputStreamReader);
            AtomicInteger indexAtomic = new AtomicInteger(0);
            String text = buffer.lines().collect(Collectors.joining("\n"));
            Collection<Token> tokens = new TokenScanner(text, indexAtomic.incrementAndGet()).scanTokens();
            assertFalse(tokens.isEmpty());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}