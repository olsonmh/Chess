package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece implements Cloneable {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

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

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    public boolean inBounds(ChessPosition pose, ChessPosition myPose){
        if(pose.getRow() < 1 || pose.getRow() > 8){
            return false;
        }
        if(pose.getColumn() < 1 || pose.getColumn() > 8){
            return false;
        }
        return pose.getRow() != myPose.getRow() || pose.getColumn() != myPose.getColumn();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece piece = (ChessPiece) o;
        return pieceColor == piece.pieceColor && type == piece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> valid_moves = new HashSet<>();
        switch(board.getPiece(myPosition).type){
            case KING:
                for(int dy=-1; dy<=1; ++dy){
                    for(int dx=-1; dx<=1; ++dx){
                        ChessPosition pose = new ChessPosition(myPosition.getRow()+dy, myPosition.getColumn()+dx);
                        if(inBounds(pose, myPosition)){
                            if(board.getPiece(pose) != null){
                                if(board.getPiece(pose).pieceColor != this.pieceColor){
                                    valid_moves.add(new ChessMove(myPosition, pose, null));
                                }
                            }
                            else{valid_moves.add(new ChessMove(myPosition, pose, null));}
                        }
                    }
                }
                break;
            case ROOK:
                for(int j=-1; j<=1; j+=2){
                    for(int dy=1; dy<=8; ++dy){
                        ChessPosition pose = new ChessPosition(myPosition.getRow()+(dy*j), myPosition.getColumn());
                        if(inBounds(pose, myPosition)){
                            if(board.getPiece(pose) != null){
                                if(board.getPiece(pose).pieceColor != this.pieceColor){
                                    valid_moves.add(new ChessMove(myPosition, pose, null));
                                }
                                break;
                            }
                            else{valid_moves.add(new ChessMove(myPosition, pose, null));}
                        }
                    }
                }
                for(int j=-1; j<=1; j+=2){
                    for(int dx=1; dx<=8; ++dx){
                        ChessPosition pose = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+(dx*j));
                        if(inBounds(pose, myPosition)){
                            if(board.getPiece(pose) != null){
                                if(board.getPiece(pose).pieceColor != this.pieceColor){
                                    valid_moves.add(new ChessMove(myPosition, pose, null));
                                }
                                break;
                            }
                            else{valid_moves.add(new ChessMove(myPosition, pose, null));}
                        }
                    }
                }

                break;
            case BISHOP:
                for(int j=-1; j<=1; j+=2){
                    for(int d = 1; d<=8; ++d){
                        ChessPosition pose = new ChessPosition(myPosition.getRow()+(d*j), myPosition.getColumn()+(d*j));
                        if(inBounds(pose, myPosition)){
                            if(board.getPiece(pose) != null){
                                if(board.getPiece(pose).pieceColor != this.pieceColor){
                                    valid_moves.add(new ChessMove(myPosition, pose, null));
                                }
                                break;
                            }
                            else{valid_moves.add(new ChessMove(myPosition, pose, null));}
                        }
                    }
                }
                for(int j=-1; j<=1; j+=2){
                    for(int d = 1; d<=8; ++d){
                        ChessPosition pose = new ChessPosition(myPosition.getRow()-(d*j), myPosition.getColumn()+(d*j));
                        if(inBounds(pose, myPosition)){
                            if(board.getPiece(pose) != null){
                                if(board.getPiece(pose).pieceColor != this.pieceColor){
                                    valid_moves.add(new ChessMove(myPosition, pose, null));
                                }
                                break;
                            }
                            else{valid_moves.add(new ChessMove(myPosition, pose, null));}
                        }
                    }
                }
                break;
            case QUEEN:
                for(int j=-1; j<=1; j+=2){
                    for(int dy=1; dy<=8; ++dy){
                        ChessPosition pose = new ChessPosition(myPosition.getRow()+(dy*j), myPosition.getColumn());
                        if(inBounds(pose, myPosition)){
                            if(board.getPiece(pose) != null){
                                if(board.getPiece(pose).pieceColor != this.pieceColor){
                                    valid_moves.add(new ChessMove(myPosition, pose, null));
                                }
                                break;
                            }
                            else{valid_moves.add(new ChessMove(myPosition, pose, null));}
                        }
                    }
                }
                for(int j=-1; j<=1; j+=2){
                    for(int dx=1; dx<=8; ++dx){
                        ChessPosition pose = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+(dx*j));
                        if(inBounds(pose, myPosition)){
                            if(board.getPiece(pose) != null){
                                if(board.getPiece(pose).pieceColor != this.pieceColor){
                                    valid_moves.add(new ChessMove(myPosition, pose, null));
                                }
                                break;
                            }
                            else{valid_moves.add(new ChessMove(myPosition, pose, null));}
                        }
                    }
                }
                for(int j=-1; j<=1; j+=2){
                    for(int d = 1; d<=8; ++d){
                        ChessPosition pose = new ChessPosition(myPosition.getRow()+(d*j), myPosition.getColumn()+(d*j));
                        if(inBounds(pose, myPosition)){
                            if(board.getPiece(pose) != null){
                                if(board.getPiece(pose).pieceColor != this.pieceColor){
                                    valid_moves.add(new ChessMove(myPosition, pose, null));
                                }
                                break;
                            }
                            else{valid_moves.add(new ChessMove(myPosition, pose, null));}
                        }
                    }
                }
                for(int j=-1; j<=1; j+=2){
                    for(int d = 1; d<=8; ++d){
                        ChessPosition pose = new ChessPosition(myPosition.getRow()-(d*j), myPosition.getColumn()+(d*j));
                        if(inBounds(pose, myPosition)){
                            if(board.getPiece(pose) != null){
                                if(board.getPiece(pose).pieceColor != this.pieceColor){
                                    valid_moves.add(new ChessMove(myPosition, pose, null));
                                }
                                break;
                            }
                            else{valid_moves.add(new ChessMove(myPosition, pose, null));}
                        }
                    }
                }
                break;
            case KNIGHT:
                for(int j=-1; j<=1; j+=2){
                    for(int dx=-1; dx<=1; dx+=2){
                        ChessPosition pose = new ChessPosition(myPosition.getRow()+2*j, myPosition.getColumn()+dx);
                        if(inBounds(pose, myPosition)){
                            if(board.getPiece(pose) != null){
                                if(board.getPiece(pose).pieceColor != this.pieceColor){
                                    valid_moves.add(new ChessMove(myPosition, pose, null));
                                }
                            }
                            else{valid_moves.add(new ChessMove(myPosition, pose, null));}
                        }
                    }
                }
                for(int j=-1; j<=1; j+=2){
                    for(int dy=-1; dy<=1; dy+=2){
                        ChessPosition pose = new ChessPosition(myPosition.getRow()+dy, myPosition.getColumn()+2*j);
                        if(inBounds(pose, myPosition)){
                            if(board.getPiece(pose) != null){
                                if(board.getPiece(pose).pieceColor != this.pieceColor){
                                    valid_moves.add(new ChessMove(myPosition, pose, null));
                                }
                            }
                            else{valid_moves.add(new ChessMove(myPosition, pose, null));}
                        }
                    }
                }
                break;
            case PAWN:
                if(this.pieceColor == ChessGame.TeamColor.WHITE){
                    ChessPosition pose = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
                    if(myPosition.getRow() == 7){
                        if(inBounds(pose, myPosition)){
                            if(board.getPiece(pose) == null){
                                valid_moves.add(new ChessMove(myPosition, pose, PieceType.QUEEN));
                                valid_moves.add(new ChessMove(myPosition, pose, PieceType.ROOK));
                                valid_moves.add(new ChessMove(myPosition, pose, PieceType.BISHOP));
                                valid_moves.add(new ChessMove(myPosition, pose, PieceType.KNIGHT));

                            }
                        }
                        for(int dx=-1; dx<=1; dx+=2){
                            ChessPosition pose3 = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+dx);
                            if(inBounds(pose3, myPosition)){
                                if(board.getPiece(pose3) != null){
                                    if(board.getPiece(pose3).pieceColor != this.pieceColor){
                                        valid_moves.add(new ChessMove(myPosition, pose3, PieceType.QUEEN));
                                        valid_moves.add(new ChessMove(myPosition, pose3, PieceType.ROOK));
                                        valid_moves.add(new ChessMove(myPosition, pose3, PieceType.BISHOP));
                                        valid_moves.add(new ChessMove(myPosition, pose3, PieceType.KNIGHT));
                                    }
                                }
                            }
                        }
                    }
                    else{
                        if(inBounds(pose, myPosition)){
                            if(board.getPiece(pose) == null){
                                valid_moves.add(new ChessMove(myPosition, pose, null));
                                if(myPosition.getRow() == 2){
                                    ChessPosition pose2 = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
                                    if(board.getPiece(pose2) == null){
                                        valid_moves.add(new ChessMove(myPosition, pose2, null));
                                    }
                                }
                            }
                        }
                        for(int dx=-1; dx<=1; dx+=2){
                            ChessPosition pose3 = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+dx);
                            if(inBounds(pose3, myPosition)){
                                if(board.getPiece(pose3) != null){
                                    if(board.getPiece(pose3).pieceColor != this.pieceColor){
                                        valid_moves.add(new ChessMove(myPosition, pose3, null));
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
                                valid_moves.add(new ChessMove(myPosition, pose, PieceType.QUEEN));
                                valid_moves.add(new ChessMove(myPosition, pose, PieceType.ROOK));
                                valid_moves.add(new ChessMove(myPosition, pose, PieceType.BISHOP));
                                valid_moves.add(new ChessMove(myPosition, pose, PieceType.KNIGHT));

                            }
                        }
                        for(int dx=-1; dx<=1; dx+=2){
                            ChessPosition pose3 = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-dx);
                            if(inBounds(pose3, myPosition)){
                                if(board.getPiece(pose3) != null){
                                    if(board.getPiece(pose3).pieceColor != this.pieceColor){
                                        valid_moves.add(new ChessMove(myPosition, pose3, PieceType.QUEEN));
                                        valid_moves.add(new ChessMove(myPosition, pose3, PieceType.ROOK));
                                        valid_moves.add(new ChessMove(myPosition, pose3, PieceType.BISHOP));
                                        valid_moves.add(new ChessMove(myPosition, pose3, PieceType.KNIGHT));
                                    }
                                }
                            }
                        }
                    }
                    else{
                        if(inBounds(pose, myPosition)){
                            if(board.getPiece(pose) == null){
                                valid_moves.add(new ChessMove(myPosition, pose, null));
                                if(myPosition.getRow() == 7){
                                    ChessPosition pose2 = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn());
                                    if(board.getPiece(pose2) == null){
                                        valid_moves.add(new ChessMove(myPosition, pose2, null));
                                    }
                                }
                            }
                        }
                        for(int dx=-1; dx<=1; dx+=2){
                            ChessPosition pose3 = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-dx);
                            if(inBounds(pose3, myPosition)){
                                if(board.getPiece(pose3) != null){
                                    if(board.getPiece(pose3).pieceColor != this.pieceColor){
                                        valid_moves.add(new ChessMove(myPosition, pose3, null));
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            default:
                throw new RuntimeException();
        }

        return valid_moves;
    }

    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
    }
}
