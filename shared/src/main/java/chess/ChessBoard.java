package chess;


import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    public final ChessPiece [][] board = new ChessPiece [8][8];

    public ChessBoard() {
        reset();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    public void removePiece(ChessPosition position){
        board[position.getRow()-1][position.getColumn()-1] = null;
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */

    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        reset();
        for(int dx = 1; dx<=8; ++dx){
            this.addPiece(new ChessPosition(2,dx),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN));
            this.addPiece(new ChessPosition(7,dx),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN));
        }
        this.addPiece(new ChessPosition(1,1),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(1,2),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1,3),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(1,4),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(1,5),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(1,6),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(1,7),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1,8),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(8,1),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(8,2),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8,3),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(8,4),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(8,5),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(8,6),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(8,7),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8,8),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.ROOK));

    }

    public void reset(){
        for(int dy=0; dy<=7; ++dy){
            for(int dx=0; dx<=7; ++dx){
                board[dy][dx] = null;
            }
        }
    }

    public void printout(){
        for(int i = 0; i<=7; i++){
            System.out.printf("|");
            for(int j = 0; j<=7; j++){
                char label;
                ChessPiece piece = board[i][j];
                if(piece == null){
                    label = ' ';
                }
                else{
                switch(piece.getPieceType()){
                    case PAWN:
                        label = 'p';
                        break;
                    case ROOK:
                        label = 'r';
                        break;
                    case KNIGHT:
                        label = 'n';
                        break;
                    case QUEEN:
                        label = 'q';
                        break;
                    case KING:
                        label = 'k';
                        break;
                    case BISHOP:
                        label = 'b';
                        break;
                    default:
                        throw new RuntimeException("dont goon");
                }
                if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                    label = Character.toUpperCase(label);
                }}
                System.out.printf("%c|",label);
            }
            System.out.printf("\n");
        }
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(obj.getClass() != this.getClass()){
            return false;
        }
        ChessBoard that = (ChessBoard) obj;
        return Arrays.deepEquals(board, that.board);
    }

}
