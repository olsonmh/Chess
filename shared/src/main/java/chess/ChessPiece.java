package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }


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
    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(obj.getClass() != this.getClass()){
            return false;
        }
        ChessPiece that = (ChessPiece) obj;
        return this.type == that.type && this.pieceColor == that.pieceColor;
    }
    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    public boolean positionOutOfBounds(ChessPosition pose){
        if(pose.getRow() > 8 || pose.getRow() < 1){
            return true;
        }
        return pose.getColumn() > 8 || pose.getColumn() < 1;
    }


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> valid_moves = new HashSet<ChessMove>();
        List<ChessPosition> possible_moves = new ArrayList<ChessPosition>();
        switch(this.type) {
            case KING:
                for(int dy = -1; dy<=1; ++dy) {
                    for (int dx = -1; dx <= 1; ++dx) {
                        ChessPosition pose = new ChessPosition(myPosition.getRow()+dy, myPosition.getColumn()+dx);
                        if(!positionOutOfBounds(pose)){
                            if(board.getPiece(pose) != null){
                                if(this.pieceColor != board.getPiece(pose).pieceColor){
                                    possible_moves.add(pose);
                                }

                            }
                            else{
                                possible_moves.add(pose);
                            }
                        }

                    }
                }
                break;
            case QUEEN:
                for(int i = -1; i <=1; i+=2){
                    for(int dy = 1; dy<=7; ++dy){
                        ChessPosition pose = new ChessPosition(myPosition.getRow()+(i*dy), myPosition.getColumn());
                        if(!positionOutOfBounds(pose)){
                            if(board.getPiece(pose) != null){
                                if(this.pieceColor != board.getPiece(pose).pieceColor){
                                    possible_moves.add(pose);
                                }
                                break;
                            }
                            else{
                                possible_moves.add(pose);
                            }
                        }

                    }
                }
                for(int j = -1; j<=1; j+=2){
                    for(int dx = 1; dx<=7; ++dx){
                        ChessPosition pose = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+(j*dx));
                        if(!positionOutOfBounds(pose)){
                            if(board.getPiece(pose) != null){
                                if(this.pieceColor != board.getPiece(pose).pieceColor){
                                    possible_moves.add(pose);
                                }
                                break;
                            }
                            else{
                                possible_moves.add(pose);
                            }
                        }

                    }
                }
                for(int j = -1; j<=1; j+=2){
                    for(int i = -1; i<=1; i+=2){
                        for(int d = 1; d<=7; ++d){
                            ChessPosition pose = new ChessPosition(myPosition.getRow()+(d*i*j), myPosition.getColumn()+(d*i));
                            if(!positionOutOfBounds(pose)){
                                if(board.getPiece(pose) != null){
                                    if(this.pieceColor != board.getPiece(pose).pieceColor){
                                        possible_moves.add(pose);
                                    }
                                    break;
                                }
                                else{
                                    possible_moves.add(pose);
                                }
                            }

                        }
                    }
                }
                break;
            case BISHOP:
                for(int j = -1; j<=1; j+=2){
                    for(int i = -1; i<=1; i+=2){
                        for(int d = 1; d<=7; ++d){
                            ChessPosition pose = new ChessPosition(myPosition.getRow()+(d*i*j), myPosition.getColumn()+(d*i));
                            if(!positionOutOfBounds(pose)){
                                if(board.getPiece(pose) != null){
                                    if(this.pieceColor != board.getPiece(pose).pieceColor){
                                        possible_moves.add(pose);
                                    }
                                    break;
                                }
                                else{
                                    possible_moves.add(pose);
                                }
                            }
                        }
                    }
                }
                break;
            case KNIGHT:
                for(int dx = -2; dx<=2; dx+=4){
                    for(int dy = -1; dy<=1; dy+=2){
                        ChessPosition pose = new ChessPosition(myPosition.getRow()+dy, myPosition.getColumn()+dx);
                        if(!positionOutOfBounds(pose)){
                            if(board.getPiece(pose) != null){
                                if(this.pieceColor != board.getPiece(pose).pieceColor){
                                    possible_moves.add(pose);
                                }
                            }
                            else{
                                possible_moves.add(pose);
                            }
                        }
                    }
                }
                for(int dy = -2; dy<=2; dy+=4){
                    for(int dx = -1; dx<=1; dx+=2){
                        ChessPosition pose = new ChessPosition(myPosition.getRow()+dy, myPosition.getColumn()+dx);
                        if(!positionOutOfBounds(pose)){
                            if(board.getPiece(pose) != null){
                                if(this.pieceColor != board.getPiece(pose).pieceColor){
                                    possible_moves.add(pose);
                                }

                            }
                            else{
                                possible_moves.add(pose);
                            }
                        }
                    }
                }
                break;
            case ROOK:
                for(int i = -1; i <=1; i+=2){
                    for(int dy = 1; dy<=7; ++dy){
                        ChessPosition pose = new ChessPosition(myPosition.getRow()+(i*dy), myPosition.getColumn());
                        if(!positionOutOfBounds(pose)){
                            if(board.getPiece(pose) != null){
                                if(this.pieceColor != board.getPiece(pose).pieceColor){
                                    possible_moves.add(pose);
                                }
                                break;
                            }
                            else{
                                possible_moves.add(pose);
                            }
                        }

                    }
                }
                for(int j = -1; j<=1; j+=2){
                    for(int dx = 1; dx<=7; ++dx){
                        ChessPosition pose = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+(j*dx));
                        if(!positionOutOfBounds(pose)){
                            if(board.getPiece(pose) != null){
                                if(this.pieceColor != board.getPiece(pose).pieceColor){
                                    possible_moves.add(pose);
                                }
                                break;
                            }
                            else{
                                possible_moves.add(pose);
                            }
                        }
                    }
                }
                break;
            case PAWN:
                switch(this.pieceColor){
                    case WHITE:
                        ChessPosition pose = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                        if(board.getPiece(pose) == null){
                            if(myPosition.getRow() == 7){
                                valid_moves.add(new ChessMove(myPosition, pose, PieceType.QUEEN));
                                valid_moves.add(new ChessMove(myPosition, pose, PieceType.ROOK));
                                valid_moves.add(new ChessMove(myPosition, pose, PieceType.BISHOP));
                                valid_moves.add(new ChessMove(myPosition, pose, PieceType.KNIGHT));
                            }
                            else{possible_moves.add(pose);}
                        }
                        if(myPosition.getRow() == 2){
                            ChessPosition position1 = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                            if(board.getPiece(position1) == null){
                                ChessPosition position2 = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
                                if(board.getPiece(position2) == null){
                                    possible_moves.add(new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()));
                                }

                            }
                        }
                        for(int dx = -1; dx<=1; dx+=2){
                            ChessPosition pose2 = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+dx);
                            if(!positionOutOfBounds(pose2)){
                                if(board.getPiece(pose2) != null){
                                    if(this.pieceColor != board.getPiece(pose2).pieceColor){
                                        if(myPosition.getRow() == 7){
                                            valid_moves.add(new ChessMove(myPosition, pose2, PieceType.QUEEN));
                                            valid_moves.add(new ChessMove(myPosition, pose2, PieceType.ROOK));
                                            valid_moves.add(new ChessMove(myPosition, pose2, PieceType.BISHOP));
                                            valid_moves.add(new ChessMove(myPosition, pose2, PieceType.KNIGHT));
                                        }
                                        else{possible_moves.add(pose2);}
                                    }
                                }
                            }

                        }
                        break;
                    case BLACK:
                        ChessPosition pose3 = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                        if(board.getPiece(pose3) == null){
                            if(myPosition.getRow() == 2){
                                valid_moves.add(new ChessMove(myPosition, pose3, PieceType.QUEEN));
                                valid_moves.add(new ChessMove(myPosition, pose3, PieceType.ROOK));
                                valid_moves.add(new ChessMove(myPosition, pose3, PieceType.BISHOP));
                                valid_moves.add(new ChessMove(myPosition, pose3, PieceType.KNIGHT));
                            }
                            else{possible_moves.add(pose3);}
                        }
                        if(myPosition.getRow() == 7){
                            ChessPosition position1 = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
                            if(board.getPiece(position1) == null){
                                ChessPosition position2 = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn());
                                if(board.getPiece(position2) == null){
                                    possible_moves.add(new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()));
                                }
                            }
                        }
                        for(int dx = -1; dx<=1; dx+=2){
                            ChessPosition pose2 = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-dx);
                            if(!positionOutOfBounds(pose2)){
                                if(board.getPiece(pose2) != null){
                                    if(this.pieceColor != board.getPiece(pose2).pieceColor){
                                        if(myPosition.getRow() == 2){
                                            valid_moves.add(new ChessMove(myPosition, pose2, PieceType.QUEEN));
                                            valid_moves.add(new ChessMove(myPosition, pose2, PieceType.ROOK));
                                            valid_moves.add(new ChessMove(myPosition, pose2, PieceType.BISHOP));
                                            valid_moves.add(new ChessMove(myPosition, pose2, PieceType.KNIGHT));
                                        }
                                        else{possible_moves.add(pose2);}
                                    }
                                }
                            }
                        }
                        break;
                    default:
                        throw new RuntimeException();
                }

                break;
            default:
                throw new RuntimeException("Not implemented");
        }

        for(ChessPosition pose : possible_moves){
            if(pose.getRow() == myPosition.getRow() && pose.getColumn() == myPosition.getColumn()){
                continue;
            }
            else{
                valid_moves.add(new ChessMove(myPosition, pose, null));
            }

        }

        return valid_moves;
    }
}
