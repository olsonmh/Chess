package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece [][] board = new ChessPiece [8][8];

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
        board[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */

    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        reset();
        for(int dx = 0; dx<=8; ++dx){
            this.addPiece(new ChessPosition(1,dx),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN));
            this.addPiece(new ChessPosition(6,dx),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN));
        }
        this.addPiece(new ChessPosition(0,0),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(0,1),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(0,2),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(0,3),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(0,4),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(0,5),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(0,6),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(0,7),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(7,0),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(7,1),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(7,2),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(7,3),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(7,4),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KING));
        this.addPiece(new ChessPosition(7,5),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(7,6),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(7,7),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.ROOK));

    }

    public void reset(){
        for(int dy=0; dy<=8; ++dy){
            for(int dx=0; dx<=8; ++dx){
                board[dy][dx] = null;
            }
        }
    }

}
