package com.chess;

public class Piece {
    public enum Type {
        GENERAL, GUARD, ROOK, HORSE, CANNON, ELEPHANT, SOLDIER
    }
    
    public enum Color {
        RED, BLACK
    }
    
    private final Type type;
    private final Color color;
    private Position position;
    
    public Piece(Type type, Color color, Position position) {
        this.type = type;
        this.color = color;
        this.position = position;
    }
    
    public Type getType() {
        return type;
    }
    
    public Color getColor() {
        return color;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    @Override
    public String toString() {
        return color + " " + type + " at " + position;
    }
} 