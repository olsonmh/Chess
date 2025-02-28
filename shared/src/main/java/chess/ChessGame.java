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
    private ChessBoard chess_board;

    public ChessGame() {
        turn = TeamColor.WHITE;
        chess_board = new ChessBoard();
        chess_board.resetBoard();
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
        Collection<ChessMove> valid_moves = new HashSet<>();
        ChessPiece piece = this.chess_board.getPiece(startPosition);
        if(piece == null){
            return null;
        }
        Collection<ChessMove> moves =  piece.pieceMoves(this.chess_board,startPosition);
        for(ChessMove move : moves){
            if(DoesNotMoveIntoCheck(move)){
                if(this.chess_board.getPiece(startPosition).getPieceType().equals(ChessPiece.PieceType.KING)){
                    if(isCastleMove(move, this.chess_board)){
                        if(move.getStartPosition().getColumn() > move.getEndPosition().getColumn()){
                            if(DoesNotMoveIntoCheck(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(),startPosition.getColumn()-1), null))){
                                valid_moves.add(move);
                            }
                        }
                        else{
                            if(DoesNotMoveIntoCheck(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(),startPosition.getColumn()+1), null))){
                                valid_moves.add(move);
                            }
                        }
                    }
                    else{valid_moves.add(move);}
                }
                else{
                    valid_moves.add(move);
                }
            }
        }
        return valid_moves;
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(this.chess_board.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException();
        }
        if(this.chess_board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.WHITE){
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
        boolean move_made = false;
        for(ChessMove valid_move : moves){
            if(valid_move.equals(move)){
                make_move(chess_board, move);
                move_made = true;
            }
        }
        if(!move_made){
            throw new InvalidMoveException();
        }
    }

    public boolean isInCheck(TeamColor teamColor){
        return boardIsInCheck(teamColor, this.chess_board);
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            for(int i=1; i<=8; ++i){
                for(int j=1; j<=8; ++j){
                    ChessPosition pose = new ChessPosition(i,j);
                    ChessPiece piece = this.chess_board.getPiece(pose);
                    if(piece != null){
                        if(piece.getTeamColor().equals(teamColor)){
                            Collection<ChessMove> movesForPiece = validMoves(pose);
                            for(ChessMove move : movesForPiece){
                                ChessBoard cloned_board = this.chess_board.clone();
                                make_move(cloned_board, move);
                                if(!boardIsInCheck(teamColor, cloned_board)){
                                    return false;
                                }
                            }
                        }
                    }

                }
            }
            return true;
        }

        return false;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        for(int i = 1; i<=8; ++i){
            for(int j = 1; j<=8; ++j){
                ChessPosition pose = new ChessPosition(i,j);
                ChessPiece piece = this.chess_board.getPiece(pose);
                if(piece != null){
                    if(piece.getTeamColor().equals(teamColor)){
                        Collection<ChessMove> moves = piece.pieceMoves(this.chess_board, pose);
                        for(ChessMove move : moves){
                            if(DoesNotMoveIntoCheck(move)){
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return !isInCheckmate(teamColor);
    }

    public void setBoard(ChessBoard board) {
        this.chess_board = board;
    }

    public ChessBoard getBoard() {
        return this.chess_board;
    }


    /**private methods*/
    private boolean DoesNotMoveIntoCheck(ChessMove move){
        ChessBoard cloned_board = this.chess_board.clone();
        ChessPiece piece = cloned_board.getPiece(move.getStartPosition());
        make_move(cloned_board, move);
        return !boardIsInCheck(piece.getTeamColor(), cloned_board);
    }

    private boolean boardIsInCheck(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPosition = findKing(teamColor, board);
        for(int i=1; i<=8; ++i){
            for(int j=1; j<=8; ++j){
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if(piece != null){
                    if(piece.getTeamColor() != teamColor){
                        Collection<ChessMove> moves = piece.pieceMoves(board,new ChessPosition(i,j));
                        for(ChessMove move : moves){
                            if(move.getEndPosition().equals(kingPosition)){
                                return true;
                            }
                        }
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
                if(move.getEndPosition().equals(new ChessPosition(1,3)) || move.getEndPosition().equals(new ChessPosition(1,7))){
                    if(move.getStartPosition().equals(new ChessPosition(1,5))){
                        return true;
                    }
                }
                break;
            case BLACK:
                if(move.getEndPosition().equals(new ChessPosition(8,3)) || move.getEndPosition().equals(new ChessPosition(8,7))){
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

    private void make_move(ChessBoard board, ChessMove move){
        ChessPiece newPiece = board.getPiece(move.getStartPosition());
        if(newPiece != null){
            if(newPiece.getPieceType() == ChessPiece.PieceType.KING){
                if(isCastleMove(move,board)){
                    board.addPiece(move.getEndPosition(), newPiece);
                    if(move.getEndPosition().equals(new ChessPosition(1,3))){
                        make_move(board, new ChessMove(new ChessPosition(1,1), new ChessPosition(1,4), null));
                        board.WhiteKing = false;
                    }
                    else if(move.getEndPosition().equals(new ChessPosition(1,7))){
                        make_move(board, new ChessMove(new ChessPosition(1,8), new ChessPosition(1,6), null));
                        board.WhiteKing = false;
                    }
                    else if(move.getEndPosition().equals(new ChessPosition(8,3))){
                        make_move(board, new ChessMove(new ChessPosition(8,1), new ChessPosition(8,4), null));
                        board.BlackKing = false;
                    }
                    else if(move.getEndPosition().equals(new ChessPosition(8,7))){
                        make_move(board, new ChessMove(new ChessPosition(8,8), new ChessPosition(8,6), null));
                        board.BlackKing = false;
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
                    board.WhiteKing = false;
                }
                else{board.BlackKing = false;}
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
        return turn == chessGame.turn && Objects.equals(chess_board, chessGame.chess_board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, chess_board);
    }
}
