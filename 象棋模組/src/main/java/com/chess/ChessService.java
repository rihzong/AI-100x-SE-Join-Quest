package com.chess;

public class ChessService {
    
    private final Board board;
    
    public ChessService() {
        this.board = new Board();
    }
    
    public boolean isMoveLegal(int fromRow, int fromCol, int toRow, int toCol) {
        Position from = new Position(fromRow, fromCol);
        Position to = new Position(toRow, toCol);
        
        // 檢查是否在棋盤範圍內
        if (!board.isWithinBoard(from) || !board.isWithinBoard(to)) {
            return false;
        }
        
        Piece piece = board.getPieceAt(from);
        if (piece == null) {
            return false;
        }
        
        // 根據棋子類型檢查移動是否合法
        switch (piece.getType()) {
            case GENERAL:
                return isGeneralMoveLegal(piece, from, to);
            case GUARD:
                return isGuardMoveLegal(piece, from, to);
            case ROOK:
                return isRookMoveLegal(piece, from, to);
            case HORSE:
                return isHorseMoveLegal(piece, from, to);
            case CANNON:
                return isCannonMoveLegal(piece, from, to);
            case ELEPHANT:
                return isElephantMoveLegal(piece, from, to);
            case SOLDIER:
                return isSoldierMoveLegal(piece, from, to);
            default:
                return false;
        }
    }
    
    private boolean isGeneralMoveLegal(Piece general, Position from, Position to) {
        // 將/帥只能在九宮格內移動
        if (general.getColor() == Piece.Color.RED) {
            if (!board.isRedPalaceMove(from, to)) {
                return false;
            }
        } else {
            if (!board.isBlackPalaceMove(from, to)) {
                return false;
            }
        }
        
        // 將/帥只能上下左右移動一格
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        if (!((rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1))) {
            return false;
        }
        
        // 檢查移動後是否會造成將帥面對面
        if (wouldGeneralsFaceEachOther(general, to)) {
            return false;
        }
        
        return true;
    }
    
    private boolean wouldGeneralsFaceEachOther(Piece movingGeneral, Position newPosition) {
        // 找到對方的將帥
        Piece.Color opponentColor = movingGeneral.getColor() == Piece.Color.RED ? 
                                   Piece.Color.BLACK : Piece.Color.RED;
        
        // 在棋盤上尋找對方的將帥
        for (int row = 1; row <= 10; row++) {
            for (int col = 1; col <= 9; col++) {
                Position pos = new Position(row, col);
                Piece piece = board.getPieceAt(pos);
                if (piece != null && piece.getType() == Piece.Type.GENERAL && 
                    piece.getColor() == opponentColor) {
                    // 檢查是否在同一列且中間沒有其他棋子
                    if (newPosition.getCol() == pos.getCol()) {
                        int startRow = Math.min(newPosition.getRow(), pos.getRow());
                        int endRow = Math.max(newPosition.getRow(), pos.getRow());
                        boolean hasObstacle = false;
                        for (int r = startRow + 1; r < endRow; r++) {
                            if (!board.isEmpty(new Position(r, newPosition.getCol()))) {
                                hasObstacle = true;
                                break;
                            }
                        }
                        if (!hasObstacle) {
                            return true; // 將帥面對面
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isGuardMoveLegal(Piece guard, Position from, Position to) {
        // 士只能在九宮內斜線移動一格
        if (guard.getColor() == Piece.Color.RED) {
            if (!board.isRedPalaceMove(from, to)) {
                return false;
            }
        } else {
            if (!board.isBlackPalaceMove(from, to)) {
                return false;
            }
        }
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        return rowDiff == 1 && colDiff == 1;
    }
    
    private boolean isRookMoveLegal(Piece rook, Position from, Position to) {
        // 車只能直線移動（同行或同列）
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        if (!((rowDiff == 0 && colDiff > 0) || (colDiff == 0 && rowDiff > 0))) {
            return false;
        }
        
        // 檢查路徑上是否有其他棋子阻擋
        return !board.hasObstacleBetween(from, to);
    }
    
    private boolean isHorseMoveLegal(Piece horse, Position from, Position to) {
        // 馬走日字，先直走一格，再斜走一格
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        // 檢查是否為日字移動
        if (!((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2))) {
            return false;
        }
        
        // 檢查馬腳是否被阻擋
        int legRow = from.getRow();
        int legCol = from.getCol();
        
        if (rowDiff == 2) {
            // 直走兩格，檢查中間的馬腳
            legRow = from.getRow() + (to.getRow() > from.getRow() ? 1 : -1);
        } else {
            // 橫走兩格，檢查中間的馬腳
            legCol = from.getCol() + (to.getCol() > from.getCol() ? 1 : -1);
        }
        
        return board.isEmpty(new Position(legRow, legCol));
    }
    
    private boolean isCannonMoveLegal(Piece cannon, Position from, Position to) {
        // 炮只能直線移動（同行或同列）
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        if (!((rowDiff == 0 && colDiff > 0) || (colDiff == 0 && rowDiff > 0))) {
            return false;
        }
        
        // 計算路徑上的棋子數量
        int piecesInPath = board.countPiecesBetween(from, to);
        
        // 如果目標位置有棋子，需要恰好一個炮架
        if (board.getPieceAt(to) != null) {
            return piecesInPath == 1;
        } else {
            // 如果目標位置沒有棋子，路徑必須完全清空
            return piecesInPath == 0;
        }
    }
    
    private boolean isElephantMoveLegal(Piece elephant, Position from, Position to) {
        // 象走田字，斜走兩格
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        if (rowDiff != 2 || colDiff != 2) {
            return false;
        }
        
        // 象不能過河
        if (elephant.getColor() == Piece.Color.RED && to.getRow() > 5) {
            return false;
        }
        if (elephant.getColor() == Piece.Color.BLACK && to.getRow() < 6) {
            return false;
        }
        
        // 檢查象眼是否被阻擋
        int eyeRow = from.getRow() + (to.getRow() > from.getRow() ? 1 : -1);
        int eyeCol = from.getCol() + (to.getCol() > from.getCol() ? 1 : -1);
        
        return board.isEmpty(new Position(eyeRow, eyeCol));
    }
    
    private boolean isSoldierMoveLegal(Piece soldier, Position from, Position to) {
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        if (soldier.getColor() == Piece.Color.RED) {
            // 紅方兵
            if (from.getRow() < 6) {
                // 未過河，只能向前
                return rowDiff == 1 && colDiff == 0;
            } else {
                // 已過河，可以向前或向左右
                return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
            }
        } else {
            // 黑方卒
            if (from.getRow() > 5) {
                // 未過河，只能向前
                return rowDiff == -1 && colDiff == 0;
            } else {
                // 已過河，可以向前或向左右
                return (rowDiff == -1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
            }
        }
    }
    
    public boolean isGameOver() {
        // TODO: 實作遊戲結束檢查
        return false;
    }
    
    public void placePiece(Piece.Type type, Piece.Color color, int row, int col) {
        Position position = new Position(row, col);
        Piece piece = new Piece(type, color, position);
        board.placePiece(piece);
    }
    
    public void clearBoard() {
        board.clear();
    }
    
    public Piece getPieceAt(int row, int col) {
        return board.getPieceAt(new Position(row, col));
    }
} 