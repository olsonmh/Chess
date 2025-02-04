package chess.pieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class kingMoveCalc extends moveCalc{
    public Collection<ChessMove> pieceMoves2(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> valid_moves = new HashSet<>();
        for(int dy=-1; dy<=1; ++dy){
            for(int dx=-1; dx<=1; ++dx){
                ChessPosition pose = new ChessPosition(myPosition.getRow()+dy, myPosition.getColumn()+dx);
                if(inBounds(pose, myPosition)){
                    if(board.getPiece(pose) != null){
                        if(board.getPiece(pose).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            valid_moves.add(new ChessMove(myPosition, pose, null));
                        }
                    }
                    else{valid_moves.add(new ChessMove(myPosition, pose, null));}
                }
            }
        }
        return valid_moves;
    }
}
