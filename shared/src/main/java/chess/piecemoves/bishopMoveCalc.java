package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class bishopMoveCalc extends moveCalc{
    public Collection<ChessMove> pieceMoves2(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> valid_moves = new HashSet<>();
        for(int j=-1; j<=1; j+=2){
            for(int d = 1; d<=8; ++d){
                ChessPosition pose = new ChessPosition(myPosition.getRow()+(d*j), myPosition.getColumn()+(d*j));
                if(inBounds(pose, myPosition)){
                    if(board.getPiece(pose) != null){
                        if(board.getPiece(pose).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            valid_moves.add(new ChessMove(myPosition, pose, null));
                        }
                        break;
                    }
                    else{valid_moves.add(new ChessMove(myPosition, pose, null));}
                }
            }
        }
        for(int j=-1; j<=1; j+=2){
            for(int d = 1; d<=8; ++d){
                ChessPosition pose = new ChessPosition(myPosition.getRow()-(d*j), myPosition.getColumn()+(d*j));
                if(inBounds(pose, myPosition)){
                    if(board.getPiece(pose) != null){
                        if(board.getPiece(pose).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            valid_moves.add(new ChessMove(myPosition, pose, null));
                        }
                        break;
                    }
                    else{valid_moves.add(new ChessMove(myPosition, pose, null));}
                }
            }
        }
        return valid_moves;
    }
}
