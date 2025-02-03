package chess;

import java.util.Collection;
import java.util.HashSet;

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

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> valid_moves = new HashSet<>();
        ChessPiece piece = this.chess_board.getPiece(startPosition);
        if(piece == null){
            return null;
        }
        Collection<ChessMove> moves =  piece.pieceMoves(this.chess_board,startPosition);
        for(ChessMove move : moves){
            if(DoesNotMoveIntoCheck(move)){
                valid_moves.add(move);
            }
        }
        return valid_moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
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
    private boolean DoesNotMoveIntoCheck(ChessMove move){
        ChessBoard cloned_board = this.chess_board.clone();
        ChessPiece piece = cloned_board.getPiece(move.getStartPosition());
        make_move(cloned_board, move);
        return !boardIsInCheck(piece.getTeamColor(), cloned_board);
    }

    private void make_move(ChessBoard board, ChessMove move){
        ChessPiece newPiece = board.getPiece(move.getStartPosition());
        if(move.getPromotionPiece() != null){
            ChessPiece promoPiece = new ChessPiece(newPiece.getTeamColor(), move.getPromotionPiece());
            board.addPiece(move.getEndPosition(), promoPiece);
        }
        else{
            board.addPiece(move.getEndPosition(), newPiece);
        }
        board.removePiece((move.getStartPosition()));
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public ChessPosition findKing(TeamColor teamColor, ChessBoard board){
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

    public boolean isInCheck(TeamColor teamColor){
        return boardIsInCheck(teamColor, this.chess_board);
    }

    public boolean boardIsInCheck(TeamColor teamColor, ChessBoard board) {
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

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
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

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
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

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chess_board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.chess_board;
    }
}
