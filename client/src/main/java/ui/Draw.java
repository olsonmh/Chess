package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.*;


public class Draw {
    public static void drawBoard(ChessGame game, String color, String highlightPiece){
        if (color == null){
            color = "WHITE";
        }

        Set<ChessPosition> positions = new HashSet<>();
        ChessPosition pose = null;
        if (highlightPiece != null){
            pose = getPose(highlightPiece);
            positions = getValidMoves(pose, game);
        }

        String backgroundColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        boolean flipBoard = color.equals("WHITE");

        System.out.printf("\n%s   ", backgroundColor);
        if (flipBoard) {
            for (char ch = 'a'; ch <= 'h'; ch++) {
                System.out.printf("%s %s ", backgroundColor, ch);
            }
        } else{
            for (char ch = 'h'; ch >= 'a'; ch--) {
                System.out.printf("%s %s ", backgroundColor, ch);
            }
        }
        System.out.printf("%s   " + EscapeSequences.RESET_BG_COLOR + "\n", backgroundColor);

        for(int i = 0; i<=7; i++){
            //int q = i;
            int row = flipBoard ? 7 - i : i;
            System.out.printf("%s %d ", EscapeSequences.SET_BG_COLOR_LIGHT_GREY, row+1);

            for(int j = 0; j<=7; j++){
                int col = flipBoard ? j : 7 - j;
                int row2 = flipBoard ? 8 - i : i+1;
                int col2 = flipBoard ? j+1 : 8 -j;
                if (!positions.isEmpty()){
                    if (positions.contains(new ChessPosition(row2,col2))) {
                        backgroundColor = (i + j) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_GREEN_TINT_WHITE :
                                EscapeSequences.SET_BG_COLOR_GREEN_TINT_BLACK;
                    } else {
                        backgroundColor = (i + j) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_WHITE :
                                EscapeSequences.SET_BG_COLOR_BLACK;
                    }
                } else {
                    backgroundColor = (i + j) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_WHITE :
                            EscapeSequences.SET_BG_COLOR_BLACK;
                }
                if (pose != null){
                    if (pose.equals(new ChessPosition(row2,col2))){
                        backgroundColor = (i + j) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_YELLOW :
                                EscapeSequences.SET_BG_COLOR_YELLOW_BLACK;
                    }
                }

                String label;
                ChessPiece piece = game.getBoard().board[row][col];
                if (piece == null) {
                    label = "   ";
                } else {
                    label = switch (piece.getPieceType()) {
                        case PAWN -> EscapeSequences.BLACK_PAWN;
                        case ROOK -> EscapeSequences.BLACK_ROOK;
                        case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                        case QUEEN -> EscapeSequences.BLACK_QUEEN;
                        case KING -> EscapeSequences.BLACK_KING;
                        case BISHOP -> EscapeSequences.BLACK_BISHOP;
                    };
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        label = EscapeSequences.SET_TEXT_COLOR_BLUE + label + EscapeSequences.RESET_TEXT_COLOR;
                    } else {
                        label = EscapeSequences.SET_TEXT_COLOR_RED + label + EscapeSequences.RESET_TEXT_COLOR;
                    }
                }
                System.out.printf("%s%s", backgroundColor, label);
            }
            System.out.printf("%s %d ", EscapeSequences.SET_BG_COLOR_LIGHT_GREY, row+1);
            System.out.print(EscapeSequences.RESET_BG_COLOR + "\n");
        }

        backgroundColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        System.out.printf("%s   ", backgroundColor);
        if (flipBoard){
            for (char ch = 'a'; ch <= 'h'; ch++) {
                System.out.printf("%s %s ", backgroundColor, ch);
            }
        } else {
            for (char ch = 'h'; ch >= 'a'; ch--) {
                System.out.printf("%s %s ", backgroundColor, ch);
            }
        }
        System.out.printf("%s   " + EscapeSequences.RESET_BG_COLOR + "\n", backgroundColor);

    }

    private static Set<ChessPosition> getValidMoves(ChessPosition pose, ChessGame currentGame){
        Collection<ChessMove> moves = currentGame.validMoves(pose);
        Set<ChessPosition> positions = new HashSet<>();
        for (ChessMove move : moves) {
            positions.add(move.getEndPosition());
        }

        return positions;
    }

    public static ChessPosition getPose(String piece){
        String firstLetter = piece.substring(0, 1);
        String secondLetter = piece.substring(1, 2);
        Map<String, Integer> letterToNumber = new HashMap<>();

        letterToNumber.put("a", 1);
        letterToNumber.put("b", 2);
        letterToNumber.put("c", 3);
        letterToNumber.put("d", 4);
        letterToNumber.put("e", 5);
        letterToNumber.put("f", 6);
        letterToNumber.put("g", 7);
        letterToNumber.put("h", 8);

        int secondNumber = letterToNumber.get(firstLetter);
        int firstNumber = Integer.parseInt(secondLetter);
        return new ChessPosition(firstNumber, secondNumber);
    }

}
