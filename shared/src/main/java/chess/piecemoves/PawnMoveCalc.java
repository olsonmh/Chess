package chess.piecemoves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoveCalc extends MoveCalc {
    public Collection<ChessMove> pieceMoves2(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> validMoves = new HashSet<>();
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            ChessPosition pose = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
            if(myPosition.getRow() == 7){
                if(inBounds(pose, myPosition)){
                    if(board.getPiece(pose) == null){
                        validMoves.add(new ChessMove(myPosition, pose, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, pose, ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMove(myPosition, pose, ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, pose, ChessPiece.PieceType.KNIGHT));

                    }
                }
                for(int dx=-1; dx<=1; dx+=2){
                    ChessPosition pose3 = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+dx);
                    if(inBounds(pose3, myPosition)){
                        if(board.getPiece(pose3) != null){
                            if(board.getPiece(pose3).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                                validMoves.add(new ChessMove(myPosition, pose3, ChessPiece.PieceType.QUEEN));
                                validMoves.add(new ChessMove(myPosition, pose3, ChessPiece.PieceType.ROOK));
                                validMoves.add(new ChessMove(myPosition, pose3, ChessPiece.PieceType.BISHOP));
                                validMoves.add(new ChessMove(myPosition, pose3, ChessPiece.PieceType.KNIGHT));
                            }
                        }
                    }
                }
            }
            else{
                if(inBounds(pose, myPosition)){
                    if(board.getPiece(pose) == null){
                        validMoves.add(new ChessMove(myPosition, pose, null));
                        if(myPosition.getRow() == 2){
                            ChessPosition pose2 = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
                            if(board.getPiece(pose2) == null){
                                validMoves.add(new ChessMove(myPosition, pose2, null));
                            }
                        }
                    }
                }
                for(int dx=-1; dx<=1; dx+=2){
                    ChessPosition pose3 = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+dx);
                    if(inBounds(pose3, myPosition)){
                        if(board.getPiece(pose3) != null){
                            if(board.getPiece(pose3).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                                validMoves.add(new ChessMove(myPosition, pose3, null));
                            }
                        }
                    }
                }
            }
        }
        else{
            ChessPosition pose = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
            if(myPosition.getRow() == 2){
                if(inBounds(pose, myPosition)){
                    if(board.getPiece(pose) == null){
                        validMoves.add(new ChessMove(myPosition, pose, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, pose, ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMove(myPosition, pose, ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, pose, ChessPiece.PieceType.KNIGHT));

                    }
                }
                for(int dx=-1; dx<=1; dx+=2){
                    ChessPosition pose3 = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-dx);
                    if(inBounds(pose3, myPosition)){
                        if(board.getPiece(pose3) != null){
                            if(board.getPiece(pose3).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                                validMoves.add(new ChessMove(myPosition, pose3, ChessPiece.PieceType.QUEEN));
                                validMoves.add(new ChessMove(myPosition, pose3, ChessPiece.PieceType.ROOK));
                                validMoves.add(new ChessMove(myPosition, pose3, ChessPiece.PieceType.BISHOP));
                                validMoves.add(new ChessMove(myPosition, pose3, ChessPiece.PieceType.KNIGHT));
                            }
                        }
                    }
                }
            }
            else{
                if(inBounds(pose, myPosition)){
                    if(board.getPiece(pose) == null){
                        validMoves.add(new ChessMove(myPosition, pose, null));
                        if(myPosition.getRow() == 7){
                            ChessPosition pose2 = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn());
                            if(board.getPiece(pose2) == null){
                                validMoves.add(new ChessMove(myPosition, pose2, null));
                            }
                        }
                    }
                }
                for(int dx=-1; dx<=1; dx+=2){
                    ChessPosition pose3 = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-dx);
                    if(inBounds(pose3, myPosition)){
                        if(board.getPiece(pose3) != null){
                            if(board.getPiece(pose3).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                                validMoves.add(new ChessMove(myPosition, pose3, null));
                            }
                        }
                    }
                }
            }
        }
        return validMoves;
    }
}
