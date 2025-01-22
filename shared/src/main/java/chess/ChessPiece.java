package chess;

import java.util.ArrayList;
import java.util.List;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
    }

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> valid_moves = new ArrayList<ChessMove>();
        List<ChessPosition> possible_moves = new ArrayList<ChessPosition>();
        switch(this.type) {
            case KING:
                for(int dy = -1; dy<=1; ++dy) {
                    for (int dx = -1; dx <= 1; ++dx) {
                        possible_moves.add(new ChessPosition(myPosition.getRow()+dy, myPosition.getColumn()+dx));

                    }
                }
                break;
            case QUEEN:
                for(int dy = -8; dy<=8; ++dy){
                    possible_moves.add(new ChessPosition(myPosition.getRow()+dy, myPosition.getColumn()));
                }
                for(int dx = -8; dx<=8; ++dx){
                    possible_moves.add(new ChessPosition(myPosition.getRow(), myPosition.getColumn()+dx));
                }
                for(int d = -8; d<=8; ++d){
                    possible_moves.add(new ChessPosition(myPosition.getRow()+d, myPosition.getColumn()+d));
                    possible_moves.add(new ChessPosition(myPosition.getRow()-d, myPosition.getColumn()+d));
                }
                break;
            case BISHOP:
                for(int d = -8; d<=8; ++d){
                    possible_moves.add(new ChessPosition(myPosition.getRow()+d, myPosition.getColumn()+d));
                    possible_moves.add(new ChessPosition(myPosition.getRow()-d, myPosition.getColumn()+d));
                }
                break;
            case KNIGHT:
                for(int dx = -2; dx<=2; dx+=2){
                    for(int dy = -1; dy<=1; ++dy){
                        possible_moves.add(new ChessPosition(myPosition.getRow()+dy, myPosition.getColumn()+dx));
                    }
                }
                for(int dy = -2; dy<=2; dy+=2){
                    for(int dx = -1; dx<=1; ++dx){
                        possible_moves.add(new ChessPosition(myPosition.getRow()+dy, myPosition.getColumn()+dx));
                    }
                }
                break;
            case ROOK:
                for(int dy = -8; dy<=8; ++dy){
                    possible_moves.add(new ChessPosition(myPosition.getRow()+dy, myPosition.getColumn()));
                }
                for(int dx = -8; dx<=8; ++dx){
                    possible_moves.add(new ChessPosition(myPosition.getRow(), myPosition.getColumn()+dx));
                }
                break;
            case PAWN:
                int dy=1;
                possible_moves.add(new ChessPosition(myPosition.getRow()+dy, myPosition.getColumn()));
                break;
            default:
                throw new RuntimeException("Not implemented");
        }

        for(ChessPosition pose : possible_moves){
            if(pose.getRow() == myPosition.getRow() && pose.getColumn() == myPosition.getColumn()){
                continue;
            }

            if(pose.getRow() > 8 || pose.getRow() < 0){
                continue;
            }
            if(pose.getColumn() > 8 || pose.getColumn() < 0){
                continue;
            }
            if(board.getPiece(pose) == null) {
                valid_moves.add(new ChessMove(myPosition, pose, null));
            }
        }

        return valid_moves;
    }
}
