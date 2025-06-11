package kolkokrzyzyk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmartComputerMoves {

    public int[] chooseMove(char[][] board, char mySymbol, char enemySymbol) {
        int size = board.length;
        if (size == 3) {
            return chooseMove3x3(board, mySymbol, enemySymbol);
        } else {
            return chooseMove10x10(board, mySymbol, enemySymbol);
        }
    }

    // DLA 3x3
    private int[] chooseMove3x3(char[][] board, char mySymbol, char enemySymbol) {
        int size = 3;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = mySymbol;
                    if (isWin3x3(board, mySymbol)) {
                        board[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    board[i][j] = ' ';
                }
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = enemySymbol;
                    if (isWin3x3(board, enemySymbol)) {
                        board[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    board[i][j] = ' ';
                }
            }
        }
        if (board[1][1] == ' ') return new int[]{1, 1};

        int[][] corners = { {0, 0}, {0, 2}, {2, 0}, {2, 2} };
        for (int[] c : corners) {
            if (board[c[0]][c[1]] == ' ') return c;
        }

        return findRandomMove(board);
    }

    // sprawdzam czy obecna plansza to wygrana w 3x3 dla danego gracza
    private boolean isWin3x3(char[][] board, char symbol) {
        // wiersze i kolumny
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) return true;
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) return true;
        }
        // przekątne
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) return true;
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) return true;
        return false;
    }

    // 10x10
    private int[] chooseMove10x10(char[][] board, char mySymbol, char enemySymbol) {
        int size = board.length;

        // Blokowanie trzyelementowych ciągów _XXX lub XXX_
        // Wiersze
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= size - 3; j++) {
                // _XXX
                if (j > 0 && board[i][j-1] == ' ' &&
                        board[i][j] == enemySymbol && board[i][j+1] == enemySymbol && board[i][j+2] == enemySymbol) {
                    return new int[]{i, j-1};
                }
                // XXX_
                if (j+3 < size && board[i][j] == enemySymbol && board[i][j+1] == enemySymbol && board[i][j+2] == enemySymbol &&
                        board[i][j+3] == ' ') {
                    return new int[]{i, j+3};
                }
            }
        }
        // Kolumny
        for (int j = 0; j < size; j++) {
            for (int i = 0; i <= size - 3; i++) {
                // _XXX
                if (i > 0 && board[i-1][j] == ' ' &&
                        board[i][j] == enemySymbol && board[i+1][j] == enemySymbol && board[i+2][j] == enemySymbol) {
                    return new int[]{i-1, j};
                }
                // XXX_
                if (i+3 < size && board[i][j] == enemySymbol && board[i+1][j] == enemySymbol && board[i+2][j] == enemySymbol &&
                        board[i+3][j] == ' ') {
                    return new int[]{i+3, j};
                }
            }
        }
        // Przekątna "\"
        for (int i = 0; i <= size - 3; i++) {
            for (int j = 0; j <= size - 3; j++) {
                // _XXX
                if (i > 0 && j > 0 && board[i-1][j-1] == ' ' &&
                        board[i][j] == enemySymbol && board[i+1][j+1] == enemySymbol && board[i+2][j+2] == enemySymbol) {
                    return new int[]{i-1, j-1};
                }
                // XXX_
                if (i+3 < size && j+3 < size &&
                        board[i][j] == enemySymbol && board[i+1][j+1] == enemySymbol && board[i+2][j+2] == enemySymbol &&
                        board[i+3][j+3] == ' ') {
                    return new int[]{i+3, j+3};
                }
            }
        }
        // Przekątna "/"
        for (int i = 0; i <= size - 3; i++) {
            for (int j = 2; j < size; j++) {
                // _XXX
                if (i > 0 && j+1 < size && board[i-1][j+1] == ' ' &&
                        board[i][j] == enemySymbol && board[i+1][j-1] == enemySymbol && board[i+2][j-2] == enemySymbol) {
                    return new int[]{i-1, j+1};
                }
                // XXX_
                if (i+3 < size && j-3 >= 0 &&
                        board[i][j] == enemySymbol && board[i+1][j-1] == enemySymbol && board[i+2][j-2] == enemySymbol &&
                        board[i+3][j-3] == ' ') {
                    return new int[]{i+3, j-3};
                }
            }
        }
        // Ruch losowy
        return findRandomMove(board);
    }

    // RUCH LOSOWY
    private int[] findRandomMove(char[][] board) {
        List<int[]> emptyFields = new ArrayList<>();
        int size = board.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == ' ') {
                    emptyFields.add(new int[]{i, j});
                }
            }
        }
        if (!emptyFields.isEmpty()) {
            Random rand = new Random();
            return emptyFields.get(rand.nextInt(emptyFields.size()));
        }
        return null;
    }
}