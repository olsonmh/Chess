package chess.piecemoves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class KingMoveCalc extends MoveCalc {
    public Collection<ChessMove> pieceMoves2(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> validMoves = new HashSet<>();
        for(int dy=-1; dy<=1; ++dy){
            for(int dx=-1; dx<=1; ++dx){
                ChessPosition pose = new ChessPosition(myPosition.getRow()+dy, myPosition.getColumn()+dx);
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
        //castling

        switch(board.getPiece(myPosition).getTeamColor()){
            case WHITE:
                if(myPosition.equals(new ChessPosition(1,5)) && board.whiteKing){
                    if(board.getPiece(new ChessPosition(1,8)) != null){
                        if(board.getPiece(new ChessPosition(1,8)).getPieceType() ==
                                ChessPiece.PieceType.ROOK && board.rightWhiteRook){
                            if(board.getPiece(new ChessPosition(1,6)) == null &&
                                    board.getPiece(new ChessPosition(1,7)) == null){
                                ChessPosition pose2 = new ChessPosition(1,7);
                                validMoves.add(new ChessMove(myPosition, pose2, null));
                            }
                        }
                    }
                    if(board.getPiece(new ChessPosition(1,1)) != null){
                        if(board.getPiece(new ChessPosition(1,1)).getPieceType() ==
                                ChessPiece.PieceType.ROOK && board.leftWhiteRook){
                            if(board.getPiece(new ChessPosition(1,4)) == null &&
                                    board.getPiece(new ChessPosition(1,3)) == null &&
                                    board.getPiece(new ChessPosition(1,2)) == null){
                                ChessPosition pose2 = new ChessPosition(1,3);
                                validMoves.add(new ChessMove(myPosition, pose2, null));
                            }
                        }
                    }
                }
                break;
            case BLACK:
                if(myPosition.equals(new ChessPosition(8,5)) && board.blackKing){
                    if(board.getPiece(new ChessPosition(8,8)) != null){
                        if(board.getPiece(new ChessPosition(8,8)).getPieceType() ==
                                ChessPiece.PieceType.ROOK && board.rightBlackRook){
                            if(board.getPiece(new ChessPosition(8,6)) == null &&
                                    board.getPiece(new ChessPosition(8,7)) == null){
                                ChessPosition pose2 = new ChessPosition(8,7);
                                validMoves.add(new ChessMove(myPosition, pose2, null));
                            }
                        }
                    }
                    if(board.getPiece(new ChessPosition(8,1)) != null){
                        if(board.getPiece(new ChessPosition(8,1)).getPieceType() ==
                                ChessPiece.PieceType.ROOK && board.leftBlackRook){
                            if(board.getPiece(new ChessPosition(8,4)) == null &&
                                    board.getPiece(new ChessPosition(8,3)) == null &&
                                    board.getPiece(new ChessPosition(8,2)) == null){
                                ChessPosition pose2 = new ChessPosition(8,3);
                                validMoves.add(new ChessMove(myPosition, pose2, null));
                            }
                        }
                    }
                }
                break;
            default:
                throw new RuntimeException();
        }


        return validMoves;
    }
}
