package chess.pieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
//import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public abstract class  moveCalc {
    public Collection<ChessMove> pieceMoves2(ChessBoard board, ChessPosition myPosition){
        //Collection<ChessMove> valid_moves = new HashSet<>();
        //return valid_moves;
        return new HashSet<>();
    }

    public boolean inBounds(ChessPosition pose, ChessPosition myPose){
        if(pose.getRow() < 1 || pose.getRow() > 8){
            return false;
        }
        if(pose.getColumn() < 1 || pose.getColumn() > 8){
            return false;
        }
        return pose.getRow() != myPose.getRow() || pose.getColumn() != myPose.getColumn();
    }
}
