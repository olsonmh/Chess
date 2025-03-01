package chess.piecemoves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoveCalc extends MoveCalc {
    public Collection<ChessMove> pieceMoves2(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> validMoves = new HashSet<>();

        int rowDx = 1;
        int rowPose = 7;
        int rowPose2 = 2;
        int rowDx2 = 2;
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            rowDx = -1;
            rowPose = 2;
            rowPose2 = 7;
            rowDx2 = -2;
        }

        ChessPosition pose = new ChessPosition(myPosition.getRow()+rowDx, myPosition.getColumn());
        if(myPosition.getRow() == rowPose){
            if(inBounds(pose, myPosition)){
                if(board.getPiece(pose) == null){
                    validMoves.add(new ChessMove(myPosition, pose, ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, pose, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(myPosition, pose, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(myPosition, pose, ChessPiece.PieceType.KNIGHT));

                }
            }
            for(int dx=-1; dx<=1; dx+=2){
                ChessPosition pose3 = new ChessPosition(myPosition.getRow()+rowDx, myPosition.getColumn()+dx);
                if(!inBounds(pose3, myPosition)) {
                    continue;
                }

                if(board.getPiece(pose3) != null){
                    if(board.getPiece(pose3).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        validMoves.add(new ChessMove(myPosition, pose3, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, pose3, ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMove(myPosition, pose3, ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, pose3, ChessPiece.PieceType.KNIGHT));
                    }
                }
            }
            return validMoves;
        }

        if(inBounds(pose, myPosition)){
            if(board.getPiece(pose) == null){
                validMoves.add(new ChessMove(myPosition, pose, null));
                if(myPosition.getRow() == rowPose2){
                    ChessPosition pose2 = new ChessPosition(myPosition.getRow()+rowDx2, myPosition.getColumn());
                    if(board.getPiece(pose2) == null){
                        validMoves.add(new ChessMove(myPosition, pose2, null));
                    }
                }
            }
        }

        for(int dx=-1; dx<=1; dx+=2){
            ChessPosition pose3 = new ChessPosition(myPosition.getRow()+rowDx, myPosition.getColumn()+dx);
            if(!inBounds(pose3, myPosition)) {
                continue;
            }

            if(board.getPiece(pose3) != null){
                if(board.getPiece(pose3).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    validMoves.add(new ChessMove(myPosition, pose3, null));
                }
            }
        }
        return validMoves;
    }
}
