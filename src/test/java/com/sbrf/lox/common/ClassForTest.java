package com.sbrf.lox.common;

import com.sbrf.lox.scanner.TokenScanner;
import com.sbrf.lox.token.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ClassForTest {

    private void method() {
        for (int i = 0; i < 10; ++i) {
            System.out.println(i);
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}


@ExtendWith(MockitoExtension.class)
class TokenTokenScannerTest {

    @Mock
    private TokenScanner tokenScanner;

    @Test
    public void testScanTokens() {
        // Arrange
        List<Token> expectedTokens = new ArrayList<>();
        when(tokenScanner.isAtEnd()).thenReturn(false, true);
        when(tokenScanner.scanTokens()).thenReturn(expectedTokens);

        // Act
        Collection<Token> actualTokens = tokenScanner.scanTokens();

        // Assert
        assertEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testScanTokensWithEOF() {
        // Arrange
        List<Token> expectedTokens = new ArrayList<>();
        when(tokenScanner.isAtEnd()).thenReturn(true);
        when(tokenScanner.scanTokens()).thenReturn(expectedTokens);

        // Act
        Collection<Token> actualTokens = tokenScanner.scanTokens();

        // Assert
        assertEquals(expectedTokens, actualTokens);
    }
}