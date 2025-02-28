package chess;

import chess.pieceMoves.*;

import java.util.Collection;
import java.util.Objects;

public class ChessPiece implements Cloneable {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    public PieceType getPieceType() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece piece = (ChessPiece) o;
        return pieceColor == piece.pieceColor && type == piece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pose){
        moveCalc moves = switch(getPieceType()){
            case KING -> new kingMoveCalc();
            case PAWN -> new pawnMoveCalc();
            case ROOK -> new rookMoveCalc();
            case KNIGHT -> new knightMoveCalc();
            case QUEEN -> new queenMoveCalc();
            case BISHOP -> new bishopMoveCalc();
        };
        return moves.pieceMoves2(board, pose);
    }

    @Override
    public ChessPiece clone() { //allows the chess piece to be cloned (shallow copy)
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
    }
}
