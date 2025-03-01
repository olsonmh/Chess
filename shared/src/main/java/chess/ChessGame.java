package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn;
    private ChessBoard chessBoard;

    public ChessGame() {
        turn = TeamColor.WHITE;
        chessBoard = new ChessBoard();
        chessBoard.resetBoard();
    }

    public TeamColor getTeamTurn() {
        return turn;
    }

    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    public enum TeamColor {
        WHITE,
        BLACK
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPiece piece = this.chessBoard.getPiece(startPosition);
        if(piece == null){
            return null;
        }
        Collection<ChessMove> moves =  piece.pieceMoves(this.chessBoard,startPosition);
        for(ChessMove move : moves){
            if(!doesNotMoveIntoCheck(move)) {
                continue;
            }

            if(this.chessBoard.getPiece(startPosition).getPieceType().equals(ChessPiece.PieceType.KING)){
                if(!isCastleMove(move, this.chessBoard)) {
                    validMoves.add(move);
                }

                if(move.getStartPosition().getColumn() > move.getEndPosition().getColumn()){
                    if(doesNotMoveIntoCheck(new ChessMove(startPosition,
                                            new ChessPosition(startPosition.getRow(),
                                            startPosition.getColumn()-1), null))){
                        validMoves.add(move);
                    }
                }
                else{
                    if(doesNotMoveIntoCheck(new ChessMove(startPosition,
                                            new ChessPosition(startPosition.getRow(),
                                            startPosition.getColumn()+1), null))){
                        validMoves.add(move);
                    }
                }
            }
            else{
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(this.chessBoard.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException();
        }

        if(this.chessBoard.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.WHITE){
            if(this.turn == TeamColor.BLACK){
                throw new InvalidMoveException();
            }

            turn = TeamColor.BLACK;
        }

        else{
            if(this.turn == TeamColor.WHITE){
                throw new InvalidMoveException();
            }

            turn = TeamColor.WHITE;
        }

        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        boolean moveMade = false;
        for(ChessMove validMove : moves){
            if(validMove.equals(move)){
                makeMovePrivate(chessBoard, move);
                moveMade = true;
            }
        }

        if(!moveMade){
            throw new InvalidMoveException();
        }
    }

    public boolean isInCheck(TeamColor teamColor){
        return boardIsInCheck(teamColor, this.chessBoard);
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)) {
            return false;
        }

        for(int i=1; i<=8; ++i){
            for(int j=1; j<=8; ++j){
                ChessPosition pose = new ChessPosition(i,j);
                ChessPiece piece = this.chessBoard.getPiece(pose);
                if(piece == null) {
                    continue;
                }

                if(!piece.getTeamColor().equals(teamColor)) {
                    continue;
                }

                Collection<ChessMove> movesForPiece = validMoves(pose);
                for(ChessMove move : movesForPiece){
                    ChessBoard clonedBoard = this.chessBoard.clone();
                    makeMovePrivate(clonedBoard, move);
                    if(!boardIsInCheck(teamColor, clonedBoard)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        for(int i = 1; i<=8; ++i){
            for(int j = 1; j<=8; ++j){
                ChessPosition pose = new ChessPosition(i,j);
                ChessPiece piece = this.chessBoard.getPiece(pose);
                if(piece == null) {
                    continue;
                }

                if(!piece.getTeamColor().equals(teamColor)) {
                    continue;
                }

                Collection<ChessMove> moves = piece.pieceMoves(this.chessBoard, pose);
                for(ChessMove move : moves){
                    if(doesNotMoveIntoCheck(move)){
                        return false;
                    }
                }
            }
        }
        return !isInCheckmate(teamColor);
    }

    public void setBoard(ChessBoard board) {
        this.chessBoard = board;
    }

    public ChessBoard getBoard() {
        return this.chessBoard;
    }


    /**private methods*/
    private boolean doesNotMoveIntoCheck(ChessMove move){
        ChessBoard clonedBoard = this.chessBoard.clone();
        ChessPiece piece = clonedBoard.getPiece(move.getStartPosition());
        makeMovePrivate(clonedBoard, move);
        return !boardIsInCheck(piece.getTeamColor(), clonedBoard);
    }

    private boolean boardIsInCheck(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPosition = findKing(teamColor, board);
        for(int i=1; i<=8; ++i){
            for(int j=1; j<=8; ++j){
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if(piece == null) {
                    continue;
                }

                if(piece.getTeamColor() == teamColor) {
                    continue;
                }

                Collection<ChessMove> moves = piece.pieceMoves(board,new ChessPosition(i,j));
                for(ChessMove move : moves){
                    if(move.getEndPosition().equals(kingPosition)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition findKing(TeamColor teamColor, ChessBoard board){
        for(int i=1; i<=8; ++i){
            for(int j=1; j<=8; ++j){
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if(piece != null){
                    if(piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING){
                        return new ChessPosition(i,j);
                    }
                }
            }
        }
        throw new RuntimeException();
    }

    private boolean isCastleMove(ChessMove move, ChessBoard board){
        switch(board.getPiece(move.getStartPosition()).getTeamColor()){
            case WHITE:
                if(move.getEndPosition().equals(new ChessPosition(1,3)) ||
                   move.getEndPosition().equals(new ChessPosition(1,7))){
                    if(move.getStartPosition().equals(new ChessPosition(1,5))){
                        return true;
                    }
                }
                break;
            case BLACK:
                if(move.getEndPosition().equals(new ChessPosition(8,3)) ||
                   move.getEndPosition().equals(new ChessPosition(8,7))){
                    if(move.getStartPosition().equals(new ChessPosition(8,5))){
                        return true;
                    }
                }
                break;
            default:
                throw new RuntimeException();
        }
        return false;
    }

    private void makeMovePrivate(ChessBoard board, ChessMove move){
        ChessPiece newPiece = board.getPiece(move.getStartPosition());
        if(newPiece != null){
            if(newPiece.getPieceType() == ChessPiece.PieceType.KING){
                if(isCastleMove(move,board)){
                    board.addPiece(move.getEndPosition(), newPiece);
                    if(move.getEndPosition().equals(new ChessPosition(1,3))){
                        makeMovePrivate(board, new ChessMove(new ChessPosition(1,1), new ChessPosition(1,4), null));
                        board.whiteKing = false;
                    }
                    else if(move.getEndPosition().equals(new ChessPosition(1,7))){
                        makeMovePrivate(board, new ChessMove(new ChessPosition(1,8), new ChessPosition(1,6), null));
                        board.whiteKing = false;
                    }
                    else if(move.getEndPosition().equals(new ChessPosition(8,3))){
                        makeMovePrivate(board, new ChessMove(new ChessPosition(8,1), new ChessPosition(8,4), null));
                        board.blackKing = false;
                    }
                    else if(move.getEndPosition().equals(new ChessPosition(8,7))){
                        makeMovePrivate(board, new ChessMove(new ChessPosition(8,8), new ChessPosition(8,6), null));
                        board.blackKing = false;
                    }
                    board.removePiece((move.getStartPosition()));
                    return;
                }
            }


            if(move.getPromotionPiece() != null){
                ChessPiece promoPiece = new ChessPiece(newPiece.getTeamColor(), move.getPromotionPiece());
                board.addPiece(move.getEndPosition(), promoPiece);
            }
            else{
                board.addPiece(move.getEndPosition(), newPiece);
            }
            board.removePiece((move.getStartPosition()));

            //castling
            if(newPiece.getPieceType() == ChessPiece.PieceType.KING){
                if(newPiece.getTeamColor() == TeamColor.WHITE){
                    board.whiteKing = false;
                }
                else{board.blackKing = false;}
            }
            if(newPiece.getPieceType() == ChessPiece.PieceType.ROOK){
                if(newPiece.getTeamColor() == TeamColor.WHITE){
                    if(move.getStartPosition().equals(new ChessPosition(1,1))){
                        board.leftWhiteRook = false;
                    }
                    else if(move.getStartPosition().equals(new ChessPosition(1,8))){board.rightWhiteRook = false;}
                }
                else{
                    if(move.getStartPosition().equals(new ChessPosition(8,1))){
                        board.leftBlackRook = false;
                    }
                    else if(move.getStartPosition().equals(new ChessPosition(8,8))){board.rightBlackRook = false;}
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(chessBoard, chessGame.chessBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, chessBoard);
    }
}
