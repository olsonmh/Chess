package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMoveCalc extends MoveCalc {
    public Collection<ChessMove> pieceMoves2(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> validMoves = new HashSet<>();
        for(int j=-1; j<=1; j+=2){
            for(int dx=-1; dx<=1; dx+=2){
                ChessPosition pose = new ChessPosition(myPosition.getRow()+2*j, myPosition.getColumn()+dx);
                if(inBounds(pose, myPosition)){
                    if(board.getPiece(pose) != null){
                        if(board.getPiece(pose).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            validMoves.add(new ChessMove(myPosition, pose, null));
                        }
                    }
                    else{validMoves.add(new ChessMove(myPosition, pose, null));}
                }
            }
        }
        for(int j=-1; j<=1; j+=2){
            for(int dy=-1; dy<=1; dy+=2){
                ChessPosition pose = new ChessPosition(myPosition.getRow()+dy, myPosition.getColumn()+2*j);
                if(inBounds(pose, myPosition)){
                    if(board.getPiece(pose) != null){
                        if(board.getPiece(pose).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            validMoves.add(new ChessMove(myPosition, pose, null));
                        }
                    }
                    else{validMoves.add(new ChessMove(myPosition, pose, null));}
                }
            }
        }
        return validMoves;
    }
}
