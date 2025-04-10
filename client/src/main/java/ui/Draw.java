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
            pose = getPose(highlightPiece, color);
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
                int row2 = flipBoard ? 8 -i : i+1;
                if (!positions.isEmpty()){
                    if (positions.contains(new ChessPosition(row2,j+1))) {
                        backgroundColor = (i + j) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_GREEN_TINT_WHITE : EscapeSequences.SET_BG_COLOR_GREEN_TINT_BLACK;
                    } else {
                        backgroundColor = (i + j) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_BLACK;
                    }
                } else {
                    backgroundColor = (i + j) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_BLACK;
                }
                if (pose != null){
                    if (pose.equals(new ChessPosition(row2,j+1))){
                        backgroundColor = (i + j) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_YELLOW : EscapeSequences.SET_BG_COLOR_YELLOW_BLACK;
                    }
                }

                String label;
                ChessPiece piece = game.getBoard().board[row][j];
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

    public static ChessPosition getPose(String piece, String color){
        String firstLetter = piece.substring(0, 1);
        String secondLetter = piece.substring(1, 2);
        Map<String, Integer> letterToNumberWhite = new HashMap<>();

        letterToNumberWhite.put("a", 1);
        letterToNumberWhite.put("b", 2);
        letterToNumberWhite.put("c", 3);
        letterToNumberWhite.put("d", 4);
        letterToNumberWhite.put("e", 5);
        letterToNumberWhite.put("f", 6);
        letterToNumberWhite.put("g", 7);
        letterToNumberWhite.put("h", 8);

        Map<String, Integer> letterToNumberBlack = new HashMap<>();

        letterToNumberBlack.put("a", 8);
        letterToNumberBlack.put("b", 7);
        letterToNumberBlack.put("c", 6);
        letterToNumberBlack.put("d", 5);
        letterToNumberBlack.put("e", 4);
        letterToNumberBlack.put("f", 3);
        letterToNumberBlack.put("g", 2);
        letterToNumberBlack.put("h", 1);

        int secondNumber;
        if (color != null && color.equalsIgnoreCase("black")){
            secondNumber = letterToNumberBlack.get(firstLetter);
        } else {
            secondNumber = letterToNumberWhite.get(firstLetter);
        }

        //int secondNumber = letterToNumberWhite.get(firstLetter);
        int firstNumber = Integer.parseInt(secondLetter);
        //System.out.printf("is at row %d and col %d on chessboard",firstNumber, secondNumber);
        return new ChessPosition(firstNumber, secondNumber);
    }

}
