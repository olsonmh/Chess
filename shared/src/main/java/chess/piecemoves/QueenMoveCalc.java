package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMoveCalc extends MoveCalc {
    public Collection<ChessMove> pieceMoves2(ChessBoard board, ChessPosition myPosition){
        MoveCalc moves = new RookMoveCalc();
        Collection<ChessMove> validMoves1 = moves.pieceMoves2(board, myPosition);
        MoveCalc moves2 = new BishopMoveCalc();
        Collection<ChessMove> validMoves2 = moves2.pieceMoves2(board, myPosition);
        validMoves1.addAll(validMoves2);
        return validMoves1;
    }
}
