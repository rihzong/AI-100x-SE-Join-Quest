package com.chess;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import static org.assertj.core.api.Assertions.assertThat;

public class ChineseChessSteps {
    
    private ChessService chessService;
    private boolean moveResult;
    private boolean gameOver;
    
    public ChineseChessSteps() {
        this.chessService = new ChessService();
    }
    
    @Given("the board is empty except for a Red General at \\({int}, {int})")
    public void the_board_is_empty_except_for_a_red_general_at(int row, int col) {
        chessService.clearBoard();
        chessService.placePiece(Piece.Type.GENERAL, Piece.Color.RED, row, col);
    }
    
    @When("Red moves the General from \\({int}, {int}) to \\({int}, {int})")
    public void red_moves_the_general_from_to(int fromRow, int fromCol, int toRow, int toCol) {
        moveResult = chessService.isMoveLegal(fromRow, fromCol, toRow, toCol);
    }
    
    @Then("the move is legal")
    public void the_move_is_legal() {
        assertThat(moveResult).isTrue();
    }
    
    @Then("the move is illegal")
    public void the_move_is_illegal() {
        assertThat(moveResult).isFalse();
    }
    
    @Given("the board has:")
    public void the_board_has(DataTable dataTable) {
        chessService.clearBoard();
        dataTable.asMap().forEach((piece, position) -> {
            // 跳過標題行
            if (piece.toString().equals("Piece") || position.toString().equals("Position")) {
                return;
            }
            
            String pos = position.toString().replaceAll("[()]", "");
            String[] coords = pos.split(",");
            int row = Integer.parseInt(coords[0].trim());
            int col = Integer.parseInt(coords[1].trim());
            
            String[] pieceInfo = piece.toString().split(" ");
            Piece.Color color = pieceInfo[0].equals("Red") ? Piece.Color.RED : Piece.Color.BLACK;
            Piece.Type type = Piece.Type.valueOf(pieceInfo[1].toUpperCase());
            
            chessService.placePiece(type, color, row, col);
        });
    }
    
    @When("Red moves the Rook from \\({int}, {int}) to \\({int}, {int})")
    public void red_moves_the_rook_from_to(int fromRow, int fromCol, int toRow, int toCol) {
        moveResult = chessService.isMoveLegal(fromRow, fromCol, toRow, toCol);
        // 檢查是否獲勝（如果移動合法且目標位置有對方將帥）
        if (moveResult) {
            Piece targetPiece = chessService.getPieceAt(toRow, toCol);
            if (targetPiece != null && targetPiece.getType() == Piece.Type.GENERAL && 
                targetPiece.getColor() == Piece.Color.BLACK) {
                gameOver = true;
            }
        }
    }
    
    @Then("Red wins immediately")
    public void red_wins_immediately() {
        assertThat(gameOver).isTrue();
    }
    
    @Then("the game is not over just from that capture")
    public void the_game_is_not_over_just_from_that_capture() {
        assertThat(gameOver).isFalse();
    }
    
    @Given("the board is empty except for a Red Guard at \\({int}, {int})")
    public void the_board_is_empty_except_for_a_red_guard_at(int row, int col) {
        chessService.clearBoard();
        chessService.placePiece(Piece.Type.GUARD, Piece.Color.RED, row, col);
    }
    
    @When("Red moves the Guard from \\({int}, {int}) to \\({int}, {int})")
    public void red_moves_the_guard_from_to(int fromRow, int fromCol, int toRow, int toCol) {
        moveResult = chessService.isMoveLegal(fromRow, fromCol, toRow, toCol);
    }
    
    @Given("the board is empty except for a Red Rook at \\({int}, {int})")
    public void the_board_is_empty_except_for_a_red_rook_at(int row, int col) {
        chessService.clearBoard();
        chessService.placePiece(Piece.Type.ROOK, Piece.Color.RED, row, col);
    }
    
    @Given("the board is empty except for a Red Horse at \\({int}, {int})")
    public void the_board_is_empty_except_for_a_red_horse_at(int row, int col) {
        chessService.clearBoard();
        chessService.placePiece(Piece.Type.HORSE, Piece.Color.RED, row, col);
    }
    
    @When("Red moves the Horse from \\({int}, {int}) to \\({int}, {int})")
    public void red_moves_the_horse_from_to(int fromRow, int fromCol, int toRow, int toCol) {
        moveResult = chessService.isMoveLegal(fromRow, fromCol, toRow, toCol);
    }
    
    @Given("the board is empty except for a Red Cannon at \\({int}, {int})")
    public void the_board_is_empty_except_for_a_red_cannon_at(int row, int col) {
        chessService.clearBoard();
        chessService.placePiece(Piece.Type.CANNON, Piece.Color.RED, row, col);
    }
    
    @When("Red moves the Cannon from \\({int}, {int}) to \\({int}, {int})")
    public void red_moves_the_cannon_from_to(int fromRow, int fromCol, int toRow, int toCol) {
        moveResult = chessService.isMoveLegal(fromRow, fromCol, toRow, toCol);
    }
    
    @Given("the board is empty except for a Red Elephant at \\({int}, {int})")
    public void the_board_is_empty_except_for_a_red_elephant_at(int row, int col) {
        chessService.clearBoard();
        chessService.placePiece(Piece.Type.ELEPHANT, Piece.Color.RED, row, col);
    }
    
    @When("Red moves the Elephant from \\({int}, {int}) to \\({int}, {int})")
    public void red_moves_the_elephant_from_to(int fromRow, int fromCol, int toRow, int toCol) {
        moveResult = chessService.isMoveLegal(fromRow, fromCol, toRow, toCol);
    }
    
    @Given("the board is empty except for a Red Soldier at \\({int}, {int})")
    public void the_board_is_empty_except_for_a_red_soldier_at(int row, int col) {
        chessService.clearBoard();
        chessService.placePiece(Piece.Type.SOLDIER, Piece.Color.RED, row, col);
    }
    
    @When("Red moves the Soldier from \\({int}, {int}) to \\({int}, {int})")
    public void red_moves_the_soldier_from_to(int fromRow, int fromCol, int toRow, int toCol) {
        moveResult = chessService.isMoveLegal(fromRow, fromCol, toRow, toCol);
    }
} 