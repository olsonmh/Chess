package chess;

import chess.piecemoves.*;

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
        MoveCalc moves = switch(getPieceType()){
            case KING -> new KingMoveCalc();
            case PAWN -> new PawnMoveCalc();
            case ROOK -> new RookMoveCalc();
            case KNIGHT -> new KnightMoveCalc();
            case QUEEN -> new QueenMoveCalc();
            case BISHOP -> new BishopMoveCalc();
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
