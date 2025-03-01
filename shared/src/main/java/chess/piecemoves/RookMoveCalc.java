package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class RookMoveCalc extends MoveCalc {
    public Collection<ChessMove> pieceMoves2(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> validMoves = new HashSet<>();
        for(int j=-1; j<=1; j+=2){
            for(int dy=1; dy<=8; ++dy){
                ChessPosition pose = new ChessPosition(myPosition.getRow()+(dy*j), myPosition.getColumn());
                if(inBounds(pose, myPosition)){
                    if(board.getPiece(pose) != null){
                        if(board.getPiece(pose).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            validMoves.add(new ChessMove(myPosition, pose, null));
                        }
                        break;
                    }
                    else{validMoves.add(new ChessMove(myPosition, pose, null));}
                }
            }
        }
        for(int j=-1; j<=1; j+=2){
            for(int dx=1; dx<=8; ++dx){
                ChessPosition pose = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+(dx*j));
                if(inBounds(pose, myPosition)){
                    if(board.getPiece(pose) != null){
                        if(board.getPiece(pose).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            validMoves.add(new ChessMove(myPosition, pose, null));
                        }
                        break;
                    }
                    else{validMoves.add(new ChessMove(myPosition, pose, null));}
                }
            }
        }
        return validMoves;
    }
}
