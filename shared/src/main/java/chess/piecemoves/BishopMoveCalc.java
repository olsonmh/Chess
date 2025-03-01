package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopMoveCalc extends MoveCalc {
    public Collection<ChessMove> pieceMoves2(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> validMoves = new HashSet<>();
        for(int i = -1; i <= 1; i += 2){
            for(int j=-1; j<=1; j+=2){
                for(int d = 1; d<=8; ++d){
                    ChessPosition pose = new ChessPosition(myPosition.getRow()+i*(d*j), myPosition.getColumn()+(d*j));
                    if(!inBounds(pose, myPosition)) {
                        continue;
                    }

                    if(board.getPiece(pose) == null){
                        validMoves.add(new ChessMove(myPosition, pose, null));
                        continue;
                    }

                    if(board.getPiece(pose).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        validMoves.add(new ChessMove(myPosition, pose, null));
                    }
                    break;
                }
            }
        }
        return validMoves;
    }
}
