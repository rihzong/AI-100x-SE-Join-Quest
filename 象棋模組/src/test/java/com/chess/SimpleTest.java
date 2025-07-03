package com.chess;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleTest {
    
    @Test
    public void testChessServiceCreation() {
        ChessService chessService = new ChessService();
        assertNotNull(chessService);
    }
    
    @Test
    public void testMoveLegalInitiallyFalse() {
        ChessService chessService = new ChessService();
        boolean result = chessService.isMoveLegal(1, 5, 1, 4);
        assertFalse(result); // 應該失敗，因為我們還沒有實作邏輯
    }
} 