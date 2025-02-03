package chess;
import java.util.Objects;

public class ChessPosition {

    private final int col;
    private final int row;

    public ChessPosition(int row, int col) {
        this.col = col;
        this.row = row;
    }

    public int getRow() {
        return row;

    }

    public int getColumn() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
