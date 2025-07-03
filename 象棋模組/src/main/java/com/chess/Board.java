package com.chess;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private static final int ROWS = 10;
    private static final int COLS = 9;
    
    private final Map<Position, Piece> pieces = new HashMap<>();
    
    public void placePiece(Piece piece) {
        pieces.put(piece.getPosition(), piece);
    }
    
    public Piece getPieceAt(Position position) {
        return pieces.get(position);
    }
    
    public void removePiece(Position position) {
        pieces.remove(position);
    }
    
    public boolean isEmpty(Position position) {
        return !pieces.containsKey(position);
    }
    
    public boolean isWithinBoard(Position position) {
        return position.getRow() >= 1 && position.getRow() <= ROWS &&
               position.getCol() >= 1 && position.getCol() <= COLS;
    }
    
    public boolean isRedPalace(Position position) {
        return position.getRow() >= 1 && position.getRow() <= 3 &&
               position.getCol() >= 4 && position.getCol() <= 6;
    }
    
    public boolean isBlackPalace(Position position) {
        return position.getRow() >= 8 && position.getRow() <= 10 &&
               position.getCol() >= 4 && position.getCol() <= 6;
    }
    
    public boolean isRedPalaceMove(Position from, Position to) {
        return isRedPalace(from) && isRedPalace(to);
    }
    
    public boolean isBlackPalaceMove(Position from, Position to) {
        return isBlackPalace(from) && isBlackPalace(to);
    }
    
    public void movePiece(Position from, Position to) {
        Piece piece = pieces.get(from);
        if (piece != null) {
            pieces.remove(from);
            piece.setPosition(to);
            pieces.put(to, piece);
        }
    }
    
    public void clear() {
        pieces.clear();
    }
    
    public boolean hasObstacleBetween(Position from, Position to) {
        int fromRow = from.getRow();
        int fromCol = from.getCol();
        int toRow = to.getRow();
        int toCol = to.getCol();
        
        // 檢查同行移動
        if (fromRow == toRow) {
            int startCol = Math.min(fromCol, toCol);
            int endCol = Math.max(fromCol, toCol);
            for (int col = startCol + 1; col < endCol; col++) {
                if (!isEmpty(new Position(fromRow, col))) {
                    return true;
                }
            }
        }
        // 檢查同列移動
        else if (fromCol == toCol) {
            int startRow = Math.min(fromRow, toRow);
            int endRow = Math.max(fromRow, toRow);
            for (int row = startRow + 1; row < endRow; row++) {
                if (!isEmpty(new Position(row, fromCol))) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public int countPiecesBetween(Position from, Position to) {
        int count = 0;
        int fromRow = from.getRow();
        int fromCol = from.getCol();
        int toRow = to.getRow();
        int toCol = to.getCol();
        
        // 檢查同行移動
        if (fromRow == toRow) {
            int startCol = Math.min(fromCol, toCol);
            int endCol = Math.max(fromCol, toCol);
            for (int col = startCol + 1; col < endCol; col++) {
                if (!isEmpty(new Position(fromRow, col))) {
                    count++;
                }
            }
        }
        // 檢查同列移動
        else if (fromCol == toCol) {
            int startRow = Math.min(fromRow, toRow);
            int endRow = Math.max(fromRow, toRow);
            for (int row = startRow + 1; row < endRow; row++) {
                if (!isEmpty(new Position(row, fromCol))) {
                    count++;
                }
            }
        }
        
        return count;
    }
} 