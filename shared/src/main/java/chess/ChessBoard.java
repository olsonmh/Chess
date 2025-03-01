package chess;

import java.util.Arrays;
import java.util.Objects;

public class ChessBoard implements Cloneable {
    public ChessPiece [][] board = new ChessPiece [8][8];
    public boolean blackKing = true;
    public boolean whiteKing = true;
    public boolean rightWhiteRook = true;
    public boolean leftWhiteRook = true;
    public boolean rightBlackRook = true;
    public boolean leftBlackRook = true;

    public ChessBoard() {
    }

    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    public void removePiece(ChessPosition pose){
        board[pose.getRow()-1][pose.getColumn()-1] = null;  //used for moving pieces
    }

    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    public void resetBoard() {
        for(int i=0;i<=7; ++i){
            for(int j=0; j<=7; ++j){
                board[i][j] = null;  //sets everything on the board equal to null before placing pieces
            }
        }
        for(int col = 1; col<=8; ++col){
            this.addPiece(new ChessPosition(2,col), new ChessPiece(ChessGame.TeamColor.WHITE,
                                                                        ChessPiece.PieceType.PAWN));
            this.addPiece(new ChessPosition(7,col), new ChessPiece(ChessGame.TeamColor.BLACK,
                                                                        ChessPiece.PieceType.PAWN));
        }
        this.addPiece(new ChessPosition(1,1), new ChessPiece(ChessGame.TeamColor.WHITE,
                                                                       ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(1,2), new ChessPiece(ChessGame.TeamColor.WHITE,
                                                                       ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1,3), new ChessPiece(ChessGame.TeamColor.WHITE,
                                                                       ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(1,4), new ChessPiece(ChessGame.TeamColor.WHITE,
                                                                       ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(1,5), new ChessPiece(ChessGame.TeamColor.WHITE,
                                                                       ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(1,6), new ChessPiece(ChessGame.TeamColor.WHITE,
                                                                       ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(1,7), new ChessPiece(ChessGame.TeamColor.WHITE,
                                                                       ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1,8), new ChessPiece(ChessGame.TeamColor.WHITE,
                                                                       ChessPiece.PieceType.ROOK));

        this.addPiece(new ChessPosition(8,1), new ChessPiece(ChessGame.TeamColor.BLACK,
                                                                       ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(8,2), new ChessPiece(ChessGame.TeamColor.BLACK,
                                                                       ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8,3), new ChessPiece(ChessGame.TeamColor.BLACK,
                                                                       ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(8,4), new ChessPiece(ChessGame.TeamColor.BLACK,
                                                                       ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(8,5), new ChessPiece(ChessGame.TeamColor.BLACK,
                                                                       ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(8,6), new ChessPiece(ChessGame.TeamColor.BLACK,
                                                                       ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(8,7), new ChessPiece(ChessGame.TeamColor.BLACK,
                                                                       ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8,8), new ChessPiece(ChessGame.TeamColor.BLACK,
                                                                       ChessPiece.PieceType.ROOK));

        this.whiteKing = true;
        this.blackKing = true;
        this.rightWhiteRook = true;
        this.leftWhiteRook = true;
        this.rightBlackRook = true;
        this.leftBlackRook = true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public ChessBoard clone() {  //clone methode for deep copying instead of shallow copies.
        try {
            ChessBoard clonedBoard = (ChessBoard) super.clone();
            clonedBoard.board = new ChessPiece[8][8];
            for (int i = 0; i<=7; ++i) {
                for (int j = 0; j<=7; ++j) {
                    if (this.board[i][j] != null) {
                        clonedBoard.board[i][j] = this.board[i][j].clone();
                    }
                }
            }

            return clonedBoard;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
    }

}
